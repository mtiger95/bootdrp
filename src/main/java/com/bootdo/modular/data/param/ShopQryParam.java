package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShopQryParam extends BaseParam {

    @Schema(description = "店铺名称")
    private String name;

    @Schema(description = "店铺管理员")
    private String managerId;

    @Schema(description = "状态")
    private String status;

}
