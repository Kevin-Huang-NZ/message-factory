/**
 * 
 */
package nz.co.dimu.message.common;

import java.text.ParseException;

import nz.co.dimu.message.exceptions.PacketException;

/**
 * @author huanghao
 *
 */
public interface MessageI {
	
	/**
	 * 将java实例转换成报文。
	 * 
	 * @return
	 */
	public byte[] packet() throws PacketException;
	
	/**
	 * 将报文数据域转换成java实例。
	 * 
	 * @param data 报文的数据域。
	 */
	public void parse(byte[] data) throws ParseException;

}
