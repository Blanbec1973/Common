package org.heyner.common;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {

    @Test
    void addSuffixTestWithExtension() {
        // Arrange
        Path inputPath = Paths.get("./Mon dossier/Mon fichier.xlsx");
        String suffix = "Analyze";

        // Act
        String result = FileUtils.addSuffixToFileName(inputPath,suffix);

        // Construire le chemin attendu
         String fileName = inputPath.getFileName().toString();
         String parentPath = inputPath.getParent().toString();
         String expectedFileName = fileName.substring(0, fileName.lastIndexOf('.')) + "-" + suffix + fileName.substring(fileName.lastIndexOf('.'));
         String expectedPath = parentPath + File.separator + expectedFileName;

         // Assert
        assertEquals(expectedPath,result);
    }
    @Test
    void addSuffixTestWithoutExtension() {
        // Arrange
        Path inputPath = Paths.get("./Mon dossier/MonFichier");
        String suffix = "Analyze";

        // Act
        String resultWithoutExtension  = FileUtils.addSuffixToFileName(inputPath,suffix);

        // Construire le chemin attendu
        String fileNameWithoutExtension = inputPath.getFileName().toString();
        String parentPathWithoutExtension = inputPath.getParent().toString();
        String expectedFileNameWithoutExtension = fileNameWithoutExtension + "-" + suffix;
        String expectedPathWithoutExtension = parentPathWithoutExtension + File.separator + expectedFileNameWithoutExtension;



        // Assert
        assertEquals(expectedPathWithoutExtension,resultWithoutExtension);
    }
}