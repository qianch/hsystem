<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<script type="text/javascript">
  	var path="<%=path%>";
  	var wspath = '<%=request.getServerName()%>:<%=request.getServerPort()%><%=path%>';
  		var websocket;
		if ('WebSocket' in window) {
			websocket = new WebSocket("ws://"+wspath+"/ws");
		} else if ('MozWebSocket' in window) {
			websocket = new MozWebSocket("ws://"+wspath+"/ws");
		} else {
			websocket = new SockJS("http://"+wspath+"/ws/sockjs");
		}
		websocket.onopen = function(event) {
			/* console.log("websocket is opened");
			console.log(event); */
		};
		websocket.onmessage = function(event) {
			/* console.log("received a message"); */
			var msg=JSON.parse(event.data);
			top.layer.msg('fdsafdasfdsa',{shift:4,title:'新消息',offset:'rb',time:10000,closeBtn:1,skin:'layui-layer-molv'});
		};
		websocket.onerror = function(event) {
			/* console.log("websocket error");
			console.log(event); */
		};
		websocket.onclose = function(event) {
			/* console.log("websocket is closed");
			console.log(event); */
			top.layer.msg('和服务器的消息通道已经断开，请刷新重试',{shift:4,title:'提醒',offset:'rb',time:10000,closeBtn:1,skin:'layui-layer-molv'});
		}
  </script>