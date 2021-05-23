package com.ega.controller;

import com.ega.model.User;
import com.ega.repository.UserRepository;
import com.ega.security.EGAUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private EGAUserDetailsService userDetailsService;

    @GetMapping("/getAllUsers")
    public java.util.List<com.ega.model.User> getAllUsers(){
        return userDetailsService.findAll();
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String username){
        return this.userDetailsService.findUser(username);
    }
}
