/**
 * 自动合并相同单元格
 * 用法$("#dg").datagrid("autoMergeCells",[字段数组]);
 */
$.extend($.fn.datagrid.methods, {
    	    autoMergeCells : function (jq, fields) {
    	        return jq.each(function () {
    	            var target = $(this);
    	            if (!fields) {
    	                fields = target.datagrid("getColumnFields");
    	            }
    	            var rows = target.datagrid("getRows");
    	            var i = 0,
    	            j = 0,
    	            temp = {};
    	            for (i; i < rows.length; i++) {
    	                var row = rows[i];
    	                j = 0;
    	                for (j; j < fields.length; j++) {
    	                    var field = fields[j];
    	                    var tf = temp[field];
    	                    if (!tf) {
    	                        tf = temp[field] = {};
    	                        tf[row[field]] = [i];
    	                    } else {
    	                        var tfv = tf[row[field]];
    	                        if (tfv) {
    	                            tfv.push(i);
    	                        } else {
    	                            tfv = tf[row[field]] = [i];
    	                        }
    	                    }
    	                }
    	            }
    	            $.each(temp, function (field, colunm) {
    	                $.each(colunm, function () {
    	                    var group = this;
    	                    
    	                    if (group.length > 1) {
    	                        var before,
    	                        after,
    	                        megerIndex = group[0];
    	                        for (var i = 0; i < group.length; i++) {
    	                            before = group[i];
    	                            after = group[i + 1];
    	                            if (after && (after - before) == 1) {
    	                                continue;
    	                            }
    	                            var rowspan = before - megerIndex + 1;
    	                            if (rowspan > 1) {
    	                                target.datagrid('mergeCells', {
    	                                    index : megerIndex,
    	                                    field : field,
    	                                    rowspan : rowspan
    	                                });
    	                            }
    	                            if (after && (after - before) != 1) {
    	                                megerIndex = after;
    	                            }
    	                        }
    	                    }
    	                });
    	            });
    	        });
    	    }
    	});

/**
 * The extending functionalities on datagrid columns.
 */
(function($){
	$.extend($.fn.datagrid.defaults, {
		onBeforeDragColumn: function(field){},
		onStartDragColumn: function(field){},
		onStopDragColumn: function(field){},
		onBeforeDropColumn: function(toField, fromField, point){},
		onDropColumn: function(toField, fromField, point){}	// point: 'before','after'
	});
	$.extend($.fn.treegrid.defaults, {
		onBeforeDragColumn: function(field){},
		onStartDragColumn: function(field){},
		onStopDragColumn: function(field){},
		onBeforeDropColumn: function(toField, fromField, point){},
		onDropColumn: function(toField, fromField, point){}	// point: 'before','after'
	});
	$.extend($.fn.datagrid.methods, {
		_getPluginName: function(jq){
			if (jq.data('treegrid')){
				return 'treegrid';
			} else {
				return 'datagrid';
			}
		},
		freezeColumn: function(jq, field){
			return jq.each(function(){
				var dg = $(this);
				var plugin = dg.datagrid('_getPluginName');
				var fields = dg[plugin]('getColumnFields');
				var index = $.inArray(field, fields);
				if (index >= 0){
					var col = dg[plugin]('getColumnOption', field);
					col.originalIndex = $.inArray(field, fields);
					dg[plugin]('moveColumn', {
						field: field
					});					
				}
			})
		},
		unfreezeColumn: function(jq, field){
			return jq.each(function(){
				var dg = $(this);
				var plugin = dg.datagrid('_getPluginName');
				var fields = dg[plugin]('getColumnFields', true);
				var index = $.inArray(field, fields);
				if (index >= 0){
					var col = dg[plugin]('getColumnOption', field);
					var toFields = dg[plugin]('getColumnFields', false);
					dg[plugin]('moveColumn', {
						field: field,
						before: toFields[col.originalIndex]
					});
				}
			})
		},
		/**
		 * move the column
		 * $('#dg').datagrid('moveColumn', {
		 *		field: 'itemid',
		 *		before: 'listprice'
		 *		// after: 'listprice'
		 * })
		 */
		moveColumn: function(jq, param){
			return jq.each(function(){
				var dg = $(this);
				var plugin = dg.datagrid('_getPluginName');
				var opts = dg[plugin]('options');
				var toField = param.before || param.after;
				var col = dg[plugin]('getColumnOption', param.field);
				if (col){
					var index = getIndex(param.field, true);
					if (index >= 0){
						opts.frozenColumns[0].splice(index, 1);
						if (!toField){
							opts.columns[0].push(col);
							recreate();
							return;
						}
					} else {
						index = getIndex(param.field, false);
						opts.columns[0].splice(index, 1);
						if (!toField){
							if (opts.frozenColumns[0]){
								opts.frozenColumns[0].push(col);								
							} else {
								opts.frozenColumns[0] = [col];
							}
							recreate();
							return;
						}
					}
					var toIndex = getIndex(toField, true);
					if (toIndex >= 0){
						opts.frozenColumns[0].splice(toIndex + (param.before?0:1), 0, col);
					} else {
						toIndex = getIndex(toField, false);
						opts.columns[0].splice(toIndex + (param.before?0:1), 0, col);
					}
					recreate();
				}
				function getIndex(field, frozen){
					var fields = dg[plugin]('getColumnFields', frozen);
					return $.inArray(field, fields);
				}
				function recreate(){
					var url = opts.url;
					opts.url = null;
					var data = dg[plugin]('getData');
					dg[plugin]({
						data: null
					});
					dg[plugin]('loadData', data);
					opts.url = url;
				}
			})
		}
	})
})(jQuery);

(function($){
	function initCss(){
		var css = $('#datagrid-columnmoving');
		if (!css.length){
			$('head').append(
				'<style id="datagrid-columnmoving">' +
				'.datagrid-header .datagrid-moving-left{border-left:1px solid red}' +
				'.datagrid-header .datagrid-moving-left .datagrid-cell{margin-left:-1px}' +
				'.datagrid-header .datagrid-moving-right{border-right:1px solid red;border-left:0}' +
				'</style>'
			);
		}
	}
	

	function moving(target){
		var plugin = $(target).datagrid('_getPluginName');
		var opts = $(target)[plugin]('options');
		var cells = $(target).datagrid('getPanel').find('div.datagrid-header td[field]:not(:has(.datagrid-filter-c))');
		cells.draggable({
			revert: true,
			cursor: 'pointer',
			edge: 5,
			proxy:function(source){
				var p = $('<div class="tree-node-proxy tree-dnd-no" style="position:absolute;border:1px solid #ff0000"/>').appendTo('body');
				p.html($(source).text());
				p.hide();
				return p;
			},
			onBeforeDrag: function(e){
				var field = $(this).attr('field');
				if (opts.onBeforeDragColumn.call(target, field) == false){return false;}
				if (e.which != 1){return false;}
				e.data.startLeft = $(this).offset().left;
				e.data.startTop = $(this).offset().top;
			},
			onStartDrag: function(e){
				opts.onStartDragColumn.call(target, $(this).attr('field'));
				$(this).draggable('proxy').css({
					left:-10000,
					top:-10000
				});
			},
			onStopDrag: function(){
				opts.onStopDragColumn.call(target, $(this).attr('field'));
			},
			onDrag: function(e){
				var x1=e.pageX,y1=e.pageY,x2=e.data.startX,y2=e.data.startY;
				var d = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
				if (d>3){	// when drag a little distance, show the proxy object
					$(this).draggable('proxy').show().css({
						left: e.pageX+15,
						top: e.pageY+15
					});
					this.x = e.pageX;
					this.y = e.pageY;
				}
				return false;
			}
		}).droppable({
			accept: 'td[field]:not(:has(.datagrid-filter-c))',
			onDragOver: function(e, source){
				$(source).draggable('proxy').removeClass('tree-dnd-no').addClass('tree-dnd-yes');
				var width = $(this).outerWidth();
				var x = $(this).offset().left;
				var y = $(this).offset().top;
				$(this).removeClass('datagrid-moving-left datagrid-moving-right');
				if (source.x < x + width/2){
					$(this).addClass('datagrid-moving-left');
				} else {
					$(this).addClass('datagrid-moving-right');
				}
			},
			onDragLeave: function(e, source){
				$(source).draggable('proxy').removeClass('tree-dnd-yes').addClass('tree-dnd-no');
				$(this).removeClass('datagrid-moving-left datagrid-moving-right');
			},
			onDrop: function(e, source){
				var fromField = $(source).attr('field');
				var toField = $(this).attr('field');
				var inserted = $(this).hasClass('datagrid-moving-left');
				$(this).removeClass('datagrid-moving-left datagrid-moving-right');
				
				var point = inserted ? 'before' : 'after';
				if (opts.onBeforeDropColumn.call(target, toField, fromField, point) == false){
					return;
				}
				var param = {field: fromField};
				param[point] = toField;
				$(target)[plugin]('moveColumn', param);
				$(target)[plugin]('columnMoving');
				opts.onDropColumn.call(target, toField, fromField, point);
			}
		})
	}

	$.extend($.fn.datagrid.methods, {
		columnMoving: function(jq){
			initCss();
			return jq.each(function(){
				moving(this);
			});
		}
	});

})(jQuery);
$.extend($.fn.datagrid.defaults, {
	rowHeight: 25,
	onBeforeFetch: function(page){},
	onFetch: function(page, rows){}
});

var bufferview = $.extend({}, $.fn.datagrid.defaults.view, {
	render: function(target, container, frozen){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var rows = this.rows || [];
		if (!rows.length) {
			return;
		}
		var fields = $(target).datagrid('getColumnFields', frozen);
		
		if (frozen){
			if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))){
				return;
			}
		}
		
		var index = parseInt(opts.finder.getTr(target,'','last',(frozen?1:2)).attr('datagrid-row-index'))+1 || 0;
		var table = ['<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>'];
		for(var i=0; i<rows.length; i++) {
			// get the class and style attributes for this row
			var css = opts.rowStyler ? opts.rowStyler.call(target, index, rows[i]) : '';
			var classValue = '';
			var styleValue = '';
			if (typeof css == 'string'){
				styleValue = css;
			} else if (css){
				classValue = css['class'] || '';
				styleValue = css['style'] || '';
			}
			
			var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
			var style = styleValue ? 'style="' + styleValue + '"' : '';
			var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
			table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
			table.push(this.renderRow.call(this, target, fields, frozen, index, rows[i]));
			table.push('</tr>');
			index++;
		}
		table.push('</tbody></table>');
		
		$(container).append(table.join(''));
	},
	
	onBeforeRender: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var view = this;
		this.renderedCount = 0;
		this.rows = [];
		
		dc.body1.add(dc.body2).empty();
		init();
		
		function init(){
			// erase the onLoadSuccess event, make sure it can't be triggered
			state.onLoadSuccess = opts.onLoadSuccess;
			opts.onLoadSuccess = function(){};
			setTimeout(function(){
				dc.body2.unbind('.datagrid').bind('scroll.datagrid', function(e){
					if (state.onLoadSuccess){
						opts.onLoadSuccess = state.onLoadSuccess;	// restore the onLoadSuccess event
						state.onLoadSuccess = undefined;
					}
					if (view.scrollTimer){
						clearTimeout(view.scrollTimer);
					}
					view.scrollTimer = setTimeout(function(){
						scrolling.call(view);
					}, 50);
				});
				dc.body2.triggerHandler('scroll.datagrid');
			}, 0);
		}
		
		function scrolling(){
			if (getDataHeight() < dc.body2.height() && view.renderedCount < state.data.total){
				this.getRows.call(this, target, function(rows){
					this.rows = rows;
					this.populate.call(this, target);
					dc.body2.triggerHandler('scroll.datagrid');
				});
			} else if (dc.body2.scrollTop() >= getDataHeight() - dc.body2.height()){
				this.getRows.call(this, target, function(rows){
					this.rows = rows;
					this.populate.call(this, target);
				});
			}
		}
		
		function getDataHeight(){
			// var h = 0;
			// dc.body2.children('table.datagrid-btable').each(function(){
			// 	h += $(this).outerHeight();
			// });
			// if (!h){
			// 	h = view.renderedCount * opts.rowHeight;
			// }
			// return h;
			return view.renderedCount * opts.rowHeight;
		}
	},
	
	getRows: function(target, callback){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var page = Math.floor(this.renderedCount/opts.pageSize) + 1;
		
		if (this.renderedCount >= state.data.total){return;}
		if (opts.onBeforeFetch.call(target, page) == false){return}
		
//		var rows = state.data.rows.slice(this.renderedCount, this.renderedCount+opts.pageSize);
		var index = (page-1)*opts.pageSize;
		var rows = state.data.rows.slice(index, index+opts.pageSize);
		if (rows.length){
			opts.onFetch.call(target, page, rows);
			callback.call(this, rows);
		} else {
			var param = $.extend({}, opts.queryParams, {
				page: Math.floor(this.renderedCount/opts.pageSize)+1,
				rows: opts.pageSize
			});
			if (opts.sortName){
				$.extend(param, {
					sort: opts.sortName,
					order: opts.sortOrder
				});
			}
			if (opts.onBeforeLoad.call(target, param) == false){return;}
			
			$(target).datagrid('loading');
			var result = opts.loader.call(target, param, function(data){
				$(target).datagrid('loaded');
				var data = opts.loadFilter.call(target, data);
				opts.onFetch.call(target, page, data.rows);
				if (data.rows && data.rows.length){
					state.data.rows = state.data.rows.concat(data.rows);
					callback.call(opts.view, data.rows);
				} else {
					opts.onLoadSuccess.call(target, data);
				}
			}, function(){
				$(target).datagrid('loaded');
				opts.onLoadError.apply(target, arguments);
			});
			if (result == false){
				$(target).datagrid('loaded');
				if (!state.data.rows.length){
					opts.onFetch.call(target, page, state.data.rows);
					opts.onLoadSuccess.call(target, state.data);
				}
			}
		}
	},
	
	populate: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		if (this.rows.length){
			this.renderedCount += this.rows.length;
			this.render.call(this, target, dc.body2, false);
			this.render.call(this, target, dc.body1, true);
			opts.onLoadSuccess.call(target, {
				total: state.data.total,
				rows: this.rows
			});
//			for(var i=this.renderedCount-this.rows.length; i<this.renderedCount; i++){
//				$(target).datagrid('fixRowHeight', i);
//			}
		}
	}
});
(function($){
	$.extend($.fn.datagrid.defaults, {
		clickToEdit: true,
		dblclickToEdit: false,
		navHandler: {
			'37': function(e){
				var opts = $(this).datagrid('options');
				return navHandler.call(this, e, opts.isRtl?'right':'left');
			},
			'39': function(e){
				var opts = $(this).datagrid('options');
				return navHandler.call(this, e, opts.isRtl?'left':'right');
			},
			'38': function(e){
				return navHandler.call(this, e, 'up');
			},
			'40': function(e){
				return navHandler.call(this, e, 'down');
			},
			'13': function(e){
				return enterHandler.call(this, e);
			},
			'27': function(e){
				return escHandler.call(this, e);
			},
			'8': function(e){
				return clearHandler.call(this, e);
			},
			'46': function(e){
				return clearHandler.call(this, e);
			},
			'keypress': function(e){
				if (e.metaKey || e.ctrlKey){
					return;
				}
				var dg = $(this);
				var param = dg.datagrid('cell');	// current cell information
				if (!param){return;}
				var input = dg.datagrid('input', param);
				if (!input){
					var tmp = $('<span></span>');
					tmp.html(String.fromCharCode(e.which));
					var c = tmp.text();
					tmp.remove();
					if (c){
						dg.datagrid('editCell', {
							index: param.index,
							field: param.field,
							value: c
						});
						return false;					
					}
				}
			}
		},
		onBeforeCellEdit: function(index, field){},
		onCellEdit: function(index, field, value){
			var input = $(this).datagrid('input', {index:index, field:field});
			if (input){
				if (value != undefined){
					input.val(value);
				}
			}
		},
		onSelectCell: function(index, field){},
		onUnselectCell: function(index, field){}
	});

	function navHandler(e, dir){
		var dg = $(this);
		var param = dg.datagrid('cell');
		var input = dg.datagrid('input', param);
		if (!input){
			dg.datagrid('gotoCell', dir);
			return false;
		}
	}

	function enterHandler(e){
		var dg = $(this);
		var cell = dg.datagrid('cell');
		if (!cell){return;}
		var input = dg.datagrid('input', cell);
		if (input){
			endCellEdit(this, true);
		} else {
			dg.datagrid('editCell', cell);
		}
		return false;
	}

	function escHandler(e){
		endCellEdit(this, false);
		return false;
	}

	function clearHandler(e){
		var dg = $(this);
		var param = dg.datagrid('cell');
		if (!param){return;}
		var input = dg.datagrid('input', param);
		if (!input){
			dg.datagrid('editCell', {
				index: param.index,
				field: param.field,
				value: ''
			});
			return false;
		}		
	}

	function getCurrCell(target){
		var cell = $(target).datagrid('getPanel').find('td.datagrid-row-selected');
		if (cell.length){
			return {
				index: parseInt(cell.closest('tr.datagrid-row').attr('datagrid-row-index')),
				field: cell.attr('field')
			};
		} else {
			return null;
		}
	}

	function unselectCell(target, p){
		var opts = $(target).datagrid('options');
		var cell = opts.finder.getTr(target, p.index).find('td[field="'+p.field+'"]');
		cell.removeClass('datagrid-row-selected');
		opts.onUnselectCell.call(target, p.index, p.field);
	}

	function unselectAllCells(target){
		var panel = $(target).datagrid('getPanel');
		panel.find('td.datagrid-row-selected').removeClass('datagrid-row-selected');
	}

	function selectCell(target, p){
		var opts = $(target).datagrid('options');
		if (opts.singleSelect){
			unselectAllCells(target);
		}
		var cell = opts.finder.getTr(target, p.index).find('td[field="'+p.field+'"]');
		cell.addClass('datagrid-row-selected');
		opts.onSelectCell.call(target, p.index, p.field);
	}

	function getSelectedCells(target){
		var cells = [];
		var panel = $(target).datagrid('getPanel');
		panel.find('td.datagrid-row-selected').each(function(){
			var td = $(this);
			cells.push({
				index: parseInt(td.closest('tr.datagrid-row').attr('datagrid-row-index')),
				field: td.attr('field')
			});
		});
		return cells;
	}

	function gotoCell(target, p){
		var dg = $(target);
		var opts = dg.datagrid('options');
		var panel = dg.datagrid('getPanel').focus();

		var cparam = dg.datagrid('cell');
		if (cparam){
			var input = dg.datagrid('input', cparam);
			if (input){
				input.focus();
				return;
			}
		}

		if (typeof p == 'object'){
			_go(p);
			return;
		}
		var cell = panel.find('td.datagrid-row-selected');
		if (!cell){return;}
		var fields = dg.datagrid('getColumnFields',true).concat(dg.datagrid('getColumnFields'));
		var field = cell.attr('field');
		var tr = cell.closest('tr.datagrid-row');
		var rowIndex = parseInt(tr.attr('datagrid-row-index'));
		var colIndex = $.inArray(field, fields);

		if (p == 'up' && rowIndex > 0){
			rowIndex--;
		} else if (p == 'down'){
			if (opts.finder.getRow(target, rowIndex+1)){
				rowIndex++;
			}
		} else if (p == 'left'){
			var i = colIndex - 1;
			while(i >= 0){
				var col = dg.datagrid('getColumnOption', fields[i]);
				if (!col.hidden){
					colIndex = i;
					break;
				}
				i--;
			}
		} else if (p == 'right'){
			var i = colIndex + 1;
			while(i <= fields.length-1){
				var col = dg.datagrid('getColumnOption', fields[i]);
				if (!col.hidden){
					colIndex = i;
					break;
				}
				i++;
			}
		}

		field = fields[colIndex];

		_go({index:rowIndex, field:field});

		function _go(p){
			dg.datagrid('scrollTo', p.index);
			unselectAllCells(target);
			selectCell(target, p);
			var td = opts.finder.getTr(target, p.index, 'body', 2).find('td[field="'+p.field+'"]');
			if (td.length){
				var body2 = dg.data('datagrid').dc.body2;
				var left = td.position().left;
				if (left < 0){
					body2._scrollLeft(body2._scrollLeft() + left*(opts.isRtl?-1:1));
				} else if (left+td._outerWidth()>body2.width()){
					body2._scrollLeft(body2._scrollLeft() + (left+td._outerWidth()-body2.width())*(opts.isRtl?-1:1));
				}
			}
		}
	}

	// end the current cell editing
	function endCellEdit(target, accepted){
		var dg = $(target);
		var cell = dg.datagrid('cell');
		if (cell){
			var input = dg.datagrid('input', cell);
			if (input){
				if (accepted){
					if (dg.datagrid('validateRow', cell.index)){
						dg.datagrid('endEdit', cell.index);
						dg.datagrid('gotoCell', cell);
					} else {
						dg.datagrid('gotoCell', cell);
						input.focus();
						return false;
					}
				} else {
					dg.datagrid('cancelEdit', cell.index);
					dg.datagrid('gotoCell', cell);
				}
			}
		}
		return true;
	}

	function editCell(target, param){
		var dg = $(target);
		var opts = dg.datagrid('options');
		var input = dg.datagrid('input', param);
		if (input){
			dg.datagrid('gotoCell', param);
			input.focus();
			return;
		}
		if (!endCellEdit(target, true)){return;}
		if (opts.onBeforeCellEdit.call(target, param.index, param.field) == false){
			return;
		}

		var fields = dg.datagrid('getColumnFields',true).concat(dg.datagrid('getColumnFields'));
		$.map(fields, function(field){
			var col = dg.datagrid('getColumnOption', field);
			col.editor1 = col.editor;
			if (field != param.field){
				col.editor = null;
			}
		});

		var col = dg.datagrid('getColumnOption', param.field);
		if (col.editor){
			dg.datagrid('beginEdit', param.index);
			var input = dg.datagrid('input', param);
			if (input){
				dg.datagrid('gotoCell', param);
				setTimeout(function(){
					input.unbind('.cellediting').bind('keydown.cellediting', function(e){
						if (e.keyCode == 13){
							opts.navHandler['13'].call(target, e);
							return false;
						}
					});
					input.focus();
				}, 0);
				opts.onCellEdit.call(target, param.index, param.field, param.value);
			} else {
				dg.datagrid('cancelEdit', param.index);
				dg.datagrid('gotoCell', param);
			}
		} else {
			dg.datagrid('gotoCell', param);
		}

		$.map(fields, function(field){
			var col = dg.datagrid('getColumnOption', field);
			col.editor = col.editor1;
		});
	}

	function enableCellSelecting(target){
		var dg = $(target);
		var state = dg.data('datagrid');
		var panel = dg.datagrid('getPanel');
		var opts = state.options;
		var dc = state.dc;
		panel.attr('tabindex',1).css('outline-style','none').unbind('.cellediting').bind('keydown.cellediting', function(e){
			var h = opts.navHandler[e.keyCode];
			if (h){
				return h.call(target, e);
			}
		});
		dc.body1.add(dc.body2).unbind('.cellediting').bind('click.cellediting', function(e){
			var tr = $(e.target).closest('.datagrid-row');
			if (tr.length && tr.parent().length){
				var td = $(e.target).closest('td[field]', tr);
				if (td.length){
					var index = parseInt(tr.attr('datagrid-row-index'));
					var field = td.attr('field');
					var p = {
						index: index,
						field: field
					};
					if (opts.singleSelect){
						selectCell(target, p);
					} else {
						if (opts.ctrlSelect){
							if (e.ctrlKey){
								if (td.hasClass('datagrid-row-selected')){
									unselectCell(target, p);
								} else {
									selectCell(target, p);
								}
							} else {
								unselectAllCells(target);
								selectCell(target, p);
							}
						} else {
							if (td.hasClass('datagrid-row-selected')){
								unselectCell(target, p);
							} else {
								selectCell(target, p);
							}
						}
					}
				}
			}
		}).bind('mouseover.cellediting', function(e){
			var td = $(e.target).closest('td[field]');
			if (td.length){
				td.addClass('datagrid-row-over');
				td.closest('tr.datagrid-row').removeClass('datagrid-row-over');
			}
		}).bind('mouseout.cellediting', function(e){
			var td = $(e.target).closest('td[field]');
			td.removeClass('datagrid-row-over');
		});

		opts.isRtl = dg.datagrid('getPanel').css('direction').toLowerCase()=='rtl';
		opts.OldOnBeforeSelect = opts.onBeforeSelect;
		opts.onBeforeSelect = function(){
			return false;
		};
		dg.datagrid('clearSelections');
	}

	function disableCellSelecting(target){
		var dg = $(target);
		var state = dg.data('datagrid');
		var panel = dg.datagrid('getPanel');
		var opts = state.options;
		opts.onBeforeSelect = opts.OldOnBeforeSelect || opts.onBeforeSelect;
		panel.unbind('.cellediting').find('td.datagrid-row-selected').removeClass('datagrid-row-selected');
		var dc = state.dc;
		dc.body1.add(dc.body2).unbind('cellediting');
	}

	function enableCellEditing(target){
		var dg = $(target);
		var opts = dg.datagrid('options');
		var panel = dg.datagrid('getPanel');
		panel.attr('tabindex',1).css('outline-style','none').unbind('.cellediting').bind('keydown.cellediting', function(e){
			var h = opts.navHandler[e.keyCode];
			if (h){
				return h.call(target, e);
			}
		}).bind('keypress.cellediting', function(e){
			return opts.navHandler['keypress'].call(target, e);
		});
		panel.panel('panel').unbind('.cellediting').bind('keydown.cellediting', function(e){
			e.stopPropagation();
		}).bind('keypress.cellediting', function(e){
			e.stopPropagation();
		}).bind('mouseover.cellediting', function(e){
			var td = $(e.target).closest('td[field]');
			if (td.length){
				td.addClass('datagrid-row-over');
				td.closest('tr.datagrid-row').removeClass('datagrid-row-over');
			}
		}).bind('mouseout.cellediting', function(e){
			var td = $(e.target).closest('td[field]');
			td.removeClass('datagrid-row-over');
		});

		opts.isRtl = dg.datagrid('getPanel').css('direction').toLowerCase()=='rtl';
		opts.oldOnClickCell = opts.onClickCell;
		opts.oldOnDblClickCell = opts.onDblClickCell;
		opts.onClickCell = function(index, field, value){
			if (opts.clickToEdit){
				$(this).datagrid('editCell', {index:index,field:field});
			} else {
				if (endCellEdit(this, true)){
					$(this).datagrid('gotoCell', {
						index: index,
						field: field
					});
				}
			}
			opts.oldOnClickCell.call(this, index, field, value);
		}
		if (opts.dblclickToEdit){
			opts.onDblClickCell = function(index, field, value){
				$(this).datagrid('editCell', {index:index,field:field});
				opts.oldOnDblClickCell.call(this, index, field, value);
			}
		}
		opts.OldOnBeforeSelect = opts.onBeforeSelect;
		opts.onBeforeSelect = function(){
			return false;
		};
		dg.datagrid('clearSelections')
	}

	function disableCellEditing(target){
		var dg = $(target);
		var panel = dg.datagrid('getPanel');
		var opts = dg.datagrid('options');
		opts.onClickCell = opts.oldOnClickCell || opts.onClickCell;
		opts.onDblClickCell = opts.oldOnDblClickCell || opts.onDblClickCell;
		opts.onBeforeSelect = opts.OldOnBeforeSelect || opts.onBeforeSelect;
		panel.unbind('.cellediting').find('td.datagrid-row-selected').removeClass('datagrid-row-selected');
		panel.panel('panel').unbind('.cellediting');
	}


	$.extend($.fn.datagrid.methods, {
		editCell: function(jq, param){
			return jq.each(function(){
				editCell(this, param);
			});
		},
		isEditing: function(jq, index){
			var opts = $.data(jq[0], 'datagrid').options;
			var tr = opts.finder.getTr(jq[0], index);
			return tr.length && tr.hasClass('datagrid-row-editing');
		},
		gotoCell: function(jq, param){
			return jq.each(function(){
				gotoCell(this, param);
			});
		},
		enableCellEditing: function(jq){
			return jq.each(function(){
				enableCellEditing(this);
			});
		},
		disableCellEditing: function(jq){
			return jq.each(function(){
				disableCellEditing(this);
			});
		},
		enableCellSelecting: function(jq){
			return jq.each(function(){
				enableCellSelecting(this);
			});
		},
		disableCellSelecting: function(jq){
			return jq.each(function(){
				disableCellSelecting(this);
			});
		},
		input: function(jq, param){
			if (!param){return null;}
			var ed = jq.datagrid('getEditor', param);
			if (ed){
				var t = $(ed.target);
				if (t.hasClass('textbox-f')){
					t = t.textbox('textbox');
				}
				return t;
			} else {
				return null;
			}
		},
		cell: function(jq){		// get current cell info {index,field}
			return getCurrCell(jq[0]);
		},
		getSelectedCells: function(jq){
			return getSelectedCells(jq[0]);
		}
	});

})(jQuery);
$.extend($.fn.datagrid.defaults, {
	autoUpdateDetail: true  // Define if update the row detail content when update a row
});

