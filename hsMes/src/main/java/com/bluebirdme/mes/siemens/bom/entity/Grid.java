package com.bluebirdme.mes.siemens.bom.entity;

import java.util.List;

import com.bluebirdme.mes.core.annotation.Desc;

/**
 * EasyUI表格对应传过来的数据
 * @author Goofy
 * @Date 2017年7月20日 下午1:07:18
 */
@Desc("Grid")
public class Grid<T> {

	private List<T> inserted;
	
	private List<T> deleted;
	
	private List<T> updated;
	

	public List<T> getInserted() {
		return inserted;
	}

	public void setInserted(List<T> inserted) {
		this.inserted = inserted;
	}

	public List<T> getDeleted() {
		return deleted;
	}

	public void setDeleted(List<T> deleted) {
		this.deleted = deleted;
	}

	public List<T> getUpdated() {
		return updated;
	}

	public void setUpdated(List<T> updated) {
		this.updated = updated;
	}
	
}
