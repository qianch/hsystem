<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="../../base/meta.jsp" %>

<script>
    //查询版本下的部件
    var find = path + "bom/tc/findParts";
    var partsD = path + "bom/tcBomVersionPartsDetail/list";
    var parts = path + "bom/tcBomVersionParts/list";
    var partsFW = path + "bom/tcBomVersionPartsFinishedWeightEmbryoCloth/list";
    $(function () {
        var id = $("#id").val();
        $('#partsTree').tree({
            url: find + "?id=" + id,
            method: 'get',
            animate: true,
            onClick: function (node) {
                $("#partsT").datagrid({
                    url: encodeURI(parts + "?all=1&filter[idd]=" + node.id)
                });
                $("#dddg").datagrid({
                    url: encodeURI(partsD + "?all=1&filter[ids]=" + node.id)
                });
                $("#partsFinishedWeightEmbryoCloth").datagrid({
                    url: encodeURI(partsFW + "?all=1&filter[ids]=" + node.id)
                });
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',collapsible:false" title="版本信息" height="60px">
        <input type="hidden" id="id" name="" value="${tcBomVersion.id}">
        <table width="100%" title="版本列表">
            <tr>
                <td class="title">版本号:</td>
                <td>
                    <input type="text" data-options="required:true,validType:'length[1,20]','icons':{}" readonly="true"
                           editable=false id="tcProcBomVersionCode" name="tcProcBomVersionCode"
                           value="${tcBomVersion.tcProcBomVersionCode}" class="easyui-textbox">
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'west',collapsible:false" title="部件列表" width=200px>
        <ul id="partsTree" class="easyui-tree"></ul>
    </div>
    <div data-options="region:'center',collapsible:false" title="">
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'north',collapsible:false" title="部件信息" height=40%>
                <table singleSelect="false" id="partsT" style="width:100%;" class="easyui-datagrid" url=""
                       rownumbers="false" fit="true" fitColumns="false">
                    <thead>
                    <tr>
                        <th field="ID" checkbox=true></th>
                        <th field="TCPROCBOMVERSIONPARTSCUTCODE" sortable="true" width="250"
                            data-options="editor:{type:'textbox',options:{required:true}}">包装代码
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
                    </tr>
                    </thead>
                </table>
            </div>
            <div data-options="region:'south',collapsible:false" height=60%>
                <div class="easyui-tabs" fit="true">
                    <div title="部件明细">
                        <table singleSelect="false" id="dddg" style="width:100%;" class="easyui-datagrid" url=""
                               rownumbers="true" fit="true" fitColumns="false" pagination="true">
                            <thead>
                            <tr>
                                <th field="ID" checkbox=true></th>
                                <th field="PRODUCTPROCESSNAME" sortable="true" width="200">工艺名称</th>
                                <th field="PRODUCTPROCESSCODE" sortable="true" width="230">工艺代码</th>
                                <th field="PRODUCTWIDTH" sortable="true" width="100">门幅(mm)</th>
                                <th field="PRODUCTROLLLENGTH" sortable="true" width="100">米长(m)</th>
                                <th field="PRODUCTROLLWEIGHT" sortable="true" width="100">卷重(Kg)</th>
                                <th field="DRAWINGNO" sortable="true" width="200">图号</th>
                                <th field="ROLLNO" sortable="true" width="200">卷号</th>
                                <th field="LEVELNO" sortable="true" width="200">层数</th>
                                <th field="TCPROCBOMFABRICCOUNT" sortable="true">数量(卷/套)</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div title="部件成品重量胚布信息">
                        <table singleSelect="false" fit="true" id="partsFinishedWeightEmbryoCloth" remoteSort="false"
                               fitColumns="true" pagination="true" style="width:100%;" class="easyui-datagrid" url=""
                               rownumbers="true" fitColumns="true">
                            <thead>
                            <tr>
                                <th field="ID" checkbox=true></th>
                                <th field="MATERIALNUMBER" sortable="true" width=20>物料代码</th>
                                <th field="EMBRYOCLOTHNAME" sortable="true" width=20>胚布名称</th>
                                <th field="WEIGHT" sortable="true" width=20>重量(KG)</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

