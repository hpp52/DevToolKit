package com.yingzi.myLearning.jobs;

import com.dtyunxi.dto.ResponseDto;
import com.yingzi.center.bio.api.BioImmunePlanDetailService;
import com.yingzi.center.bio.api.BioImmunePlanService;
import com.yingzi.center.bio.api.BioImmuneProgramDetailService;
import com.yingzi.center.bio.api.BioImmuneProgramService;
import com.yingzi.center.bio.common.util.BioDateUtils;
import com.yingzi.center.bio.entity.vo.BioImmunePlanDetailVo;
import com.yingzi.center.bio.entity.vo.BioImmunePlanVo;
import com.yingzi.center.bio.entity.vo.BioImmuneProgramDetailVo;
import com.yingzi.center.bio.entity.vo.BioImmuneProgramVo;
import com.yingzi.center.breeding.api.query.IBreedBreastQueryApi;
import com.yingzi.center.breeding.dto.BreedParamDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BioGeneratePlanJob  implements Job {


    public static Logger log = LoggerFactory.getLogger(BioGeneratePlanJob.class);

    @Autowired
    IBreedBreastQueryApi iBreedBreastQueryApi;

    @Autowired
    BioImmuneProgramService bioImmuneProgramService;

    @Autowired
    BioImmuneProgramDetailService bioImmuneProgramDetailService;

    @Autowired
    BioImmunePlanService bioImmunePlanService;

    @Autowired
    BioImmunePlanDetailService bioImmunePlanDetailService;

    private AtomicInteger cal = new AtomicInteger();

    private int total = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	log.info("定时任务跑啊跑 1 BioGeneratePlanJob ============= >");
    	log.info("定时任务跑啊跑 2 BioGeneratePlanJob ============= >");
    	log.info("定时任务跑啊跑 3 BioGeneratePlanJob ============= >");
    	createBioPlanTask();
    } 

    /**
     * 定时拉取数据
     * @param void
     * @author 姚亮
     */

   // @Scheduled(cron = "0/30 * * * * ?")
    private void createBioPlanTask() {

        log.info("------------------------哺乳仔猪拉去数据任务开始------------------------");
        log.info("哺乳仔猪拉去数据任务开始" + BioDateUtils.parseString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        ResponseDto<List<BreedParamDto>> listResponseDto = iBreedBreastQueryApi.queryBreedBreast();
        
       // log.info(JSONObject.toJSONString(listResponseDto));
        log.info("拉取[" + listResponseDto.getData().size() + "]条数据");
        getPlanProgram(listResponseDto.getData());
        log.info("总共更新[" + (cal.get() - total) + "]条数据");
        total = cal.get();
        log.info("------------------------哺乳仔猪拉去数据任务结束------------------------");
    }

    /**
     * 查询对应执行程序
     * @param List<BreedParamDto>
     * @author 姚亮
     */

    private void getPlanProgram(List<BreedParamDto> listBreedParamDto) {

        if (listBreedParamDto == null) {
            log.info("推送数据为null");
            return;
        }
        for (BreedParamDto breedParamDto : listBreedParamDto) {
            if (breedParamDto.getFarmId() == null || breedParamDto.getTenantId() == null || StringUtils.isEmpty(breedParamDto.getEstatus()) || breedParamDto.getBirthDate() == null || StringUtils.isEmpty(breedParamDto.getHerdId())) {
                continue;
            }
            int count = 0;
            try {
                //判断是否有生成执行计划
                BioImmunePlanVo bioImmunePlan = new BioImmunePlanVo();
                bioImmunePlan.setFarmId(breedParamDto.getFarmId());
                bioImmunePlan.setTenantId(breedParamDto.getTenantId());
                bioImmunePlan.setBirthDate(breedParamDto.getBirthDate());
                bioImmunePlan.setBatch(breedParamDto.getHerdId());
                count = bioImmunePlanService.isCreatePlan(bioImmunePlan);
            } catch (Exception e) {
                log.error("判断是否有生成执行计划报错:{}", e.getMessage());
            }

            if (count == 0) {
                BioImmuneProgramVo bioImmuneProgramVo = new BioImmuneProgramVo();
                bioImmuneProgramVo.setFarmId(breedParamDto.getFarmId());
                bioImmuneProgramVo.setTenantId(breedParamDto.getTenantId());
                bioImmuneProgramVo.setCategoryId(Long.parseLong(breedParamDto.getEstatus()));
                //查找对应执行程序
                List<BioImmuneProgramVo> bioImmuneProgramVos = bioImmuneProgramService.listBioImmuneProgramVoByParam(bioImmuneProgramVo);
                //生成执行计划和详情
                if (bioImmuneProgramVos.size() > 0) {
                    this.createPlanAndDetail(bioImmuneProgramVos, breedParamDto);
                    cal.getAndIncrement();
                }
            }
        }

    }

    /**
     * 生成对应执行计划
     * @param List<BioImmuneProgramVo>,BreedParamDto
     * @author 姚亮
     */
    @Transactional(rollbackFor = Exception.class)
    private void createPlanAndDetail(List<BioImmuneProgramVo> bioImmuneProgramVos, BreedParamDto breedParamDto) {
        try {
            for (BioImmuneProgramVo immuneProgramVo : bioImmuneProgramVos) {
                BioImmuneProgramDetailVo bioImmuneProgramDetailVo = new BioImmuneProgramDetailVo();
                bioImmuneProgramDetailVo.setMainId(immuneProgramVo.getId());
                List<BioImmuneProgramDetailVo> bioImmuneProgramDetailVos = bioImmuneProgramDetailService.listBioImmuneProgramDetailVoByParam(bioImmuneProgramDetailVo);
                BioImmunePlanVo bioImmunePlanVo = new BioImmunePlanVo();
                BeanUtils.copyProperties(immuneProgramVo, bioImmunePlanVo);
                bioImmunePlanVo.setBatch(breedParamDto.getHerdId());
                bioImmunePlanVo.setStatus(1);
                bioImmunePlanVo.setBirthDate(breedParamDto.getBirthDate());
                bioImmunePlanVo.setId(null);
                bioImmunePlanVo.setCreateTime(new Date());
                BioImmunePlanVo bioImmunePlan = bioImmunePlanService.addBioImmunePlan(bioImmunePlanVo);
                if (bioImmuneProgramDetailVos.size() > 0) {
                    for (BioImmuneProgramDetailVo immuneProgramDetailVo : bioImmuneProgramDetailVos) {
                        BioImmunePlanDetailVo bioImmunePlanDetailVo = new BioImmunePlanDetailVo();
                        BeanUtils.copyProperties(immuneProgramDetailVo, bioImmunePlanDetailVo);
                        Integer age = immuneProgramDetailVo.getAge();//日龄
                        Date planDate = DateUtils.addDays(breedParamDto.getBirthDate(), age);
                        bioImmunePlanDetailVo.setPlanDate(planDate);
                        bioImmunePlanDetailVo.setId(null);
                        bioImmunePlanDetailVo.setCreateTime(new Date());
                        bioImmunePlanDetailVo.setMainId(bioImmunePlan.getId());
                        Double tempDrugNumber = immuneProgramDetailVo.getRefeDose()* breedParamDto.getNetHeadNumber();
                        
                        int drugNumber = tempDrugNumber.intValue();
                        bioImmunePlanDetailVo.setDrugNumber(drugNumber);
                        bioImmunePlanDetailVo.setNetHeadNumber(breedParamDto.getNetHeadNumber());
                        bioImmunePlanDetailService.addBioImmunePlanDetail(bioImmunePlanDetailVo);
                    }
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

}
