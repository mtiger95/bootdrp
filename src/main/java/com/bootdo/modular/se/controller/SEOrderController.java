package com.bootdo.modular.se.controller;

import com.bootdo.core.annotation.Log;
import com.bootdo.core.pojo.response.PageJQ;
import com.bootdo.core.utils.PoiUtil;
import com.bootdo.core.pojo.request.QueryJQ;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.se.service.SEOrderService;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.validator.SEOrderValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 购货订单
 *
 * @author yogiCai
 * @date 2018-02-18 16:50:26
 */

@Controller
@RequestMapping("/se/order")
public class SEOrderController {
    @Resource
    private SEOrderValidator seOrderValidator;
    @Resource
    private SEOrderService seOrderService;

    @GetMapping()
    @RequiresPermissions("se:order:order")
    public String order() {
        return "se/order/order";
    }

    @ResponseBody
    @PostMapping(value = "/list")
    @RequiresPermissions("se:order:order")
    public PageJQ listP(@RequestBody Map<String, Object> params) {
        return list(params);
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("se:order:order")
    public PageJQ list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<SEOrderDO> orderList = seOrderService.list(query);
        int total = seOrderService.count(query);
        int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
        return new PageJQ(orderList, totalPage, query.getPage(), total);
    }

    /**
     * 单据列表导出
     */
    @ResponseBody
    @GetMapping("/export")
    @RequiresPermissions("po:order:order")
    public void export(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params, false);
        List<SEOrderDO> orderList = seOrderService.list(query);
        PoiUtil.exportExcelWithStream("SEOrderResult.xls", SEOrderDO.class, orderList);
    }

    /**
     * 审核、反审核
     */
    @Log("销售单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("se:order:audit")
    public R audit(@RequestBody Map<String, Object> params) {
        seOrderValidator.validateAudit(params);
        seOrderService.audit(params);
        return R.ok();
    }

    /**
     * 删除
     */
    @Log("销售单删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("se:order:remove")
    public R remove(@RequestParam("billNos[]") List<String> billNos) {
        seOrderValidator.validateRemove(billNos);
        seOrderService.batchRemove(billNos);
        return R.ok();
    }
}