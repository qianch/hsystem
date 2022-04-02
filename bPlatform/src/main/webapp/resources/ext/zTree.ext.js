var ZTree=ZTree=ZTree||{};

/**
 * 初始化Tree
 * 
 * @param id
 *            树的ID
 * @param settings
 *            设置
 * @param data
 *            数据
 * @param expand
 *            是否展开树
 */
ZTree.init=function(treeId,setting,data,expand){
	var z=$.fn.zTree.init($("#"+treeId), setting, data);
	$.fn.zTree.getZTreeObj(treeId).expandAll(expand==true?true:false);
	return z;
};

/**
 * 更新ztree的节点信息
 * 
 * @param treeId
 *            树的ID
 * @param nodeAttr
 *            要更新节点的KEY，用来查找的
 * @param nodeValue
 *            要更新节点的原来的值，用来查找的
 * @param updateKeyArray
 *            要更新的节点的属性，数组
 * @param updateValueArray
 *            要更新的节点的值，数组
 */
ZTree.updateNode=function(treeId,nodeAttr,nodeValue,updateKeyArray,updateValueArray){
	if(treeId==undefined||nodeAttr==undefined||nodeValue==undefined||updateKeyArray==undefined||updateValueArray==undefined){
		return;
	}
	if(!updateKeyArray instanceof Array){
		return;
	}
	if(!updateValueArray instanceof Array){
		return;
	}
	if(updateKeyArray.length!=updateKeyArray.length){
		return;
	}
	var node = $.fn.zTree.getZTreeObj(treeId).getNodeByParam(nodeAttr, nodeValue, null);
	for(var i=0;i<updateKeyArray.length;i++){
		node[updateKeyArray[i]]=updateValueArray[i];
	}
	$.fn.zTree.getZTreeObj(treeId).updateNode(node);
};

/**
 * 向ZTree添加节点
 * 
 * @param treeId
 *            树的ID
 * @param parentId
 *            父节点ID，如果是更节点则null
 * @param data
 *            节点数据
 */
ZTree.addNode=function(treeId,parentId,data){
	var idKey=$.fn.zTree.getZTreeObj(treeId).setting.data.simpleData.idKey;
	$.fn.zTree.getZTreeObj(treeId).addNodes($.fn.zTree.getZTreeObj(treeId).getNodeByParam(idKey, parentId, null), data);
};
/**
 * 删除ZTree的节点
 */
ZTree.removeNode=function(treeId,nodeId){
	var idKey=$.fn.zTree.getZTreeObj(treeId).setting.data.simpleData.idKey;
	$.fn.zTree.getZTreeObj(treeId).removeNode($.fn.zTree.getZTreeObj(treeId).getNodeByParam(idKey, nodeId, null), false);
}
/**
 * 根据节点ID查找节点
 */
ZTree.getNode=function(treeId,nodeId){
	var idKey=$.fn.zTree.getZTreeObj(treeId).setting.data.simpleData.idKey;
	return $.fn.zTree.getZTreeObj(treeId).getNodeByParam(idKey, nodeId, null)
}
/**
 * 展开所有节点
 */
ZTree.expand=function(treeId,action){
	$.fn.zTree.getZTreeObj(treeId).expandAll(action);
}
/**
 * 获取选中的节点
 */
ZTree.getSelectedNode=function(treeId){
	return $.fn.zTree.getZTreeObj(treeId).getSelectedNodes();
}