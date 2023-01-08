//package com.quanta.vi;
//
//
//import com.baomidou.mybatisplus.generator.FastAutoGenerator;
//import com.baomidou.mybatisplus.generator.config.OutputFile;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class CodeGenerator {
//    public static void main(String[] args) {
//        /* ************ 配置部分 ************ */
//        String url = ""; // 数据库url
//        String username = "root"; // 数据库用户名
//        String password = "root"; // 数据库密码
//        List<String> tables = new ArrayList<>();
//        // 添加要生成的表名
////        tables.add("book");
////        tables.add("collection");
////        tables.add("daily_record");
////        tables.add("learn_record");
////        tables.add("news");
////        tables.add("sentence");
////        tables.add("statistics");
////        tables.add("user");
////        tables.add("word");
////        tables.add("word_book");
////        tables.add("word_bool");
//
//
//        String parent = "com.quanta"; // 项目包名 com.quanta
//        String mode = "vi"; // 模块名 archetype
//        String author = "quanta";
//        /* ************ 配置部分 ************ */
//
//        String projectPath = System.getProperty("user.dir");
//        String outPutDir = projectPath + "/src/main/java";
//        FastAutoGenerator.create(url, username, password)
//                .globalConfig(builder -> {
//                    builder.author(author) // 设置作者
////                            .fileOverride() // 覆盖已生成文件
//                            .outputDir(outPutDir); // 指定输出目录
//                })
//                .packageConfig(builder -> {
//                    builder.parent(parent)
//                            .moduleName(mode)
//                            .entity("entity")
//                            .service("service")
//                            .serviceImpl("service.serviceImpl")
//                            .controller("controller")
//                            .mapper("mapper")
//                            .xml("mapper")
//                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "\\src\\main\\resources\\mapper"));
//                })
//                .strategyConfig(builder -> {
//                    builder.addInclude(tables)
//                            .serviceBuilder()
//                            .formatServiceFileName("%sService")
//                            .formatServiceImplFileName("%sServiceImpl")
//                            .entityBuilder()
//                            .enableLombok()
//                            .logicDeleteColumnName("deleted")
//                            .enableTableFieldAnnotation()
//                            .controllerBuilder()
//                            .formatFileName("%sController")
//                            .enableRestStyle()
//                            .mapperBuilder()
//                            .enableBaseResultMap()  //生成通用的resultMap
//                            .formatMapperFileName("%sMapper")
//                            .enableMapperAnnotation()
//                            .formatXmlFileName("%sMapper");
//                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
//                .execute();
//    }
//}