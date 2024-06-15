package com.hnv99.framework.config;

import com.hnv99.common.utils.ApplicationContextUtils;
import com.hnv99.system.service.impl.SysBillServiceImpl;
import com.hnv99.system.service.impl.SysSessionServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class QuartzConfig {

    @Autowired
    private SysBillServiceImpl sysBillService;

    @Autowired
    private SysSessionServiceImpl sysSessionService;

    @Bean
    public JobDetail jobDetail() {
        QuartzJobBean quartzJob = new QuartzJobBean() {
            @Override
            protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("Executing cancellation of overdue orders: " + sdf.format(new Date()));
                // Cancel all overdue orders and release seat resources
                ApplicationContextUtils.getBean("cancelTimeoutBill");
            }
        };
        return JobBuilder.newJob(quartzJob.getClass()).storeDurably().build();
    }

    @Bean
    public SimpleTrigger trigger() {
        // Execute every 5 minutes and repeat indefinitely
        SimpleScheduleBuilder scheduleBuilder =
                SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5 * 60)
                        .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(scheduleBuilder).build();
    }

}
