<!--
作者:徐波
日期:2016-10-24 15:08:19
页面:原料库存表增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--原料库存表表单-->
    <form id="materialStockForm" method="post" ajax="true" action="<%=basePath %>stock/materialStock/add"
          autocomplete="off">
        <table width="100%">
            <tr>
                <td class="title">原料判级</td>
                <!--原料判级-->
                <td>
                    <input type="text" id="state" name="state" value="" class="easyui-combobox" required="true"
                           data-options="data: [
	                        {value:'0',text:'待检'},
	                        {value:'1',text:'合格'},
	                        {value:'2',text:'不合格'}
                    	] ,icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]">
                </td>
            </tr>
        </table>
    </form>
</div>