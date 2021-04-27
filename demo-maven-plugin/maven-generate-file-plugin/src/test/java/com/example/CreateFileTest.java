package com.example;

import com.example.utils.FreeMarkerRender;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateFileTest {

    @Test
    public void createFile() throws IOException {

        Path path = Paths.get(System.getProperty("user.dir"), "target/generated-sources/fit-kms", "com/sample/spi", "Test.java");
        String rendered = FreeMarkerRender.readTemplate("Test.java.tpl");
        FileUtils.writeStringToFile(path.toFile(), rendered, StandardCharsets.UTF_8);
    }
}
