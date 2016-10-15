/**
 * 
 */
package nz.co.dimu.message.generator;

import java.util.List;
import java.util.Map;

/**
 * 保存xml中的信息以及根据xml生成的信息，用来替换模版中的变量。
 * 
 * @author huanghao
 *
 */
public class MessageMetaData {

	private String packageName;
	private String className;
//	private String fields;
	private List<Map<String, String>> fields;
	private String controlCode;
	private String fieldNames;
//	private String fieldLength;
	private List<Map<String, String>> fieldLength;
//	private String fieldTypes;
	private List<Map<String, String>> fieldTypes;
//	private String getSetMethods;
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
//	/**
//	 * @return the fields
//	 */
//	public String getFields() {
//		return fields;
//	}
//	/**
//	 * @param fields the fields to set
//	 */
//	public void setFields(String fields) {
//		this.fields = fields;
//	}
	/**
	 * @return the controlCode
	 */
	public String getControlCode() {
		return controlCode;
	}
	/**
	 * @param controlCode the controlCode to set
	 */
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	/**
	 * @return the fieldNames
	 */
	public String getFieldNames() {
		return fieldNames;
	}
	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}
//	/**
//	 * @return the fieldLength
//	 */
//	public String getFieldLength() {
//		return fieldLength;
//	}
//	/**
//	 * @param fieldLength the fieldLength to set
//	 */
//	public void setFieldLength(String fieldLength) {
//		this.fieldLength = fieldLength;
//	}
//	/**
//	 * @return the fieldTypes
//	 */
//	public String getFieldTypes() {
//		return fieldTypes;
//	}
//	/**
//	 * @param fieldTypes the fieldTypes to set
//	 */
//	public void setFieldTypes(String fieldTypes) {
//		this.fieldTypes = fieldTypes;
//	}
//	/**
//	 * @return the getSetMethods
//	 */
//	public String getGetSetMethods() {
//		return getSetMethods;
//	}
//	/**
//	 * @param getSetMethods the getSetMethods to set
//	 */
//	public void setGetSetMethods(String getSetMethods) {
//		this.getSetMethods = getSetMethods;
//	}
	/**
	 * @return the fields
	 */
	public List<Map<String, String>> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Map<String, String>> fields) {
		this.fields = fields;
	}
	/**
	 * @return the fieldLength
	 */
	public List<Map<String, String>> getFieldLength() {
		return fieldLength;
	}
	/**
	 * @param fieldLength the fieldLength to set
	 */
	public void setFieldLength(List<Map<String, String>> fieldLength) {
		this.fieldLength = fieldLength;
	}
	/**
	 * @return the fieldTypes
	 */
	public List<Map<String, String>> getFieldTypes() {
		return fieldTypes;
	}
	/**
	 * @param fieldTypes the fieldTypes to set
	 */
	public void setFieldTypes(List<Map<String, String>> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}
	
	
}
