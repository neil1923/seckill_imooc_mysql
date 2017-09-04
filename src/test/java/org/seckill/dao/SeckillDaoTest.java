package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
/*
告诉junit spring的配置文件,以便获取数据库连接池等信息
 */
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入DAO实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long id = 1;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill.getCreateTime());
    }

    @Test
    public void queryAll() throws Exception {
        //当方法中出现两个或两个以上的参数时，用到mybatis的注解@Param("参数名")
        List<Seckill> seckillList = seckillDao.queryAll(2,100);
        for(Seckill seckills : seckillList){
            System.out.println(seckills);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int result = seckillDao.reduceNumber(1,killTime);
        System.out.println("result:"+result);
    }

}