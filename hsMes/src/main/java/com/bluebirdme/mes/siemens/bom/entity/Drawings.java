package com.bluebirdme.mes.siemens.bom.entity;

import com.bluebirdme.mes.core.annotation.Desc;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 图纸BOM
 *
 * @author Goofy
 * @Date 2017年7月18日 上午11:23:21
 */
@Desc("图纸BOM")
@Entity
@Table(name = "hs_siemens_drawings_bom")
public class Drawings extends BaseDrawings implements Comparable<Drawings> {
    @Override
    public int compareTo(Drawings d) {
        return this.getPrintSort() - d.getPrintSort();
    }
}
