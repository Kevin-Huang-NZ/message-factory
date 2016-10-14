/**
 * 参数错误异常。
 */
package nz.co.dimu.message.exceptions;

/**
 * @author huanghao
 *
 */
public class BadParameterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BadParameterException() {
    }

    public BadParameterException(String message) {
        super(message);
    }

    public BadParameterException(Throwable cause) {
        super(cause);
    }

    public BadParameterException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