var detailview = $.extend({}, $.fn.datagrid.defaults.view, {
	render: function(target, container, frozen){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		if (frozen){
			if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))){
				return;
			}
		}
		
		var rows = state.data.rows;
		var fields = $(target).datagrid('getColumnFields', frozen);
		var table = [];
		table.push('<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>');
		for(var i=0; i<rows.length; i++) {
			// get the class and style attributes for this row
			var css = opts.rowStyler ? opts.rowStyler.call(target, i, rows[i]) : '';
			var classValue = '';
			var styleValue = '';
			if (typeof css == 'string'){
				styleValue = css;
			} else if (css){
				classValue = css['class'] || '';
				styleValue = css['style'] || '';
			}
			
			var cls = 'class="datagrid-row ' + (i % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
			var style = styleValue ? 'style="' + styleValue + '"' : '';
			var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + i;
			table.push('<tr id="' + rowId + '" datagrid-row-index="' + i + '" ' + cls + ' ' + style + '>');
			table.push(this.renderRow.call(this, target, fields, frozen, i, rows[i]));
			table.push('</tr>');
			
			table.push('<tr style="display:none;">');
			if (frozen){
				table.push('<td colspan=' + (fields.length+(opts.rownumbers?1:0)) + ' style="border-right:0">');
			} else {
				table.push('<td colspan=' + (fields.length) + '>');
			}

			table.push('<div class="datagrid-row-detail">');
			if (frozen){
				table.push('&nbsp;');
			} else {
				table.push(opts.detailFormatter.call(target, i, rows[i]));
			}
			table.push('</div>');

			table.push('</td>');
			table.push('</tr>');
			
		}
		table.push('</tbody></table>');
		
		$(container).html(table.join(''));
	},
	
	renderRow: function(target, fields, frozen, rowIndex, rowData){
		var opts = $.data(target, 'datagrid').options;
		
		var cc = [];
		if (frozen && opts.rownumbers){
			var rownumber = rowIndex + 1;
			if (opts.pagination){
				rownumber += (opts.pageNumber-1)*opts.pageSize;
			}
			cc.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber">'+rownumber+'</div></td>');
		}
		for(var i=0; i<fields.length; i++){
			var field = fields[i];
			var col = $(target).datagrid('getColumnOption', field);
			if (col){
				var value = rowData[field];	// the field value
				var css = col.styler ? (col.styler(value, rowData, rowIndex)||'') : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (cc){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				var cls = classValue ? 'class="' + classValue + '"' : '';
				var style = col.hidden ? 'style="display:none;' + styleValue + '"' : (styleValue ? 'style="' + styleValue + '"' : '');
				
				cc.push('<td field="' + field + '" ' + cls + ' ' + style + '>');
				
				if (col.checkbox){
					style = '';
				} else if (col.expander){
					style = "text-align:center;height:16px;";
				} else {
					style = styleValue;
					if (col.align){style += ';text-align:' + col.align + ';'}
					if (!opts.nowrap){
						style += ';white-space:normal;height:auto;';
					} else if (opts.autoRowHeight){
						style += ';height:auto;';
					}
				}
				
				cc.push('<div style="' + style + '" ');
				if (col.checkbox){
					cc.push('class="datagrid-cell-check ');
				} else {
					cc.push('class="datagrid-cell ' + col.cellClass);
				}
				cc.push('">');
				
				if (col.checkbox){
					cc.push('<input type="checkbox" name="' + field + '" value="' + (value!=undefined ? value : '') + '">');
				} else if (col.expander) {
					//cc.push('<div style="text-align:center;width:16px;height:16px;">');
					cc.push('<span class="datagrid-row-expander datagrid-row-expand" style="display:inline-block;width:16px;height:16px;cursor:pointer;" />');
					//cc.push('</div>');
				} else if (col.formatter){
					cc.push(col.formatter(value, rowData, rowIndex));
				} else {
					cc.push(value);
				}
				
				cc.push('</div>');
				cc.push('</td>');
			}
		}
		return cc.join('');
	},
	
	insertRow: function(target, index, row){
		var opts = $.data(target, 'datagrid').options;
		var dc = $.data(target, 'datagrid').dc;
		var panel = $(target).datagrid('getPanel');
		var view1 = dc.view1;
		var view2 = dc.view2;
		
		var isAppend = false;
		var rowLength = $(target).datagrid('getRows').length;
		if (rowLength == 0){
			$(target).datagrid('loadData',{total:1,rows:[row]});
			return;
		}
		
		if (index == undefined || index == null || index >= rowLength) {
			index = rowLength;
			isAppend = true;
			this.canUpdateDetail = false;
		}
		
		$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);
		
		_insert(true);
		_insert(false);
		
		this.canUpdateDetail = true;
		
		function _insert(frozen){
			var tr = opts.finder.getTr(target, index, 'body', frozen?1:2);
			if (isAppend){
				var detail = tr.next();
				var newDetail = tr.next().clone();
				tr.insertAfter(detail);
			} else {
				var newDetail = tr.next().next().clone();
			}
			newDetail.insertAfter(tr);
			newDetail.hide();
			if (!frozen){
				newDetail.find('div.datagrid-row-detail').html(opts.detailFormatter.call(target, index, row));
			}
		}
	},
	
	deleteRow: function(target, index){
		var opts = $.data(target, 'datagrid').options;
		var dc = $.data(target, 'datagrid').dc;
		var tr = opts.finder.getTr(target, index);
		tr.next().remove();
		$.fn.datagrid.defaults.view.deleteRow.call(this, target, index);
		dc.body2.triggerHandler('scroll');
	},
	
	updateRow: function(target, rowIndex, row){
		var dc = $.data(target, 'datagrid').dc;
		var opts = $.data(target, 'datagrid').options;
		var cls = $(target).datagrid('getExpander', rowIndex).attr('class');
		$.fn.datagrid.defaults.view.updateRow.call(this, target, rowIndex, row);
		$(target).datagrid('getExpander', rowIndex).attr('class',cls);
		// update the detail content
		if (opts.autoUpdateDetail && this.canUpdateDetail){
			/*var row = $(target).datagrid('getRows')[rowIndex];
			var detail = $(target).datagrid('getRowDetail', rowIndex);
			detail.html(opts.detailFormatter.call(target, rowIndex, row));*/
		}
	},
	
	bindEvents: function(target){
		var state = $.data(target, 'datagrid');

		if (state.ss.bindDetailEvents){return;}
		state.ss.bindDetailEvents = true;

		var dc = state.dc;
		var opts = state.options;
		var body = dc.body1.add(dc.body2);
		var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
		body.unbind('click').bind('click', function(e){
			var tt = $(e.target);
			var tr = tt.closest('tr.datagrid-row');
			if (!tr.length){return}
			if (tt.hasClass('datagrid-row-expander')){
				var rowIndex = parseInt(tr.attr('datagrid-row-index'));
				if (tt.hasClass('datagrid-row-expand')){
					$(target).datagrid('expandRow', rowIndex);
				} else {
					$(target).datagrid('collapseRow', rowIndex);
				}
				$(target).datagrid('fixRowHeight');
				
			} else {
				clickHandler(e);
			}
			e.stopPropagation();
		});
	},
	
	onBeforeRender: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var t = $(target);
		var hasExpander = false;
		var fields = t.datagrid('getColumnFields',true).concat(t.datagrid('getColumnFields'));
		for(var i=0; i<fields.length; i++){
			var col = t.datagrid('getColumnOption', fields[i]);
			if (col.expander){
				hasExpander = true;
				break;
			}
		}
		if (!hasExpander){
			if (opts.frozenColumns && opts.frozenColumns.length){
				opts.frozenColumns[0].splice(0,0,{field:'_expander',expander:true,width:24,resizable:false,fixed:true});
			} else {
				opts.frozenColumns = [[{field:'_expander',expander:true,width:24,resizable:false,fixed:true}]];
			}
			
			var t = dc.view1.children('div.datagrid-header').find('table');
			var td = $('<td rowspan="'+opts.frozenColumns.length+'"><div class="datagrid-header-expander" style="width:24px;"></div></td>');
			if ($('tr',t).length == 0){
				td.wrap('<tr></tr>').parent().appendTo($('tbody',t));
			} else if (opts.rownumbers){
				td.insertAfter(t.find('td:has(div.datagrid-header-rownumber)'));
			} else {
				td.prependTo(t.find('tr:first'));
			}
		}

		// if (!state.bindDetailEvents){
		// 	state.bindDetailEvents = true;
		// 	var that = this;
		// 	setTimeout(function(){
		// 		that.bindEvents(target);
		// 	},0);
		// }
	},
	
	onAfterRender: function(target){
		var that = this;
		var state = $.data(target, 'datagrid');
		var dc = state.dc;
		var opts = state.options;
		var panel = $(target).datagrid('getPanel');
		
		$.fn.datagrid.defaults.view.onAfterRender.call(this, target);
		
		if (!state.onResizeColumn){
			state.onResizeColumn = opts.onResizeColumn;
			opts.onResizeColumn = function(field, width){
				if (!opts.fitColumns){
					resizeDetails();				
				}
				var rowCount = $(target).datagrid('getRows').length;
				for(var i=0; i<rowCount; i++){
					$(target).datagrid('fixDetailRowHeight', i);
				}
				
				// call the old event code
				state.onResizeColumn.call(target, field, width);
			};
		}
		if (!state.onResize){
			state.onResize = opts.onResize;
			opts.onResize = function(width, height){
				if (opts.fitColumns){
					resizeDetails();
				}
				state.onResize.call(panel, width, height);
			};
		}

		// function resizeDetails(){
		// 	var ht = dc.header2.find('table');
		// 	var fr = ht.find('tr.datagrid-filter-row');
		// 	fr.hide();
		// 	var ww = ht.width()-1;
		// 	var details = dc.body2.find('>table.datagrid-btable>tbody>tr>td>div.datagrid-row-detail:visible')._outerWidth(ww);
		// 	details.find('.easyui-fluid').trigger('_resize');
		// 	fr.show();
		// }
		function resizeDetails(){
			var details = dc.body2.find('>table.datagrid-btable>tbody>tr>td>div.datagrid-row-detail:visible');
			if (details.length){
				var ww = 0;
				dc.header2.find('.datagrid-header-check:visible,.datagrid-cell:visible').each(function(){
					ww += $(this).outerWidth(true) + 1;
				});
				if (ww != details.outerWidth(true)){
					details._outerWidth(ww);
					details.find('.easyui-fluid').trigger('_resize');
				}
			}
		}
		
		
		this.canUpdateDetail = true;	// define if to update the detail content when 'updateRow' method is called;
		
		var footer = dc.footer1.add(dc.footer2);
		footer.find('span.datagrid-row-expander').css('visibility', 'hidden');
		$(target).datagrid('resize');

		this.bindEvents(target);
		var detail = dc.body1.add(dc.body2).find('div.datagrid-row-detail');
		detail.unbind().bind('mouseover mouseout click dblclick contextmenu scroll', function(e){
			e.stopPropagation();
		});
	}
});

$.extend($.fn.datagrid.methods, {
	fixDetailRowHeight: function(jq, index){
		return jq.each(function(){
			var opts = $.data(this, 'datagrid').options;
			if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))){
				return;
			}
			var dc = $.data(this, 'datagrid').dc;
			var tr1 = opts.finder.getTr(this, index, 'body', 1).next();
			var tr2 = opts.finder.getTr(this, index, 'body', 2).next();
			// fix the detail row height
			if (tr2.is(':visible')){
				tr1.css('height', '');
				tr2.css('height', '');
				var height = Math.max(tr1.height(), tr2.height());
				tr1.css('height', height);
				tr2.css('height', height);
			}
			dc.body2.triggerHandler('scroll');
		});
	},
	getExpander: function(jq, index){	// get row expander object
		var opts = $.data(jq[0], 'datagrid').options;
		return opts.finder.getTr(jq[0], index).find('span.datagrid-row-expander');
	},
	// get row detail container
	getRowDetail: function(jq, index){
		var opts = $.data(jq[0], 'datagrid').options;
		var tr = opts.finder.getTr(jq[0], index, 'body', 2);
		// return tr.next().find('div.datagrid-row-detail');
		return tr.next().find('>td>div.datagrid-row-detail');
	},
	expandRow: function(jq, index){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var dc = $.data(this, 'datagrid').dc;
			var expander = $(this).datagrid('getExpander', index);
			if (expander.hasClass('datagrid-row-expand')){
				expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
				var tr1 = opts.finder.getTr(this, index, 'body', 1).next();
				var tr2 = opts.finder.getTr(this, index, 'body', 2).next();
				tr1.show();
				tr2.show();
				$(this).datagrid('fixDetailRowHeight', index);
				if (opts.onExpandRow){
					var row = $(this).datagrid('getRows')[index];
					opts.onExpandRow.call(this, index, row);
				}
			}
		});
	},
	collapseRow: function(jq, index){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var dc = $.data(this, 'datagrid').dc;
			var expander = $(this).datagrid('getExpander', index);
			if (expander.hasClass('datagrid-row-collapse')){
				expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
				var tr1 = opts.finder.getTr(this, index, 'body', 1).next();
				var tr2 = opts.finder.getTr(this, index, 'body', 2).next();
				tr1.hide();
				tr2.hide();
				dc.body2.triggerHandler('scroll');
				if (opts.onCollapseRow){
					var row = $(this).datagrid('getRows')[index];
					opts.onCollapseRow.call(this, index, row);
				}
			}
		});
	}
});

