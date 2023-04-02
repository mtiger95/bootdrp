let lastSel; //jqGrid最后编辑未保存的行号
let tableGrid;
let dataForm;
let prefix = "/wh/entry";
let prefixOrder = "/wh/order";
let initData = [{},{},{}];
let mask;

let colNames_RK = ['','','商品名称','商品ID', '条形码',  '单位', '仓库', '数量', '入库单价', '入库金额', '备注'];
let colNames_CK = ['','','商品名称','商品ID', '条形码',  '单位', '仓库', '数量', '出库单位成本', '入库成本', '备注'];
let billType = $('#billType').val();
let colNames = billType === 'WH_RK_ORDER' ? colNames_RK : colNames_CK;
let dataUrl = billType === 'WH_RK_ORDER' ? '/wh/order?billType=WH_RK_ORDER' : '/wh/order?billType=WH_CK_ORDER';

$(function() {
    dataForm  = $('#data_form');
    mask = $('#mask');
    if (billType === 'WH_RK_ORDER') {
        utils.loadTypes(["data_wh_rk"], ["serviceType"]);
    } else {
        utils.loadTypes(["data_wh_ck"], ["serviceType"]);
    }
    utils.loadCategory(["CUSTOMER_DATA"], ["debtorId"], [{width:"200px"}]);

    load();
});

function load() {

    utils.createDatePicker('date_1');

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        datatype : "local",
        data: initData,
        height: 'auto',
        rowNum: 1000,
        shrinkToFit: false,
        autoScroll: true,
        rownumbers:true,
        rownumWidth:30,
        footerrow : true,
        editurl: "clientArray",
        colNames: colNames,
        colModel: [
            { name: 'act', width:60, fixed:true, sortable:false, resize:false, formatter : function(cellValue, options, rowObject) {
                    let e = '<a class="btn btn-primary btn-xs" href="#" mce_href="#" onclick="addRow(' + options.rowId + ', tableGrid)"><i class="fa  fa-plus"></i></a> ';
                    let d = '<a class="btn btn-warning btn-xs" href="#" mce_href="#" onclick="delRow('+ options.rowId + ', tableGrid)"><i class="fa fa-minus"></i></a> ';
                    return e + d ;
                }},
            { name:'id', index:'id', editable:false, hidedlg:true, hidden:true},
            { name:'entryName', index:'entryName', editable:true, edittype:'custom', width:300, editoptions: utils.myElementAndValue() },
            { name:'entryId', index:'entryId', editable:false, width:70 },
            { name:'entryBarcode', index:'entryBarcode', editable:false, width:70 },
            { name:'entryUnit', index:'entryUnit', editable:false, width:70 },
            { name:'stockNo', index:'stockNo', editable:true, width:100, edittype:'select', editoptions: utils.formatSelect("data_stock"), formatter: "select" },
            { name:'totalQty', index:'totalQty', editable:true, width:90, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'entryPrice', index:'entryPrice', editable:true, width:100, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'entryAmount', index:'entryAmount', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'remark', index:'remark', editable:true, width:100, editoptions: utils.commonEditOptions()  }
        ],
        beforeSelectRow: function(rowid) {
            if (rowid !== lastSel) {
                tableGrid.jqGrid('saveRow',lastSel); // save row
                lastSel = rowid;
            }
            return true;
        },
        onCellSelect: function(rowId,iCol,contents,event) {
            if (iCol > 1) {
                tableGrid.jqGrid('editRow', rowId, { focusField: iCol });

            }
        },
        gridComplete: function () {
            collectTotal();
        }
    });

    // 设置横向滚动条
    $('.jqGrid_wrapper .ui-jqgrid').attr('style','overflow:auto');

    // 表格失去焦点保存Row数据
    $(document).click(function(e) {
        if(tableGrid.has(e.target).size() < 1 &&　lastSel) {
            tableGrid.jqGrid('saveRow', lastSel);  // save row
            lastSel = undefined;
            collectTotal();
        }
    });
}

