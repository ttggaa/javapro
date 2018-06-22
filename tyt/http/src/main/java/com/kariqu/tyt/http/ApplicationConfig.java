package com.kariqu.tyt.http;

import org.apache.commons.cli.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class ApplicationConfig {
    private String propertyFullName;
    private String propertyName;
    private int serverPort;

    private String wechatAppid;
    private String wechatLoginUrl;
    private String wechatLoginSecret;

    private int cachedRefreshSeconds;

    private String webKey;

    //private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static ApplicationConfig instance = null;

    public final Options options = new Options()
            .addOption("e", "env", true, "desc: --env=prod or --env=dev");

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyFullName() {
        return propertyFullName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getWechatAppid() {
        return wechatAppid;
    }

    public String getWechatLoginUrl() {
        return wechatLoginUrl;
    }

    public String getWechatLoginSecret() {
        return wechatLoginSecret;
    }

    public int getCachedRefreshSeconds() {
        return cachedRefreshSeconds;
    }

    public String getWebKey() {
        return webKey;
    }

    public static ApplicationConfig getInstance() {
        if (instance == null)
            instance = new ApplicationConfig();
        return instance;
    }

    public boolean init(String[] args) {
        try {
            Map<String, String> cmdArgs = parseArgs(args);
            String postfix = cmdArgs.get("env");
            this.propertyName = postfix;
            this.propertyFullName = "application-" + postfix + ".properties";

            Parameters parameters = new Parameters();
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(parameters.properties()
                                    .setFileName(propertyFullName));
            Configuration config = builder.getConfiguration();

            this.serverPort = config.getInt("server.port");

            this.wechatAppid = config.getString("wechat.appid");
            this.wechatLoginUrl = config.getString("wechat.login.url");
            this.wechatLoginSecret = config.getString("wechat.login.secret");

            this.cachedRefreshSeconds = config.getInt("cached.refreshSeconds");
            this.webKey = config.getString("web.key");

            return true;
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(" ", ApplicationConfig.getInstance().options);
            return false;
        }
    }

    private Map<String, String> parseArgs(String[] args) throws ParseException {
        Map<String, String> cmdArgs = new HashMap<>();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(this.options, args);
        if (!cmd.hasOption("env"))
            throw new RuntimeException("parse CommandLine failed");
        cmdArgs.put("env", cmd.getOptionValue("env"));
        return cmdArgs;
    }

    private ApplicationConfig() {
    }

}
