package com.ega;

import com.ega.dao.impl.ExtendedRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories( repositoryBaseClass = ExtendedRepositoryImpl.class)
//@EntityScan("com.ega.model.user")
@EnableEurekaClient
@EnableCircuitBreaker
@EnableHystrixDashboard
public class EgaSvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(EgaSvcApplication.class, args);
    }

}
