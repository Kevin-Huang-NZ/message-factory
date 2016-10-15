/**
 * 
 */
package nz.co.dimu.message.generator;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author huanghao
 *
 */
public class MessageGenerator {

	private static final Logger log = LoggerFactory.getLogger(MessageGenerator.class);
	
	private static String templateFileName = "message.java.vm";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MetaXmlReader metaXmlReader = new MetaXmlReader("meta/00-boot-message.xml");
		MessageMetaData messageMetaData = metaXmlReader.createMetaMessage();

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		FileOutputStream fos = null;
		try {
			cfg.setClassForTemplateLoading(MessageGenerator.class, "/");
			cfg.setSharedVariable("upperFC", new UpperFirstCharacter());
			Template template = cfg.getTemplate(templateFileName); 
			Map data = new HashMap();
			data.put("controlCode", messageMetaData.getControlCode());
			data.put("className", messageMetaData.getClassName());
			data.put("packageName", messageMetaData.getPackageName());
			data.put("fields", messageMetaData.getFields());
			data.put("fieldNames", messageMetaData.getFieldNames());
			data.put("fieldLengths", messageMetaData.getFieldLength());
			data.put("fieldTypes", messageMetaData.getFieldTypes());

			fos = new FileOutputStream("D:/program/DIMU/message/src/main/test.java");
			template.process(data, new OutputStreamWriter(fos,"utf-8"));
			
		} catch (Exception e) {
			log.error("读取模版生成java文件时发生异常.", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e2) {
				}
			}
		}
	}

}
