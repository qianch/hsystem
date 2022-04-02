<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	//打印
	var showPrinter = path + "printer/showPrinterPage";
	var showPrinterList = path + "printer/showPrinterPageList";
	var doPrinter = path + "printer/doPrintBarcode";
	var doPrinterList = path + "printer/doPrintBarcodeList";

	function filter() {
		EasyUI.grid.search("dg", "dgForm");
	}

    $(function(){
        $('#dg').datagrid({
            url:"${path}device/scheduling/view/list",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

	function isProducing(index, row) {
		if (row.ISPRODUCING == 1) {
			return "background: #03b723; font-weight: bold; height: 25px; color: white;";
		} else {
			if (row.SORT) {
				return "color:orange;font-weight:bold;";
			}
		}
	}

	function loadSuccess(data) {
		//$("#dg").datagrid("autoMergeCells",['DEVICENAME','_DEVICECODE','SALESORDERCODE','PLANCODE']);
	}

	function dateFormatter(value, index, row) {
		if (isEmpty(value))
			return "";
		return value.substring(0, 10);

	}

	function beforeLoad(param) {
		if (isEmpty($("#start").datebox("getValue")) || isEmpty($("#end").datebox("getValue"))) {
			return false;
		}
		if (param['filter[dcodes]'] != undefined) {
			param['filter[dcodes]'] = param['filter[dcodes]'].replace(new RegExp('，', 'gm'), ",");
		}
	}

	function formatterIsFinish(value, row) {
		if (value == 1) {
			return '已完成';
		}
		if (value == -1) {
			return '未完成';
		}
	}
	function formatterIsClosed(value, row) {
		if (value == 0 || value == null) {
			return '未关闭';
		}
		if (value == 1) {
			return '已关闭';
		}
	}

	function rowStyler(index, row) {
		var style = "";
		if (row.WEAVEPLANPRODUCTTYPE == 1) {
			style += 'background-color:#FFD39B';
		}
		if (row.WEAVEPLANPRODUCTTYPE == 2) {
			style += 'background-color:#FFE7BA';
		}
		if (row.WEAVEPLANPRODUCTTYPE == 3) {
			style += 'background-color:#FFEFDB';
		}
		if (row.WEAVEPLANPRODUCTTYPE == 4) {
			style += 'background-color:#FFF8DC';
		}

		if (isEmpty(row.CLOSED) || row.CLOSED == 0) {
		} else {
			style += "text-decoration:line-through;background: #989696;";
		}

		return style;

	}
	function formatterC(index, row, value) {
		if (row.ISCOMEFROMTC == 1) {
			return '裁剪车间';
		}
		if (row.ISCOMEFROMTC == "" || row.ISCOMEFROMTC == null) {
			return row.CONSUMERNAME;
		}
		if (row.ISCOMEFROMTC == 0) {
			return row.CONSUMERNAME;
		}
	}

	function formatterType(value, row) {
		if (value == 1) {
			return "大卷产品";
		}
		if (value == 2) {
			return "中卷产品";
		}
		if (value == 3) {
			return "小卷产品";
		}
		if (value == 4) {
			return "其他产品";
		}
	}

	$(function() {
		var cal = new Calendar();
		cal.add(Calendar.field.MONTH, -6);
		cal.add(Calendar.field.DAY_OF_MONTH, 0);

		/* cal.set(Calendar.field.DAY_OF_MONTH, 1); */

		$("#start").datebox("setValue", cal.format("yyyy-MM-dd"));

		var cale = new Calendar();
		cale.add(Calendar.field.MONTH, 6);
		cale.add(Calendar.field.DAY_OF_MONTH, 0);

		$("#end").datebox("setValue", cale.format("yyyy-MM-dd"));

		filter();
	});


	// function doPrint(){
	// 	var r = EasyUI.grid.getOnlyOneSelected("dg");
	// 	if(r==null){
	// 		return;
	// 	}
	// 	var dialogId = Dialog.open("打印", 400, 200, showPrinter+"?weaveId="+r.ID+"&departmentName="+r.WORKSHOP, [ EasyUI.window.button("icon-ok", "打印", function() {
	// 		EasyUI.form.submit("doPrintBarcodeForm",doPrinter , function(data) {
	// 			if(data.url){
	// 				$("#download").html('<a href="' +path.replace("mes/","")+data.url+' " target="_blank">'+path.replace("mes/","")+data.url+'</a>');
	// 				Tip.success("打印成功");
	// 				Dialog.close(dialogId);
	// 			}else{
	// 				Tip.error(data);
	// 			}
	// 		})
	// 	}),EasyUI.window.button("icon-cancel", "关闭", function() {
	// 		Dialog.close(dialogId);
	// 	}) ], function() {
	// 	});
	// }


    function doPrintList(){
        var rows=EasyUI.grid.getSelections("dg")
        var ids=[];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].ID);
            if(i>0&&(rows[i].WORKSHOP != rows[i-1].WORKSHOP)){
                Tip.warn("不能批量打印不同车间的条码");
                return;
            }
            if(i>0&&(rows[i].BATCHCODE != rows[i-1].BATCHCODE)){
                Tip.warn("不能批量打印不同批号的条码");
                return;
            }
        }
        if(ids==null){
            Tip.warn("请选择编织任务");
            return;
        }
        var dialogId = Dialog.open("打印", 400, 200, showPrinterList+"?ids="+ids+"&departmentCode="+rows[0].WORKSHOPCODE, [ EasyUI.window.button("icon-ok", "打印", function() {
            EasyUI.form.submit("doPrintBarcodeForm",doPrinterList , function(data) {
                    if(data == ""){
                        Tip.success("打印成功");
                        Dialog.close(dialogId);
                    }else{
                        Tip.error(data);
                    }
            })
        }),EasyUI.window.button("icon-cancel", "关闭", function() {
            Dialog.close(dialogId);
        }) ], function() {
        });
    }

	//客户简称为裁剪车间时添加叶型
	function formatterAddYX(value,row){
		var yx=row.YX;
		if(value=="裁剪车间"){
			if(yx){
				 str=yx.split("叶型为：")[1];
			}
			if(str){
				return value+" ("+str+")";
			}
		}
		return value;
	}

</script>
