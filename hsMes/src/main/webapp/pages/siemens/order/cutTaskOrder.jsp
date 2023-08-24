<!--
作者:高飞
日期:2017-7-31 17:04:13
页面:裁剪派工单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>裁剪派工单</title>
    <%@ include file="../../base/meta.jsp" %>
    <script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.PrintArea.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.qrcode.min.js"></script>
    <%@ include file="cutTaskOrder.js.jsp" %>
    <style type="text/css">
        button {
            background: white;
            border: 1px solid #e2dbdb;
            font-size: 12px;
        }

        button:hover {
            background: #fff900;
            color: #9f00ff;
        }

        button:active {
            background: red;
            color: white;
        }

        .title {
            min-width: 100px;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="enableTask" name="ids"/>
            <jsp:param value="closeTask" name="ids"/>
            <jsp:param value="exportTask" name="ids"/>
            <jsp:param value="exportCheckBarcode" name="ids"/>
            <jsp:param value="printTask" name="ids"/>
            <jsp:param value="printBarcode" name="ids"/>
            <jsp:param value="viewDrawings" name="ids"/>
            <jsp:param value="viewSuit" name="ids"/>

            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="platform-close" name="icons"/>
            <jsp:param value="icon-excel" name="icons"/>
            <jsp:param value="icon-excel" name="icons"/>
            <jsp:param value="icon-print" name="icons"/>
            <jsp:param value="icon-print" name="icons"/>
            <jsp:param value="platform-icon78" name="icons"/>
            <jsp:param value="platform-icon68" name="icons"/>

            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="启用" name="names"/>
            <jsp:param value="关闭" name="names"/>
            <jsp:param value="导出派工单" name="names"/>
            <jsp:param value="导出小部件核对表" name="names"/>
            <jsp:param value="派工单打印" name="names"/>
            <jsp:param value="条码打印" name="names"/>
            <jsp:param value="查看图纸BOM" name="names"/>
            <jsp:param value="查看组套BOM" name="names"/>

            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="enableTask()" name="funs"/>
            <jsp:param value="closeTask()" name="funs"/>
            <jsp:param value="exportTask()" name="funs"/>
            <jsp:param value="exportCheckBarcode()" name="funs"/>
            <jsp:param value="printTask()" name="funs"/>
            <jsp:param value="printBarcode()" name="funs"/>
            <jsp:param value="viewDrawings()" name="funs"/>
            <jsp:param value="viewSuit()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <div id="p" class="easyui-panel" title="查询"
                 style="width:100%;height:120px; padding:5px;background:rgb(250, 250, 250);"
                 data-options="iconCls:'icon-search',collapsible:true,onExpand:resizeDg,onCollapse:resizeDg">
                <form action="#" id="cutTaskOrderSearchForm" autoSearch="true" autoSearchFunction="false">
                    派工单号:<input type="text" class="easyui-textbox" like="true" name="filter[CTOCODE]">
                    　订单号:<input type="text" class="easyui-textbox" like="true" name="filter[ORDERCODE]">
                    　批次号:<input type="text" class="easyui-textbox" like="true" name="filter[BATCHCODE]">
                    　　组别:<input type="text" class="easyui-textbox" like="true" name="filter[GROUPNAME]">
                    <br>
                    部件名称:<input type="text" class="easyui-textbox" like="true" name="filter[PARTNAME]">
                    客户名称:<input type="text" class="easyui-textbox" like="true" name="filter[CONSUMERSIMPLENAME]">
                    完成状态:<input type="text" class="easyui-combobox" name="filter[ISCOMPLETE]"
                                    data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'0',t:'未完成'},{'v':'1',t:'完成'}],onChange:filter">
                    客户大类:<input type="text" class="easyui-combobox" name="filter[CONSUMERCATEGORY]"
                                    data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'1',t:'国内'},{'v':'2',t:'国外'}],onChange:filter">
                    <br>
                    交货日期:<input type="text" class="easyui-datebox" name="filter[DELIVERYDATE_S]"
                                    data-options="onSelect:filter">
                    　　至　 <input type="text" class="easyui-datebox" name="filter[DELIVERYDATE_E]"
                                 data-options="onSelect:filter">
                    　　状态:<input type="text" class="easyui-combobox" name="filter[ISCLOSED]"
                                  data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'0',t:'启用'},{'v':'1',t:'关闭'}],onChange:filter">
                    　　　　<a class="easyui-linkbutton" iconcls="icon-search" onclick="filter()"> 搜索 </a>
                </form>
            </div>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}siemens/cutTaskOrder/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="
				rowStyler:styler,
				view: detailview,
                detailFormatter:function(index,row){
                    return '<div style=\'padding:2px;position:relative;\'><table class=\'ddv\'></table></div>';
                },
				onExpandRow: function(index,row){
                    var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
                    ddv.datagrid({
                        url:path+'siemens/cutTaskOrder/drawings?ctoId='+row.ID,
                        fitColumns:true,
                        singleSelect:true,
                        rownumbers:true,
                        loadMsg:'',
                        title:'小部件条码核对',
                        height:'auto',
                        columns:[[
                        	<%--
                        	{field:'ID',checkbox:'true',width:15},
                        	{field:'FRAGMENTDRAWINGNO',title:'图号',width:15},
							{field:'FRAGMENTDRAWINGVER',title:'图纸版本',width:15},
							{field:'PRINTSORT',title:'出图顺序',width:15},
                        	{field:'FARBICMODEL',title:'胚布规格',width:15},--%>
                        	{field:'FARBICMODEL',title:'胚布规格',width:15},
                        	{field:'FRAGMENTCODE',title:'小部件名称',width:15},
                        	{field:'FRAGMENTNAME',title:'小部件名称',width:15},
                        	{field:'FRAGMENTCOUNTPERDRAWINGS',title:'单套数量',width:15},
                        	{field:'NEEDTOPRINTCOUNT',title:'应打数量',width:15},
							{field:'PRINTEDCOUNT',title:'已打数量',width:15},
							{field:'EXTRAPRINTCOUNT',title:'补打数量',width:15},
							{field:'REPRINTCOUNT',title:'重打数量',width:15},
							{field:'X',title:'操作',width:15,formatter:function(v,r,i){
								var fragment=r.FRAGMENTCODE+'/'+r.FRAGMENTNAME;
								fragment=fragment.replace(/\s+/g,'&nbsp;');
								return '<button onclick=rePrint('+row.ID+','+r.DWID+',\''+fragment+'\')>重打</button>'
							}},
                        ]],
                        onResize:function(){
                            $('#dg').datagrid('fixDetailRowHeight',index);
                            ddv.datagrid('enableFilter');
                        },
                        onLoadSuccess:function(){
                            setTimeout(function(){
                                $('#dg').datagrid('fixDetailRowHeight',index);
                            },0);
                        }
                    });
                    $('#dg').datagrid('fixDetailRowHeight',index);
                }">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="ISCLOSED" sortable="true" width="8" formatter="closedFormatter">状态</th>
            <th field="CTOCODE" sortable="true" width="25">派工单编号</th>
            <th field="CTOGROUPNAME" sortable="true" width="8">组别</th>
            <th field="CTOGROUPLEADER" sortable="true" width="10">机长</th>
            <th field="TASKCODE" sortable="true" width="25">任务单编号</th>
            <th field="ORDERCODE" sortable="true" width="15">订单号</th>
            <th field="PARTNAME" sortable="true" width="15">部件名称</th>
            <th field="BATCHCODE" sortable="true" width="15">批次号</th>
            <th field="CONSUMERSIMPLENAME" sortable="true" width="15">客户简称</th>
            <th field="CONSUMERCATEGORY" sortable="true" width="15" formatter="ccFormat">客户大类</th>
            <th field="SUITCOUNT" sortable="true" width="15">任务单套数</th>
            <th field="ASSIGNSUITCOUNT" sortable="true" width="15">派工套数</th>
            <th field="PACKEDSUITCOUNT" sortable="true" width="15">已打包套数</th>
            <th field="DELIVERYDATE" sortable="true" width="15" formatter="dateFormatter">发货日期</th>
            <th field="CREATETIME" sortable="true" width="15" formatter="dateFormatter">派工时间</th>
            <th field="CREATEUSERNAME" sortable="true" width="15">派工人</th>
            <th field="ISCOMPLETE" sortable="true" width="15" formatter="completeFormatter">完成情况</th>
        </tr>
        </thead>
    </table>
</div>
<%@ include file="cutTaskOrderEdit.jsp" %>
<%@ include file="cutTaskOrderPrint.jsp" %>
<%@ include file="cutTaskOrderBarcodePrint.jsp" %>
<%@ include file="cutTaskOrderBarcodeRePrint.jsp" %>
<%@ include file="bomView.jsp" %>
</body>
</html>