$.extend($.fn.datagrid.methods, {
	subgrid: function(jq, conf){
		return jq.each(function(){
			createGrid(this, conf);

			function createGrid(target, conf, prow){
				var queryParams = $.extend({}, conf.options.queryParams||{});
				// queryParams[conf.options.foreignField] = prow ? prow[conf.options.foreignField] : undefined;
				if (prow){
					var fk = conf.options.foreignField;
					if ($.isFunction(fk)){
						$.extend(queryParams, fk.call(conf, prow));
					} else {
						queryParams[fk] = prow[fk];
					}
				}

				var plugin = conf.options.edatagrid ? 'edatagrid' : 'datagrid';

				$(target)[plugin]($.extend({}, conf.options, {
					subgrid: conf.subgrid,
					view: (conf.subgrid ? detailview : undefined),
					queryParams: queryParams,
					detailFormatter: function(index, row){
						return '<div><table class="datagrid-subgrid"></table></div>';
					},
					onExpandRow: function(index, row){
						var opts = $(this).datagrid('options');
						var rd = $(this).datagrid('getRowDetail', index);
						var dg = getSubGrid(rd);
						if (!dg.data('datagrid')){
							createGrid(dg[0], opts.subgrid, row);
						}
						rd.find('.easyui-fluid').trigger('_resize');
						setHeight(this, index);
						if (conf.options.onExpandRow){
							conf.options.onExpandRow.call(this, index, row);
						}
					},
					onCollapseRow: function(index, row){
						setHeight(this, index);
						if (conf.options.onCollapseRow){
							conf.options.onCollapseRow.call(this, index, row);
						}
					},
					onResize: function(){
						var dg = $(this).children('div.datagrid-view').children('table')
						setParentHeight(this);
					},
					onResizeColumn: function(field, width){
						setParentHeight(this);
						if (conf.options.onResizeColumn){
							conf.options.onResizeColumn.call(this, field, width);
						}
					},
					onLoadSuccess: function(data){
						setParentHeight(this);
						if (conf.options.onLoadSuccess){
							conf.options.onLoadSuccess.call(this, data);
						}
					}
				}));
			}
			function getSubGrid(rowDetail){
				var div = $(rowDetail).children('div');
				if (div.children('div.datagrid').length){
					return div.find('>div.datagrid>div.panel-body>div.datagrid-view>table.datagrid-subgrid');
				} else {
					return div.find('>table.datagrid-subgrid');
				}
			}
			function setParentHeight(target){
				var tr = $(target).closest('div.datagrid-row-detail').closest('tr').prev();
				if (tr.length){
					var index = parseInt(tr.attr('datagrid-row-index'));
					var dg = tr.closest('div.datagrid-view').children('table');
					setHeight(dg[0], index);
				}
			}
			function setHeight(target, index){
				$(target).datagrid('fixDetailRowHeight', index);
				$(target).datagrid('fixRowHeight', index);
				var tr = $(target).closest('div.datagrid-row-detail').closest('tr').prev();
				if (tr.length){
					var index = parseInt(tr.attr('datagrid-row-index'));
					var dg = tr.closest('div.datagrid-view').children('table');
					setHeight(dg[0], index);
				}
			}
		});
	},
	getSelfGrid: function(jq){
		var grid = jq.closest('.datagrid');
		if (grid.length){
			return grid.find('>.datagrid-wrap>.datagrid-view>.datagrid-f');
		} else {
			return null;
		}
	},
	getParentGrid: function(jq){
		var detail = jq.closest('div.datagrid-row-detail');
		if (detail.length){
			return detail.closest('.datagrid-view').children('.datagrid-f');
		} else {
			return null;
		}
	},
	getParentRowIndex: function(jq){
		var detail = jq.closest('div.datagrid-row-detail');
		if (detail.length){
			var tr = detail.closest('tr').prev();
			return parseInt(tr.attr('datagrid-row-index'));
		} else {
			return -1;
		}
	}
});

