<!--
作者:宋黎明
日期:2016-9-30 10:49:34
页面:成品信息JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>成品信息</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="finishedProductPrintRecord.js.jsp" %>
    <%@ include file="finishProduct.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="audit" name="ids"/>
            <jsp:param value="copy" name="ids"/>
            <jsp:param value="audit" name="ids"/>
            <jsp:param value="view" name="ids"/>
            <jsp:param value="Import" name="ids"/>
            <jsp:param value="Template" name="ids"/>
            <jsp:param value="edit" name="ids"/>

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="platform-copy" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="platform-icon154" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="icon-excel" name="icons"/>
            <jsp:param value="icon-excel" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>

            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="作废" name="names"/>
            <jsp:param value="恢复" name="names"/>
            <jsp:param value="复制" name="names"/>
            <jsp:param value="提交审核" name="names"/>
            <jsp:param value="查看审核" name="names"/>
            <jsp:param value="导入" name="names"/>
            <jsp:param value="模板导出" name="names"/>
            <jsp:param value="修改打印记录" name="names"/>

            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="old()" name="funs"/>
            <jsp:param value="resumeFinishProduct()" name="funs"/>
            <jsp:param value="copy()" name="funs"/>
            <jsp:param value="doAudit()" name="funs"/>
            <jsp:param value="view()" name="funs"/>
            <jsp:param value="Import()" name="funs"/>
            <jsp:param value="downloadTemplate()" name="funs"/>
            <jsp:param value="editfinishedProductPrintRecord()" name="funs"/>

        </jsp:include>

        <%--<jsp:param value="change" name="ids" />--%>
        <%--	<jsp:param value="icon-edit" name="icons" />--%>
        <%--<jsp:param value="产品变更" name="names" />--%>
        <%--<jsp:param value="edit(1)" name="funs" />--%>

        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-tip" onclick="checkProduct()">查看</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-tip" onclick="checkYxInfo()">查看叶型</a>

        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="finishProductSearchForm" autoSearchFunction="false">
                客户代码：<input type="text" name="filter[consumerCode]" like="true" class="easyui-textbox">
                客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox">
                客户产品：<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox">
                审核状态：<input type="text" name="filter[auditState]" class="easyui-combobox" data-options="data: [
		                        {value:'0',text:'未提交'},
		                        {value:'1',text:'审核中'},
		                        {value:'2',text:'已通过'},
		                        {value:'-1',text:'不通过'}],onSelect:filter">
                </br>
                厂内名称：<input type="text" name="filter[factoryName]" like="true" class="easyui-textbox">
                　　门幅：<input type="text" name="filter[productWidth]" like="true" class="easyui-textbox">
                工艺代码：<input type="text" name="filter[productProcessCode]" like="true" class="easyui-textbox">
                产品类型：<input type="text" name="filter[productIsTc]" class="easyui-combobox" data-options="data: [
		                        {value:'1',text:'套材'},
		                        {value:'2',text:'非套材'},
		                        {value:'-1',text:'胚布'}],onSelect:filter">    </br>
                包装代码：<input type="text" name="filter[productPackagingCode]" like="true" class="easyui-textbox">
                产品型号：<input type="text" name="filter[productModel]" like="true" class="easyui-textbox">
                作废成品：<input type="text" name="filter[old]" class="easyui-combobox"
                            data-options="data: [{value:'1',text:'已作废'}],onSelect:filter">&nbsp;&nbsp;
                定&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长：<input type="text" name="filter[productRollLength]" like="true"
                                                        class="easyui-textbox"></br>
                物料编码：<input type="text" name="filter[materielCode]" like="true" class="easyui-textbox">
                　　　ID：<input type="text" name="filter[id]" class="easyui-textbox">
                工艺名称：<input type="text" name="filter[productProcessName]" like="true" class="easyui-textbox">
                原料规格：<input type="text" name="filter[ftcBomDetailModel]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dgfinishProduct" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="false" fit="true"
           data-options="onDblClickRow:dbClickEdit,rowStyler:productStyler">
        <thead frozen="true">
        <tr>
            <th field="I" checkbox=true></th>
            <th field="ID" width="90">ID</th>
            <th field="CONSUMERCODE" sortable="true" width="90">客户代码</th>
            <th field="CONSUMERNAME" sortable="true" width="200">客户名称</th>
            <th field="ISCOMMON" sortable="true" width="50" formatter="formatterIscommon">产品属性</th>
            <th field="AUDITSTATE" sortable="true" width="100" formatter="formatterReviewState">审核状态</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="230">客户产品名称</th>
            <th field="CUSTOMERMATERIALCODEOFFP" sortable="true" width="230">客户物料号</th>
            <th field="PRODUCTCONSUMERID" sortable="true" width="230">客户id</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="160">厂内名称</th>
            <th field="PRODUCTMODEL" sortable="true" width="150">产品型号</th>
            <th field="CATEGORYNAME" sortable="true" width="75">成品类别</th>
            <th field="CATEGORYCODE" sortable="true" width="75">成品编号</th>
            <th field="PRODUCTWIDTH" sortable="true" width="75">门幅(mm)</th>
            <th field="PRODUCTROLLLENGTH" sortable="true" width="75">定长(m)</th>
            <th field="RESERVELENGTH" sortable="true" width="75">预留长度(m)</th>
            <th field="MINWEIGHT" sortable="true" width="75">下限卷重(kg)</th>
            <th field="PRODUCTROLLWEIGHT" sortable="true" width="75">定重(kg)</th>
            <th field="MAXWEIGHT" sortable="true" width="75">上限卷重(kg)</th>
            <th field="ROLLGROSSWEIGHT" sortable="true" width="75">卷毛重(kg)</th>
            <th field="PRODUCTPROCESSNAME" sortable="true" width="200" styler="vStyler">工艺名称</th>
            <th field="PRODUCTPROCESSCODE" sortable="true" width="200" formatter="bomVersionView" styler="vStyler">
                工艺标准代码
            </th>
            <th field="PRODUCTPROCESSBOMVERSION" sortable="true" width="100">工艺标准版本</th>
            <th field="PRODUCTCONSUMERBOMVERSION" sortable="true" width="100">客户版本号</th>
            <th field="PRODUCTPACKAGINGCODE" sortable="true" width="200" formatter="formatterPackagingCode">包装标准代码</th>
            <th field="PRODUCTPACKAGEVERSION" sortable="true" width="100">包装标准版本</th>
            <th field="PRODUCTSHELFLIFE" sortable="true" width="80">保质期(天)</th>
            <th field="PRODUCTISTC" sortable="true" width="80" formatter="formatterIsTc">是否为套材</th>
            <th field="PRODUCTWEIGH" sortable="true" width="80" formatter="formatterWeigh">称重规则</th>
            <th field="CARRIERCODE" width="130">衬管编码</th>
            <th field="MATERIELCODE" width="130">物料编码</th>
            <th field="PRODUCTMEMO" width="130">备注</th>
            <th field="OBSOLETE" width="130" formatter="formatterS">状态</th>
            <th field="PRODUCTRTOTALWEIGHT" width="130" >产品总克重</th>

            <th field="CREATER" sortable="true" width="130">创建人</th>
            <th field="CREATETIME" sortable="true" width="130">创建时间</th>
            <th field="MODIFYUSER" sortable="true" width="130">修改人</th>
            <th field="MODIFYTIME" sortable="true" width="130">修改时间</th>
            <!-- 				<th field="PRODUCTROLLCODE" sortable="true" width="100">卷标签代码</th>
                                <th field="PRODUCTBOXCODE" sortable="true" width="100">箱唛头代码</th>
                                <th field="PRODUCTTRAYCODE" sortable="true" width="100">托唛头代码</th> -->
        </tr>
        </thead>
    </table>
</div>
</body>
