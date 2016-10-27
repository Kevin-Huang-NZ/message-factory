/**
 * 
 */
package nz.co.dimu.mf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取message-type-mapper.xml配置文件中的内容，在内存中维护控制字和报文类之间的映射关系。
 * 单例模式。
 * 
 * @author huanghao
 *
 */
public class MessageTypeMapper {
	
	private static final Logger log = LoggerFactory.getLogger(MessageTypeMapper.class);
	
	private Map<String, String> mapper;
	private String fileName = "message-type-mapper.xml";
	
	private MessageTypeMapper() {
		mapper = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();
		InputStream fileStream = null;
        try {
        	fileStream = MessageTypeMapper.class.getClassLoader().getResourceAsStream(fileName);
            Document document = saxReader.read(fileStream);
            Element root = document.getRootElement();
            List<Node> messages = root.selectNodes("//message");
            for (Node message : messages) {
            	String controlCode = message.selectSingleNode("controlCode").getText();
            	String messageClassName = message.selectSingleNode("messageClass").getText();
            	log.info("控制字:{}  ----  报文类:{}", controlCode, messageClassName);
            	mapper.put(controlCode, messageClassName);
            }
        } catch (DocumentException e) {
        	log.error("配置文件解析失败：{}", fileName, e);
        } finally {
        	if (fileStream != null) {
        		try {
					fileStream.close();
				} catch (IOException e) {
					log.error("关闭配置文件输入流时出现异常。文件名称：{}。", fileName, e);
				}
        	}
        }
	}
	
	public static MessageTypeMapper getInstance() {
		return MessageTypeMapperHolder.INSTANCE;
	}
	
	private static class MessageTypeMapperHolder {
		private static MessageTypeMapper INSTANCE = new MessageTypeMapper();
	}
	
	/**
	 * 通过控制字获取对应的报文类的名称。
	 * 
	 * @param controlCode
	 * @return
	 */
	public String getClassName(String controlCode) {
		return this.mapper.get(controlCode);
	}
	
	public static void main(String[] args) {
		MessageTypeMapper mapper = MessageTypeMapper.getInstance();
	}
}
