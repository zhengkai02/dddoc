package indi.zk.doc.generate.controller;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import indi.zk.doc.generate.enums.FileTypeEnum;
import indi.zk.doc.generate.model.DestinationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xju_zk
 * @version 1.0
 * @className DocGenerationConroller
 * @description //mysql数据库文档生成接口
 * @data 2020-08-08 00:31
 */
@RestController
@RequestMapping("/mysql")
public class DocGenerationConroller {

    @Value("${fileOutputDir}")
    private String fileOutputDir;

    @PostMapping("/model/doc")
    public void docmentGeneration(@RequestBody DestinationSource destinationSource) {

        String jdbcUrl = "jdbc:mysql://" + destinationSource.getHost() + ":" + destinationSource.getPort() + "/" + destinationSource.getDatabase()+"?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        // 数据源
        HikariConfig config = new HikariConfig();
        String driverName = "com.mysql.cj.jdbc.Driver";
        config.setDriverClassName(driverName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(destinationSource.getUsername());
        config.setPassword(destinationSource.getPassword());
        // 设置可以获取table remark信息
        config.addDataSourceProperty("useInformationSchema", "true");
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(config);
        // 生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成配置文件
                .fileOutputDir(fileOutputDir)
                // 打开目录
                .openOutputDir(true)
                //文件类型
                .fileType(FileTypeEnum.getEngineFileTypeByCode(destinationSource.getDocType()))
                // 生成模板
                .produceType(EngineTemplateType.freemarker)
                // 自定义文件名称
                .fileName(destinationSource.getFileName()).build();
        // 忽略表
        List<String> ignoreTables = Arrays.asList(destinationSource.getIgnoreTables());
        // 忽略表前缀
        List<String> ignorePrefix = Arrays.asList(destinationSource.getIgnorePrefix());
        // 忽略表后缀
        List<String> ignoreSuffix = Arrays.asList(destinationSource.getIgnoreSuffix());

        ProcessConfig processConfig = ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                // 根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                // 根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                .ignoreTableName(ignoreTables)
                .ignoreTablePrefix(ignorePrefix)
                .ignoreTableSuffix(ignoreSuffix).build();
        // 配置
        Configuration configuration = Configuration.builder()
                .version(destinationSource.getVersion())
                // 描述
                .description("数据库设计文档")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(processConfig)
                .build();
        // 执行生成
        new DocumentationExecute(configuration).execute();

    }
}
