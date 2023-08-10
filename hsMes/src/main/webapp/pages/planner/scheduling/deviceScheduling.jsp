<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>机台排产</title>
    <%@ include file="../../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/fullcalendar/fullcalendar.min.css">
    <script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/lib/moment.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/fullcalendar.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/locale/zh-cn.js"></script>
    <%@ include file="../produce/producePlanDetailPrint/producePlanDetailPrint.js.jsp" %>
    <%@ include file="deviceScheduling.js.jsp" %>
    <style type="text/css">
        .producing {
            background: rgb(3, 183, 35);
            color: white;
            float: right;
            margin-left: 10px;
        }

        .prior {
            color: orange;
            float: right;
        }

        .datagrid-row-expander {
            margin: 4px 0;
            display: inline-block;
            width: 16px;
            height: 25px;
            cursor: pointer;
        }

        .title {
            color: #051e82 !important;
        }
    </style>
</head>
<body class="easyui-layout">
<div data-options="region:'west',width:350,minWidth:200,maxWidth:600,split:true,border:true,collapsible:true"
     title="机台排产">
    <div id="dgToolbar">
        <div class="tool">
            <a href="javascript:void(0)" plain="true" onclick="doUnSelectAll()" class="easyui-linkbutton"
               id="doUnSelectAll" class="tool" iconCls="platform-cancel_select">取消选中</a>
            <a href="javascript:void(0)" plain="true" onclick="singleSelect(false)" class="easyui-linkbutton"
               id="singleSelectfalse" class="tool" iconCls="platform-multi_select">多选</a>
            <a href="javascript:void(0)" plain="true" onclick="singleSelect(true)" class="easyui-linkbutton"
               id="singleSelecttrue" class="tool" iconCls="platform-single_select">单选</a>
        </div>
        <form id="dgForm" autoSearchFunction="false">
            车　　间：<input type="text" id="did" name="filter[did]" class="easyui-combobox" style="width:120px;"
                          url="${path}device/scheduling/department"
                          data-options="valueField:'ID',textField:'NAME',onChange:filter"><br>
            机 台 号:　<input type="text" id="dcodes" name="filter[dcodes]" in="true" class="easyui-textbox"
                             prompt="A1,A2逗号分割" style="width:122px;"><br>
            订单交期：<input type="text" style="width:120px;" class="easyui-datebox" id="start" name="filter[start]"
                            data-options="icons:[]">~<input type="text" style="width:120px;" class="easyui-datebox"
                                                            id="end"
                                                            name="filter[end]" data-options="icons:[]"><br>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="filter()">搜索</a>
        </form>
    </div>
    <table id="dg" idField="ID" singleSelect="true" class="easyui-datagrid" toolbar="#dgToolbar" pagination="false"
           rownumbers="true" fitColumns="true" fit="true"
           data-options="onBeforeLoad:beforeLoad,onLoadSuccess:loadSuccess,onClickRow:doClick,onSelectAll:onSelectAll,onUnselectAll:unSelectAll,onBeforeLoad:beforeLoad,onLoadSuccess:dgLoadSuccess">
        <thead>
        <tr>
            <th field="ID" checkbox="true" width="80"></th>
            <!-- <th field="DEVICENAME" sortable="true" width="100">设备名称</th> -->
            <th field="DEVICECODE" sortable="true" width="40">设备代码</th>
            <th field="NAME" sortable="true" width="80">所属部门</th>
            <th field="DELEVERYDATE" sortable="true" width="60" formatter="dateFormatter">订单交期</th>
        </tr>
        </thead>
    </table>
