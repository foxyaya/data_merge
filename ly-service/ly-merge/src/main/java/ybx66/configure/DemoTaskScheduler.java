package ybx66.configure;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import ybx66.service.impl.DataMergeServiceImpl;

import javax.annotation.PostConstruct;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/7 18:50
 * @description
 */
@Component
public class DemoTaskScheduler {
    private static Log log = LogFactory.getLog(DemoTaskScheduler.class);

    @Autowired
    SchedulerFactoryBean schedulerFactory;
    @Autowired
    private Scheduler scheduler;
//    @PostConstruct
//    public void init() {
//        try {
//            JobDetail jobDetail = JobBuilder.newJob(DataMergeServiceImpl.class).withIdentity("job", "test").build();
//            //2021-05-08 02:00:00 //2021-05-09 02:00:00
//            String cron = "0 0/1 * * * ? *";//"0 0 2-2 * * ? *";
//            jobDetail.getJobDataMap().put("cron", cron);
//            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger", "t1").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
//            scheduler.scheduleJob(jobDetail, trigger);
//            scheduler.start();
//            log.info("初始化成功!");
//        } catch (Exception e) {
//            log.info("", e);
//        }
//    }

    /**
     * 添加一个定时任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String cron) {
        try {
            System.out.println(" 初始化任务 " );
            scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
                log.info("添加任务成功");
                return "添加任务成功";
            }
        } catch (Exception e) {
            log.info("添加任务出现异常！,{}", e);
            return "添加任务出现异常！";
        }
        return "添加任务成功";
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        try {
            scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                //方式一 ：调用 rescheduleJob 开始
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            log.info("修改任务出现异常！,{}", e);
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            System.out.println(" 移除任务 " );
            scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            log.info("移除任务出现异常！,{}", e);
        }
    }

}
