/**
 * byte与其他数据类型的相互转换。
 */
package nz.co.dimu.message.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.dimu.message.common.FieldTypeMap;

/**
 * @author huanghao
 *
 */
public class ByteConvertor {
	
	private static final Logger log = LoggerFactory.getLogger(ByteConvertor.class);
	
	private static final String CHARSET_NAME = "UTF-8";
	
	public static byte[] convertToByte(Object data, String dataType) {
		String methodName = "from" +  StringUtils.capitalize(dataType);  
		try {
			Method method = ByteConvertor.class.getMethod(methodName, FieldTypeMap.data.get(dataType));
			return (byte[])method.invoke(ByteConvertor.class, data);
		} catch (Exception e) {
			log.error("系统目前不支持这种数据类型：{}。", dataType, e);
			return null;
		}
		
	}
	
	public static Object convertFromByte(byte[] data, String dataType) {
		String methodName = "to" + StringUtils.capitalize(dataType); 
		try {
			Method method = ByteConvertor.class.getMethod(methodName, byte[].class);
			return method.invoke(ByteConvertor.class, data);
		} catch (Exception e) {
			log.error("系统目前不支持这种数据类型：{}。", dataType, e);
			return null;
		}
		
	}
	
	/**
	 * 将字符串转换成byte数组.
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] fromString(String data) {
		try {
			return data.getBytes(CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 将byte数组转换成字符串.
	 * 
	 * @param data
	 * @return
	 */
	public static String toString(byte[] data) {
		try {
			return new String(data, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 将规约版本号转换成byte数组。
	 * 版本号格式格式Vx.y，其中x和y为整数，x占高位byte，y占低位byte。
	 * 版本号格式不满足要求，返回null。
	 * 
	 * @param version 
	 * @return
	 */
	public static byte[] fromVersion(String version) {
		if (version == null || version.length() < 4 || version.indexOf(".") < 0) {
			log.error("参数规约版本号：{}，格式不正确1。", version);
			return null;
		}
		
		String temp = version.substring(1);
		String[] arr = temp.split("\\.");
		if (arr.length != 2) {
			log.error("参数规约版本号：{}，格式不正确2。", version);
			return null;
		}
		
		String mainV = arr[0];
		String subV = arr[1];
		
		byte[] result = new byte[2];
		try {
			result[0] = Integer.valueOf(mainV).byteValue();
			result[1] = Integer.valueOf(subV).byteValue();
		} catch (Exception e) {
			log.error("参数规约版本号：{}，格式不正确3。", version);
			return null;
		}
		
		return result;
	}
	
	/**
	 * 将长度为2的byte数组转换为规约版本号字符串。
	 * 
	 * @param data
	 * @return
	 */
	public static String toVersion(byte[] data) {
		if (data == null || data.length != 2) {
			log.error("规约版本号的byte数组长度应该为2，传入的参数数组长度为：{}。", data.length);
			return null;
		}
		String result = "V" + Integer.valueOf(data[0]).toString() + "." + Integer.valueOf(data[1]).toString();
		return result;
		
	}
	
	/**
	 * 将Short类型的数据转换为byte数组，长度为2.(高位在前，低位在后)
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] fromShort(Short data) {
		byte[] result = new byte[2];

		result[0] = (byte) ((data>>8) & 0xFF);
		result[1] = (byte) (data & 0xFF);
		
		return result;
	}
	
	/**
	 * 将2个长度的byte数组转换为short类型。(高位在前，低位在后)
	 * 
	 * @param data
	 * @return
	 */
	public static Short toShort(byte[] data) {
		Short result;
		
		result = (short) (((data[0] & 0xFF)<<8) | (data[1] & 0xFF));
		
		return result;
	}
	
	/**
	 * 将Integer类型的数据转换为byte数组，长度为4.(高位在前，低位在后)
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] fromInteger(Integer data) {
		byte[] result = new byte[4];

		result[0] = (byte) ((data>>24) & 0xFF);
		result[1] = (byte) ((data>>16) & 0xFF);
		result[2] = (byte) ((data>>8) & 0xFF);
		result[3] = (byte) (data & 0xFF);
		
		return result;
	}
	
	/**
	 * 将4个长度的byte数组转换为Integer类型。(低位在后，高位在前)
	 * 
	 * @param data
	 * @return
	 */
	public static Integer toInteger(byte[] data) {
		Integer result;
		
		result = (int) (((data[0] & 0xFF)<<24)
				| ((data[1] & 0xFF)<<16)
				| ((data[2] & 0xFF)<<8)
				| (data[3] & 0xFF));
		
		return result;
	}
}
