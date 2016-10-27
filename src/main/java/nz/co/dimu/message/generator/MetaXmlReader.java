/**
 * 
 */
package nz.co.dimu.message.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.dimu.message.common.FieldTypeMap;
import nz.co.dimu.message.generator.exceptions.InvalidXmlException;
import nz.co.dimu.message.util.StringFormat;
import nz.co.dimu.mf.MessageTypeMapper;

/**
 * 读取、解析xml文件，并生成MessageMetaData对象。
 * 
 * @author huanghao
 *
 */
public class MetaXmlReader {
	
	private static final Logger log = LoggerFactory.getLogger(MetaXmlReader.class);

	private SAXReader saxReader;
	
	// 
	private String fileName;
	
	private String packageName;
	
	private String className;
	
	private String controlCode;
	
	private List<String> fields = new ArrayList<String>();
	
	private Map<String, String> fieldLength = new HashMap<String, String>();
	
	private Map<String, String> fieldTypes = new HashMap<String, String>();
	
	public MetaXmlReader(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 读取xml文件，生成MessageMetaData类对象。
	 * 
	 * @return
	 * @throws InvalidXmlException
	 */
	public MessageMetaData createMetaMessage() throws InvalidXmlException {
//		StringBuffer fieldBuf = new StringBuffer();
		StringBuffer fieldNameBuf = new StringBuffer();
//		StringBuffer fieldLengthBuf = new StringBuffer();
//		StringBuffer fieldTypeBuf = new StringBuffer();
//		StringBuffer getSetMethodBuf = new StringBuffer();
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fieldLengths = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fieldTypes = new ArrayList<Map<String, String>>();
		// 加载xml内容
		loadXml();
		
		// 根据xml内容，生成报文类的数据域、get/set方法、数据域名称列表、类型列表、长度列表
		int index = 0;
		for (String fieldName : this.fields) {
			String length = this.fieldLength.get(fieldName);
			String type = this.fieldTypes.get(fieldName);
			Class<?> typeClass = FieldTypeMap.data.get(type);
			if (index > 0) {
//				fieldBuf.append("\n");
				fieldNameBuf.append(",");
//				fieldLengthBuf.append("\n");
//				fieldTypeBuf.append("\n");
//				getSetMethodBuf.append("\n\n");
			}
			
//			fieldBuf.append(this.createField(field, typeClass));
			fieldNameBuf.append(this.createFieldName(fieldName));
//			fieldLengthBuf.append(this.createFieldLength(field, length));
//			fieldTypeBuf.append(this.createFieldType(field, type));
//			getSetMethodBuf.append(this.createGetSetMethod(field, typeClass));
			Map<String, String> field = new HashMap<String, String>();
			field.put("name", fieldName);
			field.put("typeClass", typeClass.getName());
			fields.add(field);

			Map<String, String> fieldLength = new HashMap<String, String>();
			fieldLength.put("name", fieldName);
			fieldLength.put("length", length);
			fieldLengths.add(fieldLength);
			
			Map<String, String> fieldType = new HashMap<String, String>();
			fieldType.put("name", fieldName);
			fieldType.put("type", type);
			fieldTypes.add(fieldType);
			
			index++;
		}
				
		MessageMetaData metaMessage = new MessageMetaData();
		metaMessage.setPackageName(packageName);
		metaMessage.setClassName(className);
		metaMessage.setFields(fields);
		metaMessage.setControlCode(controlCode);
		metaMessage.setFieldNames(fieldNameBuf.toString());
		metaMessage.setFieldLength(fieldLengths);
		metaMessage.setFieldTypes(fieldTypes);
//		metaMessage.setGetSetMethods(getSetMethodBuf.toString());
		return metaMessage;
	}
	
	// 读取xml文件
	private void loadXml() throws InvalidXmlException {
		saxReader = new SAXReader();
		InputStream fileStream = null;
        try {
        	fileStream = MessageTypeMapper.class.getClassLoader().getResourceAsStream(fileName);
            Document document = saxReader.read(fileStream);
            Element root = document.getRootElement();
            this.controlCode = root.selectSingleNode("//controlCode").getText();
            this.packageName = root.selectSingleNode("//packageName").getText();
            this.className = root.selectSingleNode("//className").getText();
            
            List<Node> fieldList = root.selectNodes("//field");
            for (Node field : fieldList) {
            	String fieldName = field.selectSingleNode("name").getText();
            	this.fields.add(fieldName);
            	this.fieldTypes.put(fieldName, field.selectSingleNode("type").getText());
            	this.fieldLength.put(fieldName, field.selectSingleNode("length").getText());
            }
        } catch (DocumentException e) {
        	log.error("报文类xml配置文件读取失败：{}", fileName, e);
        	throw new InvalidXmlException(StringFormat.format("报文类xml配置文件读取失败：{}", fileName), e);
        } finally {
        	if (fileStream != null) {
        		try {
					fileStream.close();
				} catch (IOException e) {
					log.error("关闭报文定义xml文件输入流时出现异常。文件名称：{}。", fileName, e);
				}
        	}
        }
	}
	
//	private String createField(String name, Class<?> fieldClass) {
//		return "private " + fieldClass.getName() + " " + name + ";";
//	}
	
	private String createFieldName(String name) {
		return "\"" + name + "\"";
	}
	
//	private String createFieldLength(String name, Integer length) {
//		return "super.dataFieldLength.put(\"" + name + "\", " + length + ");";
//	}
//	
//	private String createFieldType(String name, String type) {
//		return "super.dataFieldLength.put(\"" + name + "\", \"" + type + "\");";
//	}
//	
//	private String createGetSetMethod(String name, Class<?> fieldClass) {
//		StringBuffer sb = new StringBuffer();
//		String capName = StringUtils.capitalize(name); 
//		sb.append("public ").append(fieldClass.getName()).append(" get").append(capName).append("() {");
//		sb.append("\n");
//		sb.append("    return this.").append(name).append(";");
//		sb.append("\n");
//		sb.append("}");
//		sb.append("\n");
//		sb.append("\n");
//		sb.append("public void set").append(capName).append("(").append(fieldClass.getName()).append(" ").append(name).append(") {");
//		sb.append("\n");
//		sb.append("    this.").append(name).append(" = ").append(name).append(";");
//		sb.append("\n");
//		sb.append("}");
//		
//		return sb.toString();
//	}
}
