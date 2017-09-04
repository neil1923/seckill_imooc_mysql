package org.seckill.exception;

/**
 * 秒杀关闭异常（比如库存为0/没电了）
 */
public class SeckillCloseExcepiton extends SeckillAllException {
    public SeckillCloseExcepiton(String message) {
        super(message);
    }

    public SeckillCloseExcepiton(String message, Throwable cause) {
        super(message, cause);
    }
}
