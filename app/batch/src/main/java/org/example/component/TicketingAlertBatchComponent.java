package org.example.component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.batch.TicketingAlertBatch;
import org.example.dto.response.TicketingAlertToSchedulerDomainResponse;
import org.example.job.TicketingAlertQuartzJob;
import org.example.service.dto.response.TicketingAlertServiceResponse;
import org.example.usecase.TicketingAlertUseCase;
import org.example.vo.TicketingAlertTimeApiType;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketingAlertBatchComponent implements TicketingAlertBatch {

    private final Scheduler ticketingAlertscheduler;
    private final TicketingAlertUseCase ticketingAlertUseCase;

    @PostConstruct
    public void initializeJobsAndTriggers() {
        var ticketingAlerts = ticketingAlertUseCase.findAllTicketingAlerts();

        for (TicketingAlertToSchedulerDomainResponse response : ticketingAlerts) {
            reserveTicketingAlerts(TicketingAlertServiceResponse.from(response));
        }
    }

    @Override
    public void reserveTicketingAlerts(TicketingAlertServiceResponse ticketingAlert) {
        try {
            JobKey jobKey = getJobKey(ticketingAlert);
            boolean jobExists = ticketingAlertscheduler.checkExists(jobKey);

            if (!jobExists) {
                JobDetail jobDetail = getJobDetail(ticketingAlert);
                ticketingAlertscheduler.addJob(jobDetail, true, true);
            }

            List<TriggerKey> triggerKeysToRemove = ticketingAlert.alertTimesToRemove().stream()
                .map(alertTime -> getTriggerKey(ticketingAlert, alertTime.alertAt(),
                    alertTime.ticketingAlertTime()))
                .toList();
            ticketingAlertscheduler.unscheduleJobs(triggerKeysToRemove);

            for (var alertTime : ticketingAlert.alertTimesToAdd()) {
                Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey(ticketingAlert, alertTime.alertAt(), alertTime.ticketingAlertTime()))
                    .startAt(Date.from(alertTime.alertAt().atZone(ZoneId.systemDefault()).toInstant()))
                    .forJob(jobKey)
                    .build();

                ticketingAlertscheduler.scheduleJob(trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private TriggerKey getTriggerKey(
        TicketingAlertServiceResponse ticketingAlert,
        LocalDateTime alertTime,
        TicketingAlertTimeApiType type
    ) {
        return TriggerKey.triggerKey(
            ticketingAlert.userFcmToken() + " : "
                + ticketingAlert.showId() + " : "
                + alertTime,
            type.getTime()
        );
    }

    private JobDetail getJobDetail(TicketingAlertServiceResponse ticketingAlert) {
        JobKey jobKey = getJobKey(ticketingAlert);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("userFcmToken", ticketingAlert.userFcmToken());
        jobDataMap.put("name", ticketingAlert.name());
        jobDataMap.put("showId", ticketingAlert.showId());
        jobDataMap.put("retryCount", 0);

        return JobBuilder.newJob(TicketingAlertQuartzJob.class)
            .withIdentity(jobKey)
            .usingJobData(jobDataMap)
            .build();
    }

    private JobKey getJobKey(TicketingAlertServiceResponse ticketingAlert) {
        return new JobKey(
            ticketingAlert.userFcmToken() + " : "
                + ticketingAlert.showId()
        );
    }
}
