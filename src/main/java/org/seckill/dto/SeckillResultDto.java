package org.seckill.dto;

/**
 * Created by niu on 2017/9/3.
 */
public class SeckillResultDto<T> {

    //判断请求是否成功
    private boolean isSuccess;
    private T data;
    private String error;

    /**
     * 请求失败的构造方法
     * @param isSuccess
     * @param error
     */
    public SeckillResultDto(boolean isSuccess, String error) {
        this.isSuccess = isSuccess;
        this.error = error;
    }

    /**
     * 请求成功构造方法
     * @param isSuccess
     * @param data
     */
    public SeckillResultDto(boolean isSuccess, T data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
