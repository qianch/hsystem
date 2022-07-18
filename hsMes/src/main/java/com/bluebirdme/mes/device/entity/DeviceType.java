/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备类型
 *
 * @author 宋黎明
 * @Date 2016年09月28日 上午10:02:34
 */
@Desc("设备类型")
@Entity
@Table(name = "HS_DEVICETYPE")
@DynamicInsert
public class DeviceType extends BaseEntity {
    @Desc("设备类别名称")
    @Column(nullable = false)
    private String categoryName;

    @Desc("父节点ID")
    @Column
    @Index(name = "categoryParentId")
    private Long categoryParentId;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Long categoryParentId) {
        this.categoryParentId = categoryParentId;
    }


}
