let prefix = "/data/product";
let tableGrid;

$(function() {
    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    let rowData = window.parent.getCurrentRow();

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/listCost",
        datatype: "json",
        postData: { "productNo": rowData['entryId'] || rowData['productNo'] },
        colNames: ['', '商品编号', '采购价', '成本单价', '商品库存', '库存变更', '库存余额', '库存成本', '成本时间', '类型', '关联单号', '创建时间', '修改时间'],
        colModel: [
            { name:'id', index:'id', editable:false, width:50, hidden:true },
            { name:'productNo', index:'productNo', editable:false, width:60 },
            { name:'entryPrice', index:'entryPrice', editable:false, width:60, align: "right", formatter:"number" },
            { name:'costPrice', index:'costPrice', editable:false, width:60, align: "right", formatter:"number" },
            { name:'costQty', index:'costQty', editable:false, width:70, align: "right", formatter:"number" },
            { name:'entryQty', index:'entryQty', editable:false, width:70, align: "right", formatter:"number" },
            { name:'costBalance', index:'costBalance', editable:false, width:70, align: "right", formatter:"number" },
            { name:'costAmount', index:'costAmount', editable:false, width:80, align: "right", formatter:"number" },
            { name:'costDate', index:'costDate', editable:false, width:100, hidden: true },
            { name:'costType', index:'costType', editable:false, width:70, formatter:function (cellValue){return utils.formatEnum(cellValue, 'COST_TYPE')}, unformat: function (cellValue) { return utils.unformatEnum(cellValue, 'COST_TYPE') } },
            { name:'relateNo', index:'relateNo', editable:false, width:120 },
            { name:'createTime', index:'createTime', editable:false, width:100 },
            { name:'updateTime', index:'updateTime', editable:false,  width:100 }
        ],
        height: window.innerHeight - 120,
        autowidth: true,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#pager_list",
        viewrecords: true,
        loadComplete: function (data) {
            utils.changeRowCss(tableGrid, "costType", "MANUAL");
        }
    });

    tableGrid.jqGrid('navGrid', '#pager_list', {
        edit: false,
        add: false,
        search: false,
        del: false,
        refresh: true
    }, {
        height: 200,
        reloadAfterSubmit: true
    });

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 120);
    });
}
