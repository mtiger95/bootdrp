package com.bootdo.modular.rp.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.rp.enums.PointSearchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 积分管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class PointQryParam extends BaseParam {

    @Schema(description = "类目分类")
    private PointSearchType type;

    @Schema(description = "积分状态")
    private String status;

    @Schema(description = "客户ID")
    private String consumerId;

}
