package com.bootdo.modular.cashier.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 导入订单
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class RecordQryParam extends BaseParam {

    @Schema(description = "交易渠道")
    private String type;

    @Schema(description = "交易账号")
    private String account;

    @Schema(description = "交易方向")
    private String payDirect;

    @Schema(description = "交易状态")
    private String payStatus;

    @Schema(description = "交易类型")
    private String tradeClass;

    @Schema(description = "数据来源")
    private String source;

    @Schema(description = "资金用途")
    private String costType;

}
