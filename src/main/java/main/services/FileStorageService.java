package main.services;

import main.configuration.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String generateTextMethod(int length)   {
        String saltChars = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer strBuffer = new StringBuffer();
        java.util.Random rnd = new java.util.Random();
        while (strBuffer.length() < length) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            strBuffer.append(saltChars, index, index + 1);
        }
        return strBuffer.toString();
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                throw new IllegalArgumentException();
            }
            String firstDir = generateTextMethod(2);
            String secondDir = generateTextMethod(2);
            String thirdDir = generateTextMethod(2);
            String completePath = firstDir + "/" + secondDir + "/" + thirdDir + "/" + fileName;

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return completePath;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
