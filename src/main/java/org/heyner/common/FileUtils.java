package org.heyner.common;

import java.io.File;

public class FileUtils {

    private FileUtils() {}

    public static String addSuffixToFileName(String filePath, String suffix) {
        File file = new File(filePath);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
 
        // Si le fichier a une extension
        if (dotIndex != -1) {
            String nameWithoutExtension = fileName.substring(0, dotIndex);
            String extension = fileName.substring(dotIndex);
            return file.getParent() + File.separator + nameWithoutExtension + "-" + suffix + extension;
        } else {
            // Si le fichier n'a pas d'extension
            return file.getParent() + File.separator + fileName + "-" + suffix;
            }
    }
}
