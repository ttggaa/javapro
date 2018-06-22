package com.kariqu.tyt.http;

import com.kariqu.tyt.http.task.TaskManager;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@ComponentScan({"com.kariqu.tyt.common.persistence", "com.kariqu.tyt.http"})
@EntityScan({"com.kariqu.tyt.common.persistence"})
@EnableJpaRepositories({"com.kariqu.tyt.common.persistence"})
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private TaskManager taskManager;

	public static void main(String[] args) {

        if (!ApplicationConfig.getInstance().init(args)) {
            logger.warn("init config failed!");
            return;
        }

        SpringApplication app = new SpringApplication(Application.class);
        app.setAdditionalProfiles(ApplicationConfig.getInstance().getPropertyName());

        app.run(args);

        StringBuilder sb = new StringBuilder();
        sb.append("\n\n*******************************************************\n");
        sb.append(String.format("property: %s     port: %d"
                , ApplicationConfig.getInstance().getPropertyName()
                , ApplicationConfig.getInstance().getServerPort()
                )
        );
        sb.append("\n*******************************************************\n");
        logger.info(sb.toString());
	}


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
