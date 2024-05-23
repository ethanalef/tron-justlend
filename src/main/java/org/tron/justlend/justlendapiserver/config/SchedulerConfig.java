package org.tron.justlend.justlendapiserver.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "task.enabled", matchIfMissing = true, havingValue = "true")
public class SchedulerConfig implements SchedulingConfigurer {

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(100);
    taskScheduler.setThreadNamePrefix("scheduled-task-");
    taskScheduler.initialize();

    taskRegistrar.setTaskScheduler(taskScheduler);
  }
}