(function($){
	$.extend($.fn.datagrid.defaults, {
		dropAccept: 'tr.datagrid-row',
		dragSelection: false,
		onBeforeDrag: function(row){},	// return false to deny drag
		onStartDrag: function(row){},
		onStopDrag: function(row){},
		onDragEnter: function(targetRow, sourceRow){},	// return false to deny drop
		onDragOver: function(targetRow, sourceRow){},	// return false to deny drop
		onDragLeave: function(targetRow, sourceRow){},
		onBeforeDrop: function(targetRow, sourceRow, point){},
		onDrop: function(targetRow, sourceRow, point){},	// point:'append','top','bottom'
	});
	$.extend($.fn.datagrid.methods, {
		_appendRow: function(jq, row){
			return jq.each(function(){
				var dg = $(this);
				var rows = $.isArray(row) ? row : [row];
				$.map(rows, function(row){
					dg.datagrid('appendRow', row).datagrid('enableDnd', dg.datagrid('getRows').length-1);
					if (row._selected){
						dg.datagrid('selectRow', dg.datagrid('getRows').length-1);
					}
				});
			});
		},
		_insertRow: function(jq, param){
			return jq.each(function(){
				var dg = $(this);
				var index = param.index;
				var row = param.row;
				var rows = $.isArray(row) ? row : [row];
				$.map(rows, function(row, i){
					dg.datagrid('insertRow', {
						index: (index+i),
						row: row
					}).datagrid('enableDnd', index+i);
					if (row._selected){
						dg.datagrid('selectRow', index+i);
					}
				});
			});
		},
		_deleteRow: function(jq, row){
			return jq.each(function(){
				var dg = $(this);
				var rows = $.isArray(row) ? row : [row];
				$.map(rows, function(row){
					var index = dg.datagrid('getRowIndex', row);
					dg.datagrid('deleteRow', index);
				});
			});
		}
	});
	
	var disabledDroppingRows = [];

	function enableDroppable(aa){
		$.map(aa, function(row){
			$(row).droppable('enable');
		});
	}
	
	$.extend($.fn.datagrid.methods, {
		resetDroppable: function(jq){
			return jq.each(function(){
				var c = $(this).datagrid('getPanel')[0];
				var my = [];
				var left = [];
				for(var i=0; i<disabledDroppingRows.length; i++){
					var t = disabledDroppingRows[i];
					var p = $(t).closest('div.datagrid-wrap');
					if (p.length && p[0] == c){
						my.push(t);
					} else {
						left.push(t);
					}
				}
				disabledDroppingRows = left;
				enableDroppable(my);
			});
		},
		enableDnd: function(jq, index){
			if (!$('#datagrid-dnd-style').length){
				$('<style id="datagrid-dnd-style">' +
					'.datagrid-row-top>td{border-top:1px solid red}' +
					'.datagrid-row-bottom>td{border-bottom:1px solid red}' +
					'</style>'
				).appendTo('head');
			}
			return jq.each(function(){
				var target = this;
				var state = $.data(this, 'datagrid');
				var dg = $(this);
				var opts = state.options;
				var draggableOptions = {
					disabled: false,
					revert: true,
					cursor: 'pointer',
					proxy: function(source) {
						var p = $('<div style="z-index:9999999999999"></div>').appendTo('body');
						var draggingRow = getDraggingRow(source);
						var rows = $.isArray(draggingRow) ? draggingRow : [draggingRow];
						$.map(rows, function(row,i){
							var index = dg.datagrid('getRowIndex', row);
							var tr1 = opts.finder.getTr(target, index, 'body', 1);
							var tr2 = opts.finder.getTr(target, index, 'body', 2);
							tr2.clone().removeAttr('id').removeClass('droppable').appendTo(p);
							tr1.clone().removeAttr('id').removeClass('droppable').find('td').insertBefore(p.find('tr:eq('+i+') td:first'));
							$('<td><span class="tree-dnd-icon tree-dnd-no" style="position:static">&nbsp;</span></td>').insertBefore(p.find('tr:eq('+i+') td:first'));
						});
						p.find('td').css('vertical-align','middle');
						p.hide();
						return p;
					},
					deltaX: 15,
					deltaY: 15,
					onBeforeDrag:function(e){
						var draggingRow = getDraggingRow(this);
						if (opts.onBeforeDrag.call(target, draggingRow) == false){return false;}
						if ($(e.target).parent().hasClass('datagrid-cell-check')){return false;}
						if (e.which != 1){return false;}
					},
					onStartDrag: function() {
						$(this).draggable('proxy').css({
							left: -10000,
							top: -10000
						});
						var draggingRow = getDraggingRow(this);
						setValid(draggingRow, false);
						state.draggingRow = draggingRow;
						opts.onStartDrag.call(target, draggingRow);
					},
					onDrag: function(e) {
						var x1=e.pageX,y1=e.pageY,x2=e.data.startX,y2=e.data.startY;
						var d = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
						if (d>3){	// when drag a little distance, show the proxy object
							$(this).draggable('proxy').show();
							var tr = opts.finder.getTr(target, parseInt($(this).attr('datagrid-row-index')), 'body');
							$.extend(e.data, {
								startX: tr.offset().left,
								startY: tr.offset().top,
								offsetWidth: 0,
								offsetHeight: 0
							});
						}
						this.pageY = e.pageY;
					},
					onStopDrag:function(){
						enableDroppable(disabledDroppingRows);
						disabledDroppingRows = [];
						setValid(state.draggingRow, true);
						opts.onStopDrag.call(target, state.draggingRow);
					}
				};
				var droppableOptions = {
					accept: opts.dropAccept,
					onDragEnter: function(e, source){
						if ($(this).droppable('options').disabled){return;}
						var dTarget = getDataGridTarget(this);
						var dOpts = $(dTarget).datagrid('options');
						var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
						var sRow = getDraggingRow(source);
						var dRow = getRow(this);
						if (tr.length && dRow){
							cb();							
						}

						function cb(){
							if (opts.onDragEnter.call(target, dRow, sRow) == false){
								tr.removeClass('datagrid-row-top datagrid-row-bottom');
								tr.droppable('disable');
								tr.each(function(){
									disabledDroppingRows.push(this);
								});
							}							
						}
					},
					onDragOver: function(e, source) {
						if ($(this).droppable('options').disabled){
							return;
						}
						if ($.inArray(this, disabledDroppingRows) >= 0){
							return;
						}
						var dTarget = getDataGridTarget(this);
						var dOpts = $(dTarget).datagrid('options');
						var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
						if (tr.length){
							if (!isValid(tr)){
								setProxyFlag(source, false);
								return;
							}
						}
						setProxyFlag(source, true);

						var sRow = getDraggingRow(source);
						var dRow = getRow(this);
						if (tr.length){
							var pageY = source.pageY;
							var top = tr.offset().top;
							var bottom = tr.offset().top + tr.outerHeight();
							tr.removeClass('datagrid-row-top datagrid-row-bottom');
							if (pageY > top + (bottom - top) / 2) {
								tr.addClass('datagrid-row-bottom');
							} else {
								tr.addClass('datagrid-row-top');
							}
							if (dRow){
								cb();
							}
						}

						function cb(){
							if (opts.onDragOver.call(target, dRow, sRow) == false){
								setProxyFlag(source, false);
								tr.removeClass('datagrid-row-top datagrid-row-bottom');
								tr.droppable('disable');
								tr.each(function(){
									disabledDroppingRows.push(this);
								});
							}
						}
					},
					onDragLeave: function(e, source) {
						if ($(this).droppable('options').disabled){
							return;
						}
						setProxyFlag(source, false);
						var dTarget = getDataGridTarget(this);
						var dOpts = $(dTarget).datagrid('options');
						var tr = dOpts.finder.getTr(dTarget, $(this).attr('datagrid-row-index'));
						tr.removeClass('datagrid-row-top datagrid-row-bottom');
						var sRow = getDraggingRow(source);
						var dRow = getRow(this);
						if (dRow){
							opts.onDragLeave.call(target, dRow, sRow);
						}
					},
					onDrop: function(e, source) {
						if ($(this).droppable('options').disabled){
							return;
						}
						var sTarget = getDataGridTarget(source);
						var dTarget = getDataGridTarget(this);
						var dOpts = $(dTarget).datagrid('options');
						var tr = dOpts.finder.getTr(dTarget, null, 'highlight');

						var point = null;
						var sRow = getDraggingRow(source);
						var dRow = null;
						if (tr.length){
							if (!isValid(tr)){
								return;
							}
							point = tr.hasClass('datagrid-row-top') ? 'top' : 'bottom';
							tr.removeClass('datagrid-row-top datagrid-row-bottom');
							dRow = getRow(tr);
						}
						
						if (opts.onBeforeDrop.call(target, dRow, sRow, point) == false){
							return;
						}
						insert.call(this);
						opts.onDrop.call(target, dRow, sRow, point);
						
						function insert(){
							var destIndex = parseInt(tr.attr('datagrid-row-index'));
							
							if (!point){
								$(dTarget).datagrid('_appendRow', sRow);
								$(sTarget).datagrid('_deleteRow', sRow);
							} else if (dTarget != sTarget){
								var index = point == 'top' ? destIndex : (destIndex+1);
								if (index >= 0){
									$(sTarget).datagrid('_deleteRow', sRow);
									$(dTarget).datagrid('_insertRow', {
										index: index,
										row: sRow
									});
								}
							} else {
								var dg = $(dTarget);
								var index = point == 'top' ? destIndex : (destIndex+1);
								if (index >= 0){
									dg.datagrid('_deleteRow', sRow);
									var destIndex = parseInt(tr.attr('datagrid-row-index'));
									var index = point == 'top' ? destIndex : (destIndex+1);
									if (index >= 0){
										dg.datagrid('_insertRow', {
											index: index,
											row: sRow
										});
									}
								}
							}
						}
					}
				}
				
				if (index != undefined){
					var trs = opts.finder.getTr(this, index);
				} else {
					var trs = opts.finder.getTr(this, 0, 'allbody');
				}
				trs.draggable(draggableOptions);
				trs.droppable(droppableOptions);
				setDroppable(target);
				
				function setProxyFlag(source, allowed){
					var icon = $(source).draggable('proxy').find('span.tree-dnd-icon');
					icon.removeClass('tree-dnd-yes tree-dnd-no').addClass(allowed ? 'tree-dnd-yes' : 'tree-dnd-no');
				}
				function getRow(tr){
					if (!$(tr).hasClass('datagrid-row')){return null}
					var target = $(tr).closest('div.datagrid-view').children('table')[0];
					var opts = $(target).datagrid('options');
					return opts.finder.getRow(target, $(tr));
				}
				function getDraggingRow(tr){
					if (!$(tr).hasClass('datagrid-row')){return null}
					var target = getDataGridTarget(tr);
					var opts = $(target).datagrid('options');
					if (opts.dragSelection){
						if ($(tr).hasClass('datagrid-row-selected')){
							var rows = $(target).datagrid('getSelections');
							$.map(rows, function(row){
								row._selected = true;
							});
							return rows;
						}
					}
					var row = opts.finder.getRow(target, $(tr));
					row._selected = $(tr).hasClass('datagrid-row-selected');
					return row;
				}
				function setDroppable(target){
					getDroppableBody(target).droppable(droppableOptions).droppable('enable');
				}
				function getDataGridTarget(el){
					return $(el).closest('div.datagrid-view').children('table')[0];
				}
				function getDroppableBody(target){
					var dc = $(target).data('datagrid').dc;
					return dc.view;
				}
				function isValid(tr){
					var opts = $(tr).droppable('options');
					if (opts.disabled || opts.accept == 'no-accept'){
						return false;
					} else {
						return true;
					}
				}
				function setValid(rows, valid){
					var accept = valid ? opts.dropAccept : 'no-accept';
					$.map($.isArray(rows)?rows:[rows], function(row){
						var index = $(target).datagrid('getRowIndex', row);
						opts.finder.getTr(target, index).droppable({accept:accept});
					});
				}
			});
		}
		
	});
})(jQuery);
(function($){
	function getPluginName(target){
		if ($(target).data('treegrid')){
			return 'treegrid';
		} else {
			return 'datagrid';
		}
	}

	var autoSizeColumn1 = $.fn.datagrid.methods.autoSizeColumn;
	var loadDataMethod1 = $.fn.datagrid.methods.loadData;
	var appendMethod1 = $.fn.datagrid.methods.appendRow;
	var deleteMethod1 = $.fn.datagrid.methods.deleteRow;
	$.extend($.fn.datagrid.methods, {
		autoSizeColumn: function(jq, field){
			return jq.each(function(){
				var fc = $(this).datagrid('getPanel').find('.datagrid-header .datagrid-filter-c');
				// fc.hide();
				fc.css({
					width:'1px',
					height:0
				});
				autoSizeColumn1.call($.fn.datagrid.methods, $(this), field);
				// fc.show();
				fc.css({
					width:'',
					height:''
				});
				resizeFilter(this, field);
			});
		},
		loadData: function(jq, data){
			jq.each(function(){
				$.data(this, 'datagrid').filterSource = null;
			});
			return loadDataMethod1.call($.fn.datagrid.methods, jq, data);
		},
		appendRow: function(jq, row){
			var result = appendMethod1.call($.fn.datagrid.methods, jq, row);
			jq.each(function(){
				var state = $(this).data('datagrid');
				if (state.filterSource){
					state.filterSource.total++;
					if (state.filterSource.rows != state.data.rows){
						state.filterSource.rows.push(row);
					}
				}
			});
			return result;
		},
		deleteRow: function(jq, index){
			jq.each(function(){
				var state = $(this).data('datagrid');
				var opts = state.options;
				if (state.filterSource && opts.idField){
					if (state.filterSource.rows == state.data.rows){
						state.filterSource.total--;
					} else {
						for(var i=0; i<state.filterSource.rows.length; i++){
							var row = state.filterSource.rows[i];
							if (row[opts.idField] == state.data.rows[index][opts.idField]){
								state.filterSource.rows.splice(i,1);
								state.filterSource.total--;
								break;
							}
						}
					}
				}
			});
			return deleteMethod1.call($.fn.datagrid.methods, jq, index);		
		}
	});

	var loadDataMethod2 = $.fn.treegrid.methods.loadData;
	var appendMethod2 = $.fn.treegrid.methods.append;
	var insertMethod2 = $.fn.treegrid.methods.insert;
	var removeMethod2 = $.fn.treegrid.methods.remove;
	$.extend($.fn.treegrid.methods, {
		loadData: function(jq, data){
			jq.each(function(){
				$.data(this, 'treegrid').filterSource = null;
			});
			return loadDataMethod2.call($.fn.treegrid.methods, jq, data);
		},
		append: function(jq, param){
			return jq.each(function(){
				var state = $(this).data('treegrid');
				var opts = state.options;
				if (opts.oldLoadFilter){
					var rows = translateTreeData(this, param.data, param.parent);
					state.filterSource.total += rows.length;
					state.filterSource.rows = state.filterSource.rows.concat(rows);
					$(this).treegrid('loadData', state.filterSource)
				} else {
					appendMethod2($(this), param);
				}
			});
		},
		insert: function(jq, param){
			return jq.each(function(){
				var state = $(this).data('treegrid');
				var opts = state.options;
				if (opts.oldLoadFilter){
					var ref = param.before || param.after;
					var index = getNodeIndex(param.before || param.after);
					var pid = index>=0 ? state.filterSource.rows[index]._parentId : null;
					var rows = translateTreeData(this, [param.data], pid);
					var newRows = state.filterSource.rows.splice(0, index>=0 ? (param.before ? index : index+1) : (state.filterSource.rows.length));
					newRows = newRows.concat(rows);
					newRows = newRows.concat(state.filterSource.rows);
					state.filterSource.total += rows.length;
					state.filterSource.rows = newRows;
					$(this).treegrid('loadData', state.filterSource);

					function getNodeIndex(id){
						var rows = state.filterSource.rows;
						for(var i=0; i<rows.length; i++){
							if (rows[i][opts.idField] == id){
								return i;
							}
						}
						return -1;
					}
				} else {
					insertMethod2($(this), param);
				}
			});
		},
		remove: function(jq, id){
			jq.each(function(){
				var state = $(this).data('treegrid');
				if (state.filterSource){
					var opts = state.options;
					var rows = state.filterSource.rows;
					for(var i=0; i<rows.length; i++){
						if (rows[i][opts.idField] == id){
							rows.splice(i, 1);
							state.filterSource.total--;
							break;
						}
					}
				}
			});
			return removeMethod2(jq, id);
		}
	});

	var extendedOptions = {
		filterMenuIconCls: 'icon-ok',
		filterBtnIconCls: 'icon-filter',
		filterBtnPosition: 'right',
		filterPosition: 'bottom',
		remoteFilter: false,
		showFilterBar: true,
		filterDelay: 400,
		filterRules: [],
		// specify whether the filtered records need to match ALL or ANY of the applied filters
		filterMatchingType: 'all',	// possible values: 'all','any'
		// filterCache: {},
		filterMatcher: function(data){
			var name = getPluginName(this);
			var dg = $(this);
			var state = $.data(this, name);
			var opts = state.options;
			if (opts.filterRules.length){
				var rows = [];
				if (name == 'treegrid'){
					var rr = {};
					$.map(data.rows, function(row){
						if (isMatch(row, row[opts.idField])){
							rr[row[opts.idField]] = row;
							row = getRow(data.rows, row._parentId);
							while(row){
								rr[row[opts.idField]] = row;
								row = getRow(data.rows, row._parentId);
							}
						}
					});
					for(var id in rr){
						rows.push(rr[id]);
					}
				} else {
					for(var i=0; i<data.rows.length; i++){
						var row = data.rows[i];
						if (isMatch(row, i)){
							rows.push(row);
						}
					}
				}
				data = {
					total: data.total - (data.rows.length - rows.length),
					rows: rows
				};
			}
			return data;
			
			function isMatch(row, index){
				var rules = opts.filterRules;
				if (!rules.length){return true;}
				for(var i=0; i<rules.length; i++){
					var rule = rules[i];

					// var source = row[rule.field];
					// var col = dg.datagrid('getColumnOption', rule.field);
					// if (col && col.formatter){
					// 	source = col.formatter(row[rule.field], row, index);
					// }
					
					var col = dg.datagrid('getColumnOption', rule.field);
					var formattedValue = (col && col.formatter) ? col.formatter(row[rule.field], row, index) : undefined;
					var source = opts.val.call(dg[0], row, rule.field, formattedValue);

					if (source == undefined){
						source = '';
					}
					var op = opts.operators[rule.op];
					var matched = op.isMatch(source, rule.value);
					if (opts.filterMatchingType == 'any'){
						if (matched){return true;}
					} else {
						if (!matched){return false;}
					}
				}
				return opts.filterMatchingType == 'all';
			}
			function getRow(rows, id){
				for(var i=0; i<rows.length; i++){
					var row = rows[i];
					if (row[opts.idField] == id){
						return row;
					}
				}
				return null;
			}
		},
		defaultFilterType: 'text',
		defaultFilterOperator: 'contains',
		defaultFilterOptions: {
			onInit: function(target){
				var name = getPluginName(target);
				var opts = $(target)[name]('options');
				var field = $(this).attr('name');
				var input = $(this);
				if (input.data('textbox')){
					input = input.textbox('textbox');
				}
				input.unbind('.filter').bind('keydown.filter', function(e){
					var t = $(this);
					if (this.timer){
						clearTimeout(this.timer);
					}
					if (e.keyCode == 13){
						_doFilter();
					} else {
						this.timer = setTimeout(function(){
							_doFilter();
						}, opts.filterDelay);
					}
				});
				function _doFilter(){
					var rule = $(target)[name]('getFilterRule', field);
					var value = input.val();
					if (value != ''){
						if ((rule && rule.value!=value) || !rule){
							$(target)[name]('addFilterRule', {
								field: field,
								op: opts.defaultFilterOperator,
								value: value
							});
							$(target)[name]('doFilter');
						}
					} else {
						if (rule){
							$(target)[name]('removeFilterRule', field);
							$(target)[name]('doFilter');
						}
					}
				}
			}
		},
		filterStringify: function(data){
			return JSON.stringify(data);
		},
		// the function to retrieve the field value of a row to match the filter rule
		val: function(row, field, formattedValue){
			return formattedValue || row[field];
		},
		onClickMenu: function(item,button){}
	};
	$.extend($.fn.datagrid.defaults, extendedOptions);
	$.extend($.fn.treegrid.defaults, extendedOptions);
	
	// filter types
	$.fn.datagrid.defaults.filters = $.extend({}, $.fn.datagrid.defaults.editors, {
		label: {
			init: function(container, options){
				return $('<span></span>').appendTo(container);
			},
			getValue: function(target){
				return $(target).html();
			},
			setValue: function(target, value){
				$(target).html(value);
			},
			resize: function(target, width){
				$(target)._outerWidth(width)._outerHeight(22);
			}
		}
	});
	$.fn.treegrid.defaults.filters = $.fn.datagrid.defaults.filters;
	
	// filter operators
	$.fn.datagrid.defaults.operators = {
		nofilter: {
			text: 'No Filter'
		},
		contains: {
			text: 'Contains',
			isMatch: function(source, value){
				source = String(source);
				value = String(value);
				return source.toLowerCase().indexOf(value.toLowerCase()) >= 0;
			}
		},
		equal: {
			text: 'Equal',
			isMatch: function(source, value){
				return source == value;
			}
		},
		notequal: {
			text: 'Not Equal',
			isMatch: function(source, value){
				return source != value;
			}
		},
		beginwith: {
			text: 'Begin With',
			isMatch: function(source, value){
				source = String(source);
				value = String(value);
				return source.toLowerCase().indexOf(value.toLowerCase()) == 0;
			}
		},
		endwith: {
			text: 'End With',
			isMatch: function(source, value){
				source = String(source);
				value = String(value);
				return source.toLowerCase().indexOf(value.toLowerCase(), source.length - value.length) !== -1;
			}
		},
		less: {
			text: 'Less',
			isMatch: function(source, value){
				return source < value;
			}
		},
		lessorequal: {
			text: 'Less Or Equal',
			isMatch: function(source, value){
				return source <= value;
			}
		},
		greater: {
			text: 'Greater',
			isMatch: function(source, value){
				return source > value;
			}
		},
		greaterorequal: {
			text: 'Greater Or Equal',
			isMatch: function(source, value){
				return source >= value;
			}
		}
	};
	$.fn.treegrid.defaults.operators = $.fn.datagrid.defaults.operators;
	
	function resizeFilter(target, field){
		var toFixColumnSize = false;
		var dg = $(target);
		var header = dg.datagrid('getPanel').find('div.datagrid-header');
		var tr = header.find('.datagrid-header-row:not(.datagrid-filter-row)');
		var ff = field ? header.find('.datagrid-filter[name="'+field+'"]') : header.find('.datagrid-filter');
		ff.each(function(){
			var name = $(this).attr('name');
			var col = dg.datagrid('getColumnOption', name);
			var cc = $(this).closest('div.datagrid-filter-c');
			var btn = cc.find('a.datagrid-filter-btn');
			var cell = tr.find('td[field="'+name+'"] .datagrid-cell');
			var cellWidth = cell._outerWidth();
			if (cellWidth != _getContentWidth(cc)){
				this.filter.resize(this, cellWidth - btn._outerWidth());
			}
			if (cc.width() > col.boxWidth+col.deltaWidth-1){
				col.boxWidth = cc.width() - col.deltaWidth + 1;
				col.width = col.boxWidth + col.deltaWidth;
				toFixColumnSize = true;
			}
		});
		if (toFixColumnSize){
			$(target).datagrid('fixColumnSize');			
		}

		function _getContentWidth(cc){
			var w = 0;
			$(cc).children(':visible').each(function(){
				w += $(this)._outerWidth();
			});
			return w;
		}
	}
	
	function getFilterComponent(target, field){
		var header = $(target).datagrid('getPanel').find('div.datagrid-header');
		return header.find('tr.datagrid-filter-row td[field="'+field+'"] .datagrid-filter');
	}
	
	/**
	 * get filter rule index, return -1 if not found.
	 */
	function getRuleIndex(target, field){
		var name = getPluginName(target);
		var rules = $(target)[name]('options').filterRules;
		for(var i=0; i<rules.length; i++){
			if (rules[i].field == field){
				return i;
			}
		}
		return -1;
	}

	function getFilterRule(target, field){
		var name = getPluginName(target);
		var rules = $(target)[name]('options').filterRules;
		var index = getRuleIndex(target, field);
		if (index >= 0){
			return rules[index];
		} else {
			return null;
		}
	}
	
	function addFilterRule(target, param){
		var name = getPluginName(target);
		var opts = $(target)[name]('options');
		var rules = opts.filterRules;

		if (param.op == 'nofilter'){
			removeFilterRule(target, param.field);
		} else {
			var index = getRuleIndex(target, param.field);
			if (index >= 0){
				$.extend(rules[index], param);
			} else {
				rules.push(param);
			}
		}

		var input = getFilterComponent(target, param.field);
		if (input.length){
			if (param.op != 'nofilter'){
				var value = input.val();
				if (input.data('textbox')){
					value = input.textbox('getText');
				}
				if (value != param.value){
					input[0].filter.setValue(input, param.value);					
				}
			}
			var menu = input[0].menu;
			if (menu){
				menu.find('.'+opts.filterMenuIconCls).removeClass(opts.filterMenuIconCls);
				var item = menu.menu('findItem', opts.operators[param.op]['text']);
				menu.menu('setIcon', {
					target: item.target,
					iconCls: opts.filterMenuIconCls
				});
			}
		}
	}
	
	function removeFilterRule(target, field){
		var name = getPluginName(target);
		var dg = $(target);
		var opts = dg[name]('options');
		if (field){
			var index = getRuleIndex(target, field);
			if (index >= 0){
				opts.filterRules.splice(index, 1);
			}
			_clear([field]);
		} else {
			opts.filterRules = [];
			var fields = dg.datagrid('getColumnFields',true).concat(dg.datagrid('getColumnFields'));
			_clear(fields);
		}
		
		function _clear(fields){
			for(var i=0; i<fields.length; i++){
				var input = getFilterComponent(target, fields[i]);
				if (input.length){
					input[0].filter.setValue(input, '');
					var menu = input[0].menu;
					if (menu){
						menu.find('.'+opts.filterMenuIconCls).removeClass(opts.filterMenuIconCls);
					}
				}
			}
		}
	}
	
	function doFilter(target){
		var name = getPluginName(target);
		var state = $.data(target, name);
		var opts = state.options;
		if (opts.remoteFilter){
			$(target)[name]('load');
		} else {
			if (opts.view.type == 'scrollview' && state.data.firstRows && state.data.firstRows.length){
				state.data.rows = state.data.firstRows;
			}
			$(target)[name]('getPager').pagination('refresh', {pageNumber:1});
			$(target)[name]('options').pageNumber = 1;
			$(target)[name]('loadData', state.filterSource || state.data);
		}
	}
	
	function translateTreeData(target, children, pid){
		var opts = $(target).treegrid('options');
		if (!children || !children.length){return []}
		var rows = [];
		$.map(children, function(item){
			item._parentId = pid;
			rows.push(item);
			rows = rows.concat(translateTreeData(target, item.children, item[opts.idField]));
		});
		$.map(rows, function(row){
			row.children = undefined;
		});
		return rows;
	}

	function myLoadFilter(data, parentId){
		var target = this;
		var name = getPluginName(target);
		var state = $.data(target, name);
		var opts = state.options;

		if (name == 'datagrid' && $.isArray(data)){
			data = {
				total: data.length,
				rows: data
			};
		} else if (name == 'treegrid' && $.isArray(data)){
			var rows = translateTreeData(target, data, parentId);
			data = {
				total: rows.length,
				rows: rows
			}
		}
		if (!opts.remoteFilter){
			if (!state.filterSource){
				state.filterSource = data;
			} else {
				if (!opts.isSorting) {
					if (name == 'datagrid'){
						state.filterSource = data;
					} else {
						state.filterSource.total += data.length;
						state.filterSource.rows = state.filterSource.rows.concat(data.rows);
						if (parentId){
							return opts.filterMatcher.call(target, data);
						}
					}
				} else {
					opts.isSorting = undefined;
				}
			}
			if (!opts.remoteSort && opts.sortName){
				var names = opts.sortName.split(',');
				var orders = opts.sortOrder.split(',');
				var dg = $(target);
				state.filterSource.rows.sort(function(r1,r2){
					var r = 0;
					for(var i=0; i<names.length; i++){
						var sn = names[i];
						var so = orders[i];
						var col = dg.datagrid('getColumnOption', sn);
						var sortFunc = col.sorter || function(a,b){
							return a==b ? 0 : (a>b?1:-1);
						};
						r = sortFunc(r1[sn], r2[sn]) * (so=='asc'?1:-1);
						if (r != 0){
							return r;
						}
					}
					return r;
				});
			}
			data = opts.filterMatcher.call(target, {
				total: state.filterSource.total,
				rows: state.filterSource.rows
			});

			if (opts.pagination){
				var dg = $(target);
				var pager = dg[name]('getPager');
				pager.pagination({
					onSelectPage:function(pageNum, pageSize){
	                    opts.pageNumber = pageNum;
	                    opts.pageSize = pageSize;
	                    pager.pagination('refresh',{
	                        pageNumber:pageNum,
	                        pageSize:pageSize
	                    });
	                    //dg.datagrid('loadData', state.filterSource);
	                    dg[name]('loadData', state.filterSource);
					},
					onBeforeRefresh:function(){
						dg[name]('reload');
						return false;
					}
				});
				if (name == 'datagrid'){
					var pd = getPageData(data.rows);
					opts.pageNumber = pd.pageNumber;
					data.rows = pd.rows;
				} else {
			        var topRows = [];
			        var childRows = [];
			        $.map(data.rows, function(row){
			        	row._parentId ? childRows.push(row) : topRows.push(row);
			        });
			        data.total = topRows.length;
			        var pd = getPageData(topRows);
			        opts.pageNumber = pd.pageNumber;
			        data.rows = pd.rows.concat(childRows);
				}
			}
			$.map(data.rows, function(row){
				row.children = undefined;
			});
		}
		return data;

		function getPageData(dataRows){
			var rows = [];
			var page = opts.pageNumber;
			while(page > 0){
				var start = (page-1)*parseInt(opts.pageSize);
				var end = start + parseInt(opts.pageSize);
				rows = dataRows.slice(start, end);
				if (rows.length){
					break;
				}
				page--;
			}
			return {
				pageNumber: page>0?page:1,
				rows: rows
			};
		}
	}
	
	function init(target, filters){
		filters = filters || [];
		var name = getPluginName(target);
		var state = $.data(target, name);
		var opts = state.options;
		if (!opts.filterRules.length){
			opts.filterRules = [];
		}
		opts.filterCache = opts.filterCache || {};
		var dgOpts = $.data(target, 'datagrid').options;
		
		var onResize = dgOpts.onResize;
		dgOpts.onResize = function(width,height){
			resizeFilter(target);
			onResize.call(this, width, height);
		}
		var onBeforeSortColumn = dgOpts.onBeforeSortColumn;
		dgOpts.onBeforeSortColumn = function(sort, order){
			var result = onBeforeSortColumn.call(this, sort, order);
			if (result != false){
				opts.isSorting = true;				
			}
			return result;
		};

		var onResizeColumn = opts.onResizeColumn;
		opts.onResizeColumn = function(field,width){
			var fc = $(this).datagrid('getPanel').find('.datagrid-header .datagrid-filter-c');
			var focusOne = fc.find('.datagrid-filter:focus');
			fc.hide();
			$(target).datagrid('fitColumns');
			if (opts.fitColumns){
				resizeFilter(target);
			} else {
				resizeFilter(target, field);
			}
			fc.show();
			focusOne.blur().focus();
			onResizeColumn.call(target, field, width);
		};
		var onBeforeLoad = opts.onBeforeLoad;
		opts.onBeforeLoad = function(param1, param2){
			if (param1){
				param1.filterRules = opts.filterStringify(opts.filterRules);
			}
			if (param2){
				param2.filterRules = opts.filterStringify(opts.filterRules);
			}
			var result = onBeforeLoad.call(this, param1, param2);
			if (result != false && opts.url){
				if (name == 'datagrid'){
					state.filterSource = null;
				} else if (name == 'treegrid' && state.filterSource){
					if (param1){
						var id = param1[opts.idField];	// the id of the expanding row
						var rows = state.filterSource.rows || [];
						for(var i=0; i<rows.length; i++){
							if (id == rows[i]._parentId){	// the expanding row has children
								return false;
							}
						}
					} else {
						state.filterSource = null;
					}
				}
			}
			return result;
		};

		// opts.loadFilter = myLoadFilter;
		opts.loadFilter = function(data, parentId){
			var d = opts.oldLoadFilter.call(this, data, parentId);
			return myLoadFilter.call(this, d, parentId);
		};
		
		initCss();
		createFilter(true);
		createFilter();
		if (opts.fitColumns){
			setTimeout(function(){
				resizeFilter(target);
			}, 0);
		}

		$.map(opts.filterRules, function(rule){
			addFilterRule(target, rule);
		});
		
		function initCss(){
			if (!$('#datagrid-filter-style').length){
				$('head').append(
					'<style id="datagrid-filter-style">' +
					'a.datagrid-filter-btn{display:inline-block;width:22px;height:22px;margin:0;vertical-align:top;cursor:pointer;opacity:0.6;filter:alpha(opacity=60);}' +
					'a:hover.datagrid-filter-btn{opacity:1;filter:alpha(opacity=100);}' +
					'.datagrid-filter-row .textbox,.datagrid-filter-row .textbox .textbox-text{-moz-border-radius:0;-webkit-border-radius:0;border-radius:0;}' +
					'.datagrid-filter-row input{margin:0;-moz-border-radius:0;-webkit-border-radius:0;border-radius:0;}' +
					'.datagrid-filter-c{overflow:hidden}' +
					'.datagrid-filter-cache{position:absolute;width:10px;height:10px;left:-99999px;}' +
					'</style>'
				);
			}
		}
		
		/**
		 * create filter component
		 */
		function createFilter(frozen){
			var dc = state.dc;
			var fields = $(target).datagrid('getColumnFields', frozen);
			if (frozen && opts.rownumbers){
				fields.unshift('_');
			}
			var table = (frozen?dc.header1:dc.header2).find('table.datagrid-htable');
			
			// clear the old filter component
			table.find('.datagrid-filter').each(function(){
				if (this.filter.destroy){
					this.filter.destroy(this);
				}
				if (this.menu){
					$(this.menu).menu('destroy');
				}
			});
			table.find('tr.datagrid-filter-row').remove();
			
			var tr = $('<tr class="datagrid-header-row datagrid-filter-row"></tr>');
			if (opts.filterPosition == 'bottom'){
				tr.appendTo(table.find('tbody'));
			} else {
				tr.prependTo(table.find('tbody'));
			}
			if (!opts.showFilterBar){
				tr.hide();
			}
			
			for(var i=0; i<fields.length; i++){
				var field = fields[i];
				var col = $(target).datagrid('getColumnOption', field);
				var td = $('<td></td>').attr('field', field).appendTo(tr);
				if (col && col.hidden){
					td.hide();
				}
				if (field == '_'){
					continue;
				}
				if (col && (col.checkbox || col.expander)){
					continue;
				}

				var fopts = getFilter(field);
				if (fopts){
					$(target)[name]('destroyFilter', field);	// destroy the old filter component
				} else {
					fopts = $.extend({}, {
						field: field,
						type: opts.defaultFilterType,
						options: opts.defaultFilterOptions
					});
				}

				var div = opts.filterCache[field];
				if (!div){
					div = $('<div class="datagrid-filter-c"></div>').appendTo(td);
					var filter = opts.filters[fopts.type];
					var input = filter.init(div, $.extend({height:24},fopts.options||{}));
					input.addClass('datagrid-filter').attr('name', field);
					input[0].filter = filter;
					input[0].menu = createFilterButton(div, fopts.op);
					if (fopts.options){
						if (fopts.options.onInit){
							fopts.options.onInit.call(input[0], target);
						}
					} else {
						opts.defaultFilterOptions.onInit.call(input[0], target);
					}
					opts.filterCache[field] = div;
					resizeFilter(target, field);
				} else {
					div.appendTo(td);
				}
			}
		}
		
		function createFilterButton(container, operators){
			if (!operators){return null;}
			
			var btn = $('<a class="datagrid-filter-btn">&nbsp;</a>').addClass(opts.filterBtnIconCls);
			if (opts.filterBtnPosition == 'right'){
				btn.appendTo(container);
			} else {
				btn.prependTo(container);
			}

			var menu = $('<div></div>').appendTo('body');
			$.map(['nofilter'].concat(operators), function(item){
				var op = opts.operators[item];
				if (op){
					$('<div></div>').attr('name', item).html(op.text).appendTo(menu);
				}
			});
			menu.menu({
				alignTo:btn,
				onClick:function(item){
					var btn = $(this).menu('options').alignTo;
					var td = btn.closest('td[field]');
					var field = td.attr('field');
					var input = td.find('.datagrid-filter');
					var value = input[0].filter.getValue(input);
					
					if (opts.onClickMenu.call(target, item, btn, field) == false){
						return;
					}
					
					addFilterRule(target, {
						field: field,
						op: item.name,
						value: value
					});
					
					doFilter(target);
				}
			});

			btn[0].menu = menu;
			btn.bind('click', {menu:menu}, function(e){
				$(this.menu).menu('show');
				return false;
			});
			return menu;
		}
		
		function getFilter(field){
			for(var i=0; i<filters.length; i++){
				var filter = filters[i];
				if (filter.field == field){
					return filter;
				}
			}
			return null;
		}
	}
	
	$.extend($.fn.datagrid.methods, {
		enableFilter: function(jq, filters){
			return jq.each(function(){
				var name = getPluginName(this);
				var opts = $.data(this, name).options;
				if (opts.oldLoadFilter){
					if (filters){
						$(this)[name]('disableFilter');
					} else {
						return;
					}
				}
				opts.oldLoadFilter = opts.loadFilter;
				init(this, filters);
				$(this)[name]('resize');
				if (opts.filterRules.length){
					if (opts.remoteFilter){
						doFilter(this);
					} else if (opts.data){
						doFilter(this);
					}
				}
			});
		},
		disableFilter: function(jq){
			return jq.each(function(){
				var name = getPluginName(this);
				var state = $.data(this, name);
				var opts = state.options;
				if (!opts.oldLoadFilter){
					return;
				}
				var dc = $(this).data('datagrid').dc;
				var div = dc.view.children('.datagrid-filter-cache');
				if (!div.length){
					div = $('<div class="datagrid-filter-cache"></div>').appendTo(dc.view);
				}
				for(var field in opts.filterCache){
					$(opts.filterCache[field]).appendTo(div);
				}
				var data = state.data;
				if (state.filterSource){
					data = state.filterSource;
					$.map(data.rows, function(row){
						row.children = undefined;
					});
				}
				dc.header1.add(dc.header2).find('tr.datagrid-filter-row').remove();
				opts.loadFilter = opts.oldLoadFilter || undefined;
				opts.oldLoadFilter = null;
				$(this)[name]('resize');
				$(this)[name]('loadData', data);

				// $(this)[name]({
				// 	data: data,
				// 	loadFilter: (opts.oldLoadFilter||undefined),
				// 	oldLoadFilter: null
				// });
			});
		},
		destroyFilter: function(jq, field){
			return jq.each(function(){
				var name = getPluginName(this);
				var state = $.data(this, name);
				var opts = state.options;
				if (field){
					_destroy(field);
				} else {
					for(var f in opts.filterCache){
						_destroy(f);
					}
					$(this).datagrid('getPanel').find('.datagrid-header .datagrid-filter-row').remove();
					$(this).data('datagrid').dc.view.children('.datagrid-filter-cache').remove();
					opts.filterCache = {};
					$(this)[name]('resize');
					$(this)[name]('disableFilter');
				}

				function _destroy(field){
					var c = $(opts.filterCache[field]);
					var input = c.find('.datagrid-filter');
					if (input.length){
						var filter = input[0].filter;
						if (filter.destroy){
							filter.destroy(input[0]);
						}
					}
					c.find('.datagrid-filter-btn').each(function(){
						$(this.menu).menu('destroy');
					});
					c.remove();
					opts.filterCache[field] = undefined;
				}
			});
		},
		getFilterRule: function(jq, field){
			return getFilterRule(jq[0], field);
		},
		addFilterRule: function(jq, param){
			return jq.each(function(){
				addFilterRule(this, param);
			});
		},
		removeFilterRule: function(jq, field){
			return jq.each(function(){
				removeFilterRule(this, field);
			});
		},
		doFilter: function(jq){
			return jq.each(function(){
				doFilter(this);
			});
		},
		getFilterComponent: function(jq, field){
			return getFilterComponent(jq[0], field);
		},
		resizeFilter: function(jq, field){
			return jq.each(function(){
				resizeFilter(this, field);
			});
		}
	});
})(jQuery);


