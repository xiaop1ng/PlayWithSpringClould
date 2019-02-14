package com.xiaoping.service;

import com.xiaoping.service.fallback.ServerHiFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi", fallback  = ServerHiFallBack.class)
public interface SchedualServerHi {

    @RequestMapping(value = "/hi")
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
