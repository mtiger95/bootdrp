package com.bootdo.modular.engage.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 商品库存
 *
 * @author L
 * @since 2024-02-21 13:14
 */
@Data
public class BalanceQryParam extends BaseParam {

    @Schema(description = "库存日期")
    private Date toDate;

    @Schema(description = "仓库编号")
    private String stock;

    @Schema(description = "商品编号")
    private String productNo;

    @Schema(description = "商品类型")
    private String type;

    @Schema(description = "是否显示库存")
    private String showSto;

    @Schema(description = "商品状态")
    private String status;

}
