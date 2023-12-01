package system.share.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import system.core.service.schedule.job.JobScheduleListener;
import system.core.service.schedule.trigger.TriggerScheduleListener;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScheduleConfiguration {
    private final DataSource dataSource;
    private final ApplicationContext context;
    private final JobScheduleListener jobListener;
    private final TriggerScheduleListener triggerListener;
    private final PlatformTransactionManager transactionManager;
    private final QuartzProperties quartzProperties;

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerCustomizer() {
        return bean -> {
            bean.setAutoStartup(true);
            bean.setOverwriteExistingJobs(true);
            bean.setWaitForJobsToCompleteOnShutdown(true);
            bean.setDataSource(dataSource);
            bean.setApplicationContext(context);
            bean.setGlobalJobListeners(jobListener);
            bean.setGlobalTriggerListeners(triggerListener);
            bean.setTransactionManager(transactionManager);
            bean.setQuartzProperties(schedulerProperties());
            bean.setJobFactory(jobFactory());
        };
    }

    @Bean
    public SmartLifecycle gracefulShutdownHookForQuartz(SchedulerFactoryBean schedulerFactory) {
        return new SmartLifecycle() {
            private Boolean isRunning = false;

            @Override
            public void start() {
                log.info("[INFO] [Quartz] Getting Started ...");
                this.isRunning = true;
            }

            @Override
            public void stop(Runnable callback) {
                log.info("[INFO] [Spring] Shutting down ...");
                this.stop();
                callback.run();
            }

            @Override
            public void stop() {
                this.isRunning = false;

                try {
                    log.info("[INFO] [Quartz] Shutdown in Progress ...");
                    interruptJobs(schedulerFactory);
                    schedulerFactory.destroy();
                } catch (SchedulerException e1) {
                    try {
                        log.info("[INFO] [Quartz] Retry Shutdown: {}", e1.getMessage());
                        schedulerFactory.getScheduler().shutdown(false);
                    } catch (SchedulerException e2) {
                        log.info("[INFO] [Quartz] Shutdown failed: {}", e2.getMessage());
                    }
                }
            }

            @Override
            public boolean isAutoStartup() {
                return true;
            }

            @Override
            public boolean isRunning() {
                return this.isRunning;
            }

            @Override
            public int getPhase() {
                return Integer.MAX_VALUE;
            }
        };
    }

    private JobFactory jobFactory() {
        log.info("[INFO] Setting Job Factory");

        return new JobBeanFactory();
    }

    private Properties schedulerProperties() {
        log.info("[INFO] Setting Quartz Properties");
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        return properties;
    }

    private void interruptJobs(SchedulerFactoryBean schedulerFactory) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();

        for (JobExecutionContext context : scheduler.getCurrentlyExecutingJobs()) {
            JobDetail jobDetail = context.getJobDetail();
            log.info("[INFO] [Job] JOB Stopping ... :: [MORE] Job Key: {}", jobDetail.getKey());

            scheduler.interrupt(jobDetail.getKey());
        }
    }
}