//计算表格合计行数据
function collectTotal(){
    let totalQtyTotal=$("#table_list").getCol('totalQty',false,'sum');
    let entryAmountTotal=$("#table_list").getCol('entryAmount',false,'sum');
    // 设置表格合计项金额
    $("#table_list").footerData('set', { entryName: '合计:', totalQty: totalQtyTotal, entryAmount: utils.priceFormat(entryAmountTotal) });
}

// 自定义表格编辑框
function myelem (value, options) {
    let el =
        '<div class="input-group">' +
        '<input type="text" name="entryName" class="form-control" value="'+ value +'">' +
        '<span class="input-group-btn"> <button type="button" class="btn btn-white" onclick="add()">...</button> </span>' +
        '</div>';
    return el;
}

// 自定义表格编辑框get set方法
function myvalue(elem, operation, value) {
    if(operation === 'get') {
        return $('input', elem).val();
    } else if(operation === 'set') {
        $('input', elem).val(value);
    }
}

amountEntry = {
    // 订单分录各列值，数量、单价、商品金额
    totalObj: { totalQty: 0, entryPrice: 0, entryAmount: 0 },
    // 金额变化需更新别的金额列
    elements: {
        totalQty: ['entryAmount'],
        entryPrice: ['entryAmount']
    },
    // 计算后各列的金额
    valueObj:{ entryPrice: 0, entryAmount: 0 },
    // 各列金额计算公式
    formula: {
        entryPrice: 'totalObj.entryAmount > 0 ? (totalObj.entryAmount / totalObj.totalQty * 1.0).toFixed(2) : 0.0',
        entryAmount: '(totalObj.entryPrice * totalObj.totalQty).toFixed(2)'
    }
};

//获取编辑状态行数据
function collectRow($rowId){
    let rowId = $rowId || $("#table_list").jqGrid("getGridParam", "selrow");
    let originRow = $("#table_list tr[id="+(rowId)+"]");
    let entryPrice=originRow.find("[name='entryPrice']").val();
    let totalQty=originRow.find("[name='totalQty']").val();
    let entryAmount=originRow.find("[name='entryAmount']").val();
    amountEntry.totalObj = { entryPrice: entryPrice, totalQty: totalQty, entryAmount: entryAmount };
    return amountEntry;
}

//增加行
function addRow(rowid, tableGrid){
    let rowData = { };
    let ids = tableGrid.jqGrid('getDataIDs');
    let maxId = ids.length == 0 ? 1 : Math.max.apply(Math,ids);
    tableGrid.jqGrid('addRowData', maxId+1, rowData, 'after', rowid);//插入行
}

//删除行
function delRow(rowid, tableGrid) {
    let ids = tableGrid.jqGrid('getDataIDs')
    if (ids.length > 1) {
        tableGrid.jqGrid('delRowData', rowid);
    } else {
        layer.msg('至少保留一个分录',{time:1000});
    }
}

//保存
function save(add) {
    let order = dataForm.serializeObject();
    let entryArr = tableGrid.jqGrid("getRowData");
    order.entryVOList = [];

    if (order.serviceType == "" || order.billDate == "") {
        layer.msg((order.serviceType == "" ? "【业务类型】" : "") + (order.billDate == "" ? "【单据日期】" : "") + "不能为空！");
        return;
    }

    let validateFlag = true;
    $.each(entryArr, function (key, val) {
        delete val['act'];
        if (val['entryName'] != "" && val['entryId'] != "") {
            order.entryVOList.push(val);
        }
    });

    if (order.entryVOList.length <= 0) {
        layer.msg("单据分录不能为空！");
        return;
    }

    if (validateFlag) {
        $.ajax({
            url : prefix+"/save",
            type : "post",
            // dataType: "json",
            contentType: "application/json; charset=utf-8",
            data : JSON.stringify(order),
            success : function(r) {
                if (r.code == 0 && add == 1) { //保存并新增
                    initBillNo();
                    clearGrid();
                } else if (r.code == 0) {
                    initBillNo(r.billNo);
                }
                layer.msg(r.msg);
            }
        });
    }
}

