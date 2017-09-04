package org.seckill.web;

import org.seckill.dto.SeckillExcutionDto;
import org.seckill.dto.SeckillExposerDto;
import org.seckill.dto.SeckillResultDto;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillAllException;
import org.seckill.exception.SeckillCloseExcepiton;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Component
@RequestMapping("/seckill")//url格式：模块/资源/{}/细分
public class SeckillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * ModelAndView = model + .jsp
     * 获取商品列表
     * @param model：数据
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String List(Model model){
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    /**
     * 商品详情信息
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String Detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null){
            return "forword:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    /**
     * ajax，json暴露秒杀接口的方法
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer",
                    method = RequestMethod.GET,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResultDto<SeckillExposerDto> Exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResultDto<SeckillExposerDto> result = null;
        try{
            SeckillExposerDto exposerDto = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResultDto<SeckillExposerDto>(true,exposerDto);
        } catch (Exception e){
            e.printStackTrace();
            logger.warn("请求失败！");
            result = new SeckillResultDto<SeckillExposerDto>(false, "请求失败");
        }
        return result;
    }

    /**
     * 执行  秒杀的结果
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/excution",
                    method = RequestMethod.POST,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResultDto<SeckillExcutionDto> excution(
            @PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value="userPhone", required = false) Long userPhone){

        if(userPhone ==null){
            return new SeckillResultDto<SeckillExcutionDto>(false,"用户没有注册！");
        }

        SeckillResultDto<SeckillExcutionDto> result;
        try{
            SeckillExcutionDto excutionDto = seckillService.excuteSeckill(seckillId, userPhone, md5);
            return new SeckillResultDto<SeckillExcutionDto>(true,excutionDto);
        } catch (RepeatKillException e){
            SeckillExcutionDto excutionDto = new SeckillExcutionDto(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResultDto<SeckillExcutionDto>(true,excutionDto);
        } catch (SeckillCloseExcepiton e){
            SeckillExcutionDto excutionDto = new SeckillExcutionDto(seckillId, SeckillStateEnum.END);
            return new SeckillResultDto<SeckillExcutionDto>(true,excutionDto);
        } catch (SeckillAllException e){
            SeckillExcutionDto excutionDto = new SeckillExcutionDto(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResultDto<SeckillExcutionDto>(true,excutionDto);
        }
    }

    /**
     * 获取系统时间
     * @return
     */
    @RequestMapping(value = "/time/now",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResultDto<Long> Time(){
        Date now = new Date();
        return new SeckillResultDto<Long>(true, now.getTime());
    }

}
