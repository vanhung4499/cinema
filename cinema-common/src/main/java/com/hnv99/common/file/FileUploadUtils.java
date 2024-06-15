package com.hnv99.common.file;

import com.hnv99.common.exception.FileNameLengthLimitExceededException;
import com.hnv99.common.exception.FileSizeLimitExceededException;
import com.hnv99.common.exception.InvalidExtensionException;
import com.hnv99.common.utils.StringUtil;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * File upload utility class
 */
public class FileUploadUtils {
    /**
     * Default size: 50MB
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * Default maximum file name length: 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * Default storage directory for images
     */
    @Getter
    private static final String parentPath = ClassUtils.getDefaultClassLoader().getResource("static/images").getPath();

    public static final String actorPath = "/actor";
    public static final String cinemaPath = "/cinema";
    public static final String moviePath = "/movie";
    public static final String userPath = "/user";


    /**
     * Default upload address
     */
    @Getter
    private static String defaultBaseDir = userPath;

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    /**
     * Upload files with default configuration
     *
     * @param file The file to upload
     * @return The file name
     * @throws IOException
     */
    public static String upload(MultipartFile file) throws IOException {
        try {
            return upload(getParentPath() + getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * File upload
     *
     * @param baseDir          The base directory relative to the application
     * @param file             The file to upload
     * @param allowedExtension The allowed file extensions
     * @return The uploaded file name
     * @throws FileSizeLimitExceededException       If the file size exceeds the maximum limit
     * @throws FileNameLengthLimitExceededException If the file name length exceeds the maximum limit
     * @throws IOException                          If there is an IO exception during file read/write
     * @throws InvalidExtensionException            If there is a file validation exception
     */
    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException {
        int fileNameLength = file.getOriginalFilename().length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException("File name length cannot exceed " + FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);

        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * Encode the file name, for example: images/user/2021/3/4/***.png
     */
    public static String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateFormatUtils.format(new Date(), "yyyy/MM/dd") + "/" + UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;
        return fileName;
    }

    private static File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    private static String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = parentPath.length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = "/images/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * File size validation
     *
     * @param file              The file to upload
     * @throws FileSizeLimitExceededException If the file size exceeds the maximum limit
     * @throws InvalidExtensionException      If the extension is invalid
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException {
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException("File size cannot exceed " + DEFAULT_MAX_SIZE / 1024 / 1024 + "MB");
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException("Image format not supported: " + extension);
            }
        }

    }

    /**
     * Check if the extension is allowed
     *
     * @param extension         The file extension
     * @param allowedExtension The allowed file extensions
     * @return True if the extension is allowed, false otherwise
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the extension of the file name
     *
     * @param file The form file
     * @return The file extension
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!StringUtil.isNotEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * Delete a file
     *
     * @param filePath The file path
     * @return True if the file is successfully deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // If the path is a file and not empty, then delete
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
