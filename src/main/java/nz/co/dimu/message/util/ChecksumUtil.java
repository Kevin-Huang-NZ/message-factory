/**
 * 
 */
package nz.co.dimu.message.util;

import nz.co.dimu.message.exceptions.BadParameterException;

/**
 * @author huanghao
 *
 */
public class ChecksumUtil {
	
	/**
	 * 采用累加和取反的校验方式。
	 * 将终端号码、控制字、数据长度和数据区的所有字节进行算术累加，抛弃高位，只保留最后单字节，将单字节取反
	 * 
	 * @param data 计算对象
	 * @return 一个byte值
	 * @throws BadParameterException 参数data数组为null或者长度为0
	 */
	public static byte calculateSum(byte[] data) throws BadParameterException {
		if (data == null || data.length == 0) {
			throw new BadParameterException("计算校验码时，计算对象数组为空。");
		}

		//对byte数组执行算数相加
		long sum = 0l;
		
		for (int i = 0; i < data.length; i++) {
			sum = sum + Long.valueOf(data[i]);
		}
		
		//抛弃高位，只保留最后单字节
		byte checksum = (byte)sum;
		
		//取反
		checksum = (byte)(~checksum);
		
		return checksum;
	}
	
	/**
	 * 计算data的校验码，与参数传入的校验码比较，如果相等返回true；否则返回false.
	 * 
	 * @param data 计算校验码的原始数据，一个byte数组
	 * @param checksum 报文中的校验码
	 * @return
	 * @throws BadParameterException 参数data数组为null或者长度为0
	 */
	public static boolean verify(byte[] data, byte checksum) throws BadParameterException {
		return calculateSum(data) == checksum;
	}
}
