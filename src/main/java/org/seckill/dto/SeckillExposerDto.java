package org.seckill.dto;

/**
 * 暴露秒杀接口地址的数据传输对象
 */
public class SeckillExposerDto {

    //是否开启
    private boolean expoted;

    //加密措施
    private String md5;

    private long seckillId;

    private long startTime;

    private long endTime;

    private long now;

    /**
     * 判断是否对该商品开启并暴露秒杀的接口
     * @param expoted
     * @param seckillId
     */
    public SeckillExposerDto(boolean expoted, long seckillId) {
        this.expoted = expoted;
        this.seckillId = seckillId;
    }

    /**
     * 开启并暴露后采用加密的措施（md5）
     * @param expoted
     * @param md5
     * @param seckillId
     */
    public SeckillExposerDto(boolean expoted, String md5, long seckillId) {
        this.expoted = expoted;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    /**
     * 如果没有开启暴露接口则显示系统时间和秒杀开启结束时间
     * @param expoted
     * @param startTime
     * @param endTime
     * @param now
     */
    public SeckillExposerDto(boolean expoted, long seckillId, long startTime, long endTime, long now) {
        this.expoted = expoted;
        this.seckillId = seckillId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.now = now;
    }

    public boolean isExpoted() {
        return expoted;
    }

    public void setExpoted(boolean expoted) {
        this.expoted = expoted;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "SeckillExposerDto{" +
                "expoted=" + expoted +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", now=" + now +
                '}';
    }
}
