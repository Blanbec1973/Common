package org.heyner.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestInitializerFactory {

    private static String pathTest= "em";
    public static String getPathTest() {return pathTest;}

    public static void action(String suffix) throws IOException {
        pathTest = "target/temp-"+suffix;
        Path path = Paths.get(pathTest);
        if (Files.exists(path))
            FileUtils.cleanDirectory(new File(pathTest));
        FileUtils.copyDirectory(new File ("src/test/resources/temp/"),
                new File(pathTest+"/"));
    }

}
