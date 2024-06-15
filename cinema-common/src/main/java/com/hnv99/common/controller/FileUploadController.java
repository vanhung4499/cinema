package com.hnv99.common.controller;

import com.hnv99.common.file.FileUploadUtils;
import com.hnv99.common.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Encapsulate image upload requests
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @PostMapping("/user")
    public ResponseResult uploadUser(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.userPath);
        String filename = FileUploadUtils.upload(file);
        log.debug("Uploaded file name: " + filename);
        return ResponseResult.success((Object) filename);
    }

    @PostMapping("/movie")
    public ResponseResult uploadMovie(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.moviePath);
        String filename = FileUploadUtils.upload(file);
        log.debug("Uploaded file name: " + filename);
        return ResponseResult.success((Object) filename);
    }

    @PostMapping("/cinema")
    public ResponseResult uploadCinema(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.cinemaPath);
        String filename = FileUploadUtils.upload(file);
        log.debug("Uploaded file name: " + filename);
        return ResponseResult.success((Object) filename);
    }

    @PostMapping("/actor")
    public ResponseResult uploadActor(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.actorPath);
        String filename = FileUploadUtils.upload(file);
        log.debug("Uploaded file name: " + filename);
        return ResponseResult.success((Object) filename);
    }

    @RequestMapping("/delete")
    public ResponseResult deletePicture(String filePath) {
        try {
            String path = ResourceUtils.getURL("classpath:").getPath().substring(1) + "static" + filePath;
            log.debug("Path to delete file: " + path);
            boolean flag = FileUploadUtils.deleteFile(path);
            log.debug(flag ? "Deletion successful" : "Deletion failed");
        } catch (FileNotFoundException e) {
            System.out.println("File to delete does not exist");
        } finally {
            return ResponseResult.success();
        }
    }

}
