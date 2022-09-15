package fr.insee.survey.datacollectionmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DatacollectionManagementApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(DatacollectionManagementApplication.class, args);
	}

}
