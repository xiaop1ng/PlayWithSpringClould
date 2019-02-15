package com.xiaoping.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {

    @Value("${version}")
    private String version;

    @RequestMapping("/version")
    public String version(){
        return version;
    }

}
