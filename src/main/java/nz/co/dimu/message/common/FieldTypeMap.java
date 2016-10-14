/**
 * 
 */
package nz.co.dimu.message.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghao
 *
 */
public class FieldTypeMap {
	public static Map<String, Class<?>> data = new HashMap<String, Class<?>>();
	static {
		data.put("version", String.class);
		data.put("string", String.class);
		data.put("int", Integer.class);
		data.put("short", Short.class);
	}
}
