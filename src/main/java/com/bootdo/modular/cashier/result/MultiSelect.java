package com.bootdo.modular.cashier.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (CashierRecord)实体类
 *
 * @author makejava
 * @since 2022-06-22 13:21:49
 */
@Data
public class MultiSelect {
    /**
     * 交易渠道
     */
    private List<Map<String, String>> type = new ArrayList<>();
    /**
     * 交易账号
     */
    private List<Map<String, String>> account = new ArrayList<>();

    /**
     * 交易方向
     */
    private List<Map<String, String>> payDirect = new ArrayList<>();

    /**
     * 交易状态
     */
    private List<Map<String, String>> payStatus = new ArrayList<>();

    /**
     * 交易分类
     */
    private List<Map<String, String>> tradeClass = new ArrayList<>();

    /**
     * 数据来源
     */
    private List<Map<String, String>> source = new ArrayList<>();

    /**
     * 资金用途
     */
    private List<Map<String, String>> costType = new ArrayList<>();

}

