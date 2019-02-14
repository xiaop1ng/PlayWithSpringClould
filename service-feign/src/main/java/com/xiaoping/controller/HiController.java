package com.xiaoping.controller;

import com.xiaoping.service.SchedualServerHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {

    @Autowired
    SchedualServerHi hiService;

    @GetMapping("/hi")
    public String hi(@RequestParam String name) {
        return hiService.sayHiFromClientOne(name);
    }
}
