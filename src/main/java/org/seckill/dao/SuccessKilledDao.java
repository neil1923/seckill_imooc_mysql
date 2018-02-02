package org.seckill.dao;


import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("successKilledDao")

public interface SuccessKilledDao {

    /**
     * 插入购买明细记录，利用数据库中的联合主键（主键是两个字段）来过滤重复
     * @param seckillId（主键1）
     * @param userPhonr（主键2）
     * @return 插入的行数
     */
    public int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhonr);

    /**
     * 利用seckill的id查询多条successKilled(购买明细表记录)，并携带了秒杀产品的对象实体
     * @param seckillId
     * @return
     */
    public List<SuccessKilled> qureryByIdWithSeckills(long seckillId);

    /**
     * 利用seckill的id和秒杀成功的电话号（两个联合主键）查询一条successKilled(购买明细表记录)，并携带了秒杀产品的对象实体
     * @param seckillId
     * @param userPhone
     * @return
     */
    public SuccessKilled qureryByIdAndPhoneWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
