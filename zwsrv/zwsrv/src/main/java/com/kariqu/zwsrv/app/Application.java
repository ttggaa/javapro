package com.kariqu.zwsrv.app;

import com.kariqu.zwsrv.thelib.security.ResponseEncryptFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricsDropwizardAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
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
import java.sql.SQLSyntaxErrorException;
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
@ComponentScan({"com.kariqu.zwsrv.thelib.persistance","com.kariqu.zwsrv.thelib","com.kariqu.zwsrv.app.transaction","com.kariqu.zwsrv.app"})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static String hostAddress;
    public static int port;
    public static boolean isProdEnv=true;

    public static int CDN_PORT = 0;

    public static String hostAddressAndPort() {
        return Application.hostAddress+":"+Application.port;
    }

    public static void main(String[] args) throws UnknownHostException {

        System.setProperty("net.sf.ehcache.enableShutdownHook","true");

        SpringApplication app = new SpringApplication(Application.class);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        if (source!=null&&source.containsProperty("env")) {
            String env= source.getProperty("env");
            if (env.compareToIgnoreCase("prod")==0) {
                isProdEnv=true;
            }
            app.setAdditionalProfiles(source.getProperty("env"));
        } else {
            app.setAdditionalProfiles("dev");
        }

        // 加载配置项失败，终止程序
        Environment env = app.run(args).getEnvironment();
        if (!initConfig(env)) {
            getLog().error("initConfig failed. server exit!");
            System.exit(1);
            return;
        }

        List<String> hostAddressList=hostAddressList();
        if (hostAddressList!=null&&hostAddressList.size()>0) {
            hostAddress=hostAddressList.get(0);
        } else {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        }
        if (isProdEnv) {
            hostAddress="120.27.250.213";
        }

        port = Integer.valueOf(env.getProperty("server.port"));


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

    public static List<String> ignoreFilterList = new ArrayList<String>() {
        {
            add("/file/");
            add("/wxpay/");
        }
    };

    public static Logger getLog() { return log; }

    @Bean
    @Autowired
    public FilterRegistrationBean gzipFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new ResponseEncryptFilter(Application.ignoreFilterList,Application.isProdEnv));
        return filterBean;
    }


    // 初始化配置文件
    private static boolean initConfig(Environment env)
    {
        try {
            CDN_PORT = Integer.valueOf(env.getProperty("cdn.port"));


            getLog().info("init config: {}", CDN_PORT);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

