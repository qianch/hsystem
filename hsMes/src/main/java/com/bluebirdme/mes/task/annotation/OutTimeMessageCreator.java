package com.bluebirdme.mes.task.annotation;

import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanSalesOrders;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 每天1点自动生成订单超期和发货超期提醒
 * 
 * @author 徐波`
 * @Date 2017年03月10日 上午8:58:32
 */
//@Component
public class OutTimeMessageCreator {
	private static Logger log = LoggerFactory.getLogger(OutTimeMessageCreator.class);
	@Resource
	IMessageCreateService msgService;

	@Scheduled(cron = "0 38 14 * * ?")
	// （秒 分 时 日 月 星期 年）
	// @Scheduled(cron="0 0 1 * * ?")
	public void creator() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("isComplete", -1);
		map.put("isClosed", null);
		List<DeliveryPlan> dplist = msgService.findListByMap(
				DeliveryPlan.class, map);
		map.clear();
		map.put("isComplete", null);
		map.put("closed", null);
		List<SalesOrderDetail> sodList = msgService.findListByMap(
				SalesOrderDetail.class, map);
		for (DeliveryPlan dp : dplist) {
			// 查找过期的所有出货计划
			if (dp.getDeliveryDate().before(new Date())) {
				//需要自动关闭的超期时间
				Date outDate = new Date(new Date().getTime() - 7 * 24 * 60 * 60
						* 1000);
				//关闭所有超过超期时间的计划
				if (dp.getDeliveryDate().before(outDate)) {
					// 如果出货计划中的订单有已经出货的，不操作
					map.clear();
					map.put("deliveryId", dp.getId());
					List<DeliveryPlanSalesOrders> dpsoList = msgService
							.findListByMap(DeliveryPlanSalesOrders.class, map);
					boolean closeAble = true;
					for (DeliveryPlanSalesOrders dpso : dpsoList) {
						if (dpso.getIsFinished()==1) {
							closeAble = false;
							break;
						}
					}
					//自动关闭的计划，发出计划关闭信息
					if (closeAble) {
						dp.setIsClosed(1);
						msgService.save(dp);
						String comment = "发货计划关闭，发货计划号：" + dp.getDeliveryCode()+";超时自动关闭！";
						msgService.createClose(comment,MessageType.DELIVERY_PLAN_CLOSE);
					}
				} else {
					//未到达超期时间，并且已过发货时间的计划，发出超期提醒信息
					msgService.createOutDate(dp);
				}
			}
		}
		for (SalesOrderDetail sod : sodList) {
			msgService.createOutDate(sod);
		}
	}
}
