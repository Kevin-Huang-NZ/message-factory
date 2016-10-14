/**
 * 报文的原始byte数组格式不合法。包括如下情况：
 * 报文数组为null；
 * 报文数组长度小于报文的最小长度：12.
 * 报文未通过校验码检验。
 * 报文开始字和结束字不正确。
 */
package nz.co.dimu.message.exceptions;

/**
 * @author huanghao
 *
 */
public class InvalidRawMessage extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidRawMessage() {
    }

    public InvalidRawMessage(String message) {
        super(message);
    }

    public InvalidRawMessage(Throwable cause) {
        super(cause);
    }

    public InvalidRawMessage(String message, Throwable cause) {
        super(message, cause);
    }
    
}
