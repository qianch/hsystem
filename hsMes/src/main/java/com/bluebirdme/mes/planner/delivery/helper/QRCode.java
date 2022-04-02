package com.bluebirdme.mes.planner.delivery.helper;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 类注释
 * 
 * @author Goofy
 * @Date 2017年4月8日 下午5:00:34
 */
public class QRCode {
	private static Logger log = LoggerFactory.getLogger(QRCode.class);
	private static final String FORMAT = "PNG";

	public static void encode(String contents, OutputStream stream, int width, int height) throws WriterException, IOException {
		contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height);
		MatrixToImageWriter.writeToStream(matrix, FORMAT, stream);
	}

	/**
	 * 生成二维码
	 * 
	 * @param contents
	 *            内容，换行可以用\n
	 * @param dest
	 *            生成二维码图片地址
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @throws WriterException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void encode(String contents, String dest, int width, int height) throws WriterException, FileNotFoundException, IOException {
		contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height);
		// MatrixToImageWriter.writeToFile(matrix, format, new
		// File(dest));//过时方法不推荐
		MatrixToImageWriter.writeToStream(matrix, FORMAT, new FileOutputStream(new File(dest)));
	}

	/**
	 * 从一张图片解析出二维码信息
	 * 
	 * @param dest
	 *            目标地址
	 * @return String 二维码信息
	 * @throws IOException
	 * @throws NotFoundException
	 * @throws ChecksumException
	 * @throws FormatException
	 */
	public static String decode(String dest) throws IOException, NotFoundException, ChecksumException, FormatException {
		QRCodeReader reader = new QRCodeReader();
		BufferedImage image = ImageIO.read(new File(dest));
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap imageBinaryBitmap = new BinaryBitmap(binarizer);
		Result result = reader.decode(imageBinaryBitmap);
		return result.getText();
	}

	public static void main(String[] args) {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			bufferImg = ImageIO.read(new File("D:\\Target.PNG"));
			ImageIO.write(bufferImg, "jpg", byteArrayOut);
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet1 = wb.createSheet("test picture");
			// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
			// anchor主要用于设置图片的属性
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 55, 55, (short) 0, 0, (short) 2, 7);
			//HSSFClientAnchor anchor=new HSSFClientAnchor();
			anchor.setAnchorType(ClientAnchor.AnchorType.byId(2));
			// 插入图片
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			fileOut = new FileOutputStream("D:/测试Excel.xls");
			// 写入excel文件
			wb.write(fileOut);
			log.info("----Excle文件已生成------");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					log.error(e.getLocalizedMessage(),e);
				}
			}
		}
	}
}
