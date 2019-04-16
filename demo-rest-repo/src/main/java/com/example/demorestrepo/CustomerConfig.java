package com.example.demorestrepo;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.example.demorestrepo.entity.Customer;
import com.example.demorestrepo.entity.CustomerGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "customerEntityManagerFactory",
        transactionManagerRef = "customerTransactionManager")
class CustomerConfig {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurerAdapter() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.exposeIdsFor(Customer.class, CustomerGroup.class);
            }
        };
    }

    @Bean
    PlatformTransactionManager customerTransactionManager() {
        return new JpaTransactionManager(customerEntityManagerFactory().getObject());
    }

    @Bean
    LocalContainerEntityManagerFactoryBean customerEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(customerDataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan(CustomerConfig.class.getPackage().getName());

        return factoryBean;
    }

    @Bean
    DataSource customerDataSource() {

        return new EmbeddedDatabaseBuilder().//
                setType(EmbeddedDatabaseType.H2).//
                setName("customers").//
                build();
    }
}