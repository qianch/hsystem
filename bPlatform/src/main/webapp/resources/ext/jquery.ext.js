var JQ=JQ=JQ||{};
/**
 * 以JSON的方式返回表单结果
 * @param formId
 * @returns
 */
JQ.getFormAsJson=function(formId){
	var jsonString="{";
	var inputs=$("#"+formId).serializeArray();
	var likes=$("#"+formId+" input[like=true]");
	var ins=$("#"+formId+" input[in=true]");
	var _likes=new Array();
	var _ins=new Array();
	
	$.each(likes,function(index,e){
		_likes.push(Utils.isEmpty($(e).attr("textboxname"))?$(e).attr("name"):$(e).attr("textboxname"));
	});
	
	$.each(ins,function(index,e){
		_ins.push(Utils.isEmpty($(e).attr("textboxname"))?$(e).attr("name"):$(e).attr("textboxname"));
	});
	$.each(inputs,function(index,data){
		if(Utils.isEmpty(data.value)){
			return;
		}
		if(_likes.contains(data.name)){
			jsonString=jsonString+(jsonString=="{"?"":",")+"\""+data.name+"\":\"like\:"+data.value+"\"";
		}else{
			if(!_ins.contains(data.name)){
				jsonString=jsonString+(jsonString=="{"?"":",")+"\""+data.name+"\":\""+data.value+"\"";
			}
		}
	});
	var _inString="";
	$.each(_ins,function(index,e){
		_inString="";
		$.each(inputs,function(index2,data){
			if(e==data.name){
				if(data.value=="")return;
				_inString=_inString+(_inString==""?"":",")+data.value;
			}
		});
		if(_inString=="")return;
		jsonString=jsonString+(jsonString=="{"?"":",")+"\""+e+"\":\"in:"+_inString+"\"";
	});
	jsonString=jsonString+"}";
	jsonString=jsonString.replace(/\r\n/g,"\\r\\n");
    jsonString=jsonString.replace(/\n/g,"\\r\\n");
	return JSON.parse(jsonString);
}


/**
 * 以GET请求的方式返回表单结果
 * @param formId
 * @returns
 */
JQ.getFormAsString=function(formId){
	var formString="";
	var inputs=$("#"+formId).serializeArray();
	var likes=$("#"+formId+" input[like=true]");
	var ins=$("#"+formId+" input[in=true]");
	
	var _likes=new Array();
	var _ins=new Array();
	
	$.each(likes,function(index,e){
		_likes.push(Utils.isEmpty($(e).attr("textboxname"))?$(e).attr("name"):$(e).attr("textboxname"));
	});
	$.each(ins,function(index,e){
		_ins.push(Utils.isEmpty($(e).attr("textboxname"))?$(e).attr("name"):$(e).attr("textboxname"));
	});
	
	$.each(inputs,function(index,data){
		if(Utils.isEmpty(data.value)){
			return;
		}
		if(_likes.contains(data.name)){
			formString=formString+(formString==""?"":"&")+data.name+"=like:"+data.value;
		}else{
			if(!_ins.contains(data.name)){
				formString=formString+(formString==""?"":"&")+data.name+"="+data.value;
			}
		}
	});
	var _inString="";
	$.each(_ins,function(index,e){
		_inString="";
		$.each(inputs,function(index2,data){
			if(data.value=="")return;
			if(e==data.name){
				_inString=_inString+(_inString==""?"":",")+data.value;
			}
		});
		if(_inString=="")return;
		formString=formString+(formString==""?"":"&")+e+"=in:"+_inString;
	});
	return formString;
}
/**
 * Ajax方法封装
 * @param url
 * @param getOrPost
 * @param params
 * @param successCallback
 * @param errorCallback
 */
JQ.ajax=function(url, getOrPost, params, successCallback, errorCallback) {
	Loading.show();
	$.ajax({
		url : url,
		type : getOrPost,
		dataType : 'json',
		data : params==undefined?null:params,
		success : function(data,textStatus) {
			Loading.hide();
			if(successCallback==undefined||successCallback==null)return;
			successCallback(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Loading.hide();
			if(errorCallback==undefined||errorCallback==null)return;
			var rs = JSON.parse(XMLHttpRequest.responseText);
			errorCallback(rs);
		}
	});
}
/**
 * 调用Ajax的GET方法
 * @param url
 * @param successCallback
 * @param errorCallback
 */
JQ.ajaxGet=function(url,successCallback,errorCallback){
	JQ.ajax(url,"GET",null,successCallback,errorCallback);
}
/**
 * 调用Ajax的POST方法
 * @param url
 * @param params
 * @param successCallback
 * @param errorCallback
 */
JQ.ajaxPost=function(url,params,successCallback,errorCallback){
	JQ.ajax(url,"POST",params,successCallback,errorCallback);
}

/**
 * 文本框赋值
 * @param selector
 * @param value
 */
JQ.setValue=function(selector,value){
	$(selector).val(value);
}

/**
 * 文本框赋值
 * @param selector
 * @param value
 */
JQ.getValue=function(selector){
	return $(selector).val();
}

/**
 * 设置属性
 * @param selector
 * @param attr
 * @param value
 */
JQ.setAttr=function(selector,attr,value){
	$(selector).attr(attr,value);
};
/**
 * 获取属性
 * @param selector
 * @param attr
 * @returns
 */
JQ.getAttr=function(selector,attr){
	return $(selector).attr(attr);
}

/**
 * 设置CSS样式
 * @param attr
 * @param value
 */
JQ.setCss=function(selector,attr,value){
	$(selector).css(attr,value);
}
/**
 * 获取CSS属性
 * @param selector
 * @param attr
 * @returns
 */
JQ.getCss=function(selector,attr){
	return $(selector).css(attr);
}