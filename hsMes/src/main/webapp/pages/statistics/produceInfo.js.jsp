<!--
	作者:徐波
	日期:2017-02-07 14:49:00
	页面:产品信息查询JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	var produceTreeUrl = path + "totalStatistics/listProduct";
	var findInfoUrl = path + "mobile/common/infos";
	var findBarcodeInfoUrl=path+"mobile/common/barcodeInfos";
	var changeUrl=path+"totalStatistics/changeInfo";
	var changeMeterUrl=path+"totalStatistics/changeMeterInfo";
	var changeMemoUrl=path+"totalStatistics/changeMemoInfo";
	var abandonUrl= path + "mobile/stock/product/abandon2";
	//导出到ExcelUrl
	var exportUrl = path + "totalStatistics/export";
	var stt = "";
	function expandAll() {
		$('#produceTree').tree('expandAll');
	}

	function collapseAll() {
		$('#produceTree').tree('collapseAll');
	}

	var searchInfo = function() {
		$("#searchInput").searchbox("setText",$("#searchInput").searchbox("getText").toUpperCase());
		var t = $("#searchInput").searchbox("getText");
		var barcode = t.toString().trim();
		var url = produceTreeUrl + "?barcode=" + barcode;
		JQ.ajax(url, "get", null, function(data) {
			$('#produceTree').tree(
					{
						data : data,
						method : 'get',
						animate : true,
						formatter:function(node){
							return node.text.toUpperCase();
						},
						onClick : function(node) {
							$("#infos").empty();
							stt="";
							var barcode = node.id.toUpperCase().trim();
							//点击时获取节点id，查询对应条码的产品信息
							JQ.ajax(findInfoUrl + "?barCode=" + barcode, "get",
									null, function(ajaxData) {
										/* if (ajaxData.ERROR) {
											tip(ajaxData.MSG);
											return;
										} */

										if(ajaxData.ROLL==null&&barcode.indexOf("R") == 0){
											Tip.warn("该条码未产出登记");
											//TODO 查询条码的信息
											barcodeInfos(barcode);
											return;
										}
										if (ajaxData.REGISTER) {
											//条码扫的数据
											initProduct(ajaxData, barcode);
											barCodeInfo = ajaxData;
											$("#infos").append(stt);
											stt = "";
										} else {
											closeWaiting();
											Tip.warn("R!");
											//TODO 查询条码的信息
											barcodeInfos(barcode);
											return;
										}

									}, function(ajaxData) {
										$("#infos").empty();
										Tip.warn("查询失败!");
										return;
									});
						}
					});
			expandAll();
			var node = $('#produceTree').tree('find', barcode);
			$('#produceTree').tree('select', node.target);

		});

	}
	function barcodeInfos(barcode){
		JQ.ajax(findBarcodeInfoUrl + "?barCode=" + barcode, "get",
				null, function(ajaxData) {
			console.log(ajaxData);
			var str="条    码    号 ："+ajaxData.BARCODE+"\n";
			str+="订    单    号 ："+ajaxData.SALESORDERCODE+"\n";
			str+="批    次    号 ："+ajaxData.BATCHCODE+"\n";
			var pInfos=(ajaxData.OUTPUTSTRING).split("\t");
			str+="厂内产品名称："+pInfos[0]+"\n";
			str+="客户产品名称："+pInfos[1]+"\n";
			if(!isEmpty(pInfos[7])&&pInfos[7]!=" "){
				str+="部 件 名 称 ："+pInfos[7]+"\n";
			}
		});
	}
	function numberFormatter(value) {
		if (isEmpty(value)) {
			value="0" ;
		}
		return parseFloat(value).toFixed(1);

	}

	function abandon(code){
              Dialog.confirm(function() {
                    Loading.show();
                    $.ajax({
                          url : encodeURI(abandonUrl + "?code=" + code) ,
                          type : "get",
                          dataType : "json",
                          success : function(data) {
                                if (Tip.hasError(data)) {
                                      return;
                                }
                                $("#infos").empty();
                                $.ajax({
            						url : encodeURI(findInfoUrl+"?barCode="+code),
            						type : "get",
            						success : function(ajaxData) {
											//条码扫的数据
											initProduct(ajaxData, code);
											barCodeInfo = ajaxData;
											$("#infos").append(stt);
											stt = "";
            						}
            					});
                                Tip.success("作废成功");
                                Loading.hide();
                          }
                    });
              }, "确认作废");
  	}

	function initProduct(data, barcode) {
		_productState = data.STATE;
		if (data.STATE == 0) {
			productState = "正常";
		} else if (data.STATE == -1) {
			productState = "不合格";
		} else if (data.STATE == 3) {
			productState = "冻结";
		} else if (data.STATE == 5) {
			productState = "退货";
		} else if (data.STATE == 6) {
			productState = "超产";
		}
		//库存状态
		if (data.STOCK == 1) {
			stock = "在库";
		} else if (data.STOCK == 5) {
			stock = "退库";
		} else if (data.STOCK == -1) {
			stock = "出库";
		}else if (data.STOCK == 2) {
			stock = "待入库";
		}else if (data.STOCK == 3) {
			stock = "在途";
		}
		else if (data.STOCK == 4) {
			stock = "领料";
		}else {
			stock = "未入库";
		}

		//作废状态
		if(data.CODE.ISABANDON == 1){
			isAbandon = "已作废";
		}else{
			isAbandon = "正常";
		}
		//检测状态
		if (null != data.PPD) {
            if(data.PPD.PRODUCTSTATUS==null){
                productStatus = "未检测";
            }else if (data.PPD.PRODUCTSTATUS==1) {
                productStatus = "已送样未检测";
            }else if (data.PPD.PRODUCTSTATUS==2) {
                productStatus = "检测合格";
            }else if (data.PPD.PRODUCTSTATUS==3) {
                productStatus = "检测不合格";
            }
		}

		var batchCode=data.CODE.BATCHCODE;
		var deviceCode="";
		//判断条码类型
		if ((barcode.indexOf("R") == 0 || barcode.indexOf("P") == 0) && data.ROLL != null){ //卷
			deviceCode = data.ROLL.ROLLDEVICECODE;
			qualityGradeCode = data.ROLL.ROLLQUALITYGRADECODE;
			weight = data.ROLL.ROLLWEIGHT;
			time = isEmpty(data.ROLL.ROLLOUTPUTTIME) ? ""
					: data.ROLL.ROLLOUTPUTTIME;
			//productModel=product.PRODUCTMODEL;
		} else if (barcode.indexOf("B") == 0) { //箱
			qualityGradeCode = data.BOX.ROLLQUALITYGRADECODE;
			weight = data.BOX.WEIGHT;
			time = isEmpty(data.BOX.PACKAGINGTIME) ? ""
					: data.BOX.PACKAGINGTIME;
			//productModel="";
		} else { //托
			qualityGradeCode = data.TRAY.ROLLQUALITYGRADECODE;
			weight = data.TRAY.WEIGHT;
			time = isEmpty(data.TRAY.PACKAGINGTIME) ? ""
					: data.TRAY.PACKAGINGTIME;
			//productModel="";
		}

		//生产计划明细
		ppd = data.PPD;

		//成品信息
		product = data.PRODUCT;
		productModel = product.PRODUCTMODEL;
		stt += '<table width="100%" >';
		stt += "<tr>";
		stt += "<td class='title'>条码号</td>";
		stt += "<td>" + barcode;
		if(data.CODE.ISABANDON != 1 && barcode.indexOf("R")==0){
			stt += "   <input type=\"button\" id=\"abandonbtn\" class=\"abandonbtn\" value=\"作废条码\" onclick=\"abandon('"
				+ barcode + "')\">";
		}else if(data.CODE.ISABANDON != 1 && barcode.indexOf("P")==0){
			stt += "   <input type=\"button\" id=\"abandonbtn\" class=\"abandonbtn\" value=\"作废条码\" onclick=\"abandon('"
				+ barcode + "')\">";
		}
		stt += "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>卷重</td>";
		stt += "<td><input type=\"text\" class=\"w\" id='weight' readonly='true' value='"
				+ numberFormatter(weight) + "' > ";
		if (barcode.substr(0, 1) == "R") {
			if((!data.PACKED ) || (data.PACKED && data.TRAY!=null &&  data.TRAY.indexOf("T") == 0 && data.STOCK==0)||
					(data.PACKED && data.TRAY!=null &&  data.TRAY.indexOf("T") == 0 && data.STOCK==2) ||
					( data.PACKED && data.TRAY==null )){
				stt += "<input type=\"button\" id=\"editbtn\" class=\"modifybtn\" value=\"修改\" onclick=\"changeInfo('"
						+ barcode + "')\">";
				stt += "<input type=\"button\" id=\"savebtn\" class=\"savebtn\" value=\"保存\" style=\"display:none;\" onclick=\"saveInfo('"
						+ barcode + "')\">";
			}
		}
		if (barcode.substr(0, 1) == "P") {
			if((!data.PACKED ) ||
					(data.PACKED && data.TRAY!=null &&  data.TRAY.TRAYBARCODE.indexOf("P") == 0 && data.STOCK==0) ||
					(data.PACKED && data.TRAY!=null &&  data.TRAY.TRAYBARCODE.indexOf("P") == 0 && data.STOCK==null) ||
					(data.PACKED && data.TRAY!=null &&  data.TRAY.TRAYBARCODE.indexOf("P") == 0 && data.STOCK==2) ||
					( data.PACKED && data.TRAY==null )){
				stt += "<input type=\"button\" id=\"editbtn\" class=\"modifybtn\" value=\"修改\" onclick=\"changeInfo('"
						+ barcode + "')\">";
				stt += "<input type=\"button\" id=\"savebtn\" class=\"savebtn\" value=\"保存\" style=\"display:none;\" onclick=\"saveInfo('"
						+ barcode + "')\">";
			}
		}
		stt += "</td>";
		stt += "</tr>";
		if((barcode.indexOf("R") == 0 || barcode.indexOf("P") == 0) && data.ROLL != null){
			stt += "<tr>";
			stt += "<td class='title'>短卷米长（0表示非短卷）</td>";
			stt += "<td><input type=\"text\" class=\"w\" id='meter' readonly='true' value='"
					+ data.ROLL.ROLLREALLENGTH + "' > ";
			if (barcode.substr(0, 1) == "R") {
				stt += "<input type=\"button\" id=\"editbtnM\" class=\"modifybtn\" value=\"修改\" onclick=\"changeInfoM('"
						+ barcode + "')\">";
				stt += "<input type=\"button\" id=\"savebtnM\" class=\"savebtn\" value=\"保存\" style=\"display:none;\" onclick=\"saveInfoM('"
						+ barcode + "')\">";
			}
		}
		stt += "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>订单号</td>";
		stt += "<td>" + data.ORDER.SALESORDERSUBCODE + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>产品名称</td>";
		if(isEmpty(product.FACTORYPRODUCTNAME)){
			stt += "<td>" + product.TCPROCBOMVERSIONPARTSNAME + "</td>";
		}else
			stt += "<td>" + product.FACTORYPRODUCTNAME + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>产品规格</td>";
		if(isEmpty(product.PRODUCTMODEL)){
			stt += "<td>" + product.TCPROCBOMVERSIONPARTSTYPE + "</td>";
		}else
			stt += "<td>" + product.PRODUCTMODEL + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>批次号</td>";
		stt += "<td>" + batchCode + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>客户名称</td>";
		stt += "<td>" + data.CONSUMER.CONSUMERNAME + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>门幅</td>";

		stt += "<td>" + (isEmpty(ppd) ? data.PRODUCT.PRODUCTWIDTH : ppd.PRODUCTWIDTH)
				+ "mm</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>定长/定重</td>";
		var length=(isEmpty(ppd)? (data.PRODUCT.PRODUCTROLLLENGTH) : (ppd.PRODUCTLENGTH ));
		var weight=(isEmpty(ppd)? (data.PRODUCT.PRODUCTROLLWEIGHT) : (ppd.PRODUCTROLLWEIGHT ));
		if(length==null){
			length=0.00;
		}
		if(weight==null){
			weight=0.00;
		}
		length=parseFloat(length);
		weight=parseFloat(weight);
		length= parseFloat(length.toFixed(2));
		weight= parseFloat(weight.toFixed(2));

		stt += "<td>"
				+ length+"m/"
				+ weight+"kg";
		stt += "</tr>";
		stt += "<tr>";
		if (barcode.indexOf("B") != 0 && barcode.indexOf("T") != 0) {
			stt += "<td class='title'>产出机台</td>";
			stt += "<td>" + deviceCode + "</td>";
			stt += "</tr>";
			stt += "<tr>";
		}

		stt += "<td class='title'>产出时间</td>";
		stt += "<td>" + time + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title'>质量等级</td>";
		stt += "<td>" + qualityGradeCode + "</td>";
		stt += "</tr>";


		stt += "<tr>";
		stt += "<td class='title'>仓库信息</td>";
		stt += "<td>" +"(" +data.WAREHOUSENAME +","+data.WAREHOUSEPOSCODE+")"+ "</td>";
		stt += "</tr>";

		stt += "<tr>";
		stt += "<td class='title'>在库状态</td>";
		if(barcode.indexOf("T") == 0 || barcode.indexOf("P") == 0){
			stt += "<td>" + stock + "</td>";
		}

		stt += "</tr>";
		stt += "<tr>";

		stt += "<td class='title'>成品状态</td>";
		stt += "<td>" + productState + "</td>";
		stt += "</tr>";
		stt += "<tr>";
		stt += "<td class='title' id='abandon'>作废状态</td>";
		stt += "<td>" + isAbandon + "</td>";
		stt += "</tr>";
		if (barcode.indexOf("T") == 0 || barcode.indexOf("PHS") == 0){
            stt += "<td class='title' id='abandon'>检测状态</td>";
            stt += "<td>" + productStatus + "</td>";
            stt += "</tr>";
		}
		stt += "<tr>";
		stt += "<td class='title'>操作人</td>";
		stt += "<td>" + data.OPERATOR + " (" + data.DEPARTMENT + ")</td>";
		stt += "</tr>";

		stt += "<tr>";
		stt += "<td class='title'>备注</td>";
		stt += "<td><input type=\"textarea\" class=\"memo\"  id='memo' readonly='true' value='"
					+ data.MEMO + "' > ";
				stt += "<input type=\"button\" id=\"editbtnMemo\" class=\"modifybtn\" value=\"修改\" onclick=\"changeInfoMemo('"
						+ barcode + "')\">";
				stt += "<input type=\"button\" id=\"savebtnMemo\" class=\"savebtn\" value=\"保存\" style=\"display:none;\" onclick=\"saveInfoMemo('"
						+ barcode + "')\">";
		stt += "</td>";
		stt += "</tr>";

	}
	function changeInfo(data) {
		var editbtn = document.getElementById("editbtn");
		var savebtn = document.getElementById("savebtn");
		var weightText = document.getElementById("weight");
		editbtn.style.display = "none";
		savebtn.style.display = "";
		weightText.readOnly = false;
		weightText.focus();
	}

	function changeInfoM(data){
		var editbtn = document.getElementById("editbtnM");
		var savebtn = document.getElementById("savebtnM");
		var weightText = document.getElementById("meter");
		editbtn.style.display = "none";
		savebtn.style.display = "";
		weightText.readOnly = false;
		weightText.focus();
	}

	function changeInfoMemo(data){
		var editbtn = document.getElementById("editbtnMemo");
		var savebtn = document.getElementById("savebtnMemo");
		var memo = document.getElementById("memo");
		editbtn.style.display = "none";
		savebtn.style.display = "";
		memo.readOnly = false;
		memo.focus();
	}


	function saveInfoMemo(data) {
		var memo = document.getElementById("memo");
		if(memo.value==""){
			Tip.warn("请输入备注");
			return;
		}
		/* if(isNaN(meter.value)){
			Tip.warn("请输入合法的数字");
			return;
		} */
		var editbtn = document.getElementById("editbtnMemo");
		var savebtn = document.getElementById("savebtnMemo");

		//提交修改值
		Dialog.confirm(function() {
			JQ.ajaxPost(changeMemoUrl, {
				barcode : data.toUpperCase(),
				memo:memo.value
			}, function(data) {
				memo.readOnly = true;
				Tip.success("保存成功");
				savebtn.style.display = "none";
				editbtn.style.display = "";
			});
		},"确认修改？");

	}


	function saveInfoM(data) {
		var meter = document.getElementById("meter");
		if(meter.value==""){
			Tip.warn("请输入米长");
			return;
		}
		if(isNaN(meter.value)){
			Tip.warn("请输入合法的数字");
			return;
		}
		var editbtn = document.getElementById("editbtnM");
		var savebtn = document.getElementById("savebtnM");

		//提交修改值
		Dialog.confirm(function() {
			JQ.ajaxPost(changeMeterUrl, {
				rollbarcode : data.toUpperCase(),
				meter:meter.value
			}, function(data) {
				meter.readOnly = true;
				Tip.success("保存成功");
				editbtn.style.display = "";
				savebtn.style.display = "none";
			});
		},"确认修改？");
	}

	function saveInfo(data) {
		var weightText = document.getElementById("weight");
		if(weightText.value==""){
			Tip.warn("请输入重量");
			return;
		}
		if(isNaN(weightText.value)){
			Tip.warn("请输入合法的数字");
			return;
		}

	    weightText.value=parseInt(new Number(weightText.value)*10)/10;

		var editbtn = document.getElementById("editbtn");
		var savebtn = document.getElementById("savebtn");

		var t = $('#produceTree');
		var node = t.tree('getSelected');
		var root=t.tree('getRoot');
		var topBarcode=root.id;
		var parentNode= t.tree('getParent',node.target);
		var parentBarocde;
		var barcode=node.id;
		if(topBarcode==barcode){
			parentBarocde=topBarcode;
		}
		if(parentNode!=null){
			parentBarocde=parentNode.id;
		}
		//提交修改值
		Dialog.confirm(function() {
			JQ.ajax(changeUrl, "post", {
				barcode : barcode.toUpperCase(),
				parentBarocde:parentBarocde.toUpperCase(),
				topBarcode:topBarcode.toUpperCase(),
				newWeight:weightText.value
			}, function(data) {
			    if(typeof(data)=="string"){
                    weightText.readOnly = true;
                    Tip.success("保存成功");
                    editbtn.style.display = "";
                    savebtn.style.display = "none";
				}
			});
		});

	}
	/**
	 * 托合卷关系Excel导出
	 */
	 function Export(){
		//搜索框值
		var searchInput = $("#searchInput").textbox("getValue");
		location.href = encodeURI(exportUrl + "?barcode="+searchInput);
	}
</script>
