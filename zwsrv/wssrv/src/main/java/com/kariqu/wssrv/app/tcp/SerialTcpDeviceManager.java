package com.kariqu.wssrv.app.tcp;

import com.kariqu.wssrv.app.util.SimpleEventDispatcher;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by simon on 09/05/2018.
 */
public class SerialTcpDeviceManager extends SimpleEventDispatcher<Object> {
    private static final Logger logger = LoggerFactory.getLogger(SerialTcpDeviceManager.class);

    static private SerialTcpDeviceManager sInstance;
    static public SerialTcpDeviceManager getInstance() {
        if (sInstance == null) {
            synchronized (SerialTcpDeviceManager.class) {
                if (sInstance == null) {
                    sInstance = new SerialTcpDeviceManager(500);
                }
            }
        }
        return sInstance;
    }

    private SerialTcpDeviceManager(long milliseconds) {
        super(milliseconds);
    }

    private Map<ChannelId,SerialTcpDevice> channelMap = new ConcurrentHashMap<>();
    private Map<ChannelId,SerialTcpDevice> channelMapNoId = new ConcurrentHashMap<>();
    private Map<String,SerialTcpDevice> deviceIdMap = new ConcurrentHashMap<>();
    private List<DeviceEventListener> listeners = new ArrayList<>();

    public void addDeviceEventListener(DeviceEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeDeviceEventListener(DeviceEventListener listener) {
        listeners.remove(listener);
    }

    public void sendToTbjCmd(String deviceId, byte[] pkg) {
        put(new SerialTcpEventSendCmd(deviceId, pkg));
    }

    public void onQueryDeviceId(SerialTcpDevice device) {
        String deviceId = device.getDeviceId();
        channelMapNoId.remove(device.getCtx().channel().id());
        deviceIdMap.put(deviceId, device);

        for (DeviceEventListener listener : listeners) {
            listener.onQueryDeviceId(deviceId);
        }
    }

    public void onDropCoins(String deviceId, int coinsNum) {
        for (DeviceEventListener listener : listeners) {
            listener.onDropCoins(deviceId,coinsNum);
        }
    }

    public void onQueryReward(String deviceId, int coinsNum) {
        for (DeviceEventListener listener : listeners) {
            listener.onQueryReward(deviceId,coinsNum);
        }
    }

    public void onDropConisConfirm(String deviceId) {
        for (DeviceEventListener listener : listeners) {
            listener.onDropConisConfirm(deviceId);
        }
    }

    public void onDeviceOffline(String deviceId) {
        for (DeviceEventListener listener : listeners) {
            listener.onDeviceOffline(deviceId);
        }
    }

    @Override
    protected void dispatchOnIdle() {
        for (Map.Entry<ChannelId,SerialTcpDevice> entry : channelMapNoId.entrySet()) {
            entry.getValue().sendCommand(DeviceCmd.CMD_QUERY_DEVICE_ID);
        }
    }

    @Override
    protected void dispatchOnExit() {

    }

    @Override
    protected void dispatchInWorkThread(Object eventArg) {
        if (eventArg instanceof SerialTcpEventAccept) {
            SerialTcpEventAccept event = (SerialTcpEventAccept)eventArg;
            SerialTcpDevice device = new SerialTcpDevice(event.getCtx(), this);
            channelMap.put(event.getCtx().channel().id(),device);
            channelMapNoId.put(event.getCtx().channel().id(),device);
            logger.info("-------> accept {}", event.getCtx().channel().id());
        }
        else if (eventArg instanceof SerialTcpEventClosed) {
            SerialTcpEventClosed event = (SerialTcpEventClosed)eventArg;
            logger.info("-------->close {} ", event.getCtx().channel().id());

            SerialTcpDevice device = channelMap.get(event.getCtx().channel().id());
            if (device!=null) {
                String deviceId = device.getDeviceId();
                onDeviceOffline(deviceId);
                if (deviceId!=null&&deviceId.length()>0) {
                    deviceIdMap.remove(deviceId);
                    logger.info("-------->close {} devId: {}", event.getCtx().channel().id(), deviceId);
                }
            }
            channelMapNoId.remove(event.getCtx().channel().id());
            channelMap.remove(event.getCtx().channel().id());
        }
        else if (eventArg instanceof SerialTcpEventReceivedData) {
            SerialTcpEventReceivedData event = (SerialTcpEventReceivedData)eventArg;
            SerialTcpDevice device = channelMap.get(event.getCtx().channel().id());
            if (device == null) {
                return;
            }
            device.handleReceivedPkg(event);
        }
        else if (eventArg instanceof SerialTcpEventSendCmd) {
            SerialTcpEventSendCmd event = (SerialTcpEventSendCmd)eventArg;
            SerialTcpDevice device = deviceIdMap.get(event.getDeviceId());
            if (device!=null) {
                device.sendCommand(event.getCmd());
            }
        }
    }
}


