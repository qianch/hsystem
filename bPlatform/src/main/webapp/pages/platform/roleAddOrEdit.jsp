<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<%@ include file="../base/meta.jsp" %>
	<script type="text/javascript">
        let valid;
        let form;
        $(document).ready(function(){
		valid=$("#role").validate({
			debug : true,
			rules : {
				name : {
					required : true,
					maxlength:32,
					minlength:2
				}
			},
			onfocusout: function (element) {
			      $(element).valid();
			}
		});
		form=$("#role");
	});
	
	
	</script>
  </head>
  <body>
      <form id="role" method="post" action="<%=basePath %>role/${action eq 'add'?'add':'edit'}" autocomplete="off">
        <input type="hidden"  name="id" value="${role.id}"  required="true"/>
            <table width="100%">
            	<tr>
            		<td class="title"><span style="color:red;">*</span>角色名称:</td>
            		<td><input  id="name" required="true" name="name" value="${role.name}"  class="easyui-textbox" data-options="validType:['noSpecialChar','length[1,30]']" ></td>
            	</tr>
            </table>
      </form>
  </body>
</html>
