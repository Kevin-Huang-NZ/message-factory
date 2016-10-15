/**
 * 参数错误异常。
 */
package nz.co.dimu.message.generator.exceptions;

/**
 * @author huanghao
 *
 */
public class InvalidXmlException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidXmlException() {
    }

    public InvalidXmlException(String message) {
        super(message);
    }

    public InvalidXmlException(Throwable cause) {
        super(cause);
    }

    public InvalidXmlException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
