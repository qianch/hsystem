<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<div>
    <form id="noticeForm" style="height:100%;">
        <input type="hidden" name="id" value="${notice.id}"/>
        <input type="hidden" name="userId" value="${userId}"/>
        <input type="hidden" id="contentTxt" name="contentTxt" value="${notice.contentTxt }"/>
        <table style="width:100%;height:95%;">
            <!-- 标题 -->
            <tr style="height:35px;">
                <td class="title">标题</td>
                <td><input class="easyui-textbox" name="title" value="${notice.title}" required="true"></td>
            </tr>
            <!-- 发布者姓名 -->
            <tr style="height:35px;">
                <td class="title">发布者</td>
                <td><input class="easyui-textbox" value=" ${user.userName}" disabled="true"></td>
            </tr>
            <tr>
                <td colspan="2" style="vertical-align: top;text-align: center;"><textarea id="content" name="content"
                                                                                          style="width:99%;">${notice.content}</textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<script type='text/javascript'>
    const ue = UE.getEditor('content');
</script>

