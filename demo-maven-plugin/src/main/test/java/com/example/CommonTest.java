package com.example;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CommonTest {

    @Test
    public void testBaseDir() throws IOException {
        System.out.println(System.getProperty("basedir"));
        System.out.println(new File(".").getCanonicalFile().getAbsolutePath());
    }
}
