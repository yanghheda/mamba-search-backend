package com.rc.mambasaerchbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rc.mambasaerchbackend.mapper")
public class MambaSearchBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MambaSearchBackendApplication.class, args);
    }
}
