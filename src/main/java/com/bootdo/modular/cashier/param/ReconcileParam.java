package com.bootdo.modular.cashier.param;

import com.bootdo.core.enums.BillType;
import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.cashier.enums.DateTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 收款对账
 *
 * @author L
 * @since 2025-03-19 14:50
 */
@Data
public class ReconcileParam extends BaseParam {

    @NotNull
    @Schema(description = "日期类型")
    private DateTypeEnum dateType;

    @NotNull
    @Schema(description = "单据类型")
    private BillType billType;

    @Schema(description = "支付类型：收入、支出")
    private String payDirect;

    @Schema(description = "资金用途")
    private List<String> costType;


    /**
     * 填充参数
     */
    public ReconcileParam fillParam() {
        if (BillType.CW_SK_ORDER.equals(billType)) {
            setPayDirect("收入");
        } else {
            setCostType(Arrays.asList("经销商", "调货"));
        }
        return this;
    }

}
