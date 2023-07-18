<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bom版本信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/meta.jsp" %>
<style type="text/css">
    .mui-input-row .mui-input-clear ~ .mui-icon-clear, .mui-input-row .mui-input-speech
    ~ .mui-icon-speech, .mui-input-row .mui-input-password ~ .mui-icon-eye {
        top: 2px;
    }

    .mui-input-group .mui-input-row {
        height: 25px;
    }

    .mui-input-row label ~ input, .mui-input-row label ~ select, .mui-input-row label
    ~ textarea {
        height: 25px;
    }

    .mui-input-row .mui-input-clear ~ .mui-icon-clear, .mui-input-row .mui-input-speech
    ~ .mui-icon-speech, .mui-input-row .mui-input-password ~ .mui-icon-eye {
        top: 2px;
    }

    .mui-input-row label {
        font-family: 'Helvetica Neue', Helvetica, sans-serif;
        padding: 0px 10px 0px 15px;
        line-height: 25px;
    }

</style>
<script type="text/javascript">
    //JS代码
    var details = ${details};

    $(document).ready(function () {
        //$('.easyui-textbox').textbox('textbox').attr('readonly',true);
        for (var a = 0; a < details.length; a++) {
            add_bomDetail_data(details[a]);
        }
    });

    function add_bomDetail_data(r) {
        var _row = {
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
        $("#dg_bom_detail").datagrid("appendRow", _row);
    }

    function formatValue(value, row, index) {
        if (value == null) {
            return "";
        } else {
            //return "<div class='easyui-tooltip' title='"+value+"'>"+value+"</div>";
            return '<div class="easyui-panel easyui-tooltip" title="' + value + '" style="width:100px;padding:5px">' + value + '</div>';
        }
    }
</script>
<!--包材bom版本信息表单-->
<table>
    <tr>
        <td class="title">版本号:</td>
        <!--版本号-->
        <td><input type="text" id="packVersion" name="packVersion"
                   value="${bCBomVersion.packVersion}" class="easyui-textbox"
                   readonly="true"></td>
    </tr>
    <%-- <tr>
        <td class="title">是否启用:</td>
        <!--是否启用，-1不启用，1启用-->
        <td><input type="text" id="packEnabled" name="packEnabled"
            value="${bCBomVersion.packEnabled==-1?'不启用':'启用'}" class="easyui-textbox"
            readonly="true"></td>
    </tr>
    <tr>
        <td class="title">是否默认:</td>
        <!--是否默认，-1不是默认，1默认-->
        <td><input type="text" id="packIsDefault" name="packIsDefault"
            value="${bCBomVersion.packIsDefault==-1?'非默认':'默认'}" class="easyui-textbox"
            readonly="true"></td>
    </tr> --%>
</table>
<table title="包材bom信息">
    <tr>
        <td class="title" style="text-align: left;">总称:</td>
        <td id="PACKBOMGENERICNAME">${bcBom.packBomGenericName}</td>
        <td style="text-align: left;" class="title">门幅:</td>
        <td id="PACKBOMWIDTH">${bcBom.packBomWidth}</td>
        <td style="text-align: left;" class="title">包装大类:</td>
        <td id="PACKBOMTYPE">${bcBom.packBomType}</td>
        <td style="text-align: left;" class="title">包装标准代码:</td>
        <td id="PACKBOMCODE">${bcBom.packBomCode}</td>
    </tr>
    <tr>
        <td style="text-align: left;" class="title">产品规格:</td>
        <td id="PACKBOMMODEL">${bcBom.packBomModel}</td>
        <td style="text-align: left;" class="title">卷重:</td>
        <td id="PACKBOMWEIGHT">${bcBom.packBomWeight}</td>
        <td style="text-align: left;" class="title">卷长:</td>
        <td id="PACKBOMLENGTH">${bcBom.packBomLength}</td>
        <td style="text-align: left;" class="title">卷径:</td>
        <td id="PACKBOMRADIUS">${bcBom.packBomRadius}</td>
    </tr>
    <tr>
        <td style="text-align: left;" class="title">适用客户:</td>
        <td id="PACKBOMCONSUMERID">${consumer.consumerName}</td>
        <td style="text-align: left;" class="title">每箱卷数:</td>
        <td id="PACKBOMROLLSPERBOX">${bcBom.packBomRollsPerBox}</td>
        <td style="text-align: left;" class="title">每托箱数:</td>
        <td id="PACKBOMBOXESPERTRAY">${bcBom.packBomBoxesPerTray}</td>
        <td style="text-align: left;" class="title">每托卷数:</td>
        <td id="PACKBOMROLLSPERTRAY">${bcBom.packBomRollsPerTray}</td>
    </tr>
    <tr>
        <td style="text-align: left;" class="title">包装名称:</td>
        <td id="PACKBOMGENERICNAME">${bcBom.packBomGenericName}</td>
    </tr>
</table>
<table id="dg_bom_detail" singleSelect="false" title="包材bom明细"
       class="easyui-datagrid" url=""
       rownumbers="true" fitColumns="true" fit="true" pagination="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="PACKMATERIALNAME" width="15">包材名称</th>
        <th field="PACKMATERIALMODEL" width="15">规格</th>
        <th field="PACKMATERIALATTR" width="15">材质</th>
        <th field="PACKMATERIALCOUNT" width="15">数量</th>
        <th field="PACKMATERIALUNIT" width="15">物料单位</th>
        <th field="PACKUNIT" width="15">包装单位</th>
        <th field="PACKREQUIRE" width="15" formatter="formatValue">包装要求</th>
        <th field="PACKMEMO" width="15" formatter="formatValue">备注</th>
    </tr>
    </thead>
</table>
