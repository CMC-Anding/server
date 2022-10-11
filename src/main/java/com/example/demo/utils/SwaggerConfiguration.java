package com.example.demo.utils;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Bean
  public Docket SwaggerApi(ServletContext servletContext) {

    return new Docket(DocumentationType.SWAGGER_2)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .apiInfo(swaggerInfo())
                .groupName("Anding")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.src"))
                .paths(PathSelectors.ant("/app/**"))
                .build()
                .useDefaultResponseMessages(false);  
  }

  

  private Set<String> getConsumeContentTypes() {
      Set<String> consumes = new HashSet<>();
      consumes.add("application/json;charset=UTF-8");
      consumes.add("application/x-www-form-urlencoded");
      return consumes;
  }

  private Set<String> getProduceContentTypes() {
      Set<String> produces = new HashSet<>();
      produces.add("application/json;charset=UTF-8");
      return produces;
  }

  private ApiInfo swaggerInfo() {
    return new ApiInfoBuilder().title("Anding API Documentation")
                .description("Anding팀의 API입니다")
                // .license("")
                // .licenseUrl("")
                .version("1.0")
                .build();
  }

}