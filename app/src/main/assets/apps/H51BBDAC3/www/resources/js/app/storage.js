var LS=LS={};

LS={
	/**
	 * 设置值
	 * @param {Object} key
	 * @param {Object} value
	 */
	set:function(key,value){
		localStorage.setItem(key,value);
	},
	/**
	 * 通过Key取值
	 * @param {Object} key
	 */
	get:function(key){
		if(localStorage.getItem(key)=="true")return true;
		if(localStorage.getItem(key)=="false")return false;
		return localStorage.getItem(key);
	},
	/**
	 * 通过Key移除值
	 * @param {Object} key
	 */
	remove:function(key){
		localStorage.removeItem(key);
	},
	/**
	 * 清空所有值
	 */
	clear:function(){
		localStorage.clear();
	},
	/**
	 * 获取所有的数据
	 */
	getAll:function(){
		var st = window.localStorage;
		var ret=[];
		for(var i=0;i<st.length;i++){
			var k,v,p={};
			k=st.key(i);
			v=st.getItem(st.key(i));
			p[k]=v;
			ret.push(p);
		}
		return ret;
	},
	/**
	 * 获取所有的Key，数组
	 */
	keys:function(){
		var st = window.localStorage;
		var ret=[];
		for(var i=0;i<st.length;i++){
			ret.push(st.key(i));
		}
		return ret;
	},
	/**
	 * 获取所有的Value，数组
	 */
	values:function(){
		var st = window.localStorage;
		var ret=[];
		for(var i=0;i<st.length;i++){
			ret.push(st.getItem(st.key(i)));
		}
		return ret;
	},
	/**
	 * LocalStorage的长度
	 */
	length:function(){
		return window.localStorage.length;
	}
}
