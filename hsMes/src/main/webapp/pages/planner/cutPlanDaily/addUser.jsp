<!--
作者:宋黎明
日期:2016-12-13 13:38:47
页面:编织计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
    textarea {
        border-radius: 5px;
        height: 120px;
        resize: none;
        padding: 2px;
    }
</style>
<script type="text/javascript">
    function formatterA(value, row) {
        return '<div><a href="#"  onclick="detailed(' + row.ID + ')" >删除</a></div>'
    }

    //结束datagrid行编辑
    function endEditing() {
        if (editIndex === undefined) {
            return true;
        }
        if ($('#dg').datagrid('validateRow', editIndex)) {
            $('#dg').datagrid('endEdit', editIndex);
            editIndex = undefined;
            addindex = 0;
            return true;
        } else {
            return false;
        }
    }

    //结束编辑
    function onEndEdit(index, row) {
        addindex = 0;
    }

    //datagrid双击编辑事件
    function onDblClickCell(index, field) {
        if (editIndex !== index) {
            if (endEditing()) {
                $('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
                const ed = $('#dg').datagrid('getEditor', {
                    index: index,
                    field: field
                });
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editIndex = index;
            } else {
                setTimeout(function () {
                    $('#dg').datagrid('selectRow', editIndex);
                }, 0);
            }
        }
    }

    //datagrid单击结束编辑
    function onClickCell(index, field) {
        endEditing();
    }

    const userDgData = ${partNames};
</script>
<div style="height:100%;">
    <!--裁剪计划指定人员表单-->
    <table width="100%">
        <tr>
            <td class="title">总套数:</td>
            <td><input type="text" id="productTrayCount" name="totalTrayCount" value="2" precision="0" min="1"
                       class="easyui-numberbox" required="true"></td>
        </tr>
        <tr>
            <td class="title">备注:</td>
            <td colspan="3"><textarea id="_comment" name="_comment" style="width:99%;border:none;"
                                      placeholder="最多输入1000字"></textarea></td>
        </tr>
    </table>
    <div style="height: 60%">
        <table id="userDg" idField="" class="easyui-datagrid" fit="true" singleSelect="true" fitColumns="true"
               title="指定生产人员(双击行编辑)" style="width:100%;" data-options="onDblClickRow:userRowClick">
            <thead>
            <tr>
                <th field="PARTID" checkbox="true"></th>
                <th data-options="field:'TCPROCBOMVERSIONPARTSNAME'" width="100">部件名称</th>
                <th data-options="field:'USERCOUNT',width:150,required:true" formatter="cutDailyUsersCounts"
                    width="100">人员名称及数量
                </th>
            </tr>
            </thead>
        </table>
    </div>
</div>

