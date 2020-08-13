package indi.zk.doc.generate.enums;

import cn.smallbun.screw.core.engine.EngineFileType;
import lombok.Getter;

/**
 * @author xju_zk
 * @version 1.0
 * @className FileTypeEnum
 * @description //目标文件类型枚举
 * @data 2020-08-08 00:56
 */
@Getter
public enum FileTypeEnum {

    // 0代表HTML格式
    HTML(0, EngineFileType.HTML),
    // 1代表WORD格式
    WORD(1, EngineFileType.WORD),
    // 2代表MD格式
    MD(2, EngineFileType.MD);

    /**
     * 编码：0/1/2
     */
    private int code;
    // 目标文件类型
    private EngineFileType engineFileType;

    FileTypeEnum(Integer code, EngineFileType engineFileType) {
        this.code = code;
        this.engineFileType = engineFileType;
    }

    public static EngineFileType getEngineFileTypeByCode(Integer code) {
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.code == code) {
                return fileTypeEnum.engineFileType;
            }
        }
        return null;
    }
}
