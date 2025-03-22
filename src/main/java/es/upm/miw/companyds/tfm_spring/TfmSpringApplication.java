package es.upm.miw.companyds.tfm_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity()
public class TfmSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TfmSpringApplication.class, args);
	}

}
