package indi.zk.doc.generate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xju_zk
 * @version 1.0
 * @className DestinationSource
 * @description //TODO
 * @data 2020-08-08 00:40
 */
@Data
public class DestinationSource {
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    private Integer docType;
    private String fileName;
    private String[] ignoreTables;
    private String[] ignorePrefix;
    private String[] ignoreSuffix;
    private String version;


}
