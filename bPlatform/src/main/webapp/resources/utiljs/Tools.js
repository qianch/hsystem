var Tools = Tools = Tools || {};

/**
 * 去除字符串两端空格
 * @param {Object} str
 */
Tools.trim=function(str){
	return str.replace(/(^\s*)|(\s*$)/g,"");
}

/**
 * 字符串替换
 * @param {Object} src 源字符串
 * @param {Object} str1 替换前的字符串
 * @param {Object} str2  替换后的字符串
 */
Tools.replaceAll=function(src,str1,str2){
	return src.replace(new RegExp(str1,"gm"),str2);
}

/**
 * 长度限制
 * @param {string} str
 */
Tools.length=function(str){
	 return str.replace(/[^\x00-\xff]/g,"**").length;
}
