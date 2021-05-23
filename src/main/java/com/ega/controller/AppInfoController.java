package com.ega.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/app")
@Slf4j
public class AppInfoController {

    @GetMapping("/info")
    public String appInfo() {
        log.trace("A TRACE Message");
        log.debug("A DEBUG Message");
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");

        if(log.isWarnEnabled())
            log.warn("Entering method appInfo()...");

        log.info("...exiting method appInfo()");
        return "Application up & running!";
    }

    @GetMapping("/admin")
    public String adminDashboard(){
        return "This is admin dashboard!";
    }

    @GetMapping("/user")
    public String userDashboard(){
        return "This is user dashboard!";
    }

    @GetMapping("/default")
    public String landingPage(){
        return "Welcome";
    }

    @GetMapping("/errorPage")
    public String errorPage(){
        return "Error PAge!";
    }

    @PostMapping("/errorResponse")
    public String errorResponse(){
        System.out.println("Failure Error!!!");
        log.error("Failure Error!!!!!!!");
        return "Error occured!!!";
    }

}
