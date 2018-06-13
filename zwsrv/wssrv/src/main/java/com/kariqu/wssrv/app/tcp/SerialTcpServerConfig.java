package com.kariqu.wssrv.app.tcp;


import org.apache.commons.configuration.CompositeConfiguration;

/**
 * Created by simon on 09/05/2018.
 */
public class SerialTcpServerConfig {
    public SerialTcpServerConfig(CompositeConfiguration config) {
        if (config!=null) {
            this.port=config.getInt("tcpsrv.port");
            this.bossCount=config.getInt("tcpsrv.boss-count");
            this.workerCount=config.getInt("tcpsrv.worker-count");
            this.keepAlive=config.getBoolean("tcpsrv.keep-alive");
            this.backlog=config.getInt("tcpsrv.backlog");
        }
    }
    private int port;
    private int bossCount;
    private int workerCount;
    private boolean keepAlive;
    private int backlog;

    public int getPort() {
        return port;
    }

    public int getBossCount() {
        return bossCount;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public int getBacklog() {
        return backlog;
    }
}
