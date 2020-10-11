package main.services;

import lombok.extern.slf4j.Slf4j;
import main.API.ResponseApi;
import main.configuration.FileStorageProperties;
import main.services.interfaces.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Scope("prototype")
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${uploadDir}")
    private String uploadDir;
    private final String dirsNames;

    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.dirsNames = getDirsNames();
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
        File f = new File(uploadDir + path);
        return f.mkdirs();
    }

    public ResponseEntity storeFile(MultipartFile file, HttpServletRequest request) {
        long maxSize = 10_000_000;
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Optional<String> extension = getExtensionByStringHandling(file.getOriginalFilename());
        Map<String, String> errors = new HashMap<>(8);

        if(file.getSize() > maxSize){
            errors.put("file", "File size is too large");
        }
        if(extension.isPresent() && !extension.get().equals("png") && !extension.get().equals("jpg")){
            errors.put("file", "File extension is not correct");
        }else if(extension.isEmpty()){
            errors.put("file", "File extension is empty");
        }
        if(errors.size() == 0){
            try {
                if(fileName.contains("..")) {
                    throw new IllegalArgumentException();
                }
                File input = new File(fileName);
                file.transferTo(input);
                BufferedImage image = ImageIO.read(input);
                createDirs(dirsNames);
                String newPicPath = uploadDir + dirsNames + generateTextMethod(13) + ".png";
                String returnPicPath = "/" + newPicPath;
                File output = new File(newPicPath);
                ImageIO.write(image, "png", output);
                return new ResponseEntity<>(returnPicPath, HttpStatus.OK);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<>(ResponseApi.builder().errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    public String storeFileResized(MultipartFile file, HttpServletRequest request) {
        String currentProjectFolder = String.format("%s://%s:%d/", request.getScheme(),
                                                                    request.getServerName(),
                                                                    request.getServerPort());
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
            String newPicPath = uploadDir + dirsNames + generateTextMethod(13) + ".png";
            File output = new File(newPicPath);
            ImageIO.write(resized, "png", output);
            return currentProjectFolder + newPicPath;
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
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