</div>
<div data-options="region:'center'">
    <div id="weaveDgToolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="finish" name="ids"/>
            <jsp:param value="doClose" name="ids"/>
            <jsp:param value="sort" name="ids"/>
            <jsp:param value="cancelSort" name="ids"/>
            <jsp:param value="print" name="ids"/>
            <jsp:param value="print_tray" name="ids"/>
            <jsp:param value="viewPackageBOM" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-delete" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="platform-close" name="icons"/>
            <jsp:param value="platform-arrow_up" name="icons"/>
            <jsp:param value="icon-no" name="icons"/>
            <jsp:param value="icon-print" name="icons"/>
            <jsp:param value="icon-print" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="添加" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="完成" name="names"/>
            <jsp:param value="关闭" name="names"/>
            <jsp:param value="优先生产" name="names"/>
            <jsp:param value="取消优先" name="names"/>
            <jsp:param value="打印条码" name="names"/>
            <jsp:param value="打印托条码" name="names"/>
            <jsp:param value="doAdd()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="finish()" name="funs"/>
            <jsp:param value="doClose()" name="funs"/>
            <jsp:param value="ssort()" name="funs"/>
            <jsp:param value="cancelSort()" name="funs"/>
            <jsp:param value="doPrintList()" name="funs"/>
            <jsp:param value="doPrintTray()" name="funs"/>
            <jsp:param value="viewPackageBOM()" name="funs"/>
        </jsp:include>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editPlanDetailPrints()">修改打印属性</a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print"
           onclick="doRollPrint()"> 打印卷条码(个性化) </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
           onclick="viewPackageTask()"> 查看包装任务 </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
           onclick="viewPackageBOM()"> 查看包装BOM </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
           onclick="checkbjinfo()"> 查看 </a>
    </div>
    <table id="weaveDg" singleSelect="false"
           title="生产任务<font class='producing'>正在生产</font><font class='prior'>优先生产</font>"
           class="easyui-datagrid" toolbar="#weaveDgToolbar" rownumbers="true" fitColumns="false" pagination="false"
           fit="true" remoteSort="false" data-options="rowStyler:isProducing,onLoadSuccess:loadSuccess2">
        <!-- rowStyler:rowStyler,onDblClickRow:editDevices -->
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="DEVCODE" sortable="true" width="60">设备代码</th>
            <!-- <th field="ISFINISHED" sortable="true" width="80" formatter="formatterIsFinish">完成状态</th>
            <th field="CLOSED" sortable="true" width="80" formatter="formatterIsClosed">关闭状态</th> -->
            <!-- <th field="SALESORDERSUBCODE" sortable="true" width="130">销售订单号</th> -->
            <th field="SALESORDERSUBCODEPRINT" sortable="true" width="180">客户订单号</th>
            <th field="BATCHCODE" sortable="true" width="100">批次号</th>
            <th field="DELEVERYDATE" sortable="true" width="90" formatter="dateFormat">出货日期</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="YX" sortable="true" width="50">叶型名称</th>
            <th field="PARTNAME" sortable="true" width="50">部件名称</th>
            <th field="DRAWNO" sortable="true" width="50">图号</th>
            <th field="ROLLNO" sortable="true" width="50">卷号</th>
            <th field="LEVELNO" sortable="true" width="50">层数</th>
            <th field="SORTING" sortable="true" width="50">排序号</th>
            <th field="PLANCODE" sortable="true" width="160">生产计划单号</th>
            <th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="150">客户产品名称</th>
            <th field="PRODUCTWIDTH" sortable="true" width="80">门幅(mm)</th>
            <th field="PRODUCTLENGTH" sortable="true" width="80">卷长(m)</th>
            <th field="PRODUCTWEIGHT" sortable="true" width="80">卷重(kg)</th>
            <th field="REQCOUNT" sortable="true" width="80" formatter="planCountFormatter">计划数量</th>
            <th field="PLANTOTALWEIGHT" sortable="true" width="80" formatter="planTotalWeightFormatter2">计划总重量</th>
            <th width="80" field="PLANASSISTCOUNT" data-options="formatter:planAssistCountFormatter">排产辅助数量</th>
            <th field="RC" sortable="true" width="80" formatter="processFormatter3">生产进度</th>
            <th field="TC" sortable="true" width="80" formatter="totalTrayCount">打包进度</th>
            <!-- <th field="TOTALROLLCOUNT" sortable="true" width="100">总卷数</th>
            <th field="TOTALTRAYCOUNT" sortable="true" width="100">总托数</th> -->
            <th field="CONSUMERSIMPLENAME" sortable="true" width="250" formatter="formatterC">客户简称</th>
            <th field="CONSUMERCODE" sortable="true" width="250">客户代码</th>
            <!-- <th field="PRODUCTTYPE" sortable="true" width="80" formatter="formatterType">产品属性</th> -->
            <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
            <th field="PROCESSBOMVERSION" width="80" styler="vStyler">工艺版本</th>
            <th field="PROCREQ" width="80" styler="vStyler">工艺需求</th>
            <th field="PREQ" width="130" styler="bvStyler">包装需求</th>
            <th field="BC" width="130" styler="bvStyler">包装代码</th>
            <th field="BV" width="80" styler="bvStyler">包装版本</th>
            <th field="SALESORDERMEMO" width="80">销售订单备注</th>
            <th field="COM" width="80">订单产品备注</th>
            <th field="ISCOMEFROMTC" sortable="true" hidden="hidden" formatter="formatterIsFinish"></th>
            <th field="ISStAMP" sortable="true" hidden="hidden"></th>
        </tr>
        </thead>
    </table>
