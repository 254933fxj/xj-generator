package org.maker.meta;

/**
 * 元信息校验 用户集中处理元信息错误导致的异常
 */
public class MetaExecption extends RuntimeException {

    public MetaExecption(String message) {
        super(message);
    }

    public MetaExecption(String message, Throwable cause) {
        super(message, cause);
    }


}