$.extend($.fn.datagrid.defaults, {
	groupHeight: 25,
	expanderWidth: 30,
	groupStyler: function(value,rows){return ''}
});

var groupview = $.extend({}, $.fn.datagrid.defaults.view, {
	render: function(target, container, frozen){
		var table = [];
		var groups = this.groups;
		for(var i=0; i<groups.length; i++){
			table.push(this.renderGroup.call(this, target, i, groups[i], frozen));
		}
		$(container).html(table.join(''));
	},
	
	renderGroup: function(target, groupIndex, group, frozen){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var fields = $(target).datagrid('getColumnFields', frozen);
		var hasFrozen = opts.frozenColumns && opts.frozenColumns.length;

		if (frozen){
			if (!(opts.rownumbers || hasFrozen)){
				return '';
			}
		}
		
		var table = [];

		var css = opts.groupStyler.call(target, group.value, group.rows);
		var cs = parseCss(css, 'datagrid-group');
		table.push('<div group-index=' + groupIndex + ' ' + cs + '>');
		if ((frozen && (opts.rownumbers || opts.frozenColumns.length)) ||
				(!frozen && !(opts.rownumbers || opts.frozenColumns.length))){
			table.push('<span class="datagrid-group-expander">');
			table.push('<span class="datagrid-row-expander datagrid-row-collapse">&nbsp;</span>');
			table.push('</span>');
		}
		if ((frozen && hasFrozen) || (!frozen)){
			table.push('<span class="datagrid-group-title">');
			table.push(opts.groupFormatter.call(target, group.value, group.rows));
			table.push('</span>');
		}
		table.push('</div>');
		
		table.push('<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>');
		var index = group.startIndex;
		for(var j=0; j<group.rows.length; j++) {
			var css = opts.rowStyler ? opts.rowStyler.call(target, index, group.rows[j]) : '';
			var classValue = '';
			var styleValue = '';
			if (typeof css == 'string'){
				styleValue = css;
			} else if (css){
				classValue = css['class'] || '';
				styleValue = css['style'] || '';
			}
			
			var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
			var style = styleValue ? 'style="' + styleValue + '"' : '';
			var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
			table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
			table.push(this.renderRow.call(this, target, fields, frozen, index, group.rows[j]));
			table.push('</tr>');
			index++;
		}
		table.push('</tbody></table>');
		return table.join('');

		function parseCss(css, cls){
			var classValue = '';
			var styleValue = '';
			if (typeof css == 'string'){
				styleValue = css;
			} else if (css){
				classValue = css['class'] || '';
				styleValue = css['style'] || '';
			}
			return 'class="' + cls + (classValue ? ' '+classValue : '') + '" ' +
					'style="' + styleValue + '"';
		}
	},
	
	bindEvents: function(target){
		var state = $.data(target, 'datagrid');
		var dc = state.dc;
		var body = dc.body1.add(dc.body2);
		var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
		body.unbind('click').bind('click', function(e){
			var tt = $(e.target);
			var expander = tt.closest('span.datagrid-row-expander');
			if (expander.length){
				var gindex = expander.closest('div.datagrid-group').attr('group-index');
				if (expander.hasClass('datagrid-row-collapse')){
					$(target).datagrid('collapseGroup', gindex);
				} else {
					$(target).datagrid('expandGroup', gindex);
				}
			} else {
				clickHandler(e);
			}
			e.stopPropagation();
		});
	},
	
	onBeforeRender: function(target, rows){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		
		initCss();
		
		var groups = [];
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			var group = getGroup(row[opts.groupField]);
			if (!group){
				group = {
					value: row[opts.groupField],
					rows: [row]
				};
				groups.push(group);
			} else {
				group.rows.push(row);
			}
		}
		
		var index = 0;
		var newRows = [];
		for(var i=0; i<groups.length; i++){
			var group = groups[i];
			group.startIndex = index;
			index += group.rows.length;
			newRows = newRows.concat(group.rows);
		}
		
		state.data.rows = newRows;
		this.groups = groups;
		
		var that = this;
		setTimeout(function(){
			that.bindEvents(target);
		},0);
		
		function getGroup(value){
			for(var i=0; i<groups.length; i++){
				var group = groups[i];
				if (group.value == value){
					return group;
				}
			}
			return null;
		}
		function initCss(){
			if (!$('#datagrid-group-style').length){
				$('head').append(
					'<style id="datagrid-group-style">' +
					'.datagrid-group{height:'+opts.groupHeight+'px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;white-space:nowrap;word-break:normal;}' +
					'.datagrid-group-title,.datagrid-group-expander{display:inline-block;vertical-align:bottom;height:100%;line-height:'+opts.groupHeight+'px;padding:0 4px;}' +
					'.datagrid-group-title{position:relative;}' +
					'.datagrid-group-expander{width:'+opts.expanderWidth+'px;text-align:center;padding:0}' +
					'.datagrid-row-expander{margin:'+Math.floor((opts.groupHeight-16)/2)+'px 0;display:inline-block;width:16px;height:16px;cursor:pointer}' +
					'</style>'
				);
			}
		}
	},
	onAfterRender: function(target){
		$.fn.datagrid.defaults.view.onAfterRender.call(this, target);

		var view = this;
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		if (!state.onResizeColumn){
			state.onResizeColumn = opts.onResizeColumn;
		}
		if (!state.onResize){
			state.onResize = opts.onResize;
		}
		opts.onResizeColumn = function(field, width){
			view.resizeGroup(target);
			state.onResizeColumn.call(target, field, width);
		}
		opts.onResize = function(width, height){
			view.resizeGroup(target);		
			state.onResize.call($(target).datagrid('getPanel')[0], width, height);
		}
		view.resizeGroup(target);
	}
});

$.extend($.fn.datagrid.methods, {
	groups:function(jq){
		return jq.datagrid('options').view.groups;
	},
    expandGroup:function(jq, groupIndex){
        return jq.each(function(){
            var view = $.data(this, 'datagrid').dc.view;
            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
            var expander = group.find('span.datagrid-row-expander');
            if (expander.hasClass('datagrid-row-expand')){
                expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
                group.next('table').show();
            }
            $(this).datagrid('fixRowHeight');
        });
    },
    collapseGroup:function(jq, groupIndex){
        return jq.each(function(){
            var view = $.data(this, 'datagrid').dc.view;
            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
            var expander = group.find('span.datagrid-row-expander');
            if (expander.hasClass('datagrid-row-collapse')){
                expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
                group.next('table').hide();
            }
            $(this).datagrid('fixRowHeight');
        });
    },
    scrollToGroup: function(jq, groupIndex){
    	return jq.each(function(){
			var state = $.data(this, 'datagrid');
			var dc = state.dc;
			var grow = dc.body2.children('div.datagrid-group[group-index="'+groupIndex+'"]');
			if (grow.length){
				var groupHeight = grow.outerHeight();
				var headerHeight = dc.view2.children('div.datagrid-header')._outerHeight();
				var frozenHeight = dc.body2.outerHeight(true) - dc.body2.outerHeight();
				var top = grow.position().top - headerHeight - frozenHeight;
				if (top < 0){
					dc.body2.scrollTop(dc.body2.scrollTop() + top);
				} else if (top + groupHeight > dc.body2.height() - 18){
					dc.body2.scrollTop(dc.body2.scrollTop() + top + groupHeight - dc.body2.height() + 18);
				}
			}
    	});
    }
});

$.extend(groupview, {
	refreshGroupTitle: function(target, groupIndex){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var group = this.groups[groupIndex];
		var span = dc.body1.add(dc.body2).children('div.datagrid-group[group-index=' + groupIndex + ']').find('span.datagrid-group-title');
		span.html(opts.groupFormatter.call(target, group.value, group.rows));
	},
	resizeGroup: function(target, groupIndex){
		var state = $.data(target, 'datagrid');
		var dc = state.dc;
		var ht = dc.header2.find('table');
		var fr = ht.find('tr.datagrid-filter-row').hide();
		var ww = ht.width();
		if (groupIndex == undefined){
			var groupHeader = dc.body2.children('div.datagrid-group');
		} else {
			var groupHeader = dc.body2.children('div.datagrid-group[group-index=' + groupIndex + ']');
		}
		groupHeader._outerWidth(ww);
		var opts = state.options;
		if (opts.frozenColumns && opts.frozenColumns.length){
			var width = dc.view1.width() - opts.expanderWidth;
			var isRtl = dc.view1.css('direction').toLowerCase()=='rtl';
			groupHeader.find('.datagrid-group-title').css(isRtl?'right':'left', -width+'px');
		}
		fr.show();
	},

	insertRow: function(target, index, row){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var group = null;
		var groupIndex;
		
		if (!state.data.rows.length){
			$(target).datagrid('loadData', [row]);
			return;
		}
		
		for(var i=0; i<this.groups.length; i++){
			if (this.groups[i].value == row[opts.groupField]){
				group = this.groups[i];
				groupIndex = i;
				break;
			}
		}
		if (group){
			if (index == undefined || index == null){
				index = state.data.rows.length;
			}
			if (index < group.startIndex){
				index = group.startIndex;
			} else if (index > group.startIndex + group.rows.length){
				index = group.startIndex + group.rows.length;
			}
			$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);
			
			if (index >= group.startIndex + group.rows.length){
				_moveTr(index, true);
				_moveTr(index, false);
			}
			group.rows.splice(index - group.startIndex, 0, row);
		} else {
			group = {
				value: row[opts.groupField],
				rows: [row],
				startIndex: state.data.rows.length
			}
			groupIndex = this.groups.length;
			dc.body1.append(this.renderGroup.call(this, target, groupIndex, group, true));
			dc.body2.append(this.renderGroup.call(this, target, groupIndex, group, false));
			this.groups.push(group);
			state.data.rows.push(row);
		}

		this.setGroupIndex(target);
		this.refreshGroupTitle(target, groupIndex);
		this.resizeGroup(target);
		
		function _moveTr(index,frozen){
			var serno = frozen?1:2;
			var prevTr = opts.finder.getTr(target, index-1, 'body', serno);
			var tr = opts.finder.getTr(target, index, 'body', serno);
			tr.insertAfter(prevTr);
		}
	},
	
	updateRow: function(target, index, row){
		var opts = $.data(target, 'datagrid').options;
		$.fn.datagrid.defaults.view.updateRow.call(this, target, index, row);
		var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
		var groupIndex = parseInt(tb.prev().attr('group-index'));
		this.refreshGroupTitle(target, groupIndex);
	},
	
	deleteRow: function(target, index){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var body = dc.body1.add(dc.body2);
		
		var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
		var groupIndex = parseInt(tb.prev().attr('group-index'));
		
		$.fn.datagrid.defaults.view.deleteRow.call(this, target, index);
		
		var group = this.groups[groupIndex];
		if (group.rows.length > 1){
			group.rows.splice(index-group.startIndex, 1);
			this.refreshGroupTitle(target, groupIndex);
		} else {
			body.children('div.datagrid-group[group-index='+groupIndex+']').remove();
			for(var i=groupIndex+1; i<this.groups.length; i++){
				body.children('div.datagrid-group[group-index='+i+']').attr('group-index', i-1);
			}
			this.groups.splice(groupIndex, 1);
		}
		
		this.setGroupIndex(target);
	},

	setGroupIndex: function(target){
		var index = 0;
		for(var i=0; i<this.groups.length; i++){
			var group = this.groups[i];
			group.startIndex = index;
			index += group.rows.length;
		}
	}
});

$.extend($.fn.datagrid.defaults, {
	rowHeight: 25,
	maxDivHeight: 10000000,
	maxVisibleHeight: 15000000,
	deltaTopHeight: 0,
	onBeforeFetch: function(page){},
	onFetch: function(page, rows){},
	loader: function(param, success, error){
		var opts = $(this).datagrid('options');
		if (!opts.url) return false;
		if (opts.view.type == 'scrollview'){
			param.page = param.page || 1;
			param.rows = param.rows || opts.pageSize;
		}
		$.ajax({
			type: opts.method,
			url: opts.url,
			data: param,
			dataType: 'json',
			success: function(data){
				success(data);
			},
			error: function(){
				error.apply(this, arguments);
			}
		});
	}
});
$.extend($.fn.datagrid.defaults.finder, {
	getRow: function(target, p){	// p can be row index or tr object
		var index = (typeof p == 'object') ? p.attr('datagrid-row-index') : p;
		var opts = $(target).datagrid('options');
		if (opts.view.type == 'scrollview'){
			index -= opts.view.index;
		}
		return $.data(target, 'datagrid').data.rows[index];
	}
});

