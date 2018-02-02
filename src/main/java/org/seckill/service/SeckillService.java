package org.seckill.service;

import org.seckill.dto.SeckillExcutionDto;
import org.seckill.dto.SeckillExposerDto;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillAllException;
import org.seckill.exception.SeckillCloseExcepiton;

import java.util.List;

/**
 * 处理业务逻辑
 * 业务逻辑接口：站在"用户"的角度设计接口
 * 三个方面：方法定义粒度 + 参数 + 返回类型（return 类型/异常）
 */
public interface SeckillService {
    
    /**
     * 查询所有的秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 当秒杀开启时暴露秒杀接口，否则暴露系统时间和秒杀时间
     * 返回的类型与DTO（数据传输对象）相关联
     * @param seckillId
     */
    SeckillExposerDto exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀的操作
     * 根据md5值的匹配结果 来判断是否允许 该用户进行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExcutionDto excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseExcepiton, RepeatKillException, SeckillAllException;
}
