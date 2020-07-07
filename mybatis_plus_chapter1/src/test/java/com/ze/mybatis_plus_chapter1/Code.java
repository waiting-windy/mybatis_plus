package com.ze.mybatis_plus_chapter1;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @Author lizeyu
 * @Date 2020/7/6 21:48
 */
public class Code {

    public static void main(String[] args) {

        //需要构建一个代码自动生成器对象
        AutoGenerator generator = new AutoGenerator();
        //配置策略

        //1、全局策略
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("lizeyu");
        gc.setServiceName("%sService");
        gc.setFileOverride(false);
        gc.setSwagger2(true);
        generator.setGlobalConfig(gc);

        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/mybatis?useSSl=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("111111");
        dsc.setDbType(DbType.MYSQL);
        generator.setDataSource(dsc);

        //3、包的设置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("blog");
        pc.setParent("com.ze");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        generator.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("user");
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategyConfig.setSuperEntityClass("父类实体");
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setLogicDeleteFieldName("isDeleted");
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategyConfig.setTableFillList(tableFills);
        strategyConfig.setVersionFieldName("version");
        strategyConfig.setControllerMappingHyphenStyle(true);
        generator.setStrategy(strategyConfig);

        generator.execute();

    }
}
