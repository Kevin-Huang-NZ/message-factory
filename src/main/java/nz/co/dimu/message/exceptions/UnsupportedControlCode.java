/**
 * 报文中的控制字不在系统处理范围内。系统不支持这种控制字。
 */
package nz.co.dimu.message.exceptions;

/**
 * @author huanghao
 *
 */
public class UnsupportedControlCode extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UnsupportedControlCode() {
    }

    public UnsupportedControlCode(String message) {
        super(message);
    }

    public UnsupportedControlCode(Throwable cause) {
        super(cause);
    }

    public UnsupportedControlCode(String message, Throwable cause) {
        super(message, cause);
    }
    
}
