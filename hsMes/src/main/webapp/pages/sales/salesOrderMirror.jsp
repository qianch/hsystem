<!--
作者:徐波
日期:2017-02-07 10:23:00
页面:产品信息查询JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>产品信息查询</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="salesOrderMirror.js.jsp" %>
    <style type="text/css">
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="产品层级结构" style="width:450px;">
    <div class="easyui-layout" fit=true>
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:52px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入订单编号" searcher="searchInfo"
                   editable="true"
                   data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
        </div>
        <div data-options="region:'center'">
            <ul id="produceTree" class="easyui-tree"></ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false">
    <div style="height:33%">
        <table singleSelect="false" fit="true" id="dg" title="非套材BOM镜像明细列表"
               style="width:auto;" class="easyui-datagrid" url="" pagination="true" rownumbers="true" fitColumns="true"
               showFooter="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="FTCBOMDETAILNAME" sortable="true" width="12"
                    data-options="editor:{type:'textbox',options:{required:true,validType:'length[1,30]'}}">编织结构
                </th>
                <th field="FTCBOMDETAILMODEL" sortable="true" width="12"
                    data-options="editor:{type:'combobox',options:{'required':true,editable:true,panelHeight:240,'icons':[],url:'${path} material/list1',valueField:'value',textField:'text'}}
					">原料规格
                </th>
                <th field="FTCBOMDETAILITEMNUMBER" sortable="true" width="12"
                    data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">物料代码
                </th>
                <th field="FTCBOMDETAILWEIGHTPERSQUAREMETRE" sortable="true" width="10"
                    data-options="editor:{type:'numberbox',options:{required:true,precision:2}}">单位面积质量(g/m²)
                </th>
                <th field="FTCBOMDETAILREED" sortable="true" width="12"
                    data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">钢筘规格
                </th>
                <th field="FTCBOMDETAILGUIDENEEDLE" sortable="true" width="12"
                    data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">导纱针规格
                </th>
                <th field="FTCBOMDETAILREMARK" sortable="true" width="12"
                    data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">备注
                </th>
                <!-- <th field="FTCBOMDETAILTOTALWEIGHT" sortable="true" width="10"
                    data-options="editor:{type:'numberbox',options:{precision:2}}">总单位面积质量(g/m²)</th> -->
            </tr>
            </thead>
        </table>
    </div>

    <div style="height:33%;">
        <div class="easyui-tabs" fit="true">
            <div title="部件镜像明细列表">
                <table singleSelect="true" fit="true" id="partsDetails" remoteSort="false" fitColumns="true"
                       pagination="true" style="width:100%;" class="easyui-datagrid" url="" rownumbers="true"
                       fitColumns="true">
                    <thead>
                    <tr>
                        <th field="I" checkbox=true></th>
                        <th field="PID" width="10">产品ID</th>
                        <!-- <th field="TCFINISHEDPRODUCTID" width="20" >成品ID</th> -->
                        <th field="MATERIELCODE" sortable="true" width=50>物料编码</th>
                        <th field="FACTORYPRODUCTNAME" sortable="true" width=20>厂内名称</th>
                        <th field="PRODUCTPROCESSNAME" sortable="true" width=20>工艺名称</th>
                        <th field="PRODUCTPROCESSCODE" sortable="true" width=20>工艺代码</th>
                        <th field="PRODUCTWIDTH" sortable="true" width=8>门幅(mm)</th>
                        <th field="PRODUCTROLLLENGTH" sortable="true" width=8>米长(m)</th>
                        <th field="PRODUCTROLLWEIGHT" width="20" sortable="true">定重(kg)</th>
                        <th field="DRAWINGNO" sortable="true" width=10
                            data-options="editor:{type:'textbox',options:{required:false}}">图号
                        </th>
                        <th field="ROLLNO" width=10 data-options="editor:{type:'textbox',options:{required:false}}">
                            卷号
                        </th>
                        <th field="LEVELNO" sortable="true" width=10
                            data-options="editor:{type:'textbox',options:{required:false}}">层数
                        </th>
                        <th field="SORTING" sortable="true" width=10
                            data-options="editor:{type:'numberspinner',options:{min:1,max:9999,precision:0,required:true}}">
                            排序号
                        </th>
                        <th field="TCPROCBOMFABRICCOUNT" sortable="true" width=10
                            data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">数量(卷/套)
                        </th>
                        <th field="TCTHEORETICALWEIGH" sortable="true" width=12
                            data-options="editor:{type:'numberbox',options:{precision:2,required:false}}">理论耗用重量(kg)
                        </th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div title="部件成品重量胚布镜像信息">
                <table singleSelect="true" fit="true" id="partsFinishedWeightEmbryoCloth" remoteSort="false"
                       fitColumns="true" pagination="true" style="width:100%;" class="easyui-datagrid" url=""
                       rownumbers="true" fitColumns="true">
                    <thead>
                    <tr>
                        <th field="ID" checkbox=true></th>
                        <th field="MATERIALNUMBER" sortable="true" width=20
                            data-options="editor:{type:'textbox',options:{required:true}}">物料代码
                        </th>
                        <th field="EMBRYOCLOTHNAME" sortable="true" width=20
                            data-options="editor:{type:'textbox',options:{required:true}}">胚布名称
                        </th>
                        <th field="WEIGHT" sortable="true" width=20
                            data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">重量(KG)
                        </th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>

    </div>
    <div style="height:33%">
        <div class="easyui-tabs" fit="true">
            <div title="版本部件镜像列表">
                <table singleSelect="true" fit="true" id="parts" pagination="true" class="easyui-datagrid" url=""
                       rownumbers="true" fitColumns="false">
                    <thead>
                    <tr>
                        <th field="ID" checkbox=true></th>
                        <th field="TCPROCBOMVERSIONMATERIALNUMBER" sortable="true" width="100"
                            data-options="editor:{type:'textbox',options:{required:true}}">部件物料号
                        </th>
                        <th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="100"
                            data-options="editor:{type:'textbox',options:{required:true}}">部件名称
                        </th>
                        <th field="CUSTOMERMATERIALCODE" sortable="true" width="100"
                            data-options="editor:{type:'textbox',options:{required:true}}">客户物料号
                        </th>
                        <th field="TCPROCBOMVERSIONPARTSTYPE" sortable="true" width="100"
                            data-options="editor:{type:'combobox',options:{required:true,data:[{value:'常规部件',text:'常规部件'},{value:'成品胚布',text:'成品胚布'},{value:'中小部件',text:'中小部件'}]}}">
                            部件类型
                        </th>
                        <th field="TCPROCBOMVERSIONPARTSCUTCODE" sortable="true" width="100"
                            data-options="editor:{type:'textbox'}">工艺代码
                        </th>
                        <th field="TCPROCBOMVERSIONPARTSCOUNT" sortable="true" width="160"
                            data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">部件数量
                        </th>
                        <th field="TCPROCBOMVERSIONPARTSSUBSCOUNT" sortable="true" width="160"
                            data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">小部件个数
                        </th>
                        <th field="TCPROCBOMVERSIONPARTSWEIGHT" sortable="true" width="160"
                            data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">部件重量(kg/套)
                        </th>
                        <th field="NEEDSORT" sortable="true" width="160"
                            data-options="editor:{type:'combobox',options:{required:true,valueField:'v',textField:'t',data:[{v:'0',t:'是'},{v:'1',t:'否'}]}},formatter:function(v,r,i){return v==0?'是':'否';}">
                            按序组套
                        </th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div title="成品信息列表">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-tip"
                   onclick="checkProduct()">查看</a>

                <table singleSelect="true" fit="true" id="product" pagination="true" class="easyui-datagrid" url=""
                       rownumbers="true" fitColumns="false">
                    <thead frozen="true">
                    <tr>
                        <th field="I" checkbox=true></th>
                        <th field="ID" width="90">ID</th>
                        <th field="ISCOMMON" sortable="true" width="50" formatter="formatterIscommon">产品属性</th>
                        <th field="AUDITSTATE" sortable="true" width="100" formatter="formatterReviewState">审核状态
                        </th>
                    </tr>
                    </thead>
                    <thead>
                    <tr>
                        <th field="CONSUMERPRODUCTNAME" sortable="true" width="230">客户产品名称</th>

                        <th field="PRODUCTCONSUMERID" sortable="true" width="230">客户id</th>
                        <th field="FACTORYPRODUCTNAME" sortable="true" width="160">厂内名称</th>
                        <th field="PRODUCTMODEL" sortable="true" width="150">产品型号</th>
                        <th field="CATEGORYNAME" sortable="true" width="75">成品类别</th>
                        <th field="CATEGORYCODE" sortable="true" width="75">成品编号</th>
                        <th field="PRODUCTWIDTH" sortable="true" width="75">门幅(mm)</th>
                        <th field="PRODUCTROLLLENGTH" sortable="true" width="75">定长(m)</th>
                        <th field="MINWEIGHT" sortable="true" width="75">下限卷重(kg)</th>
                        <th field="PRODUCTROLLWEIGHT" sortable="true" width="75">定重(kg)</th>
                        <th field="MAXWEIGHT" sortable="true" width="75">上限卷重(kg)</th>
                        <th field="ROLLGROSSWEIGHT" sortable="true" width="75">卷毛重(kg)</th>
                        <th field="PRODUCTPROCESSNAME" sortable="true" width="200" styler="vStyler">工艺名称</th>
                        <th field="PRODUCTPROCESSCODE" sortable="true" width="200" styler="vStyler">工艺标准代码</th>
                        <th field="PRODUCTPROCESSBOMVERSION" sortable="true" width="100">工艺标准版本</th>
                        <th field="PRODUCTCONSUMERBOMVERSION" sortable="true" width="100">客户版本号</th>
                        <th field="PRODUCTPACKAGINGCODE" sortable="true" width="200" formatter="formatterPackagingCode">
                            包装标准代码
                        </th>
                        <th field="PRODUCTPACKAGEVERSION" sortable="true" width="100">包装标准版本</th>
                        <th field="PRODUCTSHELFLIFE" sortable="true" width="80">保质期(天)</th>
                        <th field="PRODUCTISTC" sortable="true" width="80" formatter="formatterIsTc">是否为套材</th>
                        <th field="PRODUCTWEIGH" sortable="true" width="80" formatter="formatterWeigh">称重规则</th>
                        <th field="CARRIERCODE" width="130">衬管编码</th>
                        <th field="MATERIELCODE" width="130">物料编码</th>
                        <th field="PRODUCTMEMO" width="130">备注</th>
                        <th field="OBSOLETE" width="130" formatter="formatterS">状态</th>

                        <th field="CREATER" sortable="true" width="130">创建人</th>
                        <th field="CREATETIME" sortable="true" width="130">创建时间</th>
                        <th field="CREATETIME" sortable="true" width="130" onclick="">创建时间</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
</body>