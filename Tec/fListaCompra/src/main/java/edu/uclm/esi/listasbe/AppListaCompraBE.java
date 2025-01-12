package edu.uclm.esi.listasbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.uclm.esi.fakeaccountsbe.config.EnvLoader;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class AppListaCompraBE extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
        // Cargar las variables desde el archivo .env antes de que inicie Spring Boot
        EnvLoader.loadEnv(".env");
        
		SpringApplication.run(AppListaCompraBE.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AppListaCompraBE.class);
	}
}