package main.services;

import lombok.extern.slf4j.Slf4j;
import main.configuration.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@Scope("prototype")
public class FileStorageService {

    private final String dirsNames;
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.dirsNames = getDirsNames();
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir() + dirsNames)
                .toAbsolutePath().normalize();
    }

    private String generateTextMethod(int length)   {
        String saltChars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        while (stringBuilder.length() < length) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            stringBuilder.append(saltChars, index, index + 1);
        }
        return stringBuilder.toString();
    }

    private String getDirsNames(){
        String firstDir = generateTextMethod(2);
        String secondDir = generateTextMethod(2);
        String thirdDir = generateTextMethod(2);
        return "/" + firstDir + "/" + secondDir + "/" + thirdDir + "/";
    }

    private boolean createDirs(String path){
        File f = new File("resources/upload" + path);
        return f.mkdirs();
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                throw new IllegalArgumentException();
            }
            String completePath = dirsNames + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            createDirs(dirsNames);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return completePath;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String storeFileResized(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                throw new IllegalArgumentException();
            }
            File input = new File(fileName);
            file.transferTo(input);
            BufferedImage image = ImageIO.read(input);
            BufferedImage resized = resize(image);
            createDirs(dirsNames);
            String newPicPath = "resources/upload" + dirsNames + generateTextMethod(13) + ".png";
            File output = new File(newPicPath);

            ImageIO.write(resized, "png", output);
            return "http://localhost:8080/" + newPicPath;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static BufferedImage resize(BufferedImage img) {
        Image tmp = img.getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(36, 36, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
