/**
 * 
 */
package nz.co.dimu.message.entities;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.dimu.message.common.AbstractMessage;
import nz.co.dimu.message.common.FieldTypeMap;
import nz.co.dimu.message.exceptions.PacketException;
import nz.co.dimu.message.exceptions.ParseException;
import nz.co.dimu.message.util.ByteConvertor;
import nz.co.dimu.message.util.StringFormat;

/**
 * @author huanghao
 *
 */
public class BootMessage extends AbstractMessage {
	
	private static Logger log = LoggerFactory.getLogger(BootMessage.class);
	
	// 规约版本号
	private String versionCode;
	
	public BootMessage(String terminalNumber) {
		// 设置终端号码
		super.setTerminalNumber(terminalNumber);
		// 设置控制字
		super.setControlCode(Integer.valueOf("00", 16).byteValue());
		// 设置数据域数组
		super.dataFields = new String[]{"versionCode"};
		// 设置数据域长度Map
		super.dataFieldLength = new HashMap<String, Integer>();
		super.dataFieldLength.put("versionCode", 2);
		// 设置数据域类型Map
		super.dataFieldType = new HashMap<String, String>();
		super.dataFieldType.put("versionCode", "version");
	}

	/* (non-Javadoc)
	 * @see nz.co.dimu.message.common.AbstractMessage#getDataProperties()
	 */
	@Override
	protected byte[] getDataProperties() throws PacketException {
		byte[] result = new byte[0];
		// 循环数据域数组，按照顺序获取每一个类属性的值，并转换为byte数组合并到最终计算结果中
		for (String fieldName : super.dataFields) {
			String fieldNameCaps = StringUtils.capitalize(fieldName); 
			try {
				Method method = this.getClass().getMethod("get" + fieldNameCaps);
				Object value = method.invoke(this);
				if (value == null) {
					log.error("java对象的属性值为null,属性名称：{}.", fieldName);
					throw new PacketException(StringFormat.format("java对象的属性值为null,属性名称：{}.", fieldName));
				}
				byte[] fieldBytes = ByteConvertor.convertToByte(value, super.dataFieldType.get(fieldName));
				result = ArrayUtils.addAll(result, fieldBytes);
			} catch (Exception e) {
				log.error("反射执行BootMessage类对象的get方法失败.", e);
				throw new PacketException("反射执行BootMessage类对象的get方法失败。", e);
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see nz.co.dimu.message.common.AbstractMessage#setDataProperties(byte[])
	 */
	@Override
	protected void setDataProperties(byte[] data) throws ParseException {
		// TODO
		// 参数data在MessageFactory中已经经过了校验，这里默认都是合法的
		int index = 0;
		for (String fieldName : super.dataFields) {
			String fieldType = super.dataFieldType.get(fieldName);
			Integer fieldLength = super.dataFieldLength.get(fieldName);
			String fieldNameCaps = StringUtils.capitalize(fieldName);
			try {
				byte[] subData = ArrayUtils.subarray(data, index, index + fieldLength.intValue());
				Object value = ByteConvertor.convertFromByte(subData, fieldType);
				// 
				if (value == null) {
					log.error("数据域中的某个数据转换java属性时失败.属性名：{}.", fieldName);
					throw new ParseException(StringFormat.format("数据域中的某个数据转换java属性时失败.属性名：{}.", fieldName));
				}
				Method method = this.getClass().getMethod("set" + fieldNameCaps, FieldTypeMap.data.get(fieldType));
				method.invoke(this, value);
			} catch (Exception e) {
				log.error("反射执行BootMessage类对象的set方法失败.", e);
				throw new ParseException("反射执行BootMessage类对象的set方法失败。", e);
			}
		}
	}

	/**
	 * @return the versionCode
	 */
	public String getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	
	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(BootMessage.class);
		BootMessage bootMessage = new BootMessage("ABCDEF");
		bootMessage.setVersionCode("V127.22");
		byte[] a = bootMessage.getDataProperties();
		log.info("V{}.{}", Integer.valueOf(a[0]), Integer.valueOf(a[1]));
		
		a[0] = Integer.valueOf(33).byteValue();
		bootMessage.setDataProperties(a);
		byte[] b = bootMessage.getDataProperties();
		log.info("V{}.{}", Integer.valueOf(b[0]), Integer.valueOf(b[1]));
		
		byte[] packetMessage = bootMessage.packet();
		int msgLen = packetMessage.length;
		log.info("报文长度：{}。", msgLen);
		if (msgLen >= 12) {
			log.info("起始码: {}H", Integer.toHexString(Integer.valueOf(packetMessage[0])));
			log.info("终端号码: {}", new String(ArrayUtils.subarray(packetMessage, 1, 7)));
			log.info("控制字: {}H", Integer.toHexString(Integer.valueOf(packetMessage[7])));		
			byte[] rawLength = ArrayUtils.subarray(packetMessage, 8, 10);
			byte[] temp = new byte[4];//高位在前
			temp[0] = Integer.valueOf("0", 16).byteValue();
			temp[1] = Integer.valueOf("0", 16).byteValue();
			temp[2] = rawLength[0];
			temp[3] = rawLength[1];
			log.info("数据长度: {}", ByteConvertor.toInteger(temp));
			log.info("校验码: {}", Integer.valueOf(packetMessage[msgLen - 2]));
			log.info("结束码: {}H", Integer.toHexString(Integer.valueOf(packetMessage[msgLen - 1])));
		}
	}
	
}
