    <!--
	作者:高飞
	日期:2017-7-31 17:04:13
	页面:裁剪派工单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="dlg4" class="easyui-dialog" title="条码重打" style="width:600px;height:230px;"
	data-options="
            	maximizable:true,
            	border:'none',
            	resizable:true,
            	closed:true,
                iconCls: 'icon-print',
                buttons: '#dlg-buttons4',
                modal:true
            ">
     <table style="width:100%;">
	    	<tr>
				<td class="title" style="width:150px;">小部件名称</td>
				<td style="padding:7px;">
		    		<label id='fragmentInfo'></label>
		    		<input type="hidden" id="ctoId">
		    		<input type="hidden" id="dwId">
				</td>
		  	</tr>
			  <tr>
			    <td class="title">打印机</td>
			    <td style="padding:5px;padding-left:0px;">
			    	<input type="text" id="rePrinter" class="easyui-combobox" style="width:80%!important;" data-options="icons:[],data:printers,valueField:'PRINTERNAME',textField:'PRINTERTXTNAME'">
			    </td>
			  </tr>
			  <tr>
			    <td class="title">重打数量</td>
			    <td style="padding:5px;padding-left:0px;">
			    	<input type="text" id="rePrintCount" class="easyui-numberspinner" style="width:80%!important;" data-options="icons:[],min:1,max:999">
			    </td>
			  </tr>
			  <tr>
			    <td class="title">重打原因</td>
			    <td style="padding:5px;">
			    	<textarea id="reason" style="width:80%;height:50px;padding:5px;border-radius:5px;resize:none;" maxLength="80"></textarea>
			    </td>
			  </tr>
		</table>
</div>
<div id="dlg-buttons4" style="display:none;">
	<a href="javascript:void(0)" iconCls="icon-print" class="easyui-linkbutton" onclick="doRePrint()">打印</a>
	<a href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton" onclick="javascript:$('#dlg4').dialog('close')">关闭</a>
</div>    