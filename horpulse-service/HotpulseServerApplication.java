package com.hotpulse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hotpulse.mapper")
public class HotpulseServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotpulseServerApplication.class, args);
    }

}
