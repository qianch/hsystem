package com.bluebirdme.mes.planner.cut.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("日计划和裁剪计划表")
@Entity
@Table(name = "hs_Cut_Daily_Plan_Detail")
public class CutDailyPlanDetail extends BaseEntity {
    @Desc("裁剪日计划ID")
    @Column(nullable = false)
    @Index(name = "cutPlanDailyId")
    private Long cutPlanDailyId;

    @Desc("裁剪计划ID")
    @Column(nullable = false)
    @Index(name = "cutPlanId")
    private Long cutPlanId;

    @Desc("套数")
    @Column(nullable = false)
    private Integer count;

    @Desc("用户和数量")
    @Column(nullable = false, columnDefinition = "text")
    private String userAndCount;

    @Desc("备注")
    private String comment;

    @Desc("该裁剪计划分配的所有用户的ID")
    @Column(nullable = false, columnDefinition = "text")
    private String userIds;

    @Desc("各个部件的数量")
    @Column(columnDefinition = "text")
    private String counts;

    @Desc("所有的部件")
    @Column(columnDefinition = "text")
    private String partNames;
    @Desc("所有的ID")
    @Column(columnDefinition = "text")
    private String partids;

    public String getPartids() {
        return partids;
    }

    public void setPartids(String partids) {
        this.partids = partids;
    }

    public Long getCutPlanDailyId() {
        return cutPlanDailyId;
    }

    public void setCutPlanDailyId(Long cutPlanDailyId) {
        this.cutPlanDailyId = cutPlanDailyId;
    }

    public Long getCutPlanId() {
        return cutPlanId;
    }

    public void setCutPlanId(Long cutPlanId) {
        this.cutPlanId = cutPlanId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUserAndCount() {
        return userAndCount;
    }

    public void setUserAndCount(String userAndCount) {
        this.userAndCount = userAndCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getPartNames() {
        return partNames;
    }

    public void setPartNames(String partNames) {
        this.partNames = partNames;
    }

}
