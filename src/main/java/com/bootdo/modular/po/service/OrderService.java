package com.bootdo.modular.po.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillSource;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.service.CostAmountCalculator;
import com.bootdo.modular.po.convert.OrderConverter;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.dao.OrderEntryDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.rp.convert.RPOrderConverter;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.dao.RPOrderEntryDao;
import com.bootdo.modular.rp.dao.RPOrderSettleDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author L
 */
@Service
public class OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private RPOrderDao rpOrderDao;
    @Resource
    private RPOrderEntryDao rpOrderEntryDao;
    @Resource
    private RPOrderSettleDao rpOrderSettleDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private CostAmountCalculator costAmountCalculator;


    public OrderDO get(Integer id) {
        return orderDao.get(id);
    }

    public List<OrderDO> list(Map<String, Object> map) {
        return orderDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return orderDao.count(map);
    }

    public int save(OrderDO order) {
        return orderDao.save(order);
    }


    @Transactional(rollbackFor = Exception.class)
    public int audit(Map<String, Object> params) {
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus"));
        List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", MapUtil.get(params, "billNos", List.class)));
        //去除已经是审核（未审核）状态的订单
        List<OrderDO> orderDOList1 = orderDOList.stream()
                .filter(orderDO -> !auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //审核采购单重新计算商品成本、生成收付款单
        if (CollectionUtils.isNotEmpty(orderDOList1)) {
            handleCostAndAudit(orderDOList1, auditStatus);
            handleRPOrder(orderDOList1, auditStatus);
        }
        return 1;
    }

    /**
     * 重新计算商品成本及审核订单
     */
    private int handleCostAndAudit(List<OrderDO> orderDOList, AuditStatus auditStatus) {
        for (OrderDO orderDO : orderDOList) {
            costAmountCalculator.calcPOBillCost(orderDO, auditStatus);
            orderDao.audit(ImmutableMap.of("billNo", orderDO.getBillNo(), "auditStatus", auditStatus.name()));
        }
        return 1;
    }

    /**
     * 付款单入库
     */
    private int handleRPOrder(List<OrderDO> orderDOList, AuditStatus auditStatus) {
        if (AuditStatus.NO.equals(auditStatus)) return 0;
        for (OrderDO orderDO : orderDOList) {
            //是否存在手工创建的收付款单，若存在则系统不自动处理收付款单
            int countRP = rpOrderDao.countRP(ImmutableMap.of("billSource", BillSource.USER.name(), "srcBillNo", orderDO.getBillNo()));
            if (countRP <= 0) {
                String rpBillNo = "";
                List<RPOrderDO> rpOrderDOList = rpOrderDao.list(ImmutableMap.of("srcBillNo", orderDO.getBillNo()));
                if (rpOrderDOList.size() > 0) {
                    rpBillNo = rpOrderDOList.get(0).getBillNo();
                    rpOrderDao.delete(ImmutableMap.of("billNo", rpBillNo)); //源订单号要保留
                    rpOrderEntryDao.delete(ImmutableMap.of("billNo", rpBillNo));
                    rpOrderSettleDao.delete(ImmutableMap.of("billNo", rpBillNo));
                }
                if (orderDO.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    continue; //本次付款为0不用生成付款单
                }
                List<AccountDO> accountDOList = accountDao.list(ImmutableMap.of("nos", ImmutableSet.of(orderDO.getSettleAccount())));
                RPOrderDO rpOrderDO = OrderConverter.convertRPOrder(orderDO);
                //源订单号要保留
                rpOrderDO.setBillNo(StrUtil.isEmpty(rpBillNo) ? rpOrderDO.getBillNo() : rpBillNo);
                List<RPOrderEntryDO> rpOrderEntryDOList = OrderConverter.convertRPOrderEntry(rpOrderDO, orderDO);
                List<RPOrderSettleDO> rpOrderSettleDOList = OrderConverter.convertRPOrderSettle(rpOrderDO, orderDO, RPOrderConverter.convertAccountMap(accountDOList));
                rpOrderDao.save(rpOrderDO);
                rpOrderEntryDao.saveBatch(rpOrderEntryDOList);
                rpOrderSettleDao.saveBatch(rpOrderSettleDOList);
            }
        }
        return 1;
    }

    public int remove(Integer id) {
        return orderDao.remove(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchRemove(List<String> billNos) {
        orderDao.delete(ImmutableMap.of("billNos", billNos));
        orderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }

}