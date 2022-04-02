/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.xdemo.superutil.j2se.FileUtils;


/**
 * 系统硬件信息和进程
 * @author Goofy
 * @Date 2018年4月10日 上午10:07:39
 */
public class SystemInfo {
	
Sigar sigar=new Sigar();
	
	private static volatile SystemInfo instance=null;
	
    public static SystemInfo getInstance(){
            synchronized(SystemInfo.class){
                if(instance==null){
                    instance=new SystemInfo();
                }
            }
        return instance;
    }
    private SystemInfo(){}

	/**
	 * 内存使用情况
	 * @return
	 * @throws SigarException
	 */
	public  Mem memory() throws SigarException {
		return sigar.getMem();
	}

	/**
	 * CPU使用率
	 * @throws SigarException
	 */
	public  CpuPerc[] cpu() throws SigarException {
		return sigar.getCpuPercList();
	}

	/**
	 * 任务列表
	 * @return
	 * @throws IOException
	 */
	public  String tasklist() throws IOException {
		
		ProcessBuilder pb = new ProcessBuilder("tasklist");
		Process p = pb.start();
		
		BufferedReader out = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
		BufferedReader err = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getErrorStream())));
		
		String outString;
		StringBuffer sb=new StringBuffer();
		
		while ((outString = out.readLine()) != null){
			sb.append(outString+"\n");
		}
		
		String errString = err.readLine();
		if (errString != null) {
			sb.append(errString+"\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * 文件系统使用率
	 * @throws Exception
	 */
	public  List<DiskDriver> file() throws Exception {
		List<DiskDriver> list=new ArrayList<DiskDriver>();
		FileSystem[] fsList= sigar.getFileSystemList();
		DiskDriver dd;
		for(FileSystem fs:fsList){
			if(fs.getType()!=2)continue;
			dd=new DiskDriver(fs,sigar.getFileSystemUsage(fs.getDirName()));
			list.add(dd);
		}
		return list;
	}

	public static String sizeFormat(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String formated = "";
		if (size <= 0) {
			formated = "0 B";
		} else if (size < 1024L) {
			formated = df.format((double) size) + " B";
		} else if (size < 1048576L) {
			formated = df.format((double) size / 1024L) + " KB";
		} else if (size < 1073741824L) {
			formated = df.format((double) size / 1048576L) + " MB";
		} else if (size < 1099511627776L) {
			formated = df.format((double) size / 1073741824L) + " GB";
		} else {
			formated = df.format((double) size / 1099511627776L) + " TB";
		}
		return formated;
	}
	
	public void log(String dir) throws Exception{
		
		Long now=System.currentTimeMillis();
		
		CpuPerc[] cpus = cpu();
		
		List<DiskDriver> disks = file();
		
		String tasklist=tasklist();
		
		Mem m=memory();
		
		new File(dir).mkdirs();
		String fileName=dir+"系统状态.txt";
		File file=new File(fileName);
		if(file.exists()){
			Long lastModifyTime=file.lastModified();
			if(now-lastModifyTime>120000){//如果文件上次修改时间超过2分钟，那么备份该文件，生产新文件
				FileUtils.renameFile(fileName, dir+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(now))+".txt");
			}
		}
		
		DecimalFormat format=new DecimalFormat("#.00");
		
		writeToFile("服务器状态监控\n\n记录时间\t"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n\n", fileName, false, true);
		
		writeToFile("======================== 内存 ========================", fileName, true, true);
		writeToFile("内存总量\t"+SystemInfo.sizeFormat(m.getTotal()), fileName, true, true);
		writeToFile("已用内存\t"+SystemInfo.sizeFormat(m.getUsed()), fileName, true, true);
		writeToFile("可用内存\t"+SystemInfo.sizeFormat(m.getFree()), fileName, true, true);
		writeToFile("使用率　\t"+format.format(m.getUsedPercent())+" %", fileName, true, true);
		writeToFile("======================== CPU ========================", fileName, true, true);
		int i=0;
		for(CpuPerc cpu:cpus){
			writeToFile("CPU "+(++i)+"\t使用率\t"+format.format(cpu.getCombined()*100D)+" %", fileName, true, true);
		}
		writeToFile("======================== 磁盘 ========================", fileName, true, true);
		for(DiskDriver dd:disks){
			writeToFile(dd.getFileSystem().getDirName(), fileName, true, true);
			writeToFile("\t总容量　\t"+SystemInfo.sizeFormat(dd.getFileSystemUsage().getTotal()*1024), fileName, true, true);
			writeToFile("\t已使用　\t"+SystemInfo.sizeFormat(dd.getFileSystemUsage().getUsed()*1024), fileName, true, true);
			writeToFile("\t剩余　　\t"+SystemInfo.sizeFormat(dd.getFileSystemUsage().getFree()*1024), fileName, true, true);
			writeToFile("\t使用率　\t"+format.format(dd.getFileSystemUsage().getUsePercent()*100D)+" %", fileName, true, true);
		}
		writeToFile("======================== 系统进程 ========================", fileName,true, true);
		writeToFile(tasklist, fileName,true, true);
	}
	
	public static void writeToFile(String content, String dest, boolean append,
			boolean newLine) throws IOException {
		writeToFile(content, dest, append, newLine,Charset.forName("UTF-8"));
	}
	public static void writeToFile(String content, String dest, boolean append,
			boolean newLine,Charset charset) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dest, append),charset);
		writer.write(content + (newLine == true ? System.getProperty("line.separator") : ""));
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		SystemInfo.getInstance().log("D:\\");
	}
	
}
