package org.echo.dao.mapper.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * run this class when initialize the project
 * this will help to generate domains and mappers base on tables in database.
 * Created by Administrator on 8/13/2017.
 */
public class MybatisMapperGenerator {


    public static void main(String[] args) throws Exception {
        generator();
    }

    private static void generator() throws Exception {

        List<String> warnings = new ArrayList<>();
        //指定 逆向工程配置文件
        File configFile = new File("E:\\workspaces\\taotao\\management\\dao\\src\\test\\resources\\mybatis\\generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

    }

}
