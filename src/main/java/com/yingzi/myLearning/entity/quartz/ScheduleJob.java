package com.dfkj.myLearning.entity.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
@SuppressWarnings("serial")
public class ScheduleJob implements Serializable, Job {

    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_NOT_RUNNING =0;
    public static final String CONCURRENT_IS = "1";
    public static final String CONCURRENT_NOT = "0";

    private Long id;
    private Long tenantId;
    private Integer dr;

    private String createPerson;

    private Date createTime;

    private String updatePerson;

    private Date updateTime;
    
    
    
    // cron表达式
    private String cronExpression;
    // 任务调用的方法名
    private String methodName;
    // 任务是否有状态
    private String isConcurrent;
    // 任务描述
    private String description;
    // 任务执行时调用哪个类的方法 包名+类名
    private String beanClass;
    // 任务状态
    private String jobStatus;
    // 任务分组
    private String jobGroup;
   
    // 任务名
    private String jobName;

   

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
       
    }



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getTenantId() {
		return tenantId;
	}



	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}



	public Integer getDr() {
		return dr;
	}



	public void setDr(Integer dr) {
		this.dr = dr;
	}



	public String getCreatePerson() {
		return createPerson;
	}



	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public String getUpdatePerson() {
		return updatePerson;
	}



	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}



	public Date getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



	public String getCronExpression() {
		return cronExpression;
	}



	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}



	public String getMethodName() {
		return methodName;
	}



	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	public String getIsConcurrent() {
		return isConcurrent;
	}



	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getBeanClass() {
		return beanClass;
	}



	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}


	public String getJobStatus() {
		return jobStatus;
	}



	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}



	public String getJobGroup() {
		return jobGroup;
	}



	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}



	public String getJobName() {
		return jobName;
	}



	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
    
}