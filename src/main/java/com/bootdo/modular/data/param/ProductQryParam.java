package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductQryParam extends BaseParam {

    @Schema(description = "类目分类")
    private String type;

    @Schema(description = "状态")
    private String status;

}
