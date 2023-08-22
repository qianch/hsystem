<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bom版本信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    let details = ${details};

    function _common_bcBomDetail_data(r) {
        const _row = {
            "ID": r.ID,
            "PACKMATERIALNAME": r.PACKMATERIALNAME,
            "PACKMATERIALMODEL": r.PACKMATERIALMODEL,
            "PACKMATERIALATTR": r.PACKMATERIALATTR,
            "PACKMATERIALCOUNT": r.PACKMATERIALCOUNT,
            "PACKMATERIALUNIT": r.PACKMATERIALUNIT,
            "PACKUNIT": r.PACKUNIT,
            "PACKREQUIRE": r.PACKREQUIRE,
            "PACKMEMO": r.PACKMEMO
        };
        $("#_common_bcBom_detail").datagrid("appendRow", _row);
    }

    let bom =${bom};
</script>
<div class="easyui-layout" style="width:100%;height:100%;">
    <div data-options="region:'north'" style="height:60px;">
        <table style="width: 99%;">
            <tr>
                <td class="title">总称</td>
                <td id="PACKBOMGENERICNAME"></td>
                <td class="title">包装大类</td>
                <td id="PACKBOMTYPE"></td>
                <td class="title">门幅</td>
                <td id="PACKBOMWIDTH"></td>
                <td class="title">卷重</td>
                <td id="PACKBOMWEIGHT"></td>
                <td class="title">每箱卷数</td>
                <td id="PACKBOMROLLSPERBOX"></td>
                <td class="title">每托箱数</td>
                <td id="PACKBOMBOXESPERTRAY"></td>
            </tr>
            <tr>
                <td class="title">包装标准代码</td>
                <td id="PACKBOMCODE"></td>
                <td class="title">适用客户</td>
                <td id="PACKBOMCONSUMERID"></td>
                <td class="title">产品规格</td>
                <td id="PACKBOMMODEL"></td>
                <td class="title">卷长</td>
                <td id="PACKBOMLENGTH"></td>
                <td class="title">卷径</td>
                <td id="PACKBOMRADIUS"></td>
                <td class="title">每托卷数</td>
                <td id="PACKBOMROLLSPERTRAY" colspan="3"></td>
            </tr>
            <tr>
                <td class="title">版本号:</td>
                <!--版本号-->
                <td>${bCBomVersion.packVersion}</td>
                <td class="title">是否启用:</td>
                <!--是否启用，-1不启用，1启用-->
                <td>${bCBomVersion.packEnabled==-1?'不启用':'启用'}</td>
                <td class="title">是否默认:</td>
                <!--是否默认，-1不是默认，1默认-->
                <td colspan="8">${bCBomVersion.packIsDefault==-1?'非默认':'默认'}</td>
            </tr>
        </table>
        <script type="text/javascript">
            for (const k in bom) {
                $("#" + k).html(bom[k]);
            }
        </script>
    </div>
    <div data-options="region:'center'">
        <table id="_common_bcBom_detail" singleSelect="false" title="包材bom明细"
               class="easyui-datagrid" url="" rownumbers="true" fitColumns="true" nowrap="false" style="height:80%"
               fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PACKMATERIALNAME" width="100">包材名称</th>
                <th field="PACKMATERIALMODEL" width="100">规格</th>
                <th field="PACKMATERIALATTR" width="80">材质</th>
                <th field="PACKMATERIALCOUNT" width="40">数量</th>
                <th field="PACKMATERIALUNIT" width="60">物料单位</th>
                <th field="PACKUNIT" width="60">包装单位</th>
                <th field="PACKREQUIRE" width="350">包装要求</th>
                <th field="PACKMEMO" width="150">备注</th>
            </tr>
            </thead>
        </table>
    </div>
</div>