package com.ega.controller;

import com.ega.model.EgaResponse;
import com.ega.service.EntitlementService;
import com.ega.service.ReportService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    private WebClient.Builder builder;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EntitlementService entitlementService;

    @Autowired
    private ReportService reportService;

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        /*HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        return new RestTemplate(clientHttpRequestFactory);*/
        return new RestTemplate();
    }

    @Bean
    public  WebClient.Builder getBuilder(){
        return WebClient.builder();
    }

    @GetMapping("/restTemplate")
    //@HystrixCommand(fallbackMethod = "fallbackIntegratedUsingRestTemplate")
    public ResponseEntity<Map> integratedUsingRestTemplate(){
            HttpStatus status = HttpStatus.NOT_FOUND;
        EgaResponse reportsResp = reportService.getReport();
        EgaResponse entitlementResp = entitlementService.getEntitlement();
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

    public ResponseEntity<Map> fallbackIntegratedUsingRestTemplate(){
        Map<String, Object> data = new HashMap<>();
        data.put("reportResp", "Reports");
        data.put("EntitlementResp", true);
        ResponseEntity<Map> integratedResp = new ResponseEntity<>(data, HttpStatus.OK);
        return integratedResp;
    }
}
