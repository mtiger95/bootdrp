package com.bootdo.modular.engage.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 手动调整库存
 *
 * @author L
 * @since 2023-04-04 17:30
 */
@Data
public class BalanceAdjustParam {

    @Schema(description = "商品编号，多个逗号分隔")
    private String productNos;

}
