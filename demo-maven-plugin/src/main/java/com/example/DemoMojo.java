package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * mojo name is goal.
 *
 * @see <a>https://maven.apache.org/guides/plugin/guide-java-plugin-development.html</a>
 */
@Mojo(name = "fingerprint", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class DemoMojo extends AbstractMojo {

    @Parameter(name = "fingerprint.skip", defaultValue = "false")
    private boolean skip;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().warn("fingerprint plugin skip");
            return;
        }
        getLog().info("fingerprint generate complete");
    }
}
