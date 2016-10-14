/**
 * 
 */
package nz.co.dimu.message.common;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import nz.co.dimu.message.exceptions.PacketException;
import nz.co.dimu.message.exceptions.ParseException;
import nz.co.dimu.message.util.ByteConvertor;
import nz.co.dimu.message.util.ChecksumUtil;

/**
 * @author huanghao
 *
 */
public abstract class AbstractMessage implements MessageI {
	// 起始码
	private static final byte startCode = Integer.valueOf("68", 16).byteValue();
	
	// 终端号码
	// 子类对象初始化时赋值。
	private String terminalNumber;
	
	// 控制字
	// 子类构造函数中赋值，每个子类对应一个唯一的固定值。
	private byte controlCode;
	
	// 结束码
	private static final byte endCode = Integer.valueOf("16", 16).byteValue();
	
//	// 报文固定域的长度
//	// 起始码: 1; 终端号码: 6; 控制字: 1; 数据长度: 2;  校验码: 1;  结束码: 1; 
//	private final int fixedLength = 1 + 6 + 1 + 2 + 1 + 1;

	// 数据域数组(按照预定的顺序)
	// 子类构造函数中赋值.
	protected String[] dataFields;
	
	// 数据域长度Map
	// 子类构造函数中赋值.
	protected Map<String, Integer> dataFieldLength;
	
	// 数据域类型Map
	// 子类构造函数中赋值.
	protected Map<String, String> dataFieldType;
	
	
	/**
	 * 读取java类对象的属性值，生成报文的数据域。
	 * 
	 * @return 报文数据域
	 */
	protected abstract byte[] getDataProperties() throws PacketException;
	
	/**
	 * 解析报文数据域，设置到相应的类对象的属性值。
	 * 
	 * @param data
	 */
	protected abstract void setDataProperties(byte[] data) throws ParseException;

	@Override
	public byte[] packet() throws PacketException {
		//读取数据域
		byte[] data = getDataProperties();
		
		//计算数据域长度
		//TODO
		byte[] dataLength = ArrayUtils.subarray(ByteConvertor.fromInteger(data.length), 2, 4);
		
		//计算校验位
		byte checksum = ChecksumUtil.calculateSum(data);
		
		//拼接报文
		byte[] result = ArrayUtils.addAll(new byte[]{startCode}, ByteConvertor.fromString(this.terminalNumber));
		result = ArrayUtils.addAll(result, new byte[]{this.controlCode});
		result = ArrayUtils.addAll(result, dataLength);
		result = ArrayUtils.addAll(result, data);
		result = ArrayUtils.addAll(result, new byte[]{checksum});
		result = ArrayUtils.addAll(result, new byte[]{endCode});
		return result;
	}
	
	@Override
	public void parse(byte[] data) throws ParseException {
		setDataProperties(data);
	}
	
	/**
	 * @return the terminalNumber
	 */
	public String getTerminalNumber() {
		return terminalNumber;
	}
	/**
	 * @param terminalNumber the terminalNumber to set
	 */
	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}
	/**
	 * @return the controlCode
	 */
	public byte getControlCode() {
		return controlCode;
	}
	/**
	 * @param controlCode the controlCode to set
	 */
	public void setControlCode(byte controlCode) {
		this.controlCode = controlCode;
	}
	/**
	 * @return the startCode
	 */
	public static byte getStartCode() {
		return startCode;
	}
	/**
	 * @return the endCode
	 */
	public static byte getEndCode() {
		return endCode;
	}
}
