jQuery.extend(jQuery.fn.datagrid.defaults.editors, {
	combotree : {
		init : function(container, options) {
			var editor = jQuery('<input type="text">').appendTo(container);
			editor.combotree(options);
			return editor;
		},
		destroy : function(target) {
			jQuery(target).combotree('destroy');
		},
		getValue : function(target) {
			var temp = jQuery(target).combotree('getValues');
			// alert(temp);
			return temp.join(',');
		}
	}
});