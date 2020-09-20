package main.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    @Value("${uploadDir}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
