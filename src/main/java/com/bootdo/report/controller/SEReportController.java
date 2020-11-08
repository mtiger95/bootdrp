package com.bootdo.report.controller;

import com.bootdo.common.controller.BaseController;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.R;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.report.controller.response.SEBillTotalResult;
import com.bootdo.report.controller.response.SEDebtTotalResult;
import com.bootdo.report.controller.response.echart.EChartOption;
import com.bootdo.report.service.SEReportService;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 入库出库单
 * 
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
 
@Controller
@RequestMapping("/report")
public class SEReportController extends BaseController{
	@Autowired
	private SEReportService reportService;

    /**
     * 销售总额 + 销售毛利(首页统计图)
     */
    @ResponseBody
    @RequestMapping(value = "/pSeTotal", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pSeTotal(@RequestBody Map<String, Object> params, Model model) {
        SEBillTotalResult result = new SEBillTotalResult();
        int days = MapUtils.getIntValue(params, "days"); //过去多少天
        String type = MapUtils.getString(params, "type"); //WEEK MONTH
        Map<String, Object> param = StringUtil.isEmpty(type) ? ImmutableMap.of("billDateStart", DateUtils.getStartStr(-days),"audit", AuditStatus.YES.name()) : ImmutableMap.of("billDateStart", DateUtils.getStartStr(type),"audit", AuditStatus.YES.name());
        result = reportService.pBalanceTotal(param);
        return R.ok().put("result", result);
    }

    /**
     * 客户欠款 + 供应商欠款(首页统计图)
     */
    @ResponseBody
    @RequestMapping(value = "/pDebtTotal", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pDebtTotal(@RequestBody Map<String, Object> params, Model model) {
        SEDebtTotalResult result = reportService.pDebtTotal(ImmutableMap.of("audit", AuditStatus.YES.name()));
        return R.ok().put("result", result);
    }

    /**
     * 订单趋势图(首页统计图)
     */
    @ResponseBody
    @RequestMapping(value = "/pBillTrend", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pSEBillTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = reportService.pBillTrend(params);
        return R.ok().put("result", option);
    }

    /**
     * 订单趋势饼图(首页统计图)
     */
    @ResponseBody
    @RequestMapping(value = "/pBillTrendPie", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pSEBillTrendPie(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = reportService.pBillTrendPie(params);
        return R.ok().put("result", option);
    }

    /**
     * 营业利润 + 营业净现金流(首页统计图)
     */
    @ResponseBody
    @RequestMapping(value = "/pCashTotal", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pCashTotal(@RequestBody Map<String, Object> params, Model model) {
        return reportService.pCashTotal(ImmutableMap.of("audit", AuditStatus.YES.name(), "billDate", DateUtils.getYearBegin()));
    }

    /**
     * 营业利润 + 营业净现金流 (首页趋势图)
     */
    @ResponseBody
    @RequestMapping(value = "/pCashTrend", method = RequestMethod.POST)
    @RequiresPermissions("wh:report:pBalance")
    R pCashTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = reportService.pCashTrend(params);
        return R.ok().put("result", option);
    }
}
