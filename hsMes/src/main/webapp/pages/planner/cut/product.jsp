<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<title>选择产品信息</title>
<%@ include file="../../base/jstl.jsp" %>
<script type="text/javascript">
    //添加成品信息
    const _common_product_addUrl = path + "finishProduct/add";
    //选择客户信息
    const chooseConsumer = path + "selector/consumer";
    //选择套材/非套材BOM信息
    const chooseBomUrl = path + "finishProduct/chooseBom";
    //选择包装BOM信息
    const choosePackingBomUrl = path + "selector/packingBom";
    let consumerWindow = null;
    let bomWindow = null;
    let packingBomWindow = null;
    let productIsTc = null;
    const isLoadUrl = true;

    //搜索按钮
    function _common_product_search() {
        _common_product_filter();
    }

    //查询
    function _common_product_filter() {
        EasyUI.grid.search("_common_product_dg", "_common_product_dg_form");
    }

    $(function () {
        const url = "<%=basePath %>finishProduct/list?filter[productIsTc]=-1&filter[auditState]=2&filter[consumerId]=${finalConsumerId}";
        $('#_common_product_dg').datagrid({
            url: encodeURI(url),
            onBeforeLoad: _commons_product_dg_onBeforeLoad,
            onDblClickRow: function (index, row) {
                if (typeof _common_product_dbClickRow === "function") {
                    _common_product_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为产品选择界面提供_common_product_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                if (typeof _common_product_onLoadSuccess === "function") {
                    _common_product_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义产品选择界面加载完成的方法：_common_product_onLoadSuccess(data)");
                    }
                }
            }
        });
    });

    //添加成品信息
    const _common_product_add = function () {
        const wid = Dialog.open("添加", 800, 295, _common_product_addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("finishProductForm", _common_product_addUrl, function (data) {
                        _common_product_filter();
                        if (Dialog.isMore(wid)) {
                            Dialog.close(wid);
                            add();
                        } else {
                            Dialog.close(wid);
                        }
                    });
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid);
                })], function () {
                Dialog.more(wid);
            }
        );
    };

    //选择客户
    function ChooseConsumer() {
        consumerWindow = Dialog.open("选择客户", 850, 450, chooseConsumer, [
            EasyUI.window.button("icon-save", "确认", function () {
                var _common_product_r = EasyUI.grid.getOnlyOneSelected("_common_consumer_dg");
                $('#productConsumerCode').searchbox('setValue', _common_product_r.CONSUMERCODE);
                $('#productConsumerName').searchbox('setValue', _common_product_r.CONSUMERNAME);
                JQ.setValue('#productConsumerId', _common_product_r.ID);
                Dialog.close(consumerWindow);
            }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
                Dialog.close(consumerWindow);
            })]);
    }

    //选择客户双击事件
    function _common_consumer_dbClickRow(index, row) {
        $('#productConsumerCode').searchbox('setValue', row.CONSUMERCODE);
        $('#productConsumerName').searchbox('setValue', row.CONSUMERNAME);
        JQ.setValue('#productConsumerId', row.ID);
        Dialog.close(consumerWindow);
    }

    //选择套材/非套材工艺BOM窗口
    function chooseBom() {
        const productIsTc1 = $("#productIsTc1").attr("checked");
        const productIsTc2 = $("#productIsTc2").attr("checked");
        if (productIsTc1 === 'checked') {
            productIsTc = 1;
        } else {
            productIsTc = 2;
        }
        bomWindow = Dialog.open("", 400, 450, chooseBomUrl + "?productIsTc=" + productIsTc, [
            EasyUI.window.button("icon-save", "确认", function () {
                const tcNode = $('#tcBomTree').tree('getSelected');
                const ftcNode = $('#ftcBomTree').tree('getSelected');
                if (tcNode == null) {
                    //非套材
                    const parentFtcInfo = $('#ftcBomTree').tree('getParent', ftcNode.target);
                    if (parentFtcInfo == null) {
                        Tip.warn("请选择对应的版本信息");
                        return;
                    }
                    $('#productProcessBom').searchbox('setValue', parentFtcInfo.attributes.FTCPROCBOMCODE);
                    $('#productProcessBomVersion').textbox('setValue', ftcNode.text);
                } else {
                    //套材
                    const parentTcInfo = $('#tcBomTree').tree('getParent', tcNode.target);
                    if (parentTcInfo == null) {
                        Tip.warn("请选择对应的版本信息");
                        return;
                    }
                    $('#productProcessBom').searchbox('setValue', parentTcInfo.attributes.TCPROCBOMCODE);
                    $('#productProcessBomVersion').textbox('setValue', tcNode.text);
                }
                Dialog.close(bomWindow);
            }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
                Dialog.close(bomWindow);
            })]);
    }

    //选择包装工艺BOM窗口
    function choosePackingBom() {
        packingBomWindow = Dialog.open("", 400, 450, choosePackingBomUrl, [
            EasyUI.window.button("icon-save", "确认", function () {
                const node = $('#bcBomTree').tree('getSelected');
                const parentInfo = $('#bcBomTree').tree('getParent', node.target);
                if (parentInfo == null) {
                    Tip.warn("请选择对应的版本信息");
                    return;
                }
                $('#productPackage').searchbox('setValue', parentInfo.attributes.PACKBOMCODE);
                $('#productPackageVersion').textbox('setValue', node.text);
                Dialog.close(packingBomWindow);
            }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
                Dialog.close(packingBomWindow);
            })]);
    }

    //双击选中套材BOM tree
    function dblClickTcVersion(node) {
        const parentInfo = $('#tcBomTree').tree('getParent', node.target);
        $('#productProcessBom').searchbox('setValue', parentInfo.attributes.TCPROCBOMCODE);
        $('#productProcessBomVersion').textbox('setValue', node.text);
        Dialog.close(bomWindow);
    }

    //双击选中非套材BOM tree
    function dblClickFtcVersion(node) {
        const parentInfo = $('#ftcBomTree').tree('getParent', node.target);
        $('#productProcessBom').searchbox('setValue', parentInfo.attributes.FTCPROCBOMCODE);
        $('#productProcessBomVersion').textbox('setValue', node.text);
        Dialog.close(bomWindow);
    }

    //双击选中包装BOM tree
    function dblClickBcVersion(node) {
        const parentInfo = $('#bcBomTree').tree('getParent', node.target);
        $('#productPackage').searchbox('setValue', parentInfo.attributes.PACKBOMCODE);
        $('#productPackageVersion').textbox('setValue', node.text);
        Dialog.close(packingBomWindow);
    }


    function _commons_product_dg_onBeforeLoad() {
        if (typeof _commons_product_dg_onBeforeLoad_callback == "function") {
            return _commons_product_dg_onBeforeLoad_callback();
        } else {
            if (window.console) {
                console.log("未定义产品选择界面加载完成的方法：_commons_product_dg_onBeforeLoad_callback()");
            }
            return true;
        }
    }

    function _commons_product_dg_onBeforeLoad_callback() {
        const firstLoad = $("#_common_product_dg").attr("firstLoad");
        if (firstLoad === "false" || typeof (firstLoad) == "undefined") {
            $("#_common_product_dg").attr("firstLoad", "true");
            return false;
        }
        return true;
    }
