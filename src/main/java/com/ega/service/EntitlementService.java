package com.ega.service;

import com.ega.model.EgaResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EntitlementService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackEntitlement")
    public EgaResponse getEntitlement() {
        String entURL = "http://ega-entitlement-service/entl/2";
        EgaResponse entitlementResp = restTemplate.getForObject(entURL, EgaResponse.class);
        return entitlementResp;
    }

    public EgaResponse getFallbackEntitlement() {
        EgaResponse response = new EgaResponse();
        response.setResult("VIEWER");
        return response;
    }
}
