package org.seckill.dao;


import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    public int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据ＩＤ查询秒杀对象
     * @param seckillId
     * @return
     */
    public Seckill queryById(long seckillId);

    /**
     * mysql使用的是limit（arg0,arg1）查询一个范围内的数据
     * sql server使用的是top(arg0)查询
     * 使用到mybatis的注解@Param("参数名")--目的是为了在编译期间给定参数的名称
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    public List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
