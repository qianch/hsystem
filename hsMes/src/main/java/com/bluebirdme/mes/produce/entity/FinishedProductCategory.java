package com.bluebirdme.mes.produce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 
 * 成品类别管理
 * @author king
 *
 */

@Desc("成品类别管理")
@Entity
@Table(name="HS_FINISHED_PRODUCT_CATEGORY")
@DynamicInsert
public class FinishedProductCategory extends BaseEntity {
	
	
	@Desc("类别编号")
	@Column(nullable=false,unique=true)
	private String categoryCode;
	
	
	@Desc("类别名称")
	@Column(nullable=false,unique=true)
	private String categoryName;
	
	@Desc("创建人")
	@Column(nullable=true)
	private String creater;
	
	@Desc("创建时间")
	@Column
	private Date createTime;
	
	@Desc("修改人")
	@Column
	private String  modifyUser;
	
	@Desc("修改时间")
	@Column
	private Date modifyTime;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
