(function($) {
$.extend($.fn.validatebox.defaults.rules, {
    CharAndNumber:{
    	 validator: function (value,params) {  
             var reg =/^[A-Za-z0-9]+$/;  
             return reg.test(value);
         }
    }
	,ip:{
		validator:function(value,params){
			var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g //匹配IP地址的正则表达式
			if(re.test(value))
			{
				if( RegExp.$1 <256&&RegExp.$1>0 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256&&RegExp.$4>0){
					return true;
				}
			}
			return false; 
        },  
        message : 'IP地址格式不正确'  
	},
	code:{
		validator: function (value, param) {
			// \u0391-\uFFE5 验证中文，如果不需要中文的限制，去掉即可
			if (new RegExp("[`~!@%#$^&*()=|{}':;',　\\[\\]<>/? \\.；：%……+￥（）【】‘”“'。，、？\u0391-\uFFE5]").test(value)) {  
                return false;  
            } else {  
                return true;  
            } 
        }, message: '不能含有特殊字符'  
	}
});
})(jQuery); 