package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    //注入DAO的实现类的依赖
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long seckillId = 4L;
        long userPhone  = 15613140500L;
        int insertReslut = successKilledDao.insertSuccessKilled(seckillId,userPhone);
        System.out.println(insertReslut);
    }

    @Test
    public void qureryByIdWithSeckills() throws Exception {
        long  seckillId = 1L;
        List<SuccessKilled> successkilledList = successKilledDao.qureryByIdWithSeckills(seckillId);
        for (SuccessKilled successkills:successkilledList){
            System.out.println(successkills);
        }
    }

    @Test
    public void qureryByIdAndPhoneWithSeckill() throws Exception{
        long seckillId = 1L;
        long userPhone = 18239926924L;
        SuccessKilled successKilled = successKilledDao.qureryByIdAndPhoneWithSeckill(seckillId,userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());

    }

}