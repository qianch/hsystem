package com.bluebirdme.mes.siemens.bom.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;

/**
 * @author Goofy
 * @Date 2017年7月20日 下午1:03:26
 */
@Desc("组套BOM")
@Entity
@Table(name="hs_siemens_suit_bom")
public class Suit extends BaseSuit {
	
}
