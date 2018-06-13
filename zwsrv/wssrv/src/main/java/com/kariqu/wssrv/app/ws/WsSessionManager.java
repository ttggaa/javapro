package com.kariqu.wssrv.app.ws;

import com.kariqu.wssrv.app.util.SimpleEventDispatcher;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by simon on 10/05/2018.
 */
public class WsSessionManager extends SimpleEventDispatcher<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WsSessionManager.class);

    static private WsSessionManager sInstance;
    static public WsSessionManager getInstance() {
        if (sInstance == null) {
            synchronized (WsSessionManager.class) {
                if (sInstance == null) {
                    sInstance = new WsSessionManager(500);
                }
            }
        }
        return sInstance;
    }

    static private AtomicLong al = new AtomicLong(1);

    private Map<ChannelId,WsSessionBase> channelMap = new ConcurrentHashMap<>();
    private Map<String,WsSessionBase> connIdMap = new ConcurrentHashMap<>();
    private Map<String,WsSessionSrv> srvLoginMap = new ConcurrentHashMap<>();   // 已经login的srv

    private WsSessionManager(long milliseconds) {
        super(milliseconds);
    }

    public void syncRemoveLoginSrv(String userId) {
        srvLoginMap.remove(userId);
    }

    public WsSessionSrv syncFindLoginSrv(String srvUserId) {
        return srvLoginMap.get(srvUserId);
    }

    public void syncAddLoginSrv(WsSessionSrv srv) {
        srvLoginMap.put(srv.getUserId(), srv);
    }

    public void syncSendToWithSrvUserID(String srvUserID, String content) {
        WsSessionSrv sessionSrv = srvLoginMap.get(srvUserID);
        if (sessionSrv!=null &&sessionSrv.getConnId()!=null) {
            sessionSrv.sendFinallyMessage(content);
        }
    }

    public void syncSendToWithAppConnId(String connId, String content) {
        WsSessionBase sessionBase = connIdMap.get(connId);
        if (sessionBase!=null&& sessionBase instanceof WsSessionApp) {
            sessionBase.sendFinallyMessage(content);
        }
    }

    public void syncSendToWithAppConnId(List<String> connIdList, String content) {
        for (String connId : connIdList) {
            syncSendToWithAppConnId(connId, content);
        }
    }

    public void syncSendToWithAppConnId(WsEventRoute evt) {
        syncSendToWithAppConnId(evt.getConnIds(), evt.getContent());
    }

    public void syncBroadcastToAllApp(String content) {
        for (Map.Entry<String, WsSessionBase> entry : connIdMap.entrySet()) {
            WsSessionBase sessionBase = entry.getValue();
            if (sessionBase instanceof WsSessionApp) {
                sessionBase.sendFinallyMessage(content);
            }
        }
    }

    public void asyncBroadcastToAllApp(String content) {
        put(new WsEventRoute(null,content));
    }

    public void asyncSendTo(String connId, String content) {
        List<String> connIds = new ArrayList<>();
        connIds.add(connId);
        put(new WsEventRoute(connIds, content));
    }

    public void asyncSendTo(List<String> connIds, String content) {
        put(new WsEventRoute(connIds,content));
    }


    public void asyncCloseAppChannel(String connId) {
        put(new WsEventActiveClosed(connId, null));
    }

    @Override
    protected void dispatchOnIdle() {
    }

    @Override
    protected void dispatchOnExit() {
    }

    @Override
    protected void dispatchInWorkThread(Object eventArg) {
        if (eventArg instanceof WsEventAccept) {
            WsEventAccept event = (WsEventAccept)eventArg;
            long connId = al.getAndIncrement();
            if (event.getSessionType() == WsSessionType.APP) {
                WsSessionBase sessionBase = new WsSessionApp(connId,event.getCtx());
                channelMap.put(sessionBase.channelId(),sessionBase);
                connIdMap.put(sessionBase.getConnId(),sessionBase);
                logger.info("add new app session: {}", event.getCtx().channel().id().asShortText());
            } else if (event.getSessionType() == WsSessionType.SERVER) {
                WsSessionBase sessionBase = new WsSessionSrv(connId,event.getCtx());
                channelMap.put(sessionBase.channelId(),sessionBase);
                connIdMap.put(sessionBase.getConnId(),sessionBase);
                logger.info("add new srv session: {}", event.getCtx().channel().id().asShortText());
            }
        }
        else if (eventArg instanceof WsEventClosed) {
            WsEventClosed event = (WsEventClosed)eventArg;
            ChannelId channelId = event.getCtx().channel().id();
            WsSessionBase sessionBase = channelMap.get(channelId);

            if (sessionBase!=null) {
                logger.info("WsEventClosed channelId: {} connId: {}", channelId.asShortText(), sessionBase.getConnId());
                sessionBase.handleWillRemoved();
                connIdMap.remove(sessionBase.getConnId());
                channelMap.remove(channelId);
            } else {
                logger.info("WsEventClosed channelId: {} connId: {}", channelId.asShortText(), 0);
            }
        }
        else if (eventArg instanceof WsEventCmd) {
            WsEventCmd event = (WsEventCmd)eventArg;
            WsSessionBase sessionBase = channelMap.get(event.getCtx().channel().id());
            if (sessionBase!=null) {
                sessionBase.handleEventData(event);
            }
        }
        else if (eventArg instanceof WsEventRoute) {
            WsEventRoute event = (WsEventRoute)eventArg;
            if (event.isToAllApp()) {
                syncBroadcastToAllApp(event.getContent());
            } else {
                syncSendToWithAppConnId(event);
            }
        } else if (eventArg instanceof WsEventActiveClosed) {
            // 服务端主动断开App connection
            WsEventActiveClosed event = (WsEventActiveClosed)eventArg;
            String connId = event.getConnId();
            WsSessionBase sessionBase = connIdMap.get(connId);
            if (sessionBase == null) {
                return;
            }

            // 服务器主动断开连接，不要需要调用 handleWillRemoved
            sessionBase.shutdownChannel(CloseFramer.repeatedLoginClose());
        }
    }
}
