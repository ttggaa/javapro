package com.kariqu.zwsrv.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricsDropwizardAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration(exclude = { MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class, MetricsDropwizardAutoConfiguration.class })
@EnableJpaRepositories({"com.kariqu.zwsrv.thelib.persistance", "com.kariqu.zwsrv.web.persistance", "com.kariqu.zwsrv.web.utilityex"})
@SpringBootApplication
@EntityScan({"com.kariqu.zwsrv.thelib.persistance", "com.kariqu.zwsrv.web"})
@ComponentScan({"com.kariqu.zwsrv.thelib.persistance","com.kariqu.zwsrv.thelib", "com.kariqu.zwsrv.web"})
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
		System.out.println("main exit");
	}
}
