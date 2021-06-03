package com.ega.controller;

import com.ega.model.EgaResponse;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ms")
public class MicroServicesIntController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public  WebClient.Builder getBuilder(){
        return WebClient.builder();
    }

    @GetMapping("/restTemplate")
    public ResponseEntity<Map> integratedUsingRestTemplate(){
            HttpStatus status = HttpStatus.NOT_FOUND;
            /*
            due to @LoadLanced annotation restTemplate should look for registered service instead of directly accessing it
            String reportURL = "http://localhost:9090/report/?reportName=admin";
             */
            /*
            To get list of applications registred on eureka server
             discoveryClient.getApplications()
             */
            String reportURL = "http://ega-report-service/report/?reportName=admin";
            EgaResponse reportsResp = restTemplate.getForObject(reportURL, EgaResponse.class);
            String entURL = "http://ega-entitlement-service/entl/2";
            EgaResponse entitlementResp = restTemplate.getForObject(entURL, EgaResponse.class);
            if(reportsResp.getResult() != null && entitlementResp.getResult() != null)
                status = HttpStatus.OK;
            Map<String, Object> data = new HashMap<>();
            data.put("reportResp", reportsResp.getResult());
            data.put("EntitlementResp", entitlementResp.getResult());
            ResponseEntity<Map> integratedResp = new ResponseEntity<>(data, HttpStatus.OK);
            return integratedResp;
    }

    @GetMapping("/webClientBuilder")
    public ResponseEntity<Map> integratedUsingWebClientBuilder(){
        HttpStatus status = HttpStatus.NOT_FOUND;
        String reportURL = "http://localhost:9090/report/?reportName=admin";
        EgaResponse reportsResp = builder.build()
                .get()
                .uri(reportURL)
                .retrieve()
                .bodyToMono(EgaResponse.class)
                .block();
        String entURL = "http://localhost:7070/entl/2";
        EgaResponse entlResp = builder.build()
                .get()
                .uri(entURL )
                .retrieve()
                .bodyToMono(EgaResponse.class)
                .block();
        if(reportsResp.getResult() != null
               && entlResp.getResult() != null)
            status = HttpStatus.OK;
        Map<String, Object> data = new HashMap<>();
        data.put("Report Response", reportsResp.getResult());
        data.put("Entitlement Response", entlResp.getResult());
        ResponseEntity<Map> integratedResp = new ResponseEntity<>(data, status);
        return integratedResp;
    }
}
