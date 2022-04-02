
<!--
      作者:徐波
      日期:2016-11-26 14:44:04
      页面:综合统计JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
      //添加综合统计
      var addUrl = path + "totalStatistics/add";
      //编辑综合统计
      var editUrl = path + "totalStatistics/edit";
      //删除综合统计
      var deleteUrl = path + "totalStatistics/delete";
      //修改条码重量
      var changeUrl = path + "totalStatistics/changeInfo";
      //作废卷条码
      var abandonUrl= path + "mobile/stock/product/abandon";

      var dialogWidth = 700, dialogHeight = 400;

      //查询
      function filter() {
            EasyUI.grid.search("dg", "totalStatisticsSearchForm");
      }
     //生产统计查询默认时间
	$(function() {
          $('#dg').datagrid({
                url:"${path}totalStatistics/list",
                onBeforeLoad: dgOnBeforeLoad,
          });
		$("#start").datetimebox("setValue",
				new Calendar().format("yyyy-MM-dd") + " 08:00:00");
		$("#end").datetimebox("setValue",
				new Calendar().format("yyyy-MM-dd") + " 20:00:00");
		filter();
	});

 function beforeLoad(param) {
       var s=$('#start').datetimebox("getValue");
      var e=$('#end').datetimebox("getValue");
      log(s,e);
      if(isEmpty(s)&&isEmpty(e)){
            return false;
      }
}
      function getWorkShop(value, row, index) {

            if (value == '00107') {
                  return "编织一车间";
            }
            if (value == '00108') {
                  return "编织二车间";
            }
            if (value == '00109') {
                  return "编织三车间";
            }
            if (value == '00116') {
                  return "裁剪一车间";
            }
            if (value == '00117') {
                  return "裁剪二车间";
            }
            return value;
      }
      function barcodeFormatter(value, row, index) {
            if (row.ROLLBARCODE == null || row.ROLLBARCODE == "") {
                  return row.PARTBARCODE;
            }
            return value;
      }
      function endPackFormatter(value, row,index){
            if(row.ROLLBARCODE.indexOf("T") == 0  || row.ROLLBARCODE.indexOf("P") == 0){
                  if(value == null || value == 0){
                        return "未结束";
                  }else{
                        return "已结束";
                  }
            }
            return value;
      }
      function isAbandonFormatter(value, row, index){
            if(value == 1){
                  return "已作废";
            }else{
                  return "正常";
            }
      }
      function barcodeTypeFormatter(value, row, index) {
            if (row.ROLLBARCODE == null || row.ROLLBARCODE == "") {
                  return "部件条码";
            } else if (row.ROLLBARCODE.indexOf("T") == 0) {
                  return "托条码";
            } else if (row.ROLLBARCODE.indexOf("B") == 0) {
                  return "箱条码";
            }else if (row.ROLLBARCODE.indexOf("P") == 0) {
                  return "部件条码";
            } else {
                  return "卷条码";
            }
            return value;
      }
      
      function isAbandonFormatter(value, row, index){
  		if(value == 1){
  			return "已作废";
  		}else{
  			return "正常";
  		}
  	}
      
      function stockStateFormatter(value, row, index) {
            /* var firstString=row.ROLLBARCODE+"";
            firstString=firstString.substring(0, 1);
            if(firstString!="T"){
                  return "";
            } */
            if(row.ROLLBARCODE.indexOf("T") == 0 || row.ROLLBARCODE.indexOf("P") == 0){
	            if (value == null || value == 0) {
	                  return "未入库";
	            }
	            if (value == 1) {
	                  return "在库";
	            } else if (value == -1) {
	                  return "出库";
	            } else if (value == 2) {
                      return "待入库";
                }else if (value == 3) {
                      return "在途";
                }
            }
      }

      function lockStateFormatter(value, row, index) {
            if (value == 1) {
                  return "冻结";
            } else {
                  return "正常";
            }
      }
      //添加综合统计
      var add = function() {
            var wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                        EasyUI.window.button("icon-save", "保存", function() {
                              EasyUI.form.submit("totalStatisticsForm", addUrl, function(
                                          data) {
                                    filter();
                                    if (Dialog.isMore(wid)) {
                                          Dialog.close(wid);
                                          add();
                                    } else {
                                          Dialog.close(wid);
                                    }
                              })
                        }), EasyUI.window.button("icon-cancel", "关闭", function() {
                              Dialog.close(wid)
                        }) ], function() {
                  Dialog.more(wid);
            });
      }
      var lockpage = path + "totalStatistics/lockpage";
      var lockUrl = path + "totalStatistics/lock";
      var unlockUrl = path + "totalStatistics/unLock";
      function lock() {
            var r = EasyUI.grid.getSelections("dg");
            if (r.length == 0) {
                  Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                  return;
            }
            var ids = [];
            for (var i = 0; i < r.length; i++) {
                  if (r[i].ISLOCKED == 1) {
                        Tip.warn("有些产品已经被冻结，请重新筛选");
                        return;
                        //return;
                  } else
                        ids.push(r[i].ID);
            }

            var dialogId = Dialog.open("冻结", 380, 270, lockpage + "?ids="
                        + ids.toString(), [
                        EasyUI.window.button("icon-save", "保存", function() {
                              if ($('#reasons').val() == "") {
                                    Tip.warn("请填写冻结原因");
                                    return;
                              }
                              EasyUI.form.submit("totalStatisticsForm", lockUrl,
                                          function(data) {
                                                filter();
                                                Dialog.close(dialogId);
                                                Tip.success("冻结成功");
                                          })
                        }), EasyUI.window.button("icon-cancel", "关闭", function() {
                              Dialog.close(dialogId);
                        }) ], function() {
            });

      }

      var judgepage = path + "totalStatistics/judgepage";
      var judgeUrl = path + "totalStatistics/judge";
      function judge() {
            var r = EasyUI.grid.getSelections("dg");
            if (r.length == 0) {
                  Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                  return;
            }
            var ids = [];
            for (var i = 0; i < r.length; i++) {
                  ids.push(r[i].ID);
            }

            var dialogId = Dialog.open("质量判级", 380, 100, judgepage + "?ids="
                        + ids.toString(), [
                        EasyUI.window.button("icon-save", "保存", function() {
                              if ($('#qualityGrade').combobox('getValue') == "") {
                                    Tip.warn("请选择质量等级");
                                    return;
                              }
                              EasyUI.form.submit("totalStatisticsForm", judgeUrl,
                                          function(data) {
                                                filter();
                                                Dialog.close(dialogId);
                                                Tip.success("判级成功");
                                          })
                        }), EasyUI.window.button("icon-cancel", "关闭", function() {
                              Dialog.close(dialogId);
                        }) ], function() {
            });

      }
      var complaint_page = path + "selector/complaint";
      function selectComplaint() {

            var dialogId = Dialog.open("投诉", dialogWidth, dialogHeight,
                        complaint_page, [
                                    EasyUI.window.button("icon-save", "确定", function() {
                                          var r = EasyUI.grid
                                                      .getSelections("_common_complaint_dg");
                                          if (r.length > 1) {
                                                Tip.warn("请选择唯一记录");
                                          }
                                          $('#complaintCode').searchbox("setValue",
                                                      r[0].COMPLAINTCODE);
                                          //$('#complaintCode').val(r[0].COMPLAINTCODE);
                                          Dialog.close(dialogId);
                                    }),
                                    EasyUI.window.button("icon-cancel", "关闭", function() {
                                          Dialog.close(dialogId);
                                    }) ], function() {
                              $("#" + dialogId).dialog("maximize");
                        });
      }

      function unlock() {
            var r = EasyUI.grid.getSelections("dg");
            if (r.length == 0) {
                  Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                  return;
            }
            var ids = [];

            for (var i = 0; i < r.length; i++) {
                  console.log(r[i]);
                  if (r[i].ISLOCKED == -1 || r[i].ISLOCKED == 0
                              || r[i].ISLOCKED == undefined) {
                        Tip.warn("有些产品为正常状态，请重新筛选");
                        return;
                        //return;
                  } else
                        ids.push(r[i].ID);
            }

            var dialogId = Dialog.open("解冻", 380, 270, lockpage + "?ids="
                        + ids.toString() + "&type=1", [
                        EasyUI.window.button("icon-save", "保存", function() {
                              if ($('#reasons').val() == "") {
                                    Tip.warn("请填写解冻原因");
                                    return;
                              }
                              EasyUI.form.submit("totalStatisticsForm", unlockUrl,
                                          function(data) {
                                                filter();
                                                Dialog.close(dialogId);
                                                Tip.success("解冻成功");
                                          })
                        }), EasyUI.window.button("icon-cancel", "关闭", function() {
                              Dialog.close(dialogId);
                        }) ], function() {
            });

      }
      //编辑综合统计
      var edit = function() {
            var r = EasyUI.grid.getOnlyOneSelected("dg");
            var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id="
                        + r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
                  EasyUI.form.submit("totalStatisticsForm", editUrl, function(data) {
                        filter();
                        Dialog.close(wid);
                  })
            }), EasyUI.window.button("icon-cancel", "关闭", function() {
                  Dialog.close(wid)
            }) ]);
      }

      //删除综合统计
      var doDelete = function() {
            var r = EasyUI.grid.getSelections("dg");
            if (r.length == 0) {
                  Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                  return;
            }

            var ids = [];
            for (var i = 0; i < r.length; i++) {
                  ids.push(r[i].ID);
            }
            Dialog.confirm(function() {
                  JQ.ajax(deleteUrl, "post", {
                        ids : ids.toString()
                  }, function(data) {
                        filter();
                  });
            });
      }
      /* var changeInfo = function() {
            var r = EasyUI.grid.getOnlyOneSelected("dg");
            var id = r.ID;
            var wid = Dialog.open("修改重量", dialogWidth, dialogHeight, changeUrl + "?id="
                        + r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
                  EasyUI.form.submit("changeStatisticsForm", changeUrl, function(data) {
                        filter();
                        Dialog.close(wid);
                  })
            }), EasyUI.window.button("icon-cancel", "关闭", function() {
                  Dialog.close(wid);
            }) ]);
      } */
      function buda() {
            var r = EasyUI.grid.getSelections("dg");
            if (r.length == 0) {
                  Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                  return;
            }

            var ids = [];
            for (var i = 0; i < r.length; i++) {
                  if(r[i].ROLLQUALITYGRADECODE=="A"){
                        Tip.warn("条码号："+r[i].ROLLBARCODE+"质量等级为A,不能重产质量等级为A的产品，请重新选择");
                        return;
                  }
                  ids.push(r[i].ID);
            }
            var dialogId = Dialog.open("补打条码", 380, 100, path
                        + "printer/showbudaPage" + "?ids=" + ids.toString(), [
                        EasyUI.window.button("icon-save", "打印", function() {
                              EasyUI.form.submit("doPrintBarcodeForm", path
                                          + "printer/buda", function(data) {
                                    Dialog.close(dialogId);
                              })
                        }), EasyUI.window.button("icon-cancel", "关闭", function() {
                              Dialog.close(dialogId);
                        }) ], function() {
            });
      }



      function abandon(){
            var r = EasyUI.grid.getSelections("dg");
            if (r.length == 0) {
                  Tip.warn("请至少选择一条记录");
                  return;
            }
            if (r.length > 1) {
                  Tip.warn("请选择唯一一条记录");
                  return;
            }
            if(r[0].ROLLBARCODE.indexOf("R")==0 || r[0].ROLLBARCODE.indexOf("P")==0){
                  Dialog.confirm(function() {
                        Loading.show();
                        $.ajax({
                              url : abandonUrl + "?code=" + r[0].ROLLBARCODE ,
                              type : "get",
                              dataType : "json",
                              success : function(data) {
                                    if (Tip.hasError(data)) {
                                          return;
                                    }
                                    Tip.success("作废成功");
                                    filter();
                                    Loading.hide();
                              }
                        });
                  }, "确认作废");
            }
            else{
                  Tip.warn("只能作废卷条码");
            }
      }

</script>
