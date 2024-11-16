package nl.casperdaris.boekenkast.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import io.micrometer.common.util.StringUtils;

public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (Exception e) {
            return null;
        }
    }
}
