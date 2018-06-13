package com.cgy.test.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mywebEntityManagerFactory",
        transactionManagerRef = "mywebTransactionManager",
        basePackages = {"com.cgy.test.demo.persistence.daoweb"}
)
public class MywebConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("mywebDataSource")
    private DataSource dataSource;

    @Bean(name = "mywebEntityManager")
    //@Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return mywebEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "mywebEntityManagerFactory")
    //@Primary
    public LocalContainerEntityManagerFactoryBean mywebEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages("com.cgy.test.demo.persistence.entity")
                .properties(getVendorProperties())
                .persistenceUnit("myweb")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    @Bean(name = "mywebTransactionManager")
    //@Primary
    PlatformTransactionManager mywebTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(mywebEntityManagerFactory(builder).getObject());
    }
}
