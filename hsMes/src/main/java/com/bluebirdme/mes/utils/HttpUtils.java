package com.bluebirdme.mes.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Goofy
 * Http下载工具类
 */
public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	/**
	 * 获取项目网络路径
	 * @param request
	 * @return
	 */
	public static String getContentpath(HttpServletRequest request){
		return request.getContextPath();
	}

	/**
     * 获取项目磁盘绝对路径
     */
    public static String getRealPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }

    /**
     * 使用了代理服务器的，无法获取正确地址的，使用这个方法获取访问者的IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
    	String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error(e.getLocalizedMessage(),e);
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

	/**
	 * 下载多个文件
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param files
	 *            File
	 */
	public static void download(HttpServletResponse response, File... files) {
		byte buffer[] = new byte[40960];
		try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(files[0].getName()+"等多个文件.zip", "UTF-8"));
			BufferedInputStream bis = null;
			FileInputStream fis = null;
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

			ZipEntry entry = null;

			zos.setEncoding("GBK");

			for (File file : files) {
				if (file.isDirectory())
					continue;
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);

				int count;
				while ((count = bis.read(buffer)) != -1) {
					zos.write(buffer, 0, count);
				}
				bis.close();
			}
			zos.close();
			if(fis!=null)
				fis.close();
			if(bis!=null)
				bis.close();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
	}

	public static void download(HttpServletResponse response,Workbook wb, String excelName) throws Exception {
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename="+ new String((excelName).getBytes("gbk"), "iso8859-1")+ ".xlsx");
		response.setContentType("application/msexcel");// 定义输出类型
		try {
			wb.write(response.getOutputStream());
            response.getOutputStream().flush();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		} finally {
            response.getOutputStream().close();
			wb.close();
		}
	}
}
