package com.rc.mambasaerchbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / SpringDoc 配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Java Backend Template API")
                        .description("Java 后端模板项目接口文档")
                        .version("1.0.0")
                        .contact(new Contact().name("RC").email(""))
                        .license(new License().name("MIT")));
    }
}