</script>
<div id="_common_product_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_product_dg_form" autoSearchFunction="false">
            <%-- <!-- 标识是否需要显示裁剪添加的胚布 --> --%>
            <!-- 	客户代码：<input type="text" name="filter[consumerCode]" like="true" class="easyui-textbox">
                客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox"> -->
            厂内名称：<input type="text" name="filter[factoryName]" like="true" class="easyui-textbox">
            产品型号：<input type="text" id="productModel" name="filter[productModel]" like="true"
                            class="easyui-textbox">
            显示作废：<input type="text" name="filter[old]" class="easyui-combobox"
                            data-options="data: [{value:'1',text:'已作废'}]">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add"
               onclick="copyProduct()">复制</a><br>
            门　　幅：<input type="text" id="productWidth" name="filter[productWidth]" like="true" class="easyui-textbox">
            工艺代码：<input type="text" id="procCode" name="filter[productProcessCode]" like="true"
                            class="easyui-textbox">
            工艺版本：<input type="text" id="procVersion" name="filter[productProcessBomVersion]" like="true"
                            class="easyui-textbox">
            <a id="_common_product_searchButton" href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small"
               iconcls="icon-search" onclick="_common_product_filter()">
                搜索
            </a>
        </form>
    </div>
</div>
<table id="_common_product_dg" width="100%" height="98%" idField="ID"
       data-options="pageSize:100,nowrap:false,rowStyler:productStyler" singleSelect="true" class="easyui-datagrid"
       toolbar="#_common_product_toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true">
    <thead frozen="true">
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="CONSUMERCODE" width="60" sortable="true" ${empty showCode?'':'hidden'}>客户代码</th>
        <th field="CONSUMERNAME" width="200" sortable="true">客户名称</th>
        <th field="FACTORYPRODUCTNAME" width="140" sortable="true">厂内名称</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th field="CONSUMERPRODUCTNAME" width="150" sortable="true">客户产品名称</th>
        <th field="PRODUCTMODEL" width="130" sortable="true">产品型号</th>
        <th field="PRODUCTWIDTH" width="70" sortable="true">门幅(mm)</th>
        <th field="PRODUCTROLLLENGTH" width="70" sortable="true">定长(m)</th>
        <th field="PRODUCTROLLWEIGHT" width="70" sortable="true">定重(kg)</th>
        <th field="PRODUCTPROCESSCODE" sortable="true" width="200" styler="vStyler">工艺标准代码</th>
        <th field="PRODUCTPROCESSBOMVERSION" sortable="true" width="100" styler="vStyler">工艺标准版本</th>
        <th field="PRODUCTISTC" width="60" sortable="true" formatter="formatterIsTc">类型</th>
        <th field="PRODUCTMEMO" width="100">备注</th>
    </tr>
    </thead>
</table>
