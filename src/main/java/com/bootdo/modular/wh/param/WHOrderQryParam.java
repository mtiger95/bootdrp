package com.bootdo.modular.wh.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 仓库盘点
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class WHOrderQryParam extends BaseParam {

    @Schema(description = "类目分类")
    private String serviceType;

    @Schema(description = "类目分类")
    private String auditStatus;

    @Schema(description = "类目分类")
    private String billType;

}
