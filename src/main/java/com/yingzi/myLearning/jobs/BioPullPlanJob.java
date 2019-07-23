package com.yingzi.myLearning.jobs;

import com.alibaba.fastjson.JSONObject;
import com.dtyunxi.dto.ResponseDto;
import com.dtyunxi.yundt.center.user.api.query.IRoleQueryApi;
import com.dtyunxi.yundt.center.user.eo.RoleEo;
import com.yingzi.center.bio.api.BioImmunePlanDetailService;
import com.yingzi.center.bio.common.util.BioDateUtils;
import com.yingzi.center.bio.common.util.SnowflakeIdWorker;
import com.yingzi.center.bio.common.util.SymbolUtil;
import com.yingzi.center.bio.entity.vo.BioImmunePlanVo;
import com.yingzi.center.bio.entity.vo.BioImmunePullPlanData;
import com.yingzi.center.breeding.api.query.IBreedBreastQueryApi;
import com.yingzi.center.breeding.dto.BreedParamDto;
import com.yingzi.center.taskdb.dto.ProjectId;
import com.yingzi.center.taskdb.dto.TaskId;
import com.yingzi.task.client.api.yz.YzTask;
import com.yingzi.task.client.rpc.api.YzClient;
import com.yingzi.task.client.rpc.api.YzCollection;
import com.yingzi.task.client.rpc.api.impl.YzRpcConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
public class BioPullPlanJob implements Job{

    public static Logger log = LoggerFactory.getLogger(BioPullPlanJob.class);
    private static volatile YzCollection coll = null;
    final Map<String, List<BioImmunePlanVo>> cache = new ConcurrentHashMap<String, List<BioImmunePlanVo>>();
    final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
    @Autowired
    IBreedBreastQueryApi iBreedBreastQueryApi;
    @Autowired
    private BioImmunePlanDetailService bioImmunePlanDetailService;
    @Autowired
    private IRoleQueryApi roleQueryApi;
    private String PROJECTID = "yingzibioimmuneprojecttask";


    private AtomicInteger cal = new AtomicInteger();

    private int total = 0;

    public static YzCollection getYzCollection() {
        if (coll == null) {
            synchronized (BioPullPlanJob.class) {
                if (coll == null) {
                    YzClient client = YzRpcConnection.getClient();
                    YzCollection coll = client.getCollection();
                    return coll;
                }
            }
        }
        return coll;
    }

    
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	log.info("定时任务跑啊跑 1 BioPullPlanJob============= >");
    	log.info("定时任务跑啊跑 2 BioPullPlanJob============= >");
    	log.info("定时任务跑啊跑 3 BioPullPlanJob============= >");
    	startPullTask();
    	
    }
    
    /**
     * 定时推送数据
     * @param void
     * @author hk
     */
   // @Scheduled(cron = "0/50 * * * * ?")
    private void startPullTask() {
        long startTime = System.currentTimeMillis();
        log.info("------------------------定时推送任务开始------------------------");
        log.info("推动数据开始时间" +"["+BioDateUtils.parseString(new Date(), "yyyy-MM-dd HH:mm:ss")+"]");
        List<BioImmunePullPlanData> pullPlanDataList = this.assemPullDate();
        //log.info(JSONObject.toJSONString(pullPlanDataList));
        
        int i=1;
        for (BioImmunePullPlanData pullPlanData : pullPlanDataList) {
            pullPlanData.setType(1);
            pullPlanData.setTaskType(1);
            
           /***
            * 第一步：生成任务id；
            * 第二步：生成projectId；
            * 第三步：封装数据；
            * 第四步：推送数据
            */
            
            YzTask yzTask = new YzTask(new TaskId(Long.toString(idWorker.nextId()).getBytes()));
            yzTask.setProjectId(new ProjectId(PROJECTID.getBytes()));
            
            
            yzTask.setContent(JSONObject.toJSONString(pullPlanData));
            yzTask.setCreatedTime(new Date());
            yzTask.setPausing(false);
            yzTask.setCreatePerson("0");//0-默认系统
            ResponseDto<RoleEo> roleEoResponseDto = roleQueryApi.queryEoById(pullPlanData.getRoleId());
            if (roleEoResponseDto == null || roleEoResponseDto.getData() == null) {
                continue;
            }
            
            if(pullPlanData.getFarmId()!=null) {
            	yzTask.setOrganization(pullPlanData.getFarmId().toString());
            }
            
            yzTask.setRole(roleEoResponseDto.getData().getCode());
            yzTask.setName(pullPlanData.getTitle());
            yzTask.setCreatedTime(pullPlanData.getPlanDate());
         
            log.info("向任务中心推送数据--> 第{}条，任务属性: {}",i,JSONObject.toJSONString(yzTask));
            getYzCollection().saveTask(yzTask);
            i++;
        }

        long endTime = System.currentTimeMillis();
        log.info("推送"+pullPlanDataList.size()+"条数据" + "[" + (endTime - startTime) + "]" + "ms");
        log.info("------------------------定时推送任务结束------------------------");
    }

    /**
     * 组装推送数据
     * @param void
     * @author hk
     */
    private List<BioImmunePullPlanData> assemPullDate() {

        Date startDate = new Date();
        List<BioImmunePullPlanData> planPullDataList = bioImmunePlanDetailService.getPlanMergeData(startDate, DateUtils.addDays(startDate, 1));
        if (planPullDataList.size() == 0) {
            log.info("{推送数据为null}");
            return planPullDataList;
        }
        List<BioImmunePlanVo> cacheDateList = cache.get("bio");
        if (cacheDateList != null) {
            log.info(JSONObject.toJSONString(cacheDateList));
            planPullDataList.forEach(planMergeData -> {
                StringBuilder detailIds = new StringBuilder();
                cacheDateList.forEach(cacheDate -> {
                    if (planMergeData.getBuildingId().longValue()==cacheDate.getBuildingId().longValue()
                    		&& planMergeData.getTenantId().longValue()==cacheDate.getTenantId().longValue()
                    		&& planMergeData.getFarmId().longValue()==cacheDate.getFarmId().longValue()) {
                        detailIds.append(cacheDate.getId());
                        detailIds.append(SymbolUtil.COMMA);
                    }
                });
                
                //去掉最后的逗号
                if (detailIds.length() != 0) {
                    detailIds.deleteCharAt(detailIds.length() - 1);
                    planMergeData.setDetailPlanIds(detailIds.toString());
                }
                
                
            });
            cache.remove("bio");
        }
        return planPullDataList;
    }

    /**
     * 合并已更新的数据
     * @param void
     * @author 姚亮
     */

    //@Scheduled(cron = "0/40 * * * * ?")
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
                    	//TODO 数据库字段需要拆分数量+单位
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
