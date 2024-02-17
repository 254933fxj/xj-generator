package org.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

public class main extends GenerateTemplate{
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        main mainTemplat = new main();
        mainTemplat.doGenerate();
    }
}
