package main.services;

import main.configuration.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir() + createDirs())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String generateTextMethod()   {
        String saltChars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        while (stringBuilder.length() < 2) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            stringBuilder.append(saltChars, index, index + 1);
        }
        return stringBuilder.toString();
    }

    private String createDirs(){
        String firstDir = generateTextMethod();
        String secondDir = generateTextMethod();
        String thirdDir = generateTextMethod();
        File f = new File("/resources/upload/" + firstDir + "/" + secondDir + "/" + thirdDir + "/");
        String path = "/" + firstDir + "/" + secondDir + "/" + thirdDir + "/";
        if(f.mkdir()){
            System.out.println("Success!");
        }
        return path;
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                throw new IllegalArgumentException();
            }
            String completePath = createDirs() + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return completePath;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
