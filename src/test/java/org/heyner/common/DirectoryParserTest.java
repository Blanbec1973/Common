package org.heyner.common;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DirectoryParserTest {

    private static final String TEST_DIRECTORY_BASE = "src/test/resources/";

    @Test
    void testMatchingFiles() throws IOException {
        DirectoryParser parser = new DirectoryParser(TEST_DIRECTORY_BASE + "test_directory", ".*\\.txt");
        List<Path> files = parser.getMatchingFiles();
        assertEquals(2, files.size());
        assertTrue(files.stream().anyMatch(file -> file.getFileName().toString().equals("file1.txt")));
        assertTrue(files.stream().anyMatch(file -> file.getFileName().toString().equals("file2.txt")));
    }

    @Test
    void testNoMatchingFiles() throws IOException {
        DirectoryParser parser = new DirectoryParser(TEST_DIRECTORY_BASE + "test_directory", ".*\\.jpg");
        List<Path> files = parser.getMatchingFiles();
        assertEquals(0, files.size());
    }

    @Test
    void testEmptyDirectory() throws IOException {
        DirectoryParser parser = new DirectoryParser(TEST_DIRECTORY_BASE + "empty_directory", ".*\\.txt");
        List<Path> files = parser.getMatchingFiles();
        assertEquals(0, files.size());
    }

    @Test
    void testComplexRegex() throws IOException {
        DirectoryParser parser = new DirectoryParser(TEST_DIRECTORY_BASE + "test_directory", "file[0-9]+\\.txt");
        List<Path> files = parser.getMatchingFiles();
        assertEquals(2, files.size());
        assertTrue(files.stream().anyMatch(file -> file.getFileName().toString().equals("file1.txt")));
        assertTrue(files.stream().anyMatch(file -> file.getFileName().toString().equals("file2.txt")));
    }
}
