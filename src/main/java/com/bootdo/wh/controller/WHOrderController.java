package com.bootdo.wh.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.utils.PageJQUtils;
import com.bootdo.common.utils.PoiUtil;
import com.bootdo.common.utils.QueryJQ;
import com.bootdo.common.utils.R;
import com.bootdo.wh.domain.WHOrderDO;
import com.bootdo.wh.service.WHOrderService;
import com.bootdo.wh.validator.WHOrderValidator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */

@Controller
@RequestMapping("/wh/order")
public class WHOrderController extends BaseController {
    @Resource
    private WHOrderValidator whOrderValidator;
    @Resource
    private WHOrderService whOrderService;

    /**
     * 左侧菜单单据列表页URL
     */
    @GetMapping()
    @RequiresPermissions("wh:order:order")
    public String order(@RequestParam Map<String, Object> params, Model model) {
        model.addAttribute("billType", MapUtil.getStr(params, "billType"));
        return "wh/order/order";
    }

    /**
     * 单据列表页
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("wh:order:order")
    public PageJQUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryJQ query = new QueryJQ(params);
        List<WHOrderDO> orderList = whOrderService.list(query);
        int total = whOrderService.count(query);
        int totalPage = (int) Math.ceil(1.0 * total / query.getLimit());
        return new PageJQUtils(orderList, totalPage, query.getPage(), total);
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
        List<WHOrderDO> orderList = whOrderService.list(query);
        PoiUtil.exportExcelWithStream("WHOrderResult.xls", WHOrderDO.class, orderList);
    }

    /**
     * 审核、反审核
     */
    @Log("库存单审核、反审核")
    @PostMapping("/audit")
    @ResponseBody
    @RequiresPermissions("wh:order:audit")
    public R audit(@RequestBody Map<String, Object> params) {
        whOrderValidator.validateAudit(params);
        whOrderService.audit(params);
        return R.ok();
    }

    /**
     * 删除
     */
    @Log("库存单删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("wh:order:remove")
    public R remove(@RequestParam("billNos[]") List<String> billNos) {
        whOrderValidator.validateRemove(billNos);
        whOrderService.batchRemove(billNos);
        return R.ok();
    }

}
