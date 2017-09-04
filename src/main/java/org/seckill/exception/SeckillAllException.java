package org.seckill.exception;

/**
 * 与秒杀相关的所有异常行为
 */
public class SeckillAllException extends  RuntimeException{
    public SeckillAllException(String message) {
        super(message);
    }

    public SeckillAllException(String message, Throwable cause) {
        super(message, cause);
    }
}
