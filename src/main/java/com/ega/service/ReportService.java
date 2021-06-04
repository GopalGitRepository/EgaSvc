package com.ega.service;

import com.ega.model.EgaResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReportService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackReport", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000") },
            threadPoolKey = "testRestTemplate",
            threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "30"),
            @HystrixProperty(name = "maxQueueSize", value = "10") })
    public EgaResponse getReport() {
    /*
    due to @LoadLanced annotation restTemplate should look for registered service instead of directly accessing it
    String reportURL = "http://localhost:9090/report/?reportName=admin";
     */
            /*
            To get list of applications registred on eureka server
             discoveryClient.getApplications()
             */
        String reportURL = "http://ega-report-service/report/?reportName=admin";
        return restTemplate.getForObject(reportURL, EgaResponse.class);
    }

    @HystrixCommand(fallbackMethod = "getFallbackReport")
    public EgaResponse getFallbackReport() {
        EgaResponse response = new EgaResponse();
        response.setResult("FallbackReport");
        return response;
    }
}
