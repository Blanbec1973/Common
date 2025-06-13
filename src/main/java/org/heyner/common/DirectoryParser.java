package org.heyner.common;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DirectoryParser {
    private final List<Path> matchingFiles;

    public DirectoryParser(String directoryPath, String regex) throws IOException {
        matchingFiles = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path entry : stream) {
                Matcher matcher = pattern.matcher(entry.getFileName().toString());
                if (matcher.matches()) {
                    matchingFiles.add(entry);
                }
            }
        }
    }

    public List<Path> getMatchingFiles() {
        return matchingFiles;
    }

    public boolean isEmpty() { return matchingFiles.isEmpty();}

}

