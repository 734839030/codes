package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * @author hdf
 * @see <a>http://maven.apache.org/guides/plugin/guide-java-plugin-development.html</a>
 * @see <a>http://maven.apache.org/plugin-tools/maven-plugin-tools-annotations/index.html</a>
 * <p>
 * defaultPhase valid when pom configure executions
 */
@Mojo(name = "generate-file", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateFileMojo extends AbstractMojo {

    @Parameter(property = "generate.file.skip", defaultValue = "false")
    private boolean skip;

    private Generator generator;

    /**
     * also {@code @Parameter(defaultValue = "${project}", readonly = true)}
     */
    @Component
    private MavenProject mavenProject;

    @Component
    private MavenProjectHelper mavenProjectHelper;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            this.getLog().warn(">>>>>>>>>>>> generate-file execute test skiped");
            return;
        }

        this.getLog().warn(">>>>> getArtifactId " + mavenProject.getArtifactId());
        this.getLog().warn(">>>>>" + mavenProjectHelper);
        this.getLog().info(">>>>>>>>>>>> generate-file execute test ");
    }
}
