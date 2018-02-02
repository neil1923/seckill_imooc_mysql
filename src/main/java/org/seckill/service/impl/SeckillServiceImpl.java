package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.SeckillExcutionDto;
import org.seckill.dto.SeckillExposerDto;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillAllException;
import org.seckill.exception.SeckillCloseExcepiton;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

//可以使用的注解有--@Conponent @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService{

    //使用slf4j（统一的日志打印api）日志打印
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Seckill seckill;

    //注入service依赖（可以有：,@AutoWired<spring自带的> @Resource<j2ee规范>, @Inject<j2ee规范>）
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //作为md5的盐值字符串，用于混淆md5
    private final String slat = "nys1923@VERY@ #COMPITE# @WORDS@ #LIKE#%^LOV^^AZHE^%";

    public List<Seckill> getSeckillList() {
        // return seckillDao.queryAll(10,10); // sqlserver
        return seckillDao.queryAll(0,100); // mysql
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public SeckillExposerDto exportSeckillUrl(long seckillId) {
        seckill = seckillDao.queryById(seckillId);
        //1.秒杀商品列表中没有该商品
        if(seckill == null){
            return new SeckillExposerDto(false, seckillId);
        } else {
            //2.秒杀商品列表中有该商品
            Date startTime = seckill.getStartTime();
            Date endTime = seckill.getEndTime();
            Date nowTime = new Date();
            //2.1秒杀时间未开始或者已经结束
            if(nowTime.getTime() < startTime.getTime()
                    || nowTime.getTime() > endTime.getTime()){
                return new SeckillExposerDto(false,seckillId,
                        startTime.getTime(),endTime.getTime(),nowTime.getTime());
            } else {
                //2.2秒杀时间符合要求
                //md5--转化特定字符串的过程（不可逆）
                String md5 = getMD5(seckillId);
                return new SeckillExposerDto(true, md5, seckillId);
            }
        }
    }

    /**
     * 将seckillId加密（即加上自己写的乱码字符串）
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /**
     * 使用注解方式控制事务方法的好处：
     * 1.开发团队达成一致约定--明确标注事务方法的编码风格
     * 2.保证事务执行的时间尽可能端，不要穿插其他网络操作RPC/HTTP请求，
     *      如果需要则剥离到事务方法外部（private ..）
     * 3.不是所有的操作都需要事务管理（如只有一条修改操作或只读操作时，不需要事务控制）
     */
    public SeckillExcutionDto excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseExcepiton, RepeatKillException, SeckillAllException {
        if(md5 == null || !md5.equals(getMD5(seckillId)) ){
            //1.数据被篡改（md5的值改变）,秒杀数据被重写了<SeckillStateEnum.DATE_REWRITE>
            throw new SeckillAllException ("秒杀数据被篡改！");
        }
        //2.执行秒杀逻辑(事务包括):减库存+增加购买明细（发生抛出异常后spring自动运行事务回滚）
        try {
            //2.1增加购买明细执行插入语句
            int insertResult = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //2.1.1看是否该明细被重复插入，即用户是否重复秒杀<SeckillStateEnum.REPEAT_KILL>
            if (insertResult <= 0) {
                throw new RepeatKillException("重复秒杀操作！");
            } else {
                //2.2插入成功后减库存
                Date nowTime = new Date();
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                //2.2.1判断减库存操作是否成功
                if (updateCount <= 0) {
                    //表示库存已经为0秒杀活动结束<SeckillStateEnum.END>
                    throw new SeckillCloseExcepiton("库存为0-秒杀结束！");
                } else {
                    //否则秒杀成功，得到成功插入的明细记录，并返回成功秒杀的seckill实体的信息 commit
                    SuccessKilled successKilled = successKilledDao.qureryByIdAndPhoneWithSeckill(seckillId, userPhone);
                    return new SeckillExcutionDto(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        }catch (RepeatKillException e1){
            throw e1;
        } catch (SeckillCloseExcepiton e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            //将所有的编译期间的异常转化为自己写的运行期RuntimeException的异常<SeckillStateEnum.INNER_ERROR>
            throw new SeckillAllException("seckill inner error:"+e.getMessage());
        }
    }
}
