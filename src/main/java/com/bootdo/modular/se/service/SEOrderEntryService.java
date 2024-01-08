package com.bootdo.modular.se.service;


import cn.hutool.core.util.ObjectUtil;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.dao.ConsumerDao;
import com.bootdo.modular.data.dao.ProductDao;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.service.StockService;
import com.bootdo.modular.engage.dao.ProductCostDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.se.param.SEOrderVO;
import com.bootdo.modular.se.dao.SEOrderDao;
import com.bootdo.modular.se.dao.SEOrderEntryDao;
import com.bootdo.modular.system.dao.UserDao;
import com.bootdo.modular.system.domain.UserDO;
import com.bootdo.modular.se.param.SEOrderEntryVO;
import com.bootdo.modular.se.convert.SEOrderConverter;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.domain.SEOrderEntryDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class SEOrderEntryService {
    @Resource
    private SEOrderDao seOrderDao;
    @Resource
    private SEOrderEntryDao seOrderEntryDao;
    @Resource
    private ConsumerDao consumerDao;
    @Resource
    private UserDao userDao;
    @Resource
    private StockService stockService;
    @Resource
    private ProductDao productDao;
    @Resource
    private ProductCostDao productCostDao;

    @Transactional(rollbackFor = Exception.class)
    public SEOrderDO save(SEOrderVO orderVO) {
        UserDO userDO = userDao.get(NumberUtils.toLong(orderVO.getBillerId()));
        ConsumerDO consumerDO = consumerDao.get(NumberUtils.toInt(orderVO.getConsumerId()));
        Map<String, StockDO> stockDOMap = stockService.listStock(Maps.newHashMap());
        Map<String, ProductCostDO> costDOMap = convertProductCostMap(orderVO);
        Map<String, BigDecimal> purchaseMap = convertPurchaseMap(orderVO);//商品采购价信息
        SEOrderDO orderDO = SEOrderConverter.convertOrder(orderVO, userDO, consumerDO);
        List<SEOrderEntryDO> orderEntryDOList = SEOrderConverter.convertOrderEntry(orderVO, orderDO, stockDOMap, costDOMap, purchaseMap);
        //订单入库
        seOrderDao.save(orderDO);
        seOrderEntryDao.delete(ImmutableMap.of("billNo", orderDO.getBillNo()));
        seOrderEntryDao.saveBatch(orderEntryDOList);
        return orderDO;
    }

    public SEOrderVO getOrderVO(Map<String, Object> params) {
        List<SEOrderDO> orderDOList = seOrderDao.list(params);
        List<SEOrderEntryDO> orderEntryDOList = seOrderEntryDao.list(params);
        if (CollectionUtils.isEmpty(orderDOList) || CollectionUtils.isEmpty(orderEntryDOList)) {
            return new SEOrderVO();
        }
        SEOrderVO orderVO = new SEOrderVO();
        SEOrderDO orderDO = orderDOList.get(0);
        orderVO.setBillDate(orderDO.getBillDate());
        orderVO.setBillNo(orderDO.getBillNo());
        orderVO.setBillerId(orderDO.getBillerId());
        orderVO.setConsumerId(orderDO.getConsumerId());
        orderVO.setConsumerName(orderDO.getConsumerName());
        orderVO.setEntryAmountTotal(orderDO.getEntryAmount());
        orderVO.setFinalAmountTotal(orderDO.getFinalAmount());
        orderVO.setPaymentAmountTotal(orderDO.getPaymentAmount());
        orderVO.setDiscountAmountTotal(orderDO.getDiscountAmount());
        orderVO.setDiscountRateTotal(orderDO.getDiscountRate());
        orderVO.setSettleAccountTotal(orderDO.getSettleAccount());
        orderVO.setDebtAccountTotal(NumberUtils.subtract(NumberUtils.add(orderDO.getFinalAmount(), orderDO.getExpenseFee()), orderDO.getPaymentAmount()));
        orderVO.setExpenseFeeTotal(orderDO.getExpenseFee());
        orderVO.setPurchaseFeeTotal(orderDO.getPurchaseFee());
        orderVO.setBillSource(ObjectUtil.defaultIfNull(orderDO.getBillSource(), BillSource.USER));
        orderVO.setRemark(orderDO.getRemark());
        orderVO.setAuditStatus(orderDO.getAuditStatus());
        for (SEOrderEntryDO orderEntryDO : orderEntryDOList) {
            SEOrderEntryVO entryVO = new SEOrderEntryVO();
            entryVO.setId(orderEntryDO.getId());
            entryVO.setEntryId(orderEntryDO.getEntryId());
            entryVO.setEntryName(orderEntryDO.getEntryName());
            entryVO.setEntryUnit(orderEntryDO.getEntryUnit());
            entryVO.setEntryPrice(orderEntryDO.getEntryPrice());
            entryVO.setStockNo(orderEntryDO.getStockNo());
            entryVO.setStockName(orderEntryDO.getStockName());
            entryVO.setTotalQty(orderEntryDO.getTotalQty());
            entryVO.setEntryAmount(orderEntryDO.getEntryAmount());
            entryVO.setDiscountAmount(orderEntryDO.getDiscountAmount());
            entryVO.setDiscountRate(orderEntryDO.getDiscountRate());
            entryVO.setPurchaseFee(orderEntryDO.getPurchaseFee());
            entryVO.setTotalAmount(orderEntryDO.getTotalAmount());
            entryVO.setRemark(orderEntryDO.getRemark());
            entryVO.setRequestBillNo(orderEntryDO.getRequestBillNo());
            orderVO.getEntryVOList().add(entryVO);
        }
        return orderVO;
    }

    private Map<String, ProductCostDO> convertProductCostMap(SEOrderVO orderVO) {
        List<String> entryNos = Lists.newArrayList();
        Map<String, ProductCostDO> result = Maps.newHashMap();
        List<SEOrderEntryVO> entryVOList = orderVO.getEntryVOList();
        for (SEOrderEntryVO entryVO : entryVOList) {
            entryNos.add(entryVO.getEntryId());
        }
        List<ProductCostDO> productCostDOList = productCostDao.listLate(ImmutableMap.of("productNos", entryNos));
        for (ProductCostDO productCostDO : productCostDOList) {
            result.putIfAbsent(productCostDO.getProductNo(), productCostDO);
        }
        return result;
    }

    private Map<String, BigDecimal> convertPurchaseMap(SEOrderVO orderVO) {
        List<String> entryNos = Lists.newArrayList();
        Map<String, BigDecimal> result = Maps.newHashMap();
        List<SEOrderEntryVO> entryVOList = orderVO.getEntryVOList();
        for (SEOrderEntryVO entryVO : entryVOList) {
            entryNos.add(entryVO.getEntryId());
        }
        List<ProductDO> productDOList = productDao.list(ImmutableMap.of("nos", entryNos));
        for (ProductDO productDO : productDOList) {
            result.put(productDO.getNo().toString(), productDO.getPurchasePrice());
        }
        return result;
    }

}