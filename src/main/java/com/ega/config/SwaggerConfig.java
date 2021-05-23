package com.ega.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket customConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/*"))
                .apis(RequestHandlerSelectors.basePackage("com.ega"))
                .build().apiInfo(this.getApiInfo());
    }

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo("EGA documentation", "EGA api docs", "1.0",
                "www.EnglishGuruAcademy.com/terms",
                new Contact("EGA", "www.EnglishGuruAcademy.com", "EGA@gmail.com"),
                "Licence of EGA", "www.EnglishGuruAcademy.com/licence", Collections.emptyList());
        return apiInfo;
    }
}
