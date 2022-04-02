(function($){
    //备份jquery的ajax方法
    var _ajax=$.ajax;
     
    //重写jquery的ajax方法
    $.ajax=function(opt){
        //备份opt中error和success方法
        var fn = {
            error:function(XMLHttpRequest, textStatus, errorThrown){},
            success:function(data, textStatus){}
        }
        if(opt.error){
            fn.error=opt.error;
        }
        if(opt.success){
            fn.success=opt.success;
        }
         
        //扩展错误和成功的回调
        var _opt = $.extend(opt,{
            error:function(data, textStatus, errorThrown){
                //错误方法增强处理
                if(data.readyState==0){
            		Tip.error("服务器已关闭或者服务器地址请求不到");
            		return;
            	}
            	var rs=JSON.parse(data.responseText);
            	if(rs.error=="expired"){
            		Tip.error("登陆超时，请重新登录");
            		setTimeout(function(){
        				top.location.href=path;
        			}, 1000);
            		return;
            	}
            	Tip.error(rs.error);
            	fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success:function(data, textStatus){
            	Tip.hasError(data);
                //成功回调方法增强处理
            	try {
            		fn.success(data, textStatus);
				} catch (e) {
				}
            },
            beforeSend:function(XHR){
                //提交前回调方法
            },
            complete:function(XHR, TS){
                //请求完成后回调函数 (请求成功或失败之后均调用)
            }
        });
        _ajax(_opt);
    };
})(jQuery);
