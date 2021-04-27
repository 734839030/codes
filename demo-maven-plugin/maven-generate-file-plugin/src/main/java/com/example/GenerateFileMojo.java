package com.example;

import com.example.utils.FreeMarkerRender;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * @author hdf
 * @see <a>http://maven.apache.org/guides/plugin/guide-java-plugin-development.html</a>
 * @see <a>http://maven.apache.org/plugin-tools/maven-plugin-tools-annotations/index.html</a>
 * <p>
 * defaultPhase valid when pom configure executions
 */
@Mojo(name = "generate-file", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateFileMojo extends AbstractMojo {

    private static final String LOG_FLAG = ">>>>>>>>>  ";
    @Parameter(property = "generate.file.skip", defaultValue = "false")
    private boolean skip;
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/fit-kms")
    private String outputDirectory;

    private String sourcePackagePath = "com/example/spi";

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            this.getLog().warn(LOG_FLAG + "create sample goal skiped");
            return;
        }
        String rendered = FreeMarkerRender.readTemplate("Test.java.tpl");
        try {
            FileUtils.writeStringToFile(Paths.get(outputDirectory, sourcePackagePath, "Test.java").toFile(), rendered, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new MojoExecutionException(LOG_FLAG + "create sample generated-sources fail", e);
        }

        mavenProject.addCompileSourceRoot(outputDirectory);
        this.getLog().info(LOG_FLAG + "success sample source file");
    }
}
