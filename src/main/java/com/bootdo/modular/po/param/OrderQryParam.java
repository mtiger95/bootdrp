package com.bootdo.modular.po.param;

import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 采购单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class OrderQryParam extends BaseParam {

    @Schema(description = "供应商（多个逗号分隔）")
    private String vendorId;

    @Schema(description = "订单类型")
    private String billType;

    @Schema(description = "审核状态")
    private String auditStatus;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "订单状态过滤")
    private List<OrderStatus> statusNot;

}
