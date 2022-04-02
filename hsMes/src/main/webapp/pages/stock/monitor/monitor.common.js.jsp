<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script >


var kuweis=[];

$(document).ready(function(){
	$("td[kuwei]").each(function(index,dom){
		kuweis.push($(dom).attr("kuwei"));
	});
		var type=$("body").attr("type");
		JQ.ajaxPost("<%=basePath%>stock/monitor/"+type+"/sum", {kuweis:kuweis.join(",")}, function(data){
			if(type=="yl"){
				for(var k in data){
					$("td[kuwei="+k+"]").html("<font color='red'>总重:"+data[k]+"KG</font>");
				}
			}else{
				for(var k in data){
					var info=data[k].toString().split(";");
					$("td[kuwei="+k+"]").html("<font color='red'>总托数:"+(isEmpty(info[0])?0:info[0])+"&nbsp;总卷数"+(isEmpty(info[1])?0:info[1])+"</font>");
				}
			}
		});
	
});



</script>
