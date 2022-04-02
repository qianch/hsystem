var Utils = Utils = Utils || {};

/**
 * 判断字符串是否为空
 * 
 * @param str
 * @returns {Boolean}
 */
Utils.isEmpty = function(str) {
	if (str == null || str == "" || str == undefined)
		return true;
	return false;
};
/**
 * 判断对象是否为空，undefined也是为空
 */
Utils.isNull=function(obj){
	if(obj==undefined||obj==null)return true;
	return false;
}

/**
 * 返回一个指定范围的随机数
 */
Utils.randomInt=function(min,max){
	//因为floor是向下取整，所以max+1才能取到Max的最大值
	return Math.floor(min+Math.random()*((max+1)-min));
}