</div>
<div id="packTaskInfoWin" class="easyui-window" title="包装BOM信息" data-options="modal:true,closed:true"
     style="width:500px;height:200px;">
    <div class="easyui-layout" style="width:100%;height:100%;" fit="true">
        <div data-options="region:'east',split:true" title="包装信息" style="width:200px;">
            <table id="rc" style="width: 99%;">
                <tr>
                    <td class="title" style="text-align: left;">包装标准代码</td>
                </tr>
                <tr>
                    <td id="packCode"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">适用客户</td>
                </tr>
                <tr>
                    <td id="consumerName"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">卷径/cm</td>
                </tr>
                <tr>
                    <td id="rollDiameter"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">每托卷数</td>
                </tr>
                <tr>
                    <td id="rollsPerPallet"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">托长/cm</td>
                </tr>
                <tr>
                    <td id="palletLength"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">托宽/cm</td>
                </tr>
                <tr>
                    <td id="palletWidth"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">托高/cm</td>
                </tr>
                <tr>
                    <td id="palletHeight"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">包材重量/kg</td>
                </tr>
                <tr>
                    <td id="bcTotalWeight"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">塑料膜要求</td>
                </tr>
                <tr>
                    <td id="requirement_suliaomo"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">摆放要求</td>
                </tr>
                <tr>
                    <td id="requirement_baifang"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">打包带要求</td>
                </tr>
                <tr>
                    <td id="requirement_dabaodai"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">标签要求</td>
                </tr>
                <tr>
                    <td id="requirement_biaoqian" colspan="3"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">小标签要求</td>
                </tr>
                <tr>
                    <td id="requirement_xiaobiaoqian" colspan="3"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">卷（箱）标签要求</td>
                </tr>
                <tr>
                    <td id="requirement_juanbiaoqian" colspan="3"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">托标签要求</td>
                </tr>
                <tr>
                    <td id="requirement_tuobiaoqian" colspan="3"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">缠绕要求</td>
                </tr>
                <tr>
                    <td id="requirement_chanrao" colspan="3"></td>
                </tr>
                <tr>
                    <td style="text-align: left;" class="title">其他要求</td>
                </tr>
                <tr>
                    <td id="requirement_other" colspan="3"></td>
                </tr>
            </table>
        </div>
        <div data-options="region:'center',iconCls:'icon-ok'">
            <table id="packInfoDg" singleSelect="false" title="包材信息" class="easyui-datagrid" pagination="false"
                   rownumbers="true" fitColumns="true" fit="true"
                   data-options="
		                collapsible:true,
		                rownumbers:true,
		                fitColumns:true,
		                view:groupview,
		                groupField:'CODE',
		                groupFormatter:function(value,rows){
		                    return '代码[<font color=red>'+value+'</font>] 版本[<font color=red>'+rows[0].VERSION + '</font>] - ' + rows.length + ' 种包材 <a onclick=showMore('+rows[0].VID+',\''+rows[0].CODE+'\')><font color=red>查看包装信息</font></a>';
		                }
		            ">
                <thead>
                <tr>
                    <th field="PACKMATERIALCODE" width="15%">物料代码</th>
                    <th field="PACKMATERIALCODE" width="15%">物料代码</th>
                    <th field="PACKSTANDARDCODE" width="12%">标准码</th>
                    <th field="PACKMATERIALNAME" width="12%">材料名称</th>
                    <th field="PACKMATERIALMODEL" width="15%">规格</th>
                    <th field="PACKUNIT" width="5%">单位</th>
                    <th field="PACKMATERIALCOUNT" width="5%">数量</th>
                    <th field="PACKMEMO" width="10%">备注</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>
</html>
