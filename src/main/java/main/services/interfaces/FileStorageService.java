package main.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileStorageService {
    String storeFile(MultipartFile file, HttpServletRequest request);
    String storeFileResized(MultipartFile file, HttpServletRequest request);
}
