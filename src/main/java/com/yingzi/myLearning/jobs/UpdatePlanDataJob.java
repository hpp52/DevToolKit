package com.dfkj.myLearning.jobs;

import com.dfkj.center.bio.api.BioImmunePlanDetailService;
import com.dfkj.center.bio.common.util.BioDateUtils;
import com.dfkj.center.bio.common.util.SnowflakeIdWorker;
import com.dfkj.center.bio.controller.bio.BioHealthPlanController;
import com.dfkj.center.bio.entity.vo.BioImmunePlanVo;
import com.dfkj.center.breeding.api.query.IBreedBreastQueryApi;
import com.dfkj.center.breeding.dto.BreedParamDto;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class UpdatePlanDataJob implements Job {
	private static Logger log = LoggerFactory.getLogger(BioHealthPlanController.class);
	
    final Map<String, List<BioImmunePlanVo>> cache = new ConcurrentHashMap<String, List<BioImmunePlanVo>>();
    final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
    @Autowired
    IBreedBreastQueryApi iBreedBreastQueryApi;
    @Autowired
    private BioImmunePlanDetailService bioImmunePlanDetailService;
  


    private AtomicInteger cal = new AtomicInteger();

    private int total = 0;

	
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	log.info("定时任务跑啊跑 1 UpdatePlanDataJob============= >");
    	log.info("定时任务跑啊跑 2 UpdatePlanDataJob============= >");
    	log.info("定时任务跑啊跑 3 UpdatePlanDataJob============= >");
    	mergeUpdateData();
    }

    @Transactional
    private void mergeUpdateData() {

        long startTime = System.currentTimeMillis();
        log.info("------------------------更新数据开始------------------------");
        log.info("合并数据开始时间=" + BioDateUtils.parseString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        
        
        List<BioImmunePlanVo> bioImmunePlanDetailVos = bioImmunePlanDetailService.selectUpdateData();
        if (bioImmunePlanDetailVos.size() == 0) {
            log.info("{本地数据库没有更新的数据}");
            return;
        }
        cache.put("bio", bioImmunePlanDetailVos);
        
        
        /**异步出去***/
        
        List<BreedParamDto> listResponseDto = iBreedBreastQueryApi.queryBreedBreast().getData();
        if (listResponseDto.size() == 0) {
            log.info("{没有推送的数据}");
            return ;
        }
        for (BioImmunePlanVo updateDate : bioImmunePlanDetailVos) {
            for (BreedParamDto originalData : listResponseDto) {
                if (originalData.getFarmId() == null 
                		|| originalData.getTenantId() == null 
                		|| StringUtils.isBlank(originalData.getEstatus()) 
                		|| originalData.getBirthDate() == null 
                		|| StringUtils.isBlank(originalData.getHerdId())) {
                    continue;
                }
                //判断是否一条记录
                if (updateDate.getBatch().equals(originalData.getHerdId()) 
                		&& updateDate.getFarmId().longValue() == originalData.getFarmId().longValue() 
                		&& updateDate.getTenantId().longValue() == (originalData.getTenantId().longValue()) 
                		&& updateDate.getBirthDate().compareTo(originalData.getBirthDate()) == 0) {
                    //净头数小于记录数量更新数据库
                    if (originalData.getNetHeadNumber() < (updateDate.getNetHeadNumber())) {
                    	//药品数=净头数*药品计量
                    	Double tempDrugNumber=updateDate.getNetHeadNumber() * updateDate.getRefeDose();
                        int drugNumber =tempDrugNumber.intValue(); 
                        bioImmunePlanDetailService.updateBioImmunePlanDrugNumber(updateDate.getMainId(), originalData.getNetHeadNumber(),drugNumber);
                        cal.getAndIncrement();
                        continue;
                    }
                }
            }
        }
        log.info("总共更新[" + (cal.get() - total) + "]条数据"+"-"+BioDateUtils.parseString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        total = cal.get();
        long endTime = System.currentTimeMillis();
        log.info("合并数据结束总共用时" + "[" + (endTime - startTime) + "]" + "ms");
        log.info("------------------------更新任务结束------------------------");
    }

}
