package org.heyner.common;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void addSuffixTest() {
        // Arrange
        String input = "C:\\Mon dossier\\Mon fichier.xlsx";
        String suffix = "Analyze";

        // Act
        String result = FileUtils.addSuffixToFileName(input,suffix);

        // Assert
        assertEquals("C:\\Mon dossier\\Mon fichier-Analyze.xlsx",result);
    }

}