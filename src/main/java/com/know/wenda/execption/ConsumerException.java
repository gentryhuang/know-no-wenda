
package com.know.wenda.execption;

/**
 * ConsumerException
 *
 * @author hlb
 */

public class ConsumerException extends RuntimeException {
    private static final long serialVersionUID = 649415394381978034L;

    /**
     *  异常业务编码
     */
    private Integer code;

    /**
     * 根据异常构造业务对象，设置编码及内容
     *
     * @param code    错误码
     * @param message 异常信息
     */
    public ConsumerException(final Integer code, final String message) {
        super(message);
        this.code = code;
    }

    /**
     * 根据异常信息和原生异常构造对象
     *
     * @param code    错误码
     * @param message 异常信息
     * @param cause   原生异常
     */
    public ConsumerException(final Integer code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}