package com.yingzi.myLearning.aop;

import com.alibaba.fastjson.JSONObject;
import com.yingzi.center.bio.api.IObjectEventApi;
import com.yingzi.center.bio.api.VetDiseaseDiagnosisService;
import com.yingzi.center.bio.contstant.BioCenterConstant;
import com.yingzi.center.bio.contstant.RedisConstant;
import com.yingzi.center.bio.entity.vo.BioCastrateRecordVo;
import com.yingzi.center.bio.entity.vo.BioInnocuousRecordVo;
import com.yingzi.center.bio.entity.vo.VetDiagnosisRecordVo;
import com.yingzi.center.bio.entity.vo.VetDiseaseDiagnosisVo;
import com.yingzi.center.bio.entity.vo.event.BioObjectEventVo;
import com.yingzi.center.bio.entity.vo.health.HealthRecordVo;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BioEventAop {
	private static Logger logger = LoggerFactory.getLogger(BioEventAop.class);
	
	@Autowired
	private IObjectEventApi objectEventService;
	@Autowired
	private VetDiseaseDiagnosisService vetDiseaseDiagnosisService;
	
	@Autowired
	StringRedisTemplate redisTemplate;
	
	
	@Pointcut("@annotation(com.yingzi.center.bio.aop.BioEvent)||"
			+ " @within(com.yingzi.center.bio.aop.BioEvent)")
	public void pointCut() {

	}
	/**
	 * 注意是返回后才进行插入记录-主要是因为id的获取
	 */
	@AfterReturning("pointCut()")
	public void  doHandler(JoinPoint joinPoint) throws Throwable {
		logger.info("----> 记录生物信息全局事件--开始");
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			BioObjectEventVo record=null;
			if(arg instanceof VetDiseaseDiagnosisVo) {
				VetDiseaseDiagnosisVo vetDiseaseDiagnosisVo=(VetDiseaseDiagnosisVo)arg;
				record=getByArg(vetDiseaseDiagnosisVo);
				logger.info("----> {}",BioCenterConstant.EventType.DISEASE_DIAGNOSIS.getMsg());
			}
			if(arg instanceof HealthRecordVo) {
				HealthRecordVo bioHealthRecordVo=(HealthRecordVo)arg;
				record=getByArg(bioHealthRecordVo);
				logger.info("----> {}",BioCenterConstant.EventType.HEALTHCARE.getValue());
			}
			if(arg instanceof BioCastrateRecordVo){
				BioCastrateRecordVo bioCastrateRecordVo=(BioCastrateRecordVo)arg;
				record=getByArg(bioCastrateRecordVo);
				logger.info("----> {}",BioCenterConstant.EventType.CASTRATION.getMsg());
			}

			if(arg instanceof BioInnocuousRecordVo){
				BioInnocuousRecordVo bioInnocuousRecordVo=(BioInnocuousRecordVo)arg;
				record=getByArg(bioInnocuousRecordVo);
				logger.info("----> {}",BioCenterConstant.EventType.INNOCUOUSTREATMENT.getMsg());
			}
			if(arg instanceof VetDiagnosisRecordVo) {
				VetDiagnosisRecordVo vetDiagnosisRecordVo=(VetDiagnosisRecordVo)arg;
				record=getByArg(vetDiagnosisRecordVo);
				logger.info("----> {}",BioCenterConstant.EventType.DIAGNOSIS_RECORD.getMsg());
			}

			if(record!=null) {
				objectEventService.save(record);
				logger.info("----> 记录生物信息全局事件--结束");
			}
			
		}
		
	
	}
	
	

	private BioObjectEventVo getByArg (HealthRecordVo arg){
		BioObjectEventVo record=new BioObjectEventVo();
		//TODO
		record.setCategoryId(arg.getCategoryId());//猪群类型I
		record.setCreatePerson(arg.getCreatePerson());
		record.setEventDate(arg.getExecDate());//执行日期
		record.setEventId(arg.getId());
		record.setEventType(BioCenterConstant.EventType.HEALTHCARE.getCode());
		record.setFarmId(arg.getFarmId());
		record.setTenantId(arg.getTenantId());
		record.setIdentityId(arg.getIdentityId());
		record.setType(arg.getType());
		record.setDemandId(arg.getDemandId());
		if((int)arg.getType()==BioCenterConstant.PigType.SINGGLE.getValue()) {
			record.setNum(1);//单个
		}else {
			record.setNum(arg.getNum());
		}
		sendMessage(record);
		return record;
	}

	private BioObjectEventVo getByArg (BioCastrateRecordVo arg){
		BioObjectEventVo record=new BioObjectEventVo();
		record.setCreatePerson(arg.getCreatePerson());
		//record.setEventDate(arg.getCastrateDate());
		record.setEventId(arg.getId());
		record.setEventType(BioCenterConstant.EventType.CASTRATION.getCode());
		record.setFarmId(arg.getFarmId());
		record.setTenantId(arg.getTenantId());
		record.setIdentityId(arg.getPeppaId());
		record.setCategoryId(0);//0-哺乳仔猪
		record.setType(1);//猪群
		return record;
	}

	private BioObjectEventVo getByArg (BioInnocuousRecordVo arg){
		BioObjectEventVo record=new BioObjectEventVo();
		record.setCategory("");
		record.setCreatePerson(arg.getCreatePerson());
		record.setEventDate(arg.getDeadDate());
		record.setEventId(arg.getId());
		record.setEventType(BioCenterConstant.EventType.INNOCUOUSTREATMENT.getCode());
		record.setFarmId(arg.getFarmId());
		record.setTenantId(arg.getTenantId());
		record.setIdentityId(arg.getObjectNo());
		record.setType(arg.getObjectType());
		return record;
	}
	private BioObjectEventVo getByArg(VetDiseaseDiagnosisVo arg) {
		BioObjectEventVo record=new BioObjectEventVo();
		record.setCategory(arg.getTypeName());
		record.setCreatePerson(arg.getCreatePerson());
		record.setEventDate(arg.getVisitTime());
		record.setEventId(arg.getId());
		record.setEventType(BioCenterConstant.EventType.DISEASE_DIAGNOSIS.getCode());
		record.setFarmId(arg.getFarmId());
		record.setTenantId(arg.getTenantId());
		record.setIdentityId(arg.getObjectNo());
		record.setType(arg.getObjectType());
		return record;

	}
	private BioObjectEventVo getByArg(VetDiagnosisRecordVo arg) {
		BioObjectEventVo record=new BioObjectEventVo();
		record.setCategory(arg.getCategory());
		record.setCreatePerson(arg.getCreatePerson());
		record.setEventDate(arg.getExecDate());
		record.setEventId(arg.getId());
		record.setEventType(BioCenterConstant.EventType.DIAGNOSIS_RECORD.getCode());
		record.setTenantId(arg.getTenantId());
		//用药记录的批次号去取疾病诊疗的批次号
		VetDiseaseDiagnosisVo vetDiseaseDiagnosisVo =vetDiseaseDiagnosisService.getVetDiseaseDiagnosisVo(arg.getDiseaseDiagnosisId());
		record.setIdentityId(vetDiseaseDiagnosisVo.getObjectNo());
		record.setType(vetDiseaseDiagnosisVo.getObjectType());
		record.setFarmId(vetDiseaseDiagnosisVo.getFarmId());
		return record;
	}
	
	
	/**
	 * redis 发布消息-发到app运用上
	 * @param record
	 */
	private void sendMessage(BioObjectEventVo record) {
		String sendMessage = JSONObject.toJSONString(record);
		if(StringUtils.isNotBlank(sendMessage)) {
			logger.info("==> send message to bio app. info：{}",sendMessage);
			redisTemplate.convertAndSend(RedisConstant.EVENT_CHANNEL, sendMessage);
		}
	}
	
}
