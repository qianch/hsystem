<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择人员信息</title>
<%@ include file="../base/jstl.jsp" %>
<style>
    form {
        margin: 0;
        padding: 0;
    }
</style>
<script type="text/javascript">
    let details =${details};

    function _common_bomDetail_data(r) {
        const _row = {
            "ID": r.ID,
            "FTCBOMDETAILNAME": r.FTCBOMDETAILNAME,
            "FTCBOMDETAILMODEL": r.FTCBOMDETAILMODEL,
            "FTCBOMDETAILWEIGHTPERSQUAREMETRE": r.FTCBOMDETAILWEIGHTPERSQUAREMETRE,
            "FTCBOMDETAILTOTALWEIGHT": r.FTCBOMDETAILTOTALWEIGHT,
        };
        $("#_common_ftcBom_detail").datagrid("appendRow", _row);
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north'" style="height:78px">
        <!--非套材bom版本信息表单-->
        <form id="ftcBomVersionForm" method="post" ajax="true" autocomplete="off">
            <table width="100%">
                <tr>
                    <td class="title">版本号:</td>
                    <!--版本号-->
                    <td><input type="text" value="${ftcBomVersion.ftcProcBomVersionCode}" class="easyui-textbox"
                               readonly="true"></td>
                </tr>
                <tr>
                    <td class="title">是否启用:</td>
                    <!--是否启用，-1不启用，1启用-->
                    <td><input type="text" value="${ftcBomVersion.ftcProcBomVersionEnabled==-1?'不启用':'启用'}"
                               class="easyui-textbox" readonly="true"></td>
                </tr>
                <tr>
                    <td class="title">是否默认:</td>
                    <!--是否默认，-1不是默认，1默认-->
                    <td><input type="text" value="${ftcBomVersion.ftcProcBomVersionDefault==-1?'非默认':'默认'}"
                               class="easyui-textbox" readonly="true"></td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center'">
        <table id="_common_ftcBom_detail" singleSelect="false" title="非套材Bom明细" class="easyui-datagrid" url=""
               rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="FTCBOMDETAILNAME" width="15">原料名称</th>
                <th field="FTCBOMDETAILMODEL" width="15">原料规格</th>
                <th field="FTCBOMDETAILWEIGHTPERSQUAREMETRE" width="15">单位面积质量(g/m²)</th>
                <th field="FTCBOMDETAILTOTALWEIGHT" width="15">总单位面积质量(g/m²)</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
