package nz.co.dimu.mf;

import java.lang.reflect.Constructor;
import java.text.ParseException;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.dimu.message.common.AbstractMessage;
import nz.co.dimu.message.common.MessageI;
import nz.co.dimu.message.exceptions.BadParameterException;
import nz.co.dimu.message.exceptions.InvalidRawMessage;
import nz.co.dimu.message.exceptions.UnsupportedControlCode;
import nz.co.dimu.message.util.ByteConvertor;
import nz.co.dimu.message.util.ChecksumUtil;
import nz.co.dimu.message.util.StringFormat;

/**
 * 解析、校验报文。
 * 根据报文的控制字，判断报文的Java类型，并将报文的数据域初始化为相应的报文类对象。
 * 
 * @author huanghao
 *
 */
public class MessageFactory {
	
	private static final Logger log = LoggerFactory.getLogger(MessageFactory.class);
	
	public MessageI createMessage(byte[] rawMessage) throws InvalidRawMessage, UnsupportedControlCode {
		if (rawMessage == null || rawMessage.length < 12) {
			throw new InvalidRawMessage("报文字节数组为空或者不满足最小长度12.");
		}
		
		// 读取报文内容
		// 开始码
		byte startCode = rawMessage[0];
		// 终端号码
		String terminalNumber = new String(ArrayUtils.subarray(rawMessage, 1, 7));
		// 控制字
		byte controlCode = rawMessage[7];
		String controlCodeStr = Integer.toHexString((controlCode & 0xFF));
		// 读取长度，转换为Integer
		byte[] rawLength = ArrayUtils.subarray(rawMessage, 8, 10);
		byte[] temp = new byte[4];//高位在前
		temp[0] = Integer.valueOf("0", 16).byteValue();
		temp[1] = Integer.valueOf("0", 16).byteValue();
		temp[2] = rawLength[0];
		temp[3] = rawLength[1];
		Integer dataLength = ByteConvertor.toInteger(temp);
		// 校验码
		byte checksum = rawMessage[rawMessage.length - 2];
		// 结束码
		byte endCode = rawMessage[rawMessage.length - 1];
		
		
		// 报文校验
		// 开始码
		if (startCode != AbstractMessage.getStartCode()) {
			throw new InvalidRawMessage("非法报文：报文不是以预设开始码（68H）开始.");
		}
		// 结束码
		if (endCode != AbstractMessage.getEndCode()) {
			throw new InvalidRawMessage("非法报文：报文不是以预设结束码（16H）结束.");
		}
		// 数据域长度
		if (dataLength + 12 != rawMessage.length) {
			throw new InvalidRawMessage("非法报文：数据域长度字段内容与真实的数据域长度不符.");
		}
		// 校验码 
		boolean isChecksum = false;
		try {
			isChecksum = ChecksumUtil.verify(ArrayUtils.subarray(rawMessage, 1, rawMessage.length - 2), checksum);
		} catch (BadParameterException e) {
			isChecksum = false;
		}
		if (!isChecksum) {
			throw new InvalidRawMessage("非法报文：未通过报文校验码检查.");
		}
		// 控制字
		String className = MessageTypeMapper.getInstance().getClassName(controlCodeStr);
		if (className == null) {
			throw new UnsupportedControlCode(StringFormat.format("系统不支持这个控制字：{}.", controlCodeStr));
		}
		
		MessageI result = null;
		try {
			@SuppressWarnings("unchecked")
			Class<MessageI> cls = (Class<MessageI>) Class.forName(className);
			@SuppressWarnings("rawtypes")
			Class[] paramTypes = new Class[]{ String.class };
			Object[] params = new Object[]{ terminalNumber };
			Constructor<MessageI> con = cls.getConstructor(paramTypes); 
			result = con.newInstance(params);
		} catch (Exception e) {
			result = null;
			log.error("加载报文类时失败，报文类名：{}.", className, e);
		}
		
		if (result != null) {
			try {
				result.parse(ArrayUtils.subarray(rawMessage, 10, rawMessage.length - 2));
			} catch (ParseException e) {
				log.error("解析报文数据语时发生异常。", e);
				result = null;
			}
		}
		return result;
	}

}
