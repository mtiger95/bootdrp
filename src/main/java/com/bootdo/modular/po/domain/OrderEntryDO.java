package com.bootdo.modular.po.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bootdo.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 购货订单分录
 *
 * @author yogiCai
 * @since 2018-01-21 12:38:44
 */
@TableName(value = "po_order_entry")
@Data
public class OrderEntryDO extends BaseEntity {
    /**
     *
     */
    private Integer id;
    /**
     * 店铺编号
     */
    private String shopNo;
    /**
     * 单据编号
     */
    private String billNo;
    /**
     * 商品ID
     */
    private String entryId;
    /**
     * 商品名称
     */
    private String entryName;
    /**
     * 商品单位
     */
    private String entryUnit;
    /**
     * 商品单价
     */
    private BigDecimal entryPrice;
    /**
     * 仓库编号
     */
    private String stockNo;
    /**
     * 仓库名称
     */
    private String stockName;
    /**
     * 数量
     */
    private BigDecimal totalQty;
    /**
     * 金额
     */
    private BigDecimal entryAmount;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 优惠率
     */
    private BigDecimal discountRate;
    /**
     * 采购费用
     */
    private BigDecimal purchaseFee;
    /**
     * 合计金额
     */
    private BigDecimal totalAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 购货订单号
     */
    private String requestBillNo;

}
