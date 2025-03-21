package es.upm.miw.companyds.tfm_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class TfmSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TfmSpringApplication.class, args);
	}

}
