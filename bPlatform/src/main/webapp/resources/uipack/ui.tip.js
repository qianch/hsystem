var Tip=Tip=Tip||{};


/**
 * 警告
 */
Tip.warn=function(msg){
	$.messager.show({
        title:tipInfo,
        msg:"<div class='messager-icon messager-warning'></div>"+msg,
        showType:'show'
    });
};

/**
 * 成功
 */
Tip.success=function(msg){
	$.messager.show({
        title:tipInfo,
        msg:"<div class='messager-icon messager-info'></div>"+msg,
        showType:'show'
    });
};

/**
 * 错误
 */
Tip.error=function(msg){
	$.messager.show({
        title:tipInfo,
        msg:"<div class='messager-icon messager-error'></div>"+msg,
        showType:'show'
    });
};

Tip.hasError=function(data){
	if(data==null)return true;
	if(data.error!=undefined){
		if(data.error=="expired"){
			Tip.warn("登录超时，请刷新页面重新登陆");
		}else{
			Tip.error(data.error);
		}
		return true;
	}
	return false;
};

Tip.dealError=function(data){
	if(data.readyState==0){
		Tip.error("服务器已关闭或者服务器地址请求不到");
		return;
	}
	var rs=JSON.parse(data.responseText);
	Tip.error(rs.error);
};