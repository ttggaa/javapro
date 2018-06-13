package com.kariqu.wssrv.app.ws;

import com.kariqu.wssrv.app.error.ErrorCode;
import com.kariqu.wssrv.app.util.AESCoder;
import com.kariqu.wssrv.app.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by simon on 10/05/2018.
 */
public class WsSessionSrv extends WsSessionBase {

    private static final Logger logger = LoggerFactory.getLogger(WsSessionSrv.class);

    //public static Map<String,WsSessionSrv> srvLoginMap = new ConcurrentHashMap<>(); //userID,WsSessionBase

    private String userId;
    private String userName;

    public WsSessionSrv(long connId, ChannelHandlerContext ctx) {
        super(connId, ctx);

        this.userId = "";
        this.userName = "";
    }

    public String getUserId() {
        return userId!=null?userId:"";

    }

    public String getUserName() {
        return userName!=null?userName:"";

    }

    @Override
    public void handleWillRemoved() {
        if (getUserId()!=null&&getUserId().length()>0) {
            WsSessionManager.getInstance().syncRemoveLoginSrv(getUserId());
        }
    }

    @Override
    protected void reqLogin(WsEventCmd cmd) {
        if (isLoggedIn()) {
            sendResponse(WsCmdType.RSP_LOGIN, ErrorCode.ERROR_REPEATED_LOGIN);
            logger.warn("session is logined. {} {}", getUserId(), getUserName());
            return;
        }

        ServerLogin serverLogin = JsonUtil.convertJson2Model(cmd.getCmdHead().getContent(), ServerLogin.class);
        if (serverLogin == null) {
            sendResponse(WsCmdType.RSP_LOGIN, ErrorCode.ERROR_UNKNOWN);
            logger.info("srv session parse content error. userId: {} channelId: {}", userId, channelId());
            return;
        }

        String userId = serverLogin.getUserID();
        String userName = serverLogin.getUserName();
        if (userId == null || userName == null) {
            sendResponse(WsCmdType.RSP_LOGIN, ErrorCode.ERROR_PARSE_PARAMS);
            logger.info("srv session userId userName null channelId: {}", channelId());
            return;
        }

        WsSessionSrv sessionSrv = WsSessionManager.getInstance().syncFindLoginSrv(userId);
        if (sessionSrv != null) {
            // 出现了重复登陆,断开之间的连接
            logger.info("srv session repeated login. userId: {} channelId: {}", userId, channelId());
            sessionSrv.sendResponse(WsCmdType.RSP_REPEATED_LOGIN, ErrorCode.ERROR_REPEATED_LOGIN);
            sessionSrv.logout();
            sessionSrv.shutdownChannel(CloseFramer.repeatedLoginClose());
        }

        this.userId = userId;
        this.userName = userName;
        logger.info("srv session login success. userId: {} userName: {} channelId: {}", userId, userName, channelId());
        WsSessionManager.getInstance().syncAddLoginSrv(this);
        sendResponse(WsCmdType.RSP_LOGIN);
    }

    @Override
    protected void reqBroadcastAllApp(WsEventCmd cmd) {
        if (isLoggedIn()) {
            logger.info("reqBroadcastAllApp userId: {} userName: {}", getUserId(), getUserName());
            String jsonString = cmd.getCmdHead().getContent();
            if (jsonString!=null&&jsonString.length()>0) {
                try {
                    String jsonStringEncrypt = AESCoder.encrypt(WsSessionBase.cmdSecretKey, cmd.getCmdHead().getContent());
                    if (jsonStringEncrypt!=null&&jsonStringEncrypt.length()>0) {
                        WsSessionManager.getInstance().syncBroadcastToAllApp(jsonStringEncrypt);
                    }
                } catch (Exception e) {
                    logger.warn("broadcastLoginApp json string encrypt error {}", jsonString);
                }
            }
        } else {
            sendResponse(WsCmdType.RSP_BROADCAST_ALL_APP, ErrorCode.ERROR_UNLOGIN);
            logger.warn("session isUnlogin. userId: {} userName: {}", getUserId(), getUserName());
        }
    }

    @Override
    protected void reqRouter(WsEventCmd cmd) {
        if (isLoggedIn()) {
            logger.info("srv reqRouter userId: {} userName: {}", getUserId(), getUserName());

            List<String> connIdList = cmd.getCmdHead().getTo();
            String jsonString = cmd.getCmdHead().getContent();
            if (connIdList!=null&&jsonString!=null&&jsonString.length()>0) {
                try {
                    String jsonStringEncrypt = AESCoder.encrypt(WsSessionBase.cmdSecretKey, cmd.getCmdHead().getContent());
                    if (jsonStringEncrypt!=null&&jsonStringEncrypt.length()>0) {
                        WsSessionManager.getInstance().syncSendToWithAppConnId(connIdList, jsonStringEncrypt);
                    }
                } catch (Exception e) {
                    logger.warn("broadcastLoginApp json string encrypt error {}", jsonString);
                }
            }
        } else {
            logger.warn("session isUnlogin. userId: {} userName: {}", getUserId(), getUserName());
        }
    }

    private boolean isLoggedIn() {
        return userId!=null && !userId.isEmpty();

    }

    private void logout()  {
        this.userId = "";
        this.userName = "";
    }
}
