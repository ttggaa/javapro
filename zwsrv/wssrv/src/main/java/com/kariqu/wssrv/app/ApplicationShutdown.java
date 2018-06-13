package com.kariqu.wssrv.app;

import com.kariqu.wssrv.app.room.GameRoomManager;
import com.kariqu.wssrv.app.tcp.SerialTcpDeviceManager;
import com.kariqu.wssrv.app.tcp.SerialTcpServer;
import com.kariqu.wssrv.app.ws.WebSocketServer;
import com.kariqu.wssrv.app.ws.WsSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ApplicationShutdown {
    private static final Logger log = LoggerFactory.getLogger(ApplicationShutdown.class);

    public static class KillHandler implements SignalHandler {
        private ApplicationShutdown applicationShutdown;

        public KillHandler(ApplicationShutdown app) {
            this.applicationShutdown = app;
        }

        public void registerSignal(String signalName) {
            Signal signal = new Signal(signalName);
            Signal.handle(signal, this);
        }

        @Override
        public void handle(Signal signal) {
            if (signal.getName().equals("TERM")) {
                log.info("signal: {} {}", signal.getName(), signal.getNumber());
                applicationShutdown.shutdown();
            } else if (signal.getName().equals("INT") || signal.getName().equals("HUP")) {
                log.info("signal: {} {}", signal.getName(), signal.getNumber());
                applicationShutdown.shutdown();
            } else {
                log.info("unregister signal: {} {}", signal.getName(), signal.getNumber());
            }
        }
    }

    private KillHandler killHandler;
    private WebSocketServer wsSrvServer;
    private WebSocketServer wsAppServer;
    private SerialTcpServer tcpServer;
    private ConfigurableApplicationContext springContext;


    public ApplicationShutdown() {
        this.killHandler = new KillHandler(this);
        //this.killHandler.registerSignal("TERM");
        //this.killHandler.registerSignal("INT");

        this.wsSrvServer = null;
        this.wsAppServer = null;
        this.tcpServer = null;
        this.springContext = null;
    }

    public void setWsSrvServer(WebSocketServer wsSrvServer) {
        this.wsSrvServer = wsSrvServer;
    }

    public void setWsAppServer(WebSocketServer wsAppServer) {
        this.wsAppServer = wsAppServer;
    }

    public void setTcpServer(SerialTcpServer tcpServer) {
        this.tcpServer = tcpServer;
    }

    public void setSpringContext(ConfigurableApplicationContext springContext) {
        this.springContext = springContext;
    }

    public void shutdown() {
        /*
        // 停止工作线程
        GameRoomManager.getInstance().shutdown();
        SerialTcpDeviceManager.getInstance().shutdown();
        WsSessionManager.getInstance().shutdown();

        // 停止网络连接
        this.tcpServer.shutdown();
        this.wsSrvServer.shutdown();
        this.wsAppServer.shutdown();
        this.springContext.close();
        */
    }
}
