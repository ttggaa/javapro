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
        entityManagerFactoryRef = "mytestEntityManagerFactory",
        transactionManagerRef = "mytestTransactionManager",
        basePackages = {"com.cgy.test.demo.persistence.dao"}
)
public class MytestConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("mytestDataSource")
    private DataSource dataSource;

    @Bean(name = "mytestEntityManager")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return mytestEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "mytestEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean mytestEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.cgy.test.demo.persistence.entity")
                .properties(getVendorProperties())
                .persistenceUnit("mytest")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    /*
    private Map<String, Object> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
    */

    @Bean(name = "mytestTransactionManager")
    @Primary
    PlatformTransactionManager mytestTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(mytestEntityManagerFactory(builder).getObject());
    }
}
