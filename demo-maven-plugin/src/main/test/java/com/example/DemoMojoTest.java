package com.example;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * @see <a>https://maven.apache.org/plugin-testing/maven-plugin-testing-harness/getting-started/index.html</a>
 */
public class DemoMojoTest extends AbstractMojoTestCase {


    protected void setUp()
            throws Exception {
        // required
        super.setUp();
    }

    protected void tearDown()
            throws Exception {
        // required
        super.tearDown();
    }

    public void testSomething()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        DemoMojo demoMojo = (DemoMojo) lookupMojo("fingerprint", pom);
        demoMojo.execute();
    }
}