var scrollview = $.extend({}, $.fn.datagrid.defaults.view, {
	type: 'scrollview',
	index: 0,
	r1: [],
	r2: [],
	rows: [],
	render: function(target, container, frozen){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var rows = this.rows || [];
		if (!rows.length) {
			return;
		}
		var fields = $(target).datagrid('getColumnFields', frozen);
		
		if (frozen){
			if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))){
				return;
			}
		}
		
		var index = this.index;
		var table = ['<div class="datagrid-btable-top"></div>',
		             '<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>'];
		for(var i=0; i<rows.length; i++) {
			var css = opts.rowStyler ? opts.rowStyler.call(target, index, rows[i]) : '';
			var classValue = '';
			var styleValue = '';
			if (typeof css == 'string'){
				styleValue = css;
			} else if (css){
				classValue = css['class'] || '';
				styleValue = css['style'] || '';
			}
			var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
			var style = styleValue ? 'style="' + styleValue + '"' : '';
			var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
			table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
			table.push(this.renderRow.call(this, target, fields, frozen, index, rows[i]));
			table.push('</tr>');

			// render the detail row
			if (opts.detailFormatter){
				table.push('<tr style="display:none;">');
				if (frozen){
					table.push('<td colspan=' + (fields.length+(opts.rownumbers?1:0)) + ' style="border-right:0">');
				} else {
					table.push('<td colspan=' + (fields.length) + '>');
				}
				table.push('<div class="datagrid-row-detail">');
				if (frozen){
					table.push('&nbsp;');
				} else {
					table.push(opts.detailFormatter.call(target, index, rows[i]));
				}
				table.push('</div>');
				table.push('</td>');
				table.push('</tr>');
			}

			index++;
		}
		table.push('</tbody></table>');
		table.push('<div class="datagrid-btable-bottom"></div>');
		
		$(container).html(table.join(''));
	},
	
	renderRow: function(target, fields, frozen, rowIndex, rowData){
		var opts = $.data(target, 'datagrid').options;
		
		var cc = [];
		if (frozen && opts.rownumbers){
			var rownumber = rowIndex + 1;
			// if (opts.pagination){
			// 	rownumber += (opts.pageNumber-1)*opts.pageSize;
			// }
			cc.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber">'+rownumber+'</div></td>');
		}
		for(var i=0; i<fields.length; i++){
			var field = fields[i];
			var col = $(target).datagrid('getColumnOption', field);
			if (col){
				var value = rowData[field];	// the field value
				var css = col.styler ? (col.styler(value, rowData, rowIndex)||'') : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (cc){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				var cls = classValue ? 'class="' + classValue + '"' : '';
				var style = col.hidden ? 'style="display:none;' + styleValue + '"' : (styleValue ? 'style="' + styleValue + '"' : '');
				
				cc.push('<td field="' + field + '" ' + cls + ' ' + style + '>');
				
				if (col.checkbox){
					style = '';
				} else if (col.expander){
					style = "text-align:center;height:16px;";
				} else {
					style = styleValue;
					if (col.align){style += ';text-align:' + col.align + ';'}
					if (!opts.nowrap){
						style += ';white-space:normal;height:auto;';
					} else if (opts.autoRowHeight){
						style += ';height:auto;';
					}
				}
				
				cc.push('<div style="' + style + '" ');
				if (col.checkbox){
					cc.push('class="datagrid-cell-check ');
				} else {
					cc.push('class="datagrid-cell ' + col.cellClass);
				}
				cc.push('">');
				
				if (col.checkbox){
					cc.push('<input type="checkbox" name="' + field + '" value="' + (value!=undefined ? value : '') + '">');
				} else if (col.expander) {
					//cc.push('<div style="text-align:center;width:16px;height:16px;">');
					cc.push('<span class="datagrid-row-expander datagrid-row-expand" style="display:inline-block;width:16px;height:16px;cursor:pointer;" />');
					//cc.push('</div>');
				} else if (col.formatter){
					cc.push(col.formatter(value, rowData, rowIndex));
				} else {
					cc.push(value);
				}
				
				cc.push('</div>');
				cc.push('</td>');
			}
		}
		return cc.join('');
	},
	
	bindEvents: function(target){
		var state = $.data(target, 'datagrid');
		var dc = state.dc;
		var opts = state.options;
		var body = dc.body1.add(dc.body2);
		var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
		body.unbind('click').bind('click', function(e){
			var tt = $(e.target);
			var tr = tt.closest('tr.datagrid-row');
			if (!tr.length){return}
			if (tt.hasClass('datagrid-row-expander')){
				var rowIndex = parseInt(tr.attr('datagrid-row-index'));
				if (tt.hasClass('datagrid-row-expand')){
					$(target).datagrid('expandRow', rowIndex);
				} else {
					$(target).datagrid('collapseRow', rowIndex);
				}
				$(target).datagrid('fixRowHeight');
				
			} else {
				clickHandler(e);
			}
			e.stopPropagation();
		});
	},
	
	onBeforeRender: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var view = this;

		state.data.firstRows = state.data.rows;
		state.data.rows = [];

		dc.body1.add(dc.body2).empty();
		this.rows = [];	// the rows to be rendered
		this.r1 = this.r2 = [];	// the first part and last part of rows
		
		init();
		createHeaderExpander();
		
		function init(){
			var pager = $(target).datagrid('getPager');
			pager.pagination({
				onSelectPage: function(pageNum, pageSize){
					opts.pageNumber = pageNum || 1;
					opts.pageSize = pageSize;
					pager.pagination('refresh',{
						pageNumber:pageNum,
						pageSize:pageSize
					});
					$(target).datagrid('gotoPage', opts.pageNumber);
				}
			});
			// erase the onLoadSuccess event, make sure it can't be triggered
			state.onLoadSuccess = opts.onLoadSuccess;
			opts.onLoadSuccess = function(){};
			if (!opts.remoteSort){
				var onBeforeSortColumn = opts.onBeforeSortColumn;
				opts.onBeforeSortColumn = function(name, order){
					var result = onBeforeSortColumn.call(this, name, order);
					if (result == false){
						return false;
					}
					state.data.rows = state.data.firstRows;
				}
			}
			dc.body2.unbind('.datagrid');
			setTimeout(function(){
				dc.body2.unbind('.datagrid').bind('scroll.datagrid', function(e){
					if (state.onLoadSuccess){
						opts.onLoadSuccess = state.onLoadSuccess;	// restore the onLoadSuccess event
						state.onLoadSuccess = undefined;
					}
					if (view.scrollTimer){
						clearTimeout(view.scrollTimer);
					}
					view.scrollTimer = setTimeout(function(){
						view.scrolling.call(view, target);
					}, 50);
				});
				dc.body2.triggerHandler('scroll.datagrid');
			}, 0);
		}
		function createHeaderExpander(){
			if (!opts.detailFormatter){return}
			
			var t = $(target);
			var hasExpander = false;
			var fields = t.datagrid('getColumnFields',true).concat(t.datagrid('getColumnFields'));
			for(var i=0; i<fields.length; i++){
				var col = t.datagrid('getColumnOption', fields[i]);
				if (col.expander){
					hasExpander = true;
					break;
				}
			}
			if (!hasExpander){
				if (opts.frozenColumns && opts.frozenColumns.length){
					opts.frozenColumns[0].splice(0,0,{field:'_expander',expander:true,width:24,resizable:false,fixed:true});
				} else {
					opts.frozenColumns = [[{field:'_expander',expander:true,width:24,resizable:false,fixed:true}]];
				}
				
				var t = dc.view1.children('div.datagrid-header').find('table');
				var td = $('<td rowspan="'+opts.frozenColumns.length+'"><div class="datagrid-header-expander" style="width:24px;"></div></td>');
				if ($('tr',t).length == 0){
					td.wrap('<tr></tr>').parent().appendTo($('tbody',t));
				} else if (opts.rownumbers){
					td.insertAfter(t.find('td:has(div.datagrid-header-rownumber)'));
				} else {
					td.prependTo(t.find('tr:first'));
				}
			}
			
			setTimeout(function(){
				view.bindEvents(target);
			},0);
		}
	},
	
	onAfterRender: function(target){
		$.fn.datagrid.defaults.view.onAfterRender.call(this, target);
		var dc = $.data(target, 'datagrid').dc;
		var footer = dc.footer1.add(dc.footer2);
		footer.find('span.datagrid-row-expander').css('visibility', 'hidden');
	},

	scrolling: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		if (!opts.finder.getRows(target).length){
			this.reload.call(this, target);
		} else {
			if (!dc.body2.is(':visible')){return}
			var headerHeight = dc.view2.children('div.datagrid-header').outerHeight();
			
			var topDiv = dc.body2.children('div.datagrid-btable-top');
			var bottomDiv = dc.body2.children('div.datagrid-btable-bottom');
			if (!topDiv.length || !bottomDiv.length){return;}
			var top = topDiv.position().top + topDiv._outerHeight() - headerHeight;
			var bottom = bottomDiv.position().top - headerHeight;
			top = Math.floor(top);
			bottom = Math.floor(bottom);

			if (top > dc.body2.height() || bottom < 0){
				this.reload.call(this, target);
			} else if (top > 0){
				var page = Math.floor(this.index/opts.pageSize);
				this.getRows.call(this, target, page, function(rows){
					this.page = page;
					this.r2 = this.r1;
					this.r1 = rows;
					this.index = (page-1)*opts.pageSize;
					this.rows = this.r1.concat(this.r2);
					this.populate.call(this, target);
				});
			} else if (bottom < dc.body2.height()){
				if (state.data.rows.length+this.index >= state.data.total){
					return;
				}
				var page = Math.floor(this.index/opts.pageSize)+2;
				if (this.r2.length){
					page++;
				}
				this.getRows.call(this, target, page, function(rows){
					this.page = page;
					if (!this.r2.length){
						this.r2 = rows;
					} else {
						this.r1 = this.r2;
						this.r2 = rows;
						this.index += opts.pageSize;
					}
					this.rows = this.r1.concat(this.r2);
					this.populate.call(this, target);
				});
			}
		}
	},
	reload: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var top = $(dc.body2).scrollTop() + opts.deltaTopHeight;
		var index = Math.floor(top/opts.rowHeight);
		var page = Math.floor(index/opts.pageSize) + 1;
		
		this.getRows.call(this, target, page, function(rows){
			this.page = page;
			this.index = (page-1)*opts.pageSize;
			this.rows = rows;
			this.r1 = rows;
			this.r2 = [];
			this.populate.call(this, target);
			dc.body2.triggerHandler('scroll.datagrid');
		});
	},
	
	getRows: function(target, page, callback){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var index = (page-1)*opts.pageSize;

		if (index < 0){return}
		if (opts.onBeforeFetch.call(target, page) == false){return;}

		var rows = state.data.firstRows.slice(index, index+opts.pageSize);
		if (rows.length && (rows.length==opts.pageSize || index+rows.length==state.data.total)){
			opts.onFetch.call(target, page, rows);
			callback.call(this, rows);
		} else {
			var param = $.extend({}, opts.queryParams, {
				page: page,
				rows: opts.pageSize
			});
			if (opts.sortName){
				$.extend(param, {
					sort: opts.sortName,
					order: opts.sortOrder
				});
			}
			if (opts.onBeforeLoad.call(target, param) == false) return;
			
			$(target).datagrid('loading');
			var result = opts.loader.call(target, param, function(data){
				$(target).datagrid('loaded');
				var data = opts.loadFilter.call(target, data);
				opts.onFetch.call(target, page, data.rows);
				if (data.rows && data.rows.length){
					callback.call(opts.view, data.rows);
				} else {
					opts.onLoadSuccess.call(target, data);
				}
			}, function(){
				$(target).datagrid('loaded');
				opts.onLoadError.apply(target, arguments);
			});
			if (result == false){
				$(target).datagrid('loaded');
				if (!state.data.firstRows.length){
					opts.onFetch.call(target, page, state.data.firstRows);
					opts.onLoadSuccess.call(target, state.data);
				}
			}
		}
	},
	
	populate: function(target){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var dc = state.dc;
		var rowHeight = opts.rowHeight;
		var maxHeight = opts.maxDivHeight;

		if (this.rows.length){
			opts.view.render.call(opts.view, target, dc.body2, false);
			opts.view.render.call(opts.view, target, dc.body1, true);
			
			var body = dc.body1.add(dc.body2);
			var topDiv = body.children('div.datagrid-btable-top');
			var bottomDiv = body.children('div.datagrid-btable-bottom');
			var topHeight = this.index * rowHeight;
			var bottomHeight = state.data.total*rowHeight - this.rows.length*rowHeight - topHeight;
			fillHeight(topDiv, topHeight);
			fillHeight(bottomDiv, bottomHeight);

			state.data.rows = this.rows;
			
			var spos = dc.body2.scrollTop() + opts.deltaTopHeight;
			if (topHeight > opts.maxVisibleHeight){
				opts.deltaTopHeight = topHeight - opts.maxVisibleHeight;
				fillHeight(topDiv, topHeight - opts.deltaTopHeight);
			} else {
				opts.deltaTopHeight = 0;
			}
			if (bottomHeight > opts.maxVisibleHeight){
				fillHeight(bottomDiv, opts.maxVisibleHeight);
			} else if (bottomHeight == 0){
				var lastCount = state.data.total % opts.pageSize;
				if (lastCount){
					fillHeight(bottomDiv, dc.body2.height() - lastCount * rowHeight);
				}
			}

			$(target).datagrid('setSelectionState');
			dc.body2.scrollTop(spos - opts.deltaTopHeight);

			var pager = $(target).datagrid('getPager');
			pager.pagination('refresh', {
				pageNumber: this.page
			});

			opts.onLoadSuccess.call(target, {
				total: state.data.total,
				rows: this.rows
			});
		}
		function fillHeight(div, height){
			var count = Math.floor(height/maxHeight);
			var leftHeight = height - maxHeight*count;
			if (height < 0){
				leftHeight = 0;
			}
			var cc = [];
			for(var i=0; i<count; i++){
				cc.push('<div style="height:'+maxHeight+'px"></div>');
			}
			cc.push('<div style="height:'+leftHeight+'px"></div>');
			$(div).html(cc.join(''));
		}
	},

	updateRow: function(target, rowIndex, row){
		var opts = $.data(target, 'datagrid').options;
		var rows = $(target).datagrid('getRows');
		var rowData = opts.finder.getRow(target, rowIndex);

		var oldStyle = _getRowStyle(rowIndex);
		$.extend(rowData, row);
		var newStyle = _getRowStyle(rowIndex);
		var oldClassValue = oldStyle.c;
		var styleValue = newStyle.s;
		var classValue = 'datagrid-row ' + (rowIndex % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + newStyle.c;
		
		function _getRowStyle(rowIndex){
			var css = opts.rowStyler ? opts.rowStyler.call(target, rowIndex, rowData) : '';
			var classValue = '';
			var styleValue = '';
			if (typeof css == 'string'){
				styleValue = css;
			} else if (css){
				classValue = css['class'] || '';
				styleValue = css['style'] || '';
			}
			return {c:classValue, s:styleValue};
		}
		function _update(frozen){
			var fields = $(target).datagrid('getColumnFields', frozen);
			var tr = opts.finder.getTr(target, rowIndex, 'body', (frozen?1:2));
			var checked = tr.find('div.datagrid-cell-check input[type=checkbox]').is(':checked');
			tr.html(this.renderRow.call(this, target, fields, frozen, rowIndex, rowData));
			tr.attr('style', styleValue).removeClass(oldClassValue).addClass(classValue);
			if (checked){
				tr.find('div.datagrid-cell-check input[type=checkbox]')._propAttr('checked', true);
			}
		}
		
		_update.call(this, true);
		_update.call(this, false);
		$(target).datagrid('fixRowHeight', rowIndex);
	},

	// insertRow: function(target, index, row){
	// 	var state = $.data(target, 'datagrid');
	// 	var data = state.data;

	// 	if (index == undefined || index == null) index = data.rows.length;
	// 	if (index > data.rows.length) index = data.rows.length;
	// 	$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);
	// 	if (data.firstRows && index <= data.firstRows.length){
	// 		data.firstRows.splice(index, 0, row);
	// 	}
	// },
	insertRow: function(target, index, row){
		var state = $.data(target, 'datagrid');
		var opts = state.options;
		var data = state.data;

		var total = $(target).datagrid('getData').total;
		if (index == null){index = total;}
		if (index > total){index = total;}
		if (data.firstRows && index <= data.firstRows.length){
			data.firstRows.splice(index, 0, row);
		}
		data.total++;

		var rows = this.r1.concat(this.r2);
		if (index < this.index){
			this.reload.call(this, target);
		} else if (index <= this.index+rows.length){
			rows.splice(index - this.index, 0, row);
			this.r1 = rows.splice(0, opts.pageSize);
			if (this.r2.length){
				this.r2 = rows.splice(0, opts.pageSize);
			}
			this.rows = this.r1.concat(this.r2);
			this.populate.call(this, target);
			state.dc.body2.triggerHandler('scroll.datagrid');
		}
	},

	// deleteRow: function(target, index){
	// 	var data = $(target).datagrid('getData');
	// 	$.fn.datagrid.defaults.view.deleteRow.call(this, target, index);
	// 	if (data.firstRows){
	// 		data.firstRows.splice(index, 1);
	// 	}
	// },
	deleteRow: function(target, index){
		var state = $.data(target, 'datagrid');
		var data = state.data;
		var opts = state.options;
		if (data.firstRows){
			data.firstRows.splice(index, 1);
		}
		data.total--;

		var rows = this.r1.concat(this.r2);
		if (index < this.index){
			this.reload.call(this, target);
		} else if (index < this.index+rows.length){
			rows.splice(index - this.index, 1);
			this.r1 = rows.splice(0, opts.pageSize);
			if (this.r1.length < opts.pageSize){
				this.reload.call(this, target);
			} else {
				this.r2 = [];
				this.rows = this.r1.concat(this.r2);
				this.populate.call(this, target);
				state.dc.body2.triggerHandler('scroll.datagrid');
			}
		}
	}
});

$.fn.datagrid.methods.baseGetRowIndex = $.fn.datagrid.methods.getRowIndex;
$.fn.datagrid.methods.baseScrollTo = $.fn.datagrid.methods.scrollTo;
$.fn.datagrid.methods.baseGotoPage = $.fn.datagrid.methods.gotoPage;
$.extend($.fn.datagrid.methods, {
	getRowIndex: function(jq, id){
		var opts = jq.datagrid('options');
		if (opts.view.type == 'scrollview'){
			// return jq.datagrid('baseGetRowIndex', id) + opts.view.index;
			var index = jq.datagrid('baseGetRowIndex', id);
			if (index == -1){
				return -1;
			} else {
				return index + opts.view.index;
			}
		} else {
			return jq.datagrid('baseGetRowIndex', id);
		}
	},
	getRow: function(jq, index){
		return jq.datagrid('options').finder.getRow(jq[0], index);
	},
	gotoPage: function(jq, param){
		return jq.each(function(){
			var target = this;
			var opts = $(target).datagrid('options');
			if (opts.view.type == 'scrollview'){
				var page, callback;
				if (typeof param == 'object'){
					page = param.page;
					callback = param.callback;
				} else {
					page = param;
				}
				opts.view.getRows.call(opts.view, target, page, function(rows){
					this.page = page;
					this.index = (page-1)*opts.pageSize;
					this.rows = rows;
					this.r1 = rows;
					this.r2 = [];
					this.populate.call(this, target);
					$(target).data('datagrid').dc.body2.scrollTop(this.index * opts.rowHeight - opts.deltaTopHeight);
					if (callback){
						callback.call(target, page);
					}
				});
			} else {
				$(target).datagrid('baseGotoPage', param);
			}
		});
	},
	scrollTo: function(jq, param){
		return jq.each(function(){
			var target = this;
			var opts = $(target).datagrid('options');
			var index, callback;
			if (typeof param == 'object'){
				index = param.index;
				callback = param.callback;
			} else {
				index = param;
			}
			var view = opts.view;
			if (view.type == 'scrollview'){
				if (index >= view.index && index < view.index+view.rows.length){
					$(target).datagrid('baseScrollTo', index);
					if (callback){
						callback.call(target, index);
					}
				} else if (index >= 0){
					var page = Math.floor(index/opts.pageSize) + 1;
					$(target).datagrid('gotoPage', {
						page: page,
						callback: function(){
							setTimeout(function(){
								$(target).datagrid('baseScrollTo', index);
								if (callback){
									callback.call(target, index);
								}
							}, 0);							
						}
					});
				}
			} else {
				$(target).datagrid('baseScrollTo', index);
				if (callback){
					callback.call(target, index);
				}
			}
		});
	}
});

$.extend($.fn.datagrid.methods, {
	fixDetailRowHeight: function(jq, index){
		return jq.each(function(){
			var opts = $.data(this, 'datagrid').options;
			var dc = $.data(this, 'datagrid').dc;
			var tr1 = opts.finder.getTr(this, index, 'body', 1).next();
			var tr2 = opts.finder.getTr(this, index, 'body', 2).next();
			// fix the detail row height
			if (tr2.is(':visible')){
				tr1.css('height', '');
				tr2.css('height', '');
				var height = Math.max(tr1.height(), tr2.height());
				tr1.css('height', height);
				tr2.css('height', height);
			}
			dc.body2.triggerHandler('scroll');
		});
	},
	getExpander: function(jq, index){	// get row expander object
		var opts = $.data(jq[0], 'datagrid').options;
		return opts.finder.getTr(jq[0], index).find('span.datagrid-row-expander');
	},
	// get row detail container
	getRowDetail: function(jq, index){
		var opts = $.data(jq[0], 'datagrid').options;
		var tr = opts.finder.getTr(jq[0], index, 'body', 2);
		return tr.next().find('div.datagrid-row-detail');
	},
	expandRow: function(jq, index){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var dc = $.data(this, 'datagrid').dc;
			var expander = $(this).datagrid('getExpander', index);
			if (expander.hasClass('datagrid-row-expand')){
				expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
				var tr1 = opts.finder.getTr(this, index, 'body', 1).next();
				var tr2 = opts.finder.getTr(this, index, 'body', 2).next();
				tr1.show();
				tr2.show();
				$(this).datagrid('fixDetailRowHeight', index);
				if (opts.onExpandRow){
					var row = $(this).datagrid('getRows')[index];
					opts.onExpandRow.call(this, index, row);
				}
			}
		});
	},
	collapseRow: function(jq, index){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var dc = $.data(this, 'datagrid').dc;
			var expander = $(this).datagrid('getExpander', index);
			if (expander.hasClass('datagrid-row-collapse')){
				expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
				var tr1 = opts.finder.getTr(this, index, 'body', 1).next();
				var tr2 = opts.finder.getTr(this, index, 'body', 2).next();
				tr1.hide();
				tr2.hide();
				dc.body2.triggerHandler('scroll');
				if (opts.onCollapseRow){
					var row = $(this).datagrid('getRows')[index];
					opts.onCollapseRow.call(this, index, row);
				}
			}
		});
	}
});
/**
 * edatagrid - jQuery EasyUI
 * 
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2011-2015 www.jeasyui.com 
 * 
 * Dependencies:
 *   datagrid
 *   messager
 * 
 */
(function($){
	// var oldLoadDataMethod = $.fn.datagrid.methods.loadData;
	// $.fn.datagrid.methods.loadData = function(jq, data){
	// 	jq.each(function(){
	// 		$.data(this, 'datagrid').filterSource = null;
	// 	});
	// 	return oldLoadDataMethod.call($.fn.datagrid.methods, jq, data);
	// };

	var autoGrids = [];
	function checkAutoGrid(){
		autoGrids = $.grep(autoGrids, function(t){
			return t.length && t.data('edatagrid');
		});
	}
	function saveAutoGrid(omit){
		checkAutoGrid();
		$.map(autoGrids, function(t){
			if (t[0] != $(omit)[0]){
				t.edatagrid('saveRow');
			}
		});
		checkAutoGrid();
	}
	function addAutoGrid(dg){
		checkAutoGrid();
		for(var i=0; i<autoGrids.length; i++){
			if ($(autoGrids[i])[0] == $(dg)[0]){return;}
		}
		autoGrids.push($(dg));
	}
	function delAutoGrid(dg){
		checkAutoGrid();
		autoGrids = $.grep(autoGrids, function(t){
			return $(t)[0] != $(dg)[0];
		});
	}

	$(function(){
		$(document).unbind('.edatagrid').bind('mousedown.edatagrid', function(e){
			var p = $(e.target).closest('div.datagrid-view,div.combo-panel,div.window,div.window-mask');
			if (p.length){
				if (p.hasClass('datagrid-view')){
					saveAutoGrid(p.children('table'));
				}
				return;
			}
			saveAutoGrid();
		});
	});
	
	function buildGrid(target){
		var opts = $.data(target, 'edatagrid').options;
		$(target).datagrid($.extend({}, opts, {
			onDblClickCell:function(index,field,value){
				if (opts.editing){
					$(this).edatagrid('editRow', index);
					focusEditor(target, field);
				}
				if (opts.onDblClickCell){
					opts.onDblClickCell.call(target, index, field, value);
				}
			},
			onClickCell:function(index,field,value){
				// if (opts.editing && opts.editIndex >= 0){
				// 	$(this).edatagrid('editRow', index);
				// 	focusEditor(target, field);
				// }
				if (opts.editIndex >= 0){
					var dg = $(this);
					if (opts.editing){
						dg.edatagrid('editRow', index);
					} else {
						setTimeout(function(){
							dg.edatagrid('selectRow', opts.editIndex);
						}, 0);
					}
					focusEditor(target, field);
				}
				if (opts.onClickCell){
					opts.onClickCell.call(target, index, field, value);
				}
			},
			onBeforeEdit: function(index, row){
				if (opts.onBeforeEdit){
					if (opts.onBeforeEdit.call(target, index, row) == false){
						return false;
					}
				}
				if (opts.autoSave){
					addAutoGrid(this);
				}
				opts.originalRow = $.extend(true, [], row);
			},
			onAfterEdit: function(index, row){
				delAutoGrid(this);
				opts.editIndex = -1;
				var url = row.isNewRecord ? opts.saveUrl : opts.updateUrl;
				if (url){
					var changed = false;
					var fields = $(this).edatagrid('getColumnFields',true).concat($(this).edatagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var field = fields[i];
						var col = $(this).edatagrid('getColumnOption', field);
						if (col.editor && opts.originalRow[field] != row[field]){
							changed = true;
							break;
						}
					}
					if (changed){
						opts.poster.call(target, url, row, function(data){
							if (data.isError){
								var originalRow = opts.originalRow;
								$(target).edatagrid('cancelRow',index);
								$(target).edatagrid('selectRow',index);
								$(target).edatagrid('editRow',index);
								opts.originalRow = originalRow;
								opts.onError.call(target, index, data);
								return;
							}
							data.isNewRecord = null;
							$(target).datagrid('updateRow', {
								index: index,
								row: data
							});
							if (opts.tree){
								var idValue = row[opts.idField||'id'];
								var t = $(opts.tree);
								var node = t.tree('find', idValue);
								if (node){
									node.text = row[opts.treeTextField];
									t.tree('update', node);
								} else {
									var pnode = t.tree('find', row[opts.treeParentField]);
									t.tree('append', {
										parent: (pnode ? pnode.target : null),
										data: [{id:idValue,text:row[opts.treeTextField]}]
									});
								}
							}
							opts.onSuccess.call(target, index, row);
							opts.onSave.call(target, index, row);
						}, function(data){
							opts.onError.call(target, index, data);
						});
					} else {
						opts.onSave.call(target, index, row);
					}
				} else {
					row.isNewRecord = false;
					opts.onSave.call(target, index, row);
				}
				if (opts.onAfterEdit) opts.onAfterEdit.call(target, index, row);
			},
			onCancelEdit: function(index, row){
				delAutoGrid(this);
				opts.editIndex = -1;
				if (row.isNewRecord) {
					$(this).datagrid('deleteRow', index);
				}
				if (opts.onCancelEdit) opts.onCancelEdit.call(target, index, row);
			},
			onBeforeLoad: function(param){
				if (opts.onBeforeLoad.call(target, param) == false){return false}
				$(this).edatagrid('cancelRow');
				if (opts.tree){
					var node = $(opts.tree).tree('getSelected');
					param[opts.treeParentField] = node ? node.id : undefined;
				}
			}
		}));
		
		
		
		if (opts.tree){
			$(opts.tree).tree({
				url: opts.treeUrl,
				onClick: function(node){
					$(target).datagrid('load');
				},
				onDrop: function(dest,source,point){
					var targetId = $(this).tree('getNode', dest).id;
					var data = {
						id:source.id,
						targetId:targetId,
						point:point
					};
					opts.poster.call(target, opts.treeDndUrl, data, function(result){
						$(target).datagrid('load');
					});
				}
			});
		}
	}

	function focusEditor(target, field){
		var opts = $(target).edatagrid('options');
		var t;
		var editor = $(target).datagrid('getEditor', {index:opts.editIndex,field:field});
		if (editor){
			t = editor.target;
		} else {
			var editors = $(target).datagrid('getEditors', opts.editIndex);
			if (editors.length){
				t = editors[0].target;
			}
		}
		if (t){
			if ($(t).hasClass('textbox-f')){
				$(t).textbox('textbox').focus();
			} else {
				$(t).focus();					
			}
		}
	}
	
	$.fn.edatagrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.edatagrid.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'edatagrid');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'edatagrid', {
					options: $.extend({}, $.fn.edatagrid.defaults, $.fn.edatagrid.parseOptions(this), options)
				});
			}
			buildGrid(this);
		});
	};
	
	$.fn.edatagrid.parseOptions = function(target){
		return $.extend({}, $.fn.datagrid.parseOptions(target), {
		});
	};
	
	$.fn.edatagrid.methods = {
		options: function(jq){
			var opts = $.data(jq[0], 'edatagrid').options;
			return opts;
		},
		loadData: function(jq, data){
			return jq.each(function(){
				$(this).edatagrid('cancelRow');
				$(this).datagrid('loadData', data);
			});
		},
		enableEditing: function(jq){
			return jq.each(function(){
				var opts = $.data(this, 'edatagrid').options;
				opts.editing = true;
			});
		},
		disableEditing: function(jq){
			return jq.each(function(){
				var opts = $.data(this, 'edatagrid').options;
				opts.editing = false;
			});
		},
		isEditing: function(jq, index){
			var opts = $.data(jq[0], 'edatagrid').options;
			var tr = opts.finder.getTr(jq[0], index);
			return tr.length && tr.hasClass('datagrid-row-editing');
		},
		editRow: function(jq, index){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				var editIndex = opts.editIndex;
				if (editIndex != index){
					if (dg.datagrid('validateRow', editIndex)){
						if (editIndex>=0){
							if (opts.onBeforeSave.call(this, editIndex) == false) {
								setTimeout(function(){
									dg.datagrid('selectRow', editIndex);
								},0);
								return;
							}
						}
						dg.datagrid('endEdit', editIndex);
						dg.datagrid('beginEdit', index);
						if (!dg.edatagrid('isEditing', index)){
							return;
						}
						opts.editIndex = index;
						focusEditor(this);
						
						var rows = dg.datagrid('getRows');
						opts.onEdit.call(this, index, rows[index]);
					} else {
						setTimeout(function(){
							dg.datagrid('selectRow', editIndex);
						}, 0);
					}
				}
			});
		},
		addRow: function(jq, index){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				if (opts.editIndex >= 0){
					if (!dg.datagrid('validateRow', opts.editIndex)){
						dg.datagrid('selectRow', opts.editIndex);
						return;
					}
					if (opts.onBeforeSave.call(this, opts.editIndex) == false){
						setTimeout(function(){
							dg.datagrid('selectRow', opts.editIndex);
						},0);
						return;
					}
					dg.datagrid('endEdit', opts.editIndex);
				}
				
				function _add(index, row){
					if (index == undefined){
						dg.datagrid('appendRow', row);
						opts.editIndex = dg.datagrid('getRows').length - 1;
					} else {
						dg.datagrid('insertRow', {index:index,row:row});
						opts.editIndex = index;
					}
				}
				if (typeof index == 'object'){
					_add(index.index, $.extend(index.row, {isNewRecord:true}))
				} else {
					_add(index, {isNewRecord:true});
				}
								
				dg.datagrid('beginEdit', opts.editIndex);
				dg.datagrid('selectRow', opts.editIndex);
				
				var rows = dg.datagrid('getRows');
				if (opts.tree){
					var node = $(opts.tree).tree('getSelected');
					rows[opts.editIndex][opts.treeParentField] = (node ? node.id : 0);
				}
				
				opts.onAdd.call(this, opts.editIndex, rows[opts.editIndex]);
			});
		},
		saveRow: function(jq){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				if (opts.editIndex >= 0){
					if (opts.onBeforeSave.call(this, opts.editIndex) == false) {
						setTimeout(function(){
							dg.datagrid('selectRow', opts.editIndex);
						},0);
						return;
					}
					$(this).datagrid('endEdit', opts.editIndex);
				}
			});
		},
		cancelRow: function(jq){
			return jq.each(function(){
				var opts = $.data(this, 'edatagrid').options;
				if (opts.editIndex >= 0){
					$(this).datagrid('cancelEdit', opts.editIndex);
				}
			});
		},
		destroyRow: function(jq, index){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				
				var rows = [];
				if (index == undefined){
					rows = dg.datagrid('getSelections');
				} else {
					var rowIndexes = $.isArray(index) ? index : [index];
					for(var i=0; i<rowIndexes.length; i++){
						var row = opts.finder.getRow(this, rowIndexes[i]);
						if (row){
							rows.push(row);
						}
					}
				}

				if (!rows.length){
					$.messager.show({
						title: opts.destroyMsg.norecord.title,
						msg: opts.destroyMsg.norecord.msg
					});
					return;
				}
				
				$.messager.confirm(opts.destroyMsg.confirm.title,opts.destroyMsg.confirm.msg,function(r){
					if (r){
						for(var i=0; i<rows.length; i++){
							_del(rows[i]);
						}
						dg.datagrid('clearSelections');
					}
				});
				
				function _del(row){
					var index = dg.datagrid('getRowIndex', row);
					if (index == -1){return}
					if (row.isNewRecord){
						dg.datagrid('cancelEdit', index);
					} else {
						if (opts.destroyUrl){
							var idValue = row[opts.idField||'id'];
							opts.poster.call(dg[0], opts.destroyUrl, {id:idValue}, function(data){
								var index = dg.datagrid('getRowIndex', idValue);
								if (data.isError){
									dg.datagrid('selectRow', index);
									opts.onError.call(dg[0], index, data);
									return;
								}
								if (opts.tree){
									dg.datagrid('reload');
									var t = $(opts.tree);
									var node = t.tree('find', idValue);
									if (node){
										t.tree('remove', node.target);
									}
								} else {
									dg.datagrid('cancelEdit', index);
									dg.datagrid('deleteRow', index);
								}
								opts.onDestroy.call(dg[0], index, row);
								var pager = dg.datagrid('getPager');
								if (pager.length && !dg.datagrid('getRows').length){
									dg.datagrid('options').pageNumber = pager.pagination('options').pageNumber;
									dg.datagrid('reload');
								}
							}, function(data){
								opts.onError.call(dg[0], index, data);
							});
						} else {
							dg.datagrid('cancelEdit', index);
							dg.datagrid('deleteRow', index);
							opts.onDestroy.call(dg[0], index, row);
						}
					}
				}
			});
		}
	};
	
	$.fn.edatagrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		singleSelect: true,
		editing: true,
		editIndex: -1,
		destroyMsg:{
			norecord:{
				title:'Warning',
				msg:'No record is selected.'
			},
			confirm:{
				title:'Confirm',
				msg:'Are you sure you want to delete?'
			}
		},
		poster: function(url, data, success, error){
			$.ajax({
				type: 'post',
				url: url,
				data: data,
				dataType: 'json',
				success: function(data){
					success(data);
				},
				error: function(jqXHR, textStatus, errorThrown){
					error({
						jqXHR: jqXHR,
						textStatus: textStatus,
						errorThrown: errorThrown
					});
				}
			});
		},
		
		autoSave: false,	// auto save the editing row when click out of datagrid
		url: null,	// return the datagrid data
		saveUrl: null,	// return the added row
		updateUrl: null,	// return the updated row
		destroyUrl: null,	// return {success:true}
		
		tree: null,		// the tree selector
		treeUrl: null,	// return tree data
		treeDndUrl: null,	// to process the drag and drop operation, return {success:true}
		treeTextField: 'name',
		treeParentField: 'parentId',
		
		onAdd: function(index, row){},
		onEdit: function(index, row){},
		onBeforeSave: function(index){},
		onSave: function(index, row){},
		onSuccess: function(index, row){},
		onDestroy: function(index, row){},
		onError: function(index, row){}
	});
	
	////////////////////////////////
	$.parser.plugins.push('edatagrid');
})(jQuery);(function($){
	$.extend($.fn.treegrid.defaults, {
		dropAccept:'tr[node-id]',
		onBeforeDrag: function(row){},	// return false to deny drag
		onStartDrag: function(row){},
		onStopDrag: function(row){},
		onDragEnter: function(targetRow, sourceRow){},	// return false to deny drop
		onDragOver: function(targetRow, sourceRow){},	// return false to deny drop
		onDragLeave: function(targetRow, sourceRow){},
		onBeforeDrop: function(targetRow, sourceRow, point){},
		onDrop: function(targetRow, sourceRow, point){},	// point:'append','top','bottom'
	});
	
	$.extend($.fn.treegrid.methods, {
		resetDnd: function(jq){
			return jq.each(function(){
				var state = $.data(this, 'treegrid');
				var opts = state.options;
				var row = $(this).treegrid('find', state.draggingNodeId);
				if (row){
					var tr = opts.finder.getTr(this, row[opts.idField]);
					tr.each(function(){
						var target = this;
						$(target).data('draggable').droppables = $('.droppable:visible').filter(function(){
							return target != this;
						}).filter(function(){
							var accept = $.data(this, 'droppable').options.accept;
							if (accept){
								return $(accept).filter(function(){
									return this == target;
								}).length > 0;
							} else {
								return true;
							}
						});
					});
				}
			});
		},
		enableDnd: function(jq, id){
			if (!$('#treegrid-dnd-style').length){
				$('head').append(
						'<style id="treegrid-dnd-style">' +
						'.treegrid-row-top td{border-top:1px solid red}' +
						'.treegrid-row-bottom td{border-bottom:1px solid red}' +
						'.treegrid-row-append .tree-title{border:1px solid red}' +
						'</style>'
				);
			}
			return jq.each(function(){
				var target = this;
				var state = $.data(this, 'treegrid');
				if (!state.disabledNodes){
					state.disabledNodes = [];					
				}
				var t = $(this);
				var opts = state.options;
				if (id){
					var nodes = opts.finder.getTr(target, id);
					var rows = t.treegrid('getChildren', id);
					for(var i=0; i<rows.length; i++){
						nodes = nodes.add(opts.finder.getTr(target, rows[i][opts.idField]));
					}
				} else {
					var nodes = t.treegrid('getPanel').find('tr[node-id]');
				}
				nodes.draggable({
					disabled:false,
					revert:true,
					cursor:'pointer',
					proxy: function(source){
						var row = t.treegrid('find', $(source).attr('node-id'));
						var p = $('<div class="tree-node-proxy"></div>').appendTo('body');
						p.html('<span class="tree-dnd-icon tree-dnd-no">&nbsp;</span>'+row[opts.treeField]);
						p.hide();
						return p;
					},
					deltaX: 15,
					deltaY: 15,
					onBeforeDrag:function(e){
						if (opts.onBeforeDrag.call(target, getRow(this)) == false){return false}
						if ($(e.target).hasClass('tree-hit') || $(e.target).parent().hasClass('datagrid-cell-check')){return false;}
						if (e.which != 1){return false;}
					},
					onStartDrag:function(){
						$(this).draggable('proxy').css({
							left:-10000,
							top:-10000
						});
						var row = getRow(this);
						state.draggingNodeId = row[opts.idField];
						setValid(state.draggingNodeId, false);
						opts.onStartDrag.call(target, row);
					},
					onDrag:function(e){
						var x1=e.pageX,y1=e.pageY,x2=e.data.startX,y2=e.data.startY;
						var d = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
						if (d>3){	// when drag a little distance, show the proxy object
							$(this).draggable('proxy').show();
							var tr = opts.finder.getTr(target, $(this).attr('node-id'));
							var treeTitle = tr.find('span.tree-title');
							e.data.startX = treeTitle.offset().left;
							e.data.startY = treeTitle.offset().top;
							e.data.offsetWidth = 0;
							e.data.offsetHeight = 0;
						}
						this.pageY = e.pageY;
					},
					onStopDrag:function(){
						setValid(state.draggingNodeId, true);
						for(var i=0; i<state.disabledNodes.length; i++){
							var tr = opts.finder.getTr(target, state.disabledNodes[i]);
							tr.droppable('enable');
						}
						state.disabledNodes = [];
						var row = t.treegrid('find', state.draggingNodeId);
						state.draggingNodeId = undefined;
						opts.onStopDrag.call(target, row);
					}
				});
				var view = $(target).data('datagrid').dc.view;
				view.add(nodes).droppable({
					accept:opts.dropAccept,
					onDragEnter: function(e, source){
						var nodeId = $(this).attr('node-id');
						var dTarget = getGridTarget(this);
						var dOpts = $(dTarget).treegrid('options');
						var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
						var sRow = getRow(source);
						var dRow = getRow(this);
						if (tr.length && dRow){
							cb();
						}

						function cb(){
							if (opts.onDragEnter.call(target, dRow, sRow) == false){
								allowDrop(source, false);
								tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
								tr.droppable('disable');
								state.disabledNodes.push(nodeId);
							}
						}
					},
					onDragOver:function(e,source){
						var nodeId = $(this).attr('node-id');
						if ($.inArray(nodeId, state.disabledNodes) >= 0){return;}
						var dTarget = getGridTarget(this);
						var dOpts = $(dTarget).treegrid('options');
						var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
						if (tr.length){
							if (!isValid(tr)){
								allowDrop(source, false);
								return;
							}
						}
						allowDrop(source, true);
						var sRow = getRow(source);
						var dRow = getRow(this);
						if (tr.length){
							var pageY = source.pageY;
							var top = tr.offset().top;
							var bottom = top + tr.outerHeight();
							tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
							if (pageY > top + (bottom - top) / 2){
								if (bottom - pageY < 5){
									tr.addClass('treegrid-row-bottom');
								} else {
									tr.addClass('treegrid-row-append');
								}
							} else {
								if (pageY - top < 5){
									tr.addClass('treegrid-row-top');
								} else {
									tr.addClass('treegrid-row-append');
								}
							}
							if (dRow){
								cb();
							}
						}

						function cb(){
							if (opts.onDragOver.call(target, dRow, sRow) == false){
								allowDrop(source, false);
								tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
								tr.droppable('disable');
								state.disabledNodes.push(nodeId);
							}
						}
					},
					onDragLeave:function(e,source){
						allowDrop(source, false);
						var dTarget = getGridTarget(this);
						var dOpts = $(dTarget).treegrid('options');
						var sRow = getRow(source);
						var dRow = getRow(this);
						var tr = dOpts.finder.getTr(dTarget, $(this).attr('node-id'));
						tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
						if (dRow){
							opts.onDragLeave.call(target, dRow, sRow);
						}
					},
					onDrop:function(e,source){
						var point = 'append';
						var dRow = null;
						var sRow = getRow(source);
						var sTarget = getGridTarget(source);
						var dTarget = getGridTarget(this);
						var dOpts = $(dTarget).treegrid('options');
						var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
						if (tr.length){
							if (!isValid(tr)){
								return;
							}
							dRow = getRow(tr);
							if (tr.hasClass('treegrid-row-append')){
								point = 'append';
							} else {
								point = tr.hasClass('treegrid-row-top') ? 'top' : 'bottom';
							}
							tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
						}
						if (opts.onBeforeDrop.call(target, dRow, sRow, point) == false){
							return;
						}
						insert.call(this);
						opts.onDrop.call(target, dRow, sRow, point);

						function insert(){
							var data = $(sTarget).treegrid('pop', sRow[opts.idField]);
							if (point == 'append'){
								if (dRow){
									$(dTarget).treegrid('append', {
										parent: dRow[opts.idField],
										data: [data]
									});
									if (dRow.state == 'closed'){
										$(dTarget).treegrid('expand', dRow[opts.idField]);
									}
								} else {
									$(dTarget).treegrid('append', {parent:null, data:[data]});
								}
								$(dTarget).treegrid('enableDnd', sRow[opts.idField]);
							} else {
								var param = {data:data};
								if (point == 'top'){
									param.before = dRow[opts.idField];
								} else {
									param.after = dRow[opts.idField];
								}
								$(dTarget).treegrid('insert', param);
								$(dTarget).treegrid('enableDnd', sRow[opts.idField]);
							}
						}
					}
				});
				
				function allowDrop(source, allowed){
					var icon = $(source).draggable('proxy').find('span.tree-dnd-icon');
					icon.removeClass('tree-dnd-yes tree-dnd-no').addClass(allowed ? 'tree-dnd-yes' : 'tree-dnd-no');
				}
				function getRow(tr){
					var target = getGridTarget(tr);
					var nodeId = $(tr).attr('node-id');
					return $(target).treegrid('find', nodeId);
				}

				function getGridTarget(el){
					return $(el).closest('div.datagrid-view').children('table')[0];
				}
				function isValid(tr){
					var opts = $(tr).droppable('options');
					if (opts.disabled || opts.accept == 'no-accept'){
						return false;
					} else {
						return true;
					}
				}
				function setValid(id, valid){
					var accept = valid ? opts.dropAccept : 'no-accept';
					var tr = opts.finder.getTr(target, id);
					tr.droppable({accept:accept});
					tr.next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({accept:accept});
				}
			});
		}
	});
})(jQuery);

/************************************************EASYUI默认属性设置************************************************/
$.extend($.fn.textbox.defaults,{"icons":[{"iconCls": "icon-clear",
                       				handler: function(e){
                    					$(e.data.target).textbox('clear').textbox('textbox').focus();
                       				}
}
]});
$.extend($.fn.combobox.defaults,{"icons":[{"iconCls": "icon-clear",
		handler: function(e){
		$(e.data.target).combobox('clear');
		}
}
]});
$.extend($.fn.combotree.defaults,{"icons":[{"iconCls": "icon-clear",
	handler: function(e){
	$(e.data.target).combotree('clear');
	}
}
]});
$.extend($.fn.numberbox.defaults,{"icons":[{"iconCls": "icon-clear",
	handler: function(e){
	$(e.data.target).numberbox('clear');
	}
}
]});

$.extend($.fn.searchbox.defaults,{"icons":[{"iconCls": "icon-clear",
	handler: function(e){
	$(e.data.target).numberbox('clear');
	}
}
]});

//定义日期默认的按钮
var datebox_buttons=$.fn.datebox.defaults.buttons;
datebox_buttons.splice(1,0,{
	"text":"清空",
	"handler":function(target){
		$(target).datebox("setText","");
		$(target).datebox("setValue","");
		$(target).datebox("hidePanel");
	}
});

var datetimebox_buttons=$.fn.datetimebox.defaults.buttons;
datetimebox_buttons.push({
	"text":"清空",
	"handler":function(target){
		$(target).datetimebox("setText","");
		$(target).datebox("setValue","");
		$(target).datetimebox("hidePanel");
	}
});



$.extend($.fn.combobox.defaults,{"panelHeight":"auto","editable":false});
$.extend($.fn.combo.defaults,{"panelHeight":"auto","editable":false});
$.extend($.fn.combogrid.defaults,{"panelHeight":"auto","editable":false});
$.extend($.fn.combotree.defaults,{"panelHeight":"auto","editable":false});
$.extend($.fn.numberbox.defaults,{"min":0,"max":99999999999,"precision":4});
$.extend($.fn.searchbox.defaults,{"editable":false});



//$.extend($.fn.datagrid.defaults,{"fit":true}); //如果有子表格，这个fit=ture会对子表格产生影响，故不设置这个默认属性
$.extend($.fn.datagrid.defaults,{"rownumbers":true});
$.extend($.fn.datagrid.defaults,{"pageList":[50,30,100]});
$.extend($.fn.datagrid.defaults,{"fitColumns":true});
$.extend($.fn.datagrid.defaults,{"rowStyler":function(index,row){
													if(index%2==1){
														return "background:rgb(243, 242, 242);";
													}
												}
}); 
$.extend($.fn.datebox.defaults,{"editable":false});
$.extend($.fn.datetimebox.defaults,{"editable":false});

/************************************************EASYUI默认属性设置************************************************/


/*$.extend($.fn.textbox.defaults,{buttonText:'清除',buttonIcon:'icon-clear',onClickButton:function(){$(this).textbox("clear");}});*/
$(document).ready(function(){
//	$("body").append("<div id=\"_grid_header_menu_\" class=\"easyui-menu\" style=\"width:120px;\" data-options=\"onClick:_grid_header_menu_click\"> <div onClick=\"\" data-options=\"iconCls:'platform-table_edit'\"> <b>表格选项</b> <div style=\"width:150px;\"> <div onClick='_grid_header_menu_openRowDrag' data-options=\"iconCls:'icon-ok'\">开启行拖动</div> <div onClick='_grid_header_menu_openZebra' data-options=\"iconCls:'icon-ok'\">开启斑马线效果</div> <div onClick='_grid_header_menu_closeZebra' data-options=\"iconCls:'icon-ok',disabled:true\">关闭斑马线效果</div> <div onClick='_grid_header_menu_multiSelect' data-options=\"iconCls:'icon-ok'\">多行选择</div> <div onClick='_grid_header_menu_singleSelect' data-options=\"iconCls:'icon-ok'\">单行选择</div> </div> </div> <div class=\"menu-sep\"></div> <div onClick='_grid_header_menu_ascSort' data-options=\"iconCls:'platform-arrow_up'\">升序</div> <div onClick='_grid_header_menu_descSort' data-options=\"iconCls:'platform-arrow_down'\">降序</div> </div>");
	$("body").append("<div id=\"_grid_header_menu_\" class=\"easyui-menu\" style=\"width:120px;\" data-options=\"onClick:_grid_header_menu_click\"> <div onClick=\"\" data-options=\"iconCls:'platform-table_edit'\"> <b>表格选项</b> <div style=\"width:150px;\"> <div onClick='_grid_header_menu_openRowDrag' data-options=\"iconCls:'icon-ok'\">开启行拖动</div> <div onClick='_grid_header_menu_openZebra' data-options=\"iconCls:'icon-ok'\">开启斑马线效果</div> <div onClick='_grid_header_menu_closeZebra' data-options=\"iconCls:'icon-ok',disabled:true\">关闭斑马线效果</div> <div onClick='_grid_header_menu_multiSelect' data-options=\"iconCls:'icon-ok'\">多行选择</div> <div onClick='_grid_header_menu_singleSelect' data-options=\"iconCls:'icon-ok'\">单行选择</div> </div> </div> <div class=\"menu-sep\"></div><div onClick='showOrHideColumns' data-options=\"iconCls:'platform-app_columns'\">显示/隐藏列</div> <div class=\"menu-sep\"></div> <div onClick='_grid_header_menu_ascSort' data-options=\"iconCls:'platform-arrow_up'\">升序</div> <div onClick='_grid_header_menu_descSort' data-options=\"iconCls:'platform-arrow_down'\">降序</div> </div>");
	$("body").append("<div id=\"_columns_display_hidden_opt_\" class=\"easyui-draggable column_selection\"> <table> <tr id=\"_columns_display_hidden_header_\"> <th>显示列</th> <th></th> <th>隐藏列</th> </tr> <tr> <td id=\"_columns_display_columns_\" style=\"width:45%\"> </td> <td id=\"_columns_display_opt_\" style=\"text-align: center;vertical-align: middle;\"> <input type=\"button\" onclick=\"_columns_display_hideAll()\" value=\"全部隐藏 >>\" > <input type=\"button\" onclick=\"_columns_display_showAll()\" value=\"<< 全部显示\" > <input type=\"button\" onclick=\"_columns_display_ok()\" value=\"确定\" > <input type=\"button\" onclick=\"_columns_display_cancel()\" value=\"取消\" > </td> <td id=\"_columns_hidden_columns_\" style=\"width:45%\"> </td> </tr> </table> </div>");
	$('#_columns_display_hidden_opt_').draggable({
	    handle:'#_columns_display_hidden_header_'
	});
	$('#_grid_header_menu_').menu({});
	$(".easyui-textbox").each(function(i){
		var span = $(this).siblings("span")[0];
		var targetInput = $(span).find("input:first");
		if(targetInput){
		$(targetInput).attr("placeholder", $(this).attr("placeholder"));
		}
	});
	$("#_columns_display_columns_").delegate("div","click",function(){
		$("#_columns_hidden_columns_").prepend($(this));
	});
	$("#_columns_hidden_columns_").delegate("div","click",function(){
		$("#_columns_display_columns_").prepend($(this));
	});
});

/**
 * 扩展右键菜单
 */
(function($){
	
	$.extend($.fn.datagrid.defaults, {
		onHeaderContextMenu: function(e,field){
			e.preventDefault();
			_current_grid_context_id_=$(this).datagrid("options").id;
			_current_grid_context_field_=field;
			//弹出右键菜单
			$('#_grid_header_menu_').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
			
			/*var fields =$('#'+_current_grid_context_id_).datagrid('getColumnFields');
			
			
			
			
			var item=null;
			
			for(var i=0;i<fields.length;i++){
				var col = $('#'+_current_grid_context_id_).datagrid('getColumnOption', fields[i]);
				if(col.title==undefined)continue;
				item=$('#_grid_header_menu_').menu("findItem",col.title);
				break;
			}
			
			//显示datagrid的列
			if(item==null){
				for(var i=0; i<fields.length; i++){
	                var field = fields[i];
	                var col = $('#'+_current_grid_context_id_).datagrid('getColumnOption', field);
	                //过滤没有标题的列。如checkbox字段
	                if(col.title==undefined)continue;
	                $('#_grid_header_menu_').menu('appendItem', {
	    				text: col.title,
	                    name: field,
	                    iconCls: 'icon-ok'
	                });
	            }
			}*/
			
			//禁用启用单行选择，多行选择
			if($(this).datagrid("options").singleSelect){
				var singleItem = $('#_grid_header_menu_').menu('findItem', '单行选择');
				$('#_grid_header_menu_').menu('disableItem', singleItem.target);
				var multiItem = $('#_grid_header_menu_').menu('findItem', '多行选择');
				$('#_grid_header_menu_').menu('enableItem', multiItem.target);
			}else{
				var singleItem = $('#_grid_header_menu_').menu('findItem', '单行选择');
				$('#_grid_header_menu_').menu('enableItem', singleItem.target);
				var multiItem = $('#_grid_header_menu_').menu('findItem', '多行选择');
				$('#_grid_header_menu_').menu('disableItem', multiItem.target);
			}
			
			//冻结行禁用，启用，获取冻结列
			/*var frozenCols=$('#'+_current_grid_context_id_).datagrid('getColumnFields',true);
			
			var frozen = $('#_grid_header_menu_').menu('findItem', '冻结该列');
			var unFrozen= $('#_grid_header_menu_').menu('findItem', '取消冻结该列');
			
			$('#_grid_header_menu_').menu('enableItem', frozen.target);
			$('#_grid_header_menu_').menu('disableItem', unFrozen.target);
			
			for(var i=0;i<frozenCols.length;i++){
				if(frozenCols[i]==field){
					$('#_grid_header_menu_').menu('disableItem', frozen.target);
					$('#_grid_header_menu_').menu('enableItem', unFrozen.target);
					break;
				}
			}*/
		}
	});
})(jQuery);

var _current_grid_context_id_;
var _current_grid_context_field_;

function _grid_header_menu_click(item){
	if($(item.target).attr("onClick")==undefined){
		if(item.iconCls=="icon-ok"){
			//去掉前面的勾选图标
			$('#_grid_header_menu_').menu('setIcon',{
				 target: item.target,
                 iconCls: 'icon-empty'
			});
			//隐藏该列
			$('#'+_current_grid_context_id_).datagrid('hideColumn', item.name);
		}else{
			//显示该列，在前面加上勾选图标
			$('#'+_current_grid_context_id_).datagrid('showColumn', item.name);
			$('#_grid_header_menu_').menu('setIcon',{
				 target: item.target,
                iconCls: 'icon-ok'
			});
		}
		return;
	}
	if($(item.target).attr("onClick")=="")return;
	//调用menu的item上的onClick方法，EasyUI不支持在item上绑定onClick
	window[$(item.target).attr("onClick")]();
}

/**
 * 冻结列
 */
function _grid_header_menu_freeze(){
	$("#"+_current_grid_context_id_).datagrid("freezeColumn",_current_grid_context_field_);
	//修复行拖动
	var rowDrag = $('#_grid_header_menu_').menu('findItem', '开启行拖动');
	if(rowDrag.disabled){
		_grid_header_menu_openRowDrag();
	}
}

/**
 * 取消冻结列
 */
function _grid_header_menu_unfreeze(){
	$("#"+_current_grid_context_id_).datagrid("unfreezeColumn",_current_grid_context_field_);
	//修复行拖动
	var rowDrag = $('#_grid_header_menu_').menu('findItem', '开启行拖动');
	if(rowDrag.disabled){
		_grid_header_menu_openRowDrag();
	}
}

/**
 * 行拖动
 */
function _grid_header_menu_openRowDrag(){
	$("#"+_current_grid_context_id_).datagrid('enableDnd');
	var rowDrag = $('#_grid_header_menu_').menu('findItem', '开启行拖动');
	$('#_grid_header_menu_').menu('disableItem', rowDrag.target);
}
/**
 * 开启斑马线
 */
function _grid_header_menu_openZebra(){
	var openZebra = $('#_grid_header_menu_').menu('findItem', '开启斑马线效果');
	$('#_grid_header_menu_').menu('disableItem', openZebra.target);
	var closeZebra = $('#_grid_header_menu_').menu('findItem', '关闭斑马线效果');
	$('#_grid_header_menu_').menu('enableItem', closeZebra.target);
	$("#"+_current_grid_context_id_).datagrid({"rowStyler":function(index,row){
			if(index%2==1){
				return "background:#eceaea;";
			}
		}
	});
	fixFrozenColumns();
}
/**
 * 关闭斑马线
 */
function _grid_header_menu_closeZebra(){
	var openZebra = $('#_grid_header_menu_').menu('findItem', '开启斑马线效果');
	$('#_grid_header_menu_').menu('enableItem', openZebra.target);
	var closeZebra = $('#_grid_header_menu_').menu('findItem', '关闭斑马线效果');
	$('#_grid_header_menu_').menu('disableItem', closeZebra.target);
	$("#"+_current_grid_context_id_).datagrid({"rowStyler":null});
	fixFrozenColumns();
}

/**
 * 修复开启关闭斑马线时候的问题,行拖动问题
 */
function fixFrozenColumns(){
	
	
	//修复冻结列问题
	var frozenCols=$('#'+_current_grid_context_id_).datagrid('getColumnFields',true);
	var cols=$('#'+_current_grid_context_id_).datagrid('getColumnFields');
	if(frozenCols.length!=0){
		$("#"+_current_grid_context_id_).datagrid("freezeColumn",frozenCols[0]);
	}else{
		$("#"+_current_grid_context_id_).datagrid("freezeColumn",cols[0]);
	}
	
	//修复行拖动
	var rowDrag = $('#_grid_header_menu_').menu('findItem', '开启行拖动');
	if(rowDrag.disabled){
		setTimeout(function(){$("#"+_current_grid_context_id_).datagrid('enableDnd');}, 200);
	}
}

/**
 * 开启多选
 */
function _grid_header_menu_multiSelect(){
	$("#"+_current_grid_context_id_).datagrid({"singleSelect":false});
	fixFrozenColumns();
}
/**
 * 单选
 */
function _grid_header_menu_singleSelect(){
	$("#"+_current_grid_context_id_).datagrid({"singleSelect":true});
	fixFrozenColumns();
}

/**
 * 升序
 */
function _grid_header_menu_ascSort(){
	$("#"+_current_grid_context_id_).datagrid('sort', {
		sortName: _current_grid_context_field_,
		sortOrder: 'asc'
	});
}

/**
 * 降序
 */
function _grid_header_menu_descSort(){
	$("#"+_current_grid_context_id_).datagrid('sort', {
		sortName: _current_grid_context_field_,
		sortOrder: 'desc'
	});
}

/**
 * Datagrid扩展方法tooltip 
 * 使用说明:
 *   代码案例:
 *		$("#dg").datagrid({....}).datagrid('tooltip'); 所有列
 *		$("#dg").datagrid({....}).datagrid('tooltip',['productid','listprice']); 指定列
 */
$.extend($.fn.datagrid.methods, {
	tooltip : function (jq, fields) {
		return jq.each(function () {
			var panel = $(this).datagrid('getPanel');
			if (fields && typeof fields == 'object' && fields.sort) {
				$.each(fields, function () {
					var field = this;
					bindEvent($('.datagrid-body td[field=' + field + '] .datagrid-cell', panel));
				});
			} else {
				bindEvent($(".datagrid-body .datagrid-cell", panel));
			}
		});

		function bindEvent(jqs) {
			//先解绑事件，然后再绑定事件
			jqs.unbind("mouseover").mouseover(function () {
				var content = $(this).text();
				_dg_tooltip_remove();
				$(this).tooltip({
					content : "<pre class='dg_tooltip' onmouseover='_dg_tooltip_remove()'>"+content+"</pre>",
					trackMouse : true,
					onHide : function() {
						$(this).tooltip('destroy');
					},
					showDelay:200,
					hideDelay:0,
					onShow: function(){
						try{
							$(this).tooltip('tip').css({
		                        backgroundColor: 'white',
		                        borderColor: 'red'
		                    });
						}catch(e){
							$('.tooltip').hide();
						}
	                    
	                }
				}).tooltip('show');
			});
		}
	}
});

function _dg_tooltip_remove(){
	$(".dg_tooltip").parent().parent().remove();
}

function showOrHideColumns(){
	
	var cs=$("#"+_current_grid_context_id_).datagrid("getColumnFields");
	//var forzenCs=$("#"+_current_grid_context_id_).datagrid("getColumnFields",true);
	
	$("#_columns_display_columns_").empty();
	$("#_columns_hidden_columns_").empty();
	var options;
	
	/*for(var i=0;i<forzenCs.length;i++){
		options=$("#"+_current_grid_context_id_).datagrid("getColumnOption",forzenCs[i]);
		if(isEmpty(options.title))continue;
		if(options.hidden){
			$("#_columns_hidden_columns_").append("<div field='"+forzenCs[i]+"'>"+options.title+"</div>");
		}else{
			$("#_columns_display_columns_").append("<div field='"+forzenCs[i]+"'>"+options.title+"</div>");
		}
	}*/
	
	for(var i=0;i<cs.length;i++){
		options=$("#"+_current_grid_context_id_).datagrid("getColumnOption",cs[i]);
		if(isEmpty(options.title))continue;
		if(options.hidden){
			$("#_columns_hidden_columns_").append("<div field='"+cs[i]+"'>"+options.title+"</div>");
		}else{
			$("#_columns_display_columns_").append("<div field='"+cs[i]+"'>"+options.title+"</div>");
		}
	}
	
	$("#_columns_display_hidden_opt_").slideDown();
}

function _columns_display_ok(){
	$("#_columns_display_columns_ div").each(function(){
		$("#"+_current_grid_context_id_).datagrid("showColumn",$(this).attr("field"));
	});
	
	$("#_columns_hidden_columns_ div").each(function(){
		$("#"+_current_grid_context_id_).datagrid("hideColumn",$(this).attr("field"));
	});
	$("#"+_current_grid_context_id_).datagrid("fixColumnSize");
	_columns_display_cancel();
}

function _columns_display_cancel(){
	$("#_columns_display_hidden_opt_").slideUp();
}

function _columns_display_hideAll(){
	$("#_columns_hidden_columns_").prepend($("#_columns_display_columns_").children());
}

function _columns_display_showAll(){
	$("#_columns_display_columns_").prepend($("#_columns_hidden_columns_").children());
}

//DataGrid键盘操作事件
$.extend($.fn.datagrid.methods, {
    keyCtr : function (jq) {
        return jq.each(function () {
            var grid = $(this);
            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).unbind("keydown");//先解绑键盘事件，防止多次绑定，会触发多次事件，因此跳行问题严重
            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
                switch (e.keyCode) {
                case 38: // up
                    var selected = grid.datagrid('getSelected');
                    if (selected) {
                        var index = grid.datagrid('getRowIndex', selected);
                        grid.datagrid("unselectAll");
                        if(index==0)index=1;
                        grid.datagrid('selectRow', index - 1);
                    } else {
                        var rows = grid.datagrid('getRows');
                        grid.datagrid('selectRow', rows.length - 1);
                    }
                    break;
                case 40: // down
                    var selected = grid.datagrid('getSelected');
                    if (selected) {
                        var index = grid.datagrid('getRowIndex', selected);
                        grid.datagrid("unselectAll");
                        if(index==grid.datagrid("getRows").length-1)index-=1;
                        grid.datagrid('selectRow', index + 1);
                    } else {
                        grid.datagrid('selectRow', 0);
                    }
                    break;
                case 39://right
                	$(this).find(".datagrid-view2 .datagrid-body").scrollLeft($(this).find(".datagrid-view2 .datagrid-body").scrollLeft()+30);
                	break;
                case 37://left
                	$(this).find(".datagrid-view2 .datagrid-body").scrollLeft($(this).find(".datagrid-view2 .datagrid-body").scrollLeft()-30);
                	break;
                case 36://
                	grid.datagrid("unselectAll");
                	grid.datagrid('selectRow', 0);
                    break;
                case 35:
                	 grid.datagrid("unselectAll");
                	 grid.datagrid('selectRow', grid.datagrid("getRows").length-1);
                	break;
                }
            });
        });
    }
});



function validCode(){
	var _options = $(this).combobox('options');  
    var _data = $(this).combobox('getData');/* 下拉框所有选项 */  
    var _value = $(this).combobox('getValue');/* 用户输入的值 */  
    var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */  
    for (var i = 0; i < _data.length; i++) {  
        if (_data[i][_options.valueField] == _value) {  
            _b=true;  
            break;
        }  
    }  
    if(!_b){  
        $(this).combobox('setValue', '');
        return;
    }
}

function comboFilter(q, row){
	var opts = $(this).combobox('options');
	return row[opts.textField].toUpperCase().indexOf(q.toUpperCase()) !=-1;
}