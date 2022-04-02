/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 
 * @author Goofy
 * @Date 2017年11月16日 下午3:48:56
 */
@Entity
@Table(name = "hs_ftc_bc_bom")
public class FtcBcBom extends BaseEntity {

	@Column
	private Long pid;

	@Index(name="code")
	@Column(nullable=false,unique=true)
	private String code;

	@Column(nullable=false)
	private String name;

	@Column(nullable=false)
	private Integer level;

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Desc("是否作废，-1作废")
	@Column(name="packCanceled")
	private Integer packCanceled;
	public Integer getPackCanceled() {
		return packCanceled;
	}
	public void setPackCanceled(Integer packCanceled) {
		this.packCanceled = packCanceled;
	}

}
