package com.tessnd.games_assets.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void save(MultipartFile file) throws IllegalStateException, IOException {
        String dir = System.getProperty("user.dir") + "/" + uploadDir;
        file.transferTo(new File(dir + file.getOriginalFilename()));
    }

    public Resource getFile(String fileName) {
        String dir = System.getProperty("user.dir") + "/" + uploadDir;
        Path path = Paths.get(dir);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            return resource;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

     }
}
