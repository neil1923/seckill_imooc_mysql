package org.seckill.exception;

/**
 * 重复秒杀异常（运行期异常-spring的事务只接受运行期的异常）
 */
public class RepeatKillException extends SeckillAllException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
