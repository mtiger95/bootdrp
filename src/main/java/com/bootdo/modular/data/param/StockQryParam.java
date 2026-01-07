package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 仓库管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class StockQryParam extends BaseParam {

    @Schema(description = "状态")
    private String status;

    
}
