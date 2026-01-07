package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 供应商管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class VendorQryParam extends BaseParam {

    @Schema(description = "类目分类")
    private String type;

    @Schema(description = "状态")
    private String status;

}
