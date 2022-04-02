<!--
	作者:高飞
	日期:2016-10-25 13:52:50
	页面:流程实例JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<style type="text/css">
		.title{
			width:100px;
			padding-right:5px;
			height:30px;
			color:#0e2d5f;
			font-weight: bold;
		}
		
	</style>
   <div class="easyui-layout" data-options="fit:true,border:false">
        <div data-options="region:'east',title:'执行审批',split:true,iconCls:'platform-ok3',minWidth:250,maxWidth:400" style="width:250px;">
        	<table id="auditInfo" style="width:100%;">
        			<tr>
        				<td class="title">提审人</td>
        				<td>${createUser}</td>
        			</tr>
        			<tr>
        				<td class="title">提审时间</td>
        				<td>
        					<fmt:formatDate value="${audit.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
        				</td>
        			</tr>
        	</table>
        	<div id="msgBox" style="text-align: center;padding:3px;">
        		<textarea  style="width: 100%; height: 200px; resize: none; border: 1px solid #bfbfbf;" id="auditMsg" placeholder="审批意见（1000字以内）" maxLength="1000"></textarea>
        	</div>
        	<div id="buttonBox" style="text-align: center;padding:5px;">
        		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="decide(2)">通过</a>
        		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="decide(-1)">拒绝</a>
        	</div>
        </div>
        <div data-options="region:'center',title:'审核内容',iconCls:'icon-tip'">
            <iframe scrolling="auto" frameborder="0"  src="<%=basePath %>${audit.formUrl}" style="width:100%;height:99%;"></iframe>
        </div>
</div>

 <script type="text/javascript">
        
        var isLevelOne=${isLevelOne};
        var isLevelTwo=${isLevelTwo};
        
        var level=${setting.auditLevel};
        
        var isCompleted=${isCompleted};
        
		/* public final static Integer REJECT=-1;
		public final static Integer PASS=2;
		public final static Integer AUDITING=1; */
		
        var levelOneResult="${audit.firstAuditResult}";
        var levelTwoResult="${audit.secondAuditResult}";
        
        var levelOneResultText,levelTwoResultText;
        
        
        if(levelOneResult=="-1"){
        	levelOneResultText="不通过";
        }else{
        	levelOneResultText="通过";
        }
        
        if(levelTwoResult=="-1"){
        	levelTwoResultText="不通过";
        }else{
        	levelTwoResultText="通过";
        }
        
        if(isCompleted){
        	$("#msgBox").remove();
        	$("#buttonBox").remove();
        	//流程审核结束了,将一级审核的内容放到文本域中
        	
        	$("#auditInfo").append("<tr><td class='title'>一级审核人员</td><td>${levelOneUser }</td></tr>");
        	$("#auditInfo").append("<tr><td class='title'>一级审核结果</td><td>"+levelOneResultText+"</td></tr>");
        	$("#auditInfo").append("<tr><td class='title'>一级审核意见</td><td>${audit.firstAuditMsg}</td></tr>");
        	$("#auditInfo").append("<tr><td class='title'>一级审核时间</td><td><fmt:formatDate value="${audit.firstAuditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>");
        	if(level==2){
        		if(levelOneResult!="-1"){
        			$("#auditInfo").append("<tr><td class='title'>二级审核时间</td><td><fmt:formatDate value="${audit.secondAuditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>");
        			$("#auditInfo").append("<tr><td class='title'>二级审核人员</td><td>${levelTwoUser }</td></tr>");
        			$("#auditInfo").append("<tr><td class='title'>二级审核结果</td><td>"+levelTwoResultText+"</td></tr>");
            		$("#auditInfo").append("<tr><td class='title'>二级审核结果</td><td>${audit.secondAuditMsg}</td></tr>");
        		}
        	}
        	Tip.warn("审批已结束");
        }else{
        	if(isLevelTwo){
            	$("#auditInfo").append("<tr><td class='title'>一级审核人员</td><td>${levelOneUser }</td></tr>");
            	$("#auditInfo").append("<tr><td class='title'>一级审核结果</td><td>"+levelOneResultText+"</td></tr>");
            	$("#auditInfo").append("<tr><td class='title'>一级审核意见</td><td>${audit.firstAuditMsg}</td></tr>");
            	$("#auditInfo").append("<tr><td class='title'>一级审核时间</td><td><fmt:formatDate value="${audit.firstAuditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>");
        	}
        }
        
        if(!isLevelOne&&!isLevelTwo){
        	$("#msgBox").remove();
        	$("#buttonBox").remove();
        }
        
        function decide(result){
        	if(result == -1){
        		var value=$("#auditMsg").val();
	        	if(value == null || value==""){
        		Tip.warn("请填写审批意见!");
				return;
	        	}
        }
        	Dialog.confirm(function(){
        		Loading.show("执行审批中，数据较多，请耐心等候");
            	$.ajax({
            		url:path+"audit/${audit.id}/decide",
            		type:"post",
            		dataType:"json",
            		data:{"level":isLevelOne?1:2,"result":result,"msg":$("#auditMsg").val()},
            		success:function(data){
            			if(Tip.hasError(data)){
							Loading.hide();
            				Tip.dealError(data);
							// Tip.error(data);
            				return;
            			}
            			Loading.hide();
            			Dialog.close(dialogId);
            			filter();
            		}
            	});

        	},"确认操作？");
        }
</script>