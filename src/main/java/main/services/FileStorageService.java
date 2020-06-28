package main.services;

import main.configuration.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    private String createDirs(){
        String firstDir = generateTextMethod(2);
        String secondDir = generateTextMethod(2);
        String thirdDir = generateTextMethod(2);
        String path = "/" + firstDir + "/" + secondDir + "/" + thirdDir + "/";
        File f = new File("/resources/upload" + path);
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

    public String storeFileResized(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                throw new IllegalArgumentException();
            }
            String completePath = createDirs() + fileName;

//            Path filepath = Paths.get("/resources/upload" + completePath, file.getOriginalFilename());
//            OutputStream os = Files.newOutputStream(filepath);
//            os.write(file.getBytes());
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
//            file.transferTo(convFile);

            File input = new File(fileName);
            file.transferTo(input);
            BufferedImage image = ImageIO.read(input);
            BufferedImage resized = resize(image);
            String newPicPath = "resources/upload" + createDirs() + generateTextMethod(13) + ".jpg";
            File output = new File(newPicPath);
            ImageIO.write(resized, "jpg", output);

            return newPicPath;
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
