/**
 * 扩展数组的contains方法
 * 用法：new Array().contains(data);
 * @param item
 * @returns {Boolean}
 */
Array.prototype.contains = function(item){
    for(i=0;i<this.length;i++){
        if(this[i]==item){return true;}
    }
    return false;
};

//获得数字数组中最小项  
Array.prototype.min = function() {  
    var oValue = 0;  
    for (var i = 0; i < this.length; i++) {  
        if (this[i] < oValue) {  
            oValue = this[i];  
        }  
    }  
    return oValue;  
};

//获得数字数组中最大项  
Array.prototype.max = function() {  
    var oValue = 0;  
    for (var i = 0; i < this.length; i++) {  
        if (this[i] > oValue) {  
            oValue = this[i];  
        }  
    }  
    return oValue;  
};

Array.prototype.toString = function(){
	var ret="";
    for(i=0;i<this.length;i++){
       if(this[i]==""||this[i]==undefined||this[i]==null)continue;
       ret=ret+(ret==""?this[i]:(","+this[i]));
    }
    return ret;
};

//清除两边的空格  
String.prototype.trim = function() {  
    return this.replace(/(^\s*)|(\s*$)/g, '');  
};

//替换所有
String.prototype.replaceAll = function(oldString, newString, ignoreCase) {  
    if (!RegExp.prototype.isPrototypeOf(oldString)) {  
        return this.replace(new RegExp(oldString, (ignoreCase ? "gi" : "g")), newString);  
    } else {  
        return this.replace(oldString, newString);  
    }  
};

//扩展Date格式化  
Date.prototype.format = function(format) {  
    var o = {  
        "M+": this.getMonth() + 1, //月份           
        "d+": this.getDate(), //日           
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时           
        "H+": this.getHours(), //小时           
        "m+": this.getMinutes(), //分           
        "s+": this.getSeconds(), //秒           
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度           
        "S": this.getMilliseconds() //毫秒           
    };  
    var week = {  
        "0": "\u65e5",  
        "1": "\u4e00",  
        "2": "\u4e8c",  
        "3": "\u4e09",  
        "4": "\u56db",  
        "5": "\u4e94",  
        "6": "\u516d"  
    };  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    }  
    if (/(E+)/.test(format)) {  
        format = format.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468") : "") + week[this.getDay() + ""]);  
    }  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
        }  
    }  
    return format;  
}

