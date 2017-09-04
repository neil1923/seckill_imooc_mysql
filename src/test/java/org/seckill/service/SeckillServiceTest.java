package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.SeckillExcutionDto;
import org.seckill.dto.SeckillExposerDto;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseExcepiton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/*.xml")
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("seckillList={}",seckillList);
    }

    @Test
    public void getSeckillById() throws Exception {
        long id = 1L;
        Seckill seckill = seckillService.getSeckillById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    /**
     * 执行秒杀逻辑：先判断秒杀是否开启-->执行秒杀事务
     * 存在的问题：由于连接的是sql server的数据库
     *      所以在SuccessKilledDao.xml中执行的insert语句
     *      不能忽略主键冲突所以当重复秒杀的时候会出现编译期间的异常
     *      从而自己写的RepeatKillException不能正常抛出
     *      但是mysql可以
     */
    public void exportSeckillLogic() throws Exception {

        long seckillId = 2L;
        SeckillExposerDto seckillExposerDto = seckillService.exportSeckillUrl(seckillId);
        logger.info("seckillExposerDto={}",seckillExposerDto);

        //1.秒杀已经开始
        if (seckillExposerDto.isExpoted()){
            long userPhone = 18313713521L;
            String md5 = seckillExposerDto.getMd5();

            //加入try-catch语句是为了当出现业务需要的异常时直接抛出，从而不让单元测试处理
            try{
                SeckillExcutionDto seckillExcutionDto = seckillService.excuteSeckill(seckillId,userPhone,md5);
                logger.info("seckillExcutionDto={}",seckillExcutionDto);
            }catch (RepeatKillException e1){
                e1.getMessage();
                e1.printStackTrace();
            }catch (SeckillCloseExcepiton e2){
                e2.getMessage();
                e2.printStackTrace();
            }
        } else {
            //2.秒杀未开启(exposer--打印url还没有暴露的信息)
            logger.warn("seckillExposerDto(exposer)={}",seckillExposerDto);
        }
    }

}