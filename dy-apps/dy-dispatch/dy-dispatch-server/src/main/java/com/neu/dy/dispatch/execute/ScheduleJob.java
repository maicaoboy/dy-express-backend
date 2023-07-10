package com.neu.dy.dispatch.execute;

import com.neu.dy.dispatch.service.ScheduleJobLogService;
import com.neu.dy.dispatch.service.ScheduleJobService;
import com.neu.dy.dispatch.utils.ExceptionUtils;
import com.neu.dy.dispatch.utils.IdUtils;
import com.neu.dy.dispatch.utils.ScheduleUtils;
import com.neu.dy.entity.ScheduleJobEntity;
import com.neu.dy.entity.ScheduleJobLogEntity;
import com.neu.dy.utils.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Date;

/**
 * 定时任务类，进行智能调度操作
 */
public class ScheduleJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJob =
                (ScheduleJobEntity) context.getMergedJobDataMap().
                        get(ScheduleUtils.JOB_PARAM_KEY);
        //数据库保存执行记录
        ScheduleJobLogEntity logEntity = new ScheduleJobLogEntity();
        logEntity.setId(IdUtils.get());
        logEntity.setJobId(scheduleJob.getId());
        logEntity.setBeanName(scheduleJob.getBeanName());
        logEntity.setParams(scheduleJob.getParams());
        logEntity.setCreateDate(new Date());

        long startTime = System.currentTimeMillis();
        try {
            logger.info("任务准备执行，任务ID：{}",scheduleJob.getId());
            //根据名字获得目标对象
            Object target = SpringContextUtils.getBean(scheduleJob.getBeanName());
            //获取目标对象的run方法，也就是任务
            Method method = target.getClass().getDeclaredMethod("run", String.class, String.class, String.class, String.class);

            //通过反射调用方法
            method.invoke(target,scheduleJob.getBusinessId(),scheduleJob.getParams(),scheduleJob.getId(),logEntity.getId());
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            logEntity.setTimes((int)times);
            logEntity.setStatus(1);//成功执行
            logger.info("任务执行完毕，任务ID：{} 总共耗时:{} 毫秒",scheduleJob.getId(),times);
        }catch (Exception e){
            logger.error("任务执行失败，任务ID:{}",scheduleJob.getId(),e);
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            logEntity.setTimes((int) times);
            //任务执行失败，报异常
            logEntity.setStatus(0);
            logEntity.setError(ExceptionUtils.getErrorStackTrace(e));
        } finally {
            ScheduleJobLogService scheduleJobLogService = SpringContextUtils.getBean(ScheduleJobLogService.class);
            scheduleJobLogService.save(logEntity);
        }
    }
}
