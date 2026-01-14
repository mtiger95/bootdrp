package com.bootdo.core.consts;

/**
 * @Author: yogiCai
 * @since 2018-02-04 10:27:25
 */
public final class ErrorMessage {

    //参数相关.
    public static final String PARAM_INVALID = "参数有误";
    public static final String STATUS_AUDIT_YES = "已审核的订单不能%s";
    public static final String CW_ORDER_REMOVE = "请先删除相关联%s %s";
    public static final String CW_ORDER_AUDIT = "请先反审核相关联 %s %s";
    public static final String RP_ORDER_AUDIT = "请先审核 %s %s";
    public static final String RP_ORDER_CHECK_ORDER_NULL = "核销订单、核销金额不能为空";
    public static final String RP_ORDER_SETTLE_ITEM_NULL = "收款账户、收款金额不能为空";
    public static final String DATA_NAME_DUPLICATE = "%s名称重复";

}
