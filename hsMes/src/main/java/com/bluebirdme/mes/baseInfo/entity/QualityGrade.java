package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

/**
 * 质量等级实体类
 * @author Goofy
 * @Date 2016年10月12日 上午10:22:31
 */
@Desc(" 质量等级")
@Entity
@Table(name="hs_quality_grade")
public class QualityGrade extends BaseEntity {
	
	@Desc("等级名称")
	@Column(nullable=false)
	private String gradeName;
	
	@Desc("等级描述")
	@Column(nullable=true)
	private String gradeDesc;
	
	@Desc("备注")
	@Column(nullable=true,length = 16777215)
	private String gradeMemo;

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
	}

	public String getGradeMemo() {
		return gradeMemo;
	}

	public void setGradeMemo(String gradeMemo) {
		this.gradeMemo = gradeMemo;
	}
	
	public static void main(String[] args) throws Exception {
		DevHelper.genAll(QualityGrade.class, "高飞");
	}
	
}
