package com.bluebirdme.mes.task.annotation;

import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.Bx5GScreenProfile;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.ImageFileBxPage;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.DisplayStyleFactory.DisplayStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

/**
 * LED定时发送节目类容
 * 
 * @author Goofy
 * @Date 2016年12月19日 下午3:30:48
 */
//@Component
public class LedTask {
	private static Logger log = LoggerFactory.getLogger(LedTask.class);
	@Resource IWeavePlanService wpService;
	
	private int width=192;
	private int height=128;
	
	final static String path="C:\\LED\\";
	
	static{
		new File(path).mkdirs();
	}
	
	/**
	 * 每隔十分钟，查询一次数据，发送的LED上面去
	 * @throws Exception 
	 */
	@Scheduled(fixedRate = 600000)
	public void creator() throws Exception {
		
		final List<Map<String,Object>> list=wpService.ledWeavePlan(1);
		
		if(ListUtils.isEmpty(list))
			return;
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					Bx5GEnv.initial();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				// 初始化屏幕对象
				Bx5GScreenClient screen = new Bx5GScreenClient("WeavePackagingArea");
				// 连接到屏幕
				if(!screen.connect("10.10.1.192", 5005)){
					log.error("LED:10.10.1.192 连接不上");
					return;
				}
				
				
				try {
					List<File> files= FileUtils.getFileSort(path);
					
					for(File file:files){
						file.delete();
					}
					
					List<Map<String,String>> wps=new ArrayList<Map<String,String>>();
					
					Map<String, String> unit=new HashMap<String, String>();
					
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					
					for(Map<String,Object> m:list){
						
						unit=new HashMap<String, String>();
						
						unit.put("批次号",m.get("BATCHCODE")+"");
						unit.put("客户", m.get("CONSUMERSIMPLENAME")+"");
						unit.put("订单", m.get("SALESORDERCODE")+"");
						unit.put("产品", m.get("PRODUCTMODEL")+"");
						unit.put("卷重/卷长/门幅", (ObjectUtils.isNull(m.get("PRODUCTWEIGHT"))?"-":m.get("PRODUCTWEIGHT"))+"/"+
												  (ObjectUtils.isNull(m.get("PRODUCTLENGTH"))?"-":m.get("PRODUCTLENGTH"))+"/"+
												  (ObjectUtils.isNull(m.get("PRODUCTWIDTH"))?"-":m.get("PRODUCTWIDTH")));
						unit.put("交货日期",sdf.format((Date)m.get("DELEVERYDATE")));
//						unit.put("总数/打包/入库", m.get("TOTALTRAYCOUNT")+"/"+m.get("PACKAGEDCOUNT")+"/"+m.get("")+" 托");
						unit.put("总数/打包", (ObjectUtils.isNull(m.get("TOTALTRAYCOUNT"))?"-":m.get("TOTALTRAYCOUNT"))+"/"+m.get("PACKAGEDCOUNT")+" 托");
						
						wps.add(unit);
					}
					
					
					// 获取切换特技
					DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

					// 获取控制器参数
					Bx5GScreenProfile profile = screen.getProfile();

					// 创建节目
					ProgramBxFile program = new ProgramBxFile("P001", profile);
					// 无边框
					program.setFrameShow(false);
					// 移动速度
					program.setFrameSpeed(20);

					// 创建一个文本区域，待会要在文本区域加入文本页
					TextCaptionBxArea area = new TextCaptionBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
					area.setFrameShow(false);
					
					// 再创建一个数据页，用于显示图片
					ImageFileBxPage iPage ;
					if(wps.size()==0){
						File file = new File(path+UUID.randomUUID().toString()+".bmp");
			        	 BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			             Graphics2D g2 = (Graphics2D)bi.getGraphics();   
			             g2.setBackground(Color.BLACK);   
			             g2.clearRect(0, 0, width, height);
			             g2.setPaint(Color.RED);   
			             g2.drawString("暂无订单", 5, 10);   
			        	
			        	ImageIO.write(bi, "bmp", file);
			        	
			        	iPage = new ImageFileBxPage(file.getAbsolutePath());
			    		// 调整特技方式
			    		iPage.setDisplayStyle(styles[6]);
			    		// 调整特技速度
			    		iPage.setSpeed(5);
			    		// 调整停留时间, 单位 10ms
			    		iPage.setStayTime(400);
			    		
			    		area.addPage(iPage);
					}
					for(Map<String,String> u:wps){
						Iterator<Entry<String,String>> it=u.entrySet().iterator();
			        	int j=1;
			        	File file = new File(path+UUID.randomUUID().toString()+".bmp");
			        	 BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			             Graphics2D g2 = (Graphics2D)bi.getGraphics();   
			             g2.setBackground(Color.BLACK);   
			             g2.clearRect(0, 0, width, height);
			             g2.setPaint(Color.RED);   
			        	while(it.hasNext()){
			        		Entry<String,String> entry=it.next();
			                g2.drawString(entry.getKey()+"："+entry.getValue(), 5, (10*(j++))+((j-1)*7));   
			        	}
			        	
			        	ImageIO.write(bi, "bmp", file);
			        	
			        	iPage = new ImageFileBxPage(file.getAbsolutePath());
			    		// 调整特技方式
			    		iPage.setDisplayStyle(styles[6]);
			    		// 调整特技速度
			    		iPage.setSpeed(5);
			    		// 调整停留时间, 单位 10ms
			    		iPage.setStayTime(400);
			    		
			    		area.addPage(iPage);
			        	
					}
					
					program.addArea(area);
					
					List<ProgramBxFile> programs=new ArrayList<ProgramBxFile>();
					programs.add(program);
					
					screen.deletePrograms();
					
					screen.writePrograms(programs);
					
					screen.disconnect();
				} catch (Exception e) {
					screen.disconnect();
				}
				
			}
		}).start();
	}
}
