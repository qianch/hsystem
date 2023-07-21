<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bom版本信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/meta.jsp" %>
<style>
    form {
        margin: 0;
        padding: 0;
    }
</style>
<script type="text/javascript">
    //JS代码
    const details = ${details};
    $(document).ready(function () {
        let FTCBOMDETAILTOTALWEIGHT = 0;
        for (var a = 0; a < details.length; a++) {
            add_ftcBomDetail_data(details[a]);
            FTCBOMDETAILTOTALWEIGHT += details[a].FTCBOMDETAILWEIGHTPERSQUAREMETRE;
        }
        $("#dg_ftcBom_detail").datagrid("reloadFooter", [{
            "FTCBOMDETAILWEIGHTPERSQUAREMETRE": FTCBOMDETAILTOTALWEIGHT,
            "FTCBOMDETAILITEMNUMBER": "单位面积质量合计(g/m²)"
        }]);
    });

    function add_ftcBomDetail_data(r) {
        const _row = {
            "ID": r.ID,
            "FTCBOMDETAILNAME": r.FTCBOMDETAILNAME,
            "FTCBOMDETAILMODEL": r.FTCBOMDETAILMODEL,
            "FTCBOMDETAILITEMNUMBER": r.FTCBOMDETAILITEMNUMBER,
            "FTCBOMDETAILWEIGHTPERSQUAREMETRE": r.FTCBOMDETAILWEIGHTPERSQUAREMETRE,
            "FTCBOMDETAILREED": r.FTCBOMDETAILREED,
            "FTCBOMDETAILGUIDENEEDLE": r.FTCBOMDETAILGUIDENEEDLE,
            "FTCBOMDETAILREMARK": r.FTCBOMDETAILREMARK,
            "FTCBOMDETAILTOTALWEIGHT": r.FTCBOMDETAILTOTALWEIGHT,
        };
        $("#dg_ftcBom_detail").datagrid("appendRow", _row);
    }
</script>
<body class="easyui-layout">
<div data-options="region:'north'" style="height:78px">
    <!--非套材bom版本信息表单-->
    <form id="ftcBomVersionForm" method="post" ajax="true"
          action="<%=basePath %>ftcBomVersion/${empty bCBomVersion.id ?'add':'edit'}" autocomplete="off"
          autoSearchFunction="false">
        <input type="hidden" name="id" value="${bCBomVersion.id}"/> <input id="packBomId" type="hidden" name="packBomId"
                                                                           value="${bCBomVersion.packBomId}"/>
        <table style="width: 100%">
            <tr>
                <td class="title">版本号:</td>
                <!--版本号-->
                <td><input type="text" id="packVersion" name="packVersion"
                           value="${ftcBomVersion.ftcProcBomVersionCode}" class="easyui-textbox" readonly="true"></td>
            </tr>
            <%-- <tr>
                <td class="title">是否启用:</td>
                <!--是否启用，-1不启用，1启用-->
                <td><input type="text" id="packEnabled" name="packEnabled" value="${ftcBomVersion.ftcProcBomVersionEnabled==-1?'不启用':'启用'}" class="easyui-textbox" readonly="true"></td>
            </tr>
            <tr>
                <td class="title">是否默认:</td>
                <!--是否默认，-1不是默认，1默认-->
                <td><input type="text" id="packIsDefault" name="packIsDefault" value="${ftcBomVersion.ftcProcBomVersionDefault==-1?'非默认':'默认'}" class="easyui-textbox" readonly="true"></td>
            </tr> --%>
        </table>
    </form>
</div>
<div data-options="region:'center'">
    <table id="dg_ftcBom_detail" singleSelect="false" title="非套材Bom明细" class="easyui-datagrid" url=""
           rownumbers="true" fitColumns="true" fit="true" showFooter="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="FTCBOMDETAILNAME" width="15">编织结构</th>
            <th field="FTCBOMDETAILMODEL" width="15">原料规格</th>
            <th field="FTCBOMDETAILITEMNUMBER" width="15">物料代码</th>
            <th field="FTCBOMDETAILWEIGHTPERSQUAREMETRE" width="15">单位面积质量(g/m²)</th>
            <th field="FTCBOMDETAILREED" width="10">钢筘规格</th>
            <th field="FTCBOMDETAILGUIDENEEDLE" width="10">导纱针规格</th>
            <th field="FTCBOMDETAILREMARK" width="10">备注</th>
            <!-- 					<th field="FTCBOMDETAILTOTALWEIGHT" width="15">总单位面积质量(g/m²)</th> -->
        </tr>
        </thead>
    </table>
</div>
</body>