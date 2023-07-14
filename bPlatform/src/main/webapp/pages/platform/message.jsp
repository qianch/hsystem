<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>我的通知</title>
    <%@ include file="../base/meta.jsp" %>
    <script type="text/javascript" src="<%=basePath%>resources/platform/message.js"></script>
    <script type="text/javascript">
        function filter() {
            EasyUI.grid.search("dg", "searchbox");
        }

        function changeUrl(value) {
            $('#dg').datagrid({
                url: value
            });
            filter();
        }

        const addUrl = path + "msg/subMessage";
        const delUrl = path + "msg/unsubMessage";
        const readUrl = path + "msg/readMessage";
        const delMesUrl = path + "msg/deleteMessage";
        let wid;

        function save() {
            const r = EasyUI.grid.getSelections("dl");
            if (r.length == 0) {
                Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                return;
            }
            const ids = [];
            for (let i = 0; i < r.length; i++) {
                ids.push(r[i].ID);
            }
            //发送请求提交选中的订阅信息
            Dialog.confirm(function () {
                JQ.ajax(addUrl, "post", {
                    messageTypes: ids.toString()
                }, function (data) {
                    Dialog.close(wid);
                    $('#messageType').combobox('reload');
                    filter();
                });
            });
        }

        function del() {
            const r = EasyUI.grid.getSelections("dl");
            if (r.length === 0) {
                Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                return;
            }
            const ids = [];
            for (let i = 0; i < r.length; i++) {
                ids.push(r[i].ID);
            }
            //发送请求提交选中的订阅信息
            Dialog.confirm(function () {
                JQ.ajax(delUrl, "post", {
                    messageTypes: ids.toString()
                }, function (data) {
                    Dialog.close(wid);
                    $('#messageType').combobox('reload');
                    filter();
                });
            });
        }

        const add = function () {
            wid = Dialog.open("添加订阅", 500, 450, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    save();
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid);
                })]);
        };

        function deleteMType() {
            wid = Dialog.open("取消订阅", 500, 450, delUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    del();
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid);
                })]);
        }

        function readMessage() {
            const r = EasyUI.grid.getSelections("dg");
            if (r.length === 0) {
                Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                return;
            }
            const ids = [];
            for (let i = 0; i < r.length; i++) {
                ids.push(r[i].ID);
            }
            JQ.ajax(readUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        }

        function deleteMyMessage() {
            const r = EasyUI.grid.getSelections("dg");
            if (r.length === 0) {
                Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
                return;
            }
            const ids = [];
            for (let i = 0; i < r.length; i++) {
                ids.push(r[i].ID);
            }
            Dialog.confirm(function () {
                JQ.ajax(delMesUrl, "post", {
                    messageIds: ids.toString()
                }, function (data) {
                    Dialog.close(wid);
                    filter();
                });
            });
        }
    </script>
</head>
<body style="margin:0;">
<div id="toolbar">
    <jsp:include page="../base/toolbar.jsp">
        <jsp:param value="add" name="ids"/>
        <jsp:param value="del" name="ids"/>
        <jsp:param value="icon-add" name="icons"/>
        <jsp:param value="icon-remove" name="icons"/>
        <jsp:param value="添加订阅" name="names"/>
        <jsp:param value="取消订阅" name="names"/>
        <jsp:param value="add()" name="funs"/>
        <jsp:param value="deleteMType()" name="funs"/>
    </jsp:include>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="readMessage()">标志已读</a> <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
                                               onclick="deleteMyMessage()">删除消息</a>
    <div>
        <form id="searchbox" style="margin: 0;">
            消息类型：
            <input type="text" id="messageType" name="filter[mType]"
                   class="easyui-combobox"
                   data-options="{valueField:'ID',textField:'VALUE',url:'<%=basePath%>msg/getUserMessageType',onLoadError:function(){Tip.error('未找到订阅的内容，请确认')},onLoadSuccess:function(){filter()},onChange:function(newValue,oldValue){filter()}}">
            已读/未读： <select id="status"
                               onchange="changeUrl(this.options[this.options.selectedIndex].value)"
                               value="<%=basePath%>msg/unread">
            <option value="<%=basePath%>msg/unread">未读</option>
            <option value="<%=basePath%>msg/readed">已读</option>
            <option value="<%=basePath%>msg/list">全部</option>
        </select>
        </form>
    </div>
</div>
<table id="dg" fit="true" url="<%=basePath%>msg/unread"
       class="easyui-datagrid" url="" toolbar="#toolbar" pagination="true"
       rownumbers="true" fitColumns="true" singleSelect="false">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="MESSAGETYPE" width="30">消息类型</th>
        <th field="CONTENT" width="30">消息内容</th>
        <th field="CREATETIME" width="30">时间</th>
    </tr>
    </thead>
</table>
</body>
</html>