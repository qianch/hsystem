<!--
作者:孙利
日期:2018-03-06
页面:物料判级页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--物料判级-->
    <form id="totalStatisticsForm" method="post" ajax="true" autocomplete="off">
        <input type="hidden" name="ids" value="${ids}"/>
        <table width="100%">
            <tr>
                <td class="title">原料判级:</td>
                <td>
                    <input type="text" class="easyui-combobox" id="state" name="qualityGrade" required="true"
                           data-options="data: [
	                        {value:'0',text:'待检'},
	                        {value:'1',text:'合格'},
	                        {value:'2',text:'不合格'},
                    	] ,icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}],onSelect:filter">
                </td>
            </tr>
        </table>
    </form>
</div>