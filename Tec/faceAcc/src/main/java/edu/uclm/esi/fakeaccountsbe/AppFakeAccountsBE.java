package edu.uclm.esi.fakeaccountsbe;

import edu.uclm.esi.fakeaccountsbe.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class AppFakeAccountsBE extends SpringBootServletInitializer {

    public static void main(String[] args) {
        // Cargar las variables desde el archivo .env antes de que inicie Spring Boot
        EnvLoader.loadEnv(".env");

        // Iniciar la aplicación Spring Boot
        SpringApplication.run(AppFakeAccountsBE.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AppFakeAccountsBE.class);
    }
}
