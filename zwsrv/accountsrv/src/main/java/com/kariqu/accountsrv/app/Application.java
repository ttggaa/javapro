package com.kariqu.accountsrv.app;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricsDropwizardAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by simon on 24/11/15.
 */
//http://stackoverflow.com/questions/33997031/spring-data-jpa-no-qualifying-bean-found-for-dependency
@Configuration
@EnableAutoConfiguration(exclude = { MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class, MetricsDropwizardAutoConfiguration.class })
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableJpaRepositories({"com.kariqu.zwsrv.thelib.persistance"})
@EntityScan({"com.kariqu.zwsrv.thelib.persistance"})
@ComponentScan({"com.kariqu.zwsrv.thelib.persistance","com.kariqu.zwsrv.thelib","com.kariqu.accountsrv.app.transaction","com.kariqu.accountsrv.app"})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static String hostAddress;
    public static int port;
    public static boolean isProdEnv=true;

    public static String hostAddressAndPort() {
        return Application.hostAddress+":"+Application.port;
    }

    public static void main(String[] args) throws UnknownHostException {

        System.setProperty("net.sf.ehcache.enableShutdownHook","true");

//        RoomCmd.CommandChatMessageData data = new RoomCmd.CommandChatMessageData();
//        data.setContent("content");
//        data.setType(12);
//        data.setPlayer(new GamePlayerInfo("simon","111111"));
//        RoomCmd.CommandChatMessage commandChatMessage = new RoomCmd.CommandChatMessage();
//        commandChatMessage.setCmd(RoomCmd.CMD_ROOM_CHAT_MESSAGE);
//        commandChatMessage.setData(data);
//
//        String commandChatMessageString = JsonUtil.convertObject2Json(commandChatMessage);
//
//        new RoomEventCmd("666",commandChatMessageString);

        String envName = "";
        SpringApplication app = new SpringApplication(Application.class);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        if (source!=null&&source.containsProperty("env")) {
            String env= source.getProperty("env");
            if (env.compareToIgnoreCase("prod")==0) {
                isProdEnv=true;
            }
            app.setAdditionalProfiles(source.getProperty("env"));
            envName += source.getProperty("env");
        } else {
            app.setAdditionalProfiles("dev");
            envName += "dev";
        }

        CompositeConfiguration config = new CompositeConfiguration();
        try {
            String propertiesFileName = "application-" + envName + ".properties";
            config.addConfiguration(new SystemConfiguration());
            config.addConfiguration(new PropertiesConfiguration(propertiesFileName));
            Application.port = config.getInt("server.port");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        //本地IP地址
        List<String> hostAddressList=hostAddressList();
        if (hostAddressList!=null&&hostAddressList.size()>0) {
            hostAddress=hostAddressList.get(0);
        } else {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        }
        if (isProdEnv) {
            hostAddress="120.27.250.213";
        }


//        SerialTcpServer tcpServer = new SerialTcpServer(new SerialTcpServerConfig(config));
//        Thread tcpThread = new Thread(tcpServer);
//        tcpThread.start();
//
//        WebSocketServer srvServer = new WebSocketServer(new WebSocketConfigSrv(config));
//        Thread srvThread = new Thread(srvServer);
//        srvThread.start();
//
//        WebSocketServer appServer = new WebSocketServer(new WebSocketConfigApp(config));
//        Thread appThread = new Thread(appServer);
//        appThread.start();

        ConfigurableApplicationContext context = app.run(args);
        Environment env = context.getEnvironment();

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://127.0.0.1:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                hostAddress,
                env.getProperty("server.port"));

        String configServerStatus = env.getProperty("configserver.status");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);
    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (source!=null&&source.containsProperty("env")) {
            app.setAdditionalProfiles(source.getProperty("env"));
        } else {
            app.setAdditionalProfiles("dev");
        }
    }

    public static List<String> hostAddressList() {
        List<String> hostAddressList = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        hostAddressList.add(inetAddress.getHostAddress().toString());
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostAddressList;
    }

//    public static List<String> ignoreFilterList = new ArrayList<String>() {
//        {
//            add("/file/");
//            add("/wxpay/");
//        }
//    };
//
//    @Bean
//    @Autowired
//    public FilterRegistrationBean gzipFilter() {
//        FilterRegistrationBean filterBean = new FilterRegistrationBean();
//        filterBean.setFilter(new ResponseEncryptFilter(Application.ignoreFilterList,Application.isProdEnv));
//        return filterBean;
//    }

}



//https://github.com/seasidesky/netty-rpc
//https://github.com/zyf970617/springboot-netty-demo
//https://github.com/zbum/netty-spring-example
//https://github.com/zyf970617/springboot-netty-demo


//114.55.104.197
//        root  http://gaoxiaode.17tcw.com/
//
//        cgy     tUNOlxdqo6nkHmbGAXWv
//        MySQL 端口3810 密码zww#Test2018


