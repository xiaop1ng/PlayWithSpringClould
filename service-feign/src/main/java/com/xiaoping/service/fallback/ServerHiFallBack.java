package com.xiaoping.service.fallback;

import com.xiaoping.service.SchedualServerHi;
import org.springframework.stereotype.Component;

@Component
public class ServerHiFallBack implements SchedualServerHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "Sry, " + name + ", Server Error.";
    }
}
