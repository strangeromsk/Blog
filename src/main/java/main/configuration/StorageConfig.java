package main.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class StorageConfig implements WebMvcConfigurer, CommandLineRunner {

    @Value("${uploadDir}")
    private String location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(location + "/**/")
                .addResourceLocations("file:" + location + "/");
    }

    @Override
    public void run(String... args) {
        init();
    }

    public void init() {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }
}
