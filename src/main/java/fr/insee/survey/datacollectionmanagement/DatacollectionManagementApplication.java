package fr.insee.survey.datacollectionmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;


@SpringBootApplication
public class DatacollectionManagementApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DatacollectionManagementApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DatacollectionManagementApplication.class);
    }
}