//审核
function audit(type) {
    let billNo = $("#billNo").val();
    if (billNo &&　billNo != "") {
        $.ajax({
            url : prefixOrder+"/audit",
            type : "post",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data : JSON.stringify({ 'billNos' : [billNo],  'auditStatus' : type == 0 ? 'YES' : 'NO' }),
            success : function(r) {
                if (r.code == 0) {
                    mask.removeClass('util-has-audit');
                    mask.addClass('util-has-audit');
                }
                layer.msg(r.msg);
            }
        });
    } else {
        layer.msg('请先保存订单！');
    }
}

//历史单据
function listOrder() {
    let dataIndex;
    //触发菜单单击
    window.parent.$(".J_menuItem").each(function (index) {
        if ($(this).attr('href') == dataUrl) {
            window.parent.$(this).trigger('click');
            dataIndex = window.parent.$(this).data('index');
            return false;
        }
    });
    //重新刷订单列表
    window.parent.$('.J_mainContent .J_iframe').each(function () {
        if ($(this).data('id') == dataUrl) {
            let win = window.parent.$('iframe[name="iframe' + dataIndex +'"]')[0].contentWindow;
            if (win.reLoad) {
                win.reLoad();
            }
            return false;
        }
    });
}

//操作日志
function listLog(rowid) {
    //TODO
    layer.msg('此功能还未实现。。。');
}

//添加订单商品信息弹窗
function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1100px', '620px' ],
        content : prefix + '/add' // iframe的url
    });
}

//订单表格插入数据
function insertData(datas) {
    if (!datas || datas.length <= 0) return;
    let ids = tableGrid.jqGrid('getDataIDs');
    let maxId = ids.length == 0 ? 1 : Math.max.apply(Math, ids);
    let rowId = tableGrid.jqGrid("getGridParam", "selrow")
    for (let i = 0; i < datas.length; i++) {
        if (i === 0) {
            tableGrid.jqGrid('saveRow', rowId);
            tableGrid.jqGrid('setRowData', rowId, datas[i]);//更新当前行
        } else {
            maxId = maxId + 1;
            tableGrid.jqGrid('addRowData', maxId, datas[i], 'after', rowId);//插入行
            rowId = maxId;
        }
    }
    //选中下一个空行
    ids = tableGrid.jqGrid('getDataIDs');
    $.each(ids, function (key, val) {
        let data = tableGrid.jqGrid("getRowData", val);
        if (data['entryName'] == "") {
            tableGrid.setSelection(val, false);
            return false;
        }
    });
    collectTotal();
}

//选择订单供应商弹窗
function addHead() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1000px', '600px' ],
        content : prefix + '/addHead' // iframe的url
    });
}

//设置供应商下拉框值
function insertHead(data) {
    if (!data) return;
    $("#debtorId").val(data.no).trigger("chosen:updated");
}

//初始化订单数据
function initOrder(billNo) {
    if (billNo) {
        $.ajax({
            url: prefix + "/get",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: {"billNo": billNo},
            success: function (r) {
                if (r.code == 0) {
                    tableGrid.clearGridData();
                    tableGrid.jqGrid('setGridParam', {data: r.order.entryVOList}).trigger('reloadGrid');

                    dataForm.setForm(r.order);
                    mask.removeClass('util-has-audit');
                    mask.addClass(r.order && r.order.auditStatus == 'YES' ? 'util-has-audit' :'');

                    initBillNo(r.order.billNo);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    } else {
        initBillNo();
        clearGrid();
        dataForm.resetForm();
        mask.removeClass('util-has-audit');
    }
}

//清空表格数据
function clearGrid() {
    tableGrid.clearGridData();
    tableGrid.jqGrid('setGridParam', {data: [{}, {}, {}]}).trigger('reloadGrid');
}

//设置单据号隐藏域值
function initBillNo(ival) {
    ival = ival || "";
    $("[name=billNo]").each(function (index, element) {
        if ($(element).prop("tagName") == "SPAN") {
            $(element).html("单据编号: " + ival);
        } else {
            $(element).val(ival);
        }
    });
}