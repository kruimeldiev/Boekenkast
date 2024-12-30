package nl.casperdaris.boekenkast.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;

// TODO: Add documentation
@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String uploadBookCoverImage(@NonNull MultipartFile sourceFile,
            @NonNull Integer userId) {
        final String fileUploadSubpathString = "users/" + userId;

        return uploadFile(sourceFile, fileUploadSubpathString);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile, @NonNull String fileUploadSubpathString) {
        final String finalUploadPath = fileUploadPath + "/" + fileUploadSubpathString;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + "/" + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetFilePath;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
