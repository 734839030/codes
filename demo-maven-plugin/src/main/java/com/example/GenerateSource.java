package com.example;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.codehaus.plexus.velocity.VelocityComponent;

import java.io.StringWriter;

/**
 * 生成代码
 */
public class GenerateSource {


    private VelocityComponent velocityComponent;

    public void write() {
        //VelocityComponent velocityComponent = (DefaultVelocityComponent) lookup(VelocityComponent.ROLE);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", "Velocity");
        Template template = velocityComponent.getEngine().getTemplate("vtl/FingerprintImpl.java.vm");
        StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
    }

}
