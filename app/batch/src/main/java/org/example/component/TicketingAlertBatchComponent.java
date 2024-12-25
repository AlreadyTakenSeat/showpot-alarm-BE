package org.example.component;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.batch.TicketingAlertBatch;
import org.example.dto.response.TicketingAlertToSchedulerDomainResponse;
import org.example.job.TicketingAlertQuartzJob;
import org.example.service.dto.response.TicketingAlertServiceRequest;
import org.example.usecase.TicketingAlertUseCase;
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

    private final Scheduler ticketingAlertScheduler;
    private final TicketingAlertUseCase ticketingAlertUseCase;

    @PostConstruct
    public void initializeJobsAndTriggers() {
        var ticketingAlerts = ticketingAlertUseCase.findAllTicketingAlerts();

        for (TicketingAlertToSchedulerDomainResponse response : ticketingAlerts) {
            reserveTicketingAlerts(TicketingAlertServiceRequest.from(response));
        }
    }

    @Override
    public void reserveTicketingAlerts(TicketingAlertServiceRequest ticketingAlert) {
        try {
            JobKey jobKey = getJobKey(ticketingAlert);
            boolean jobExists = ticketingAlertScheduler.checkExists(jobKey);

            if (!jobExists) {
                JobDetail jobDetail = getJobDetail(ticketingAlert);
                ticketingAlertScheduler.addJob(jobDetail, true, true);
            }

            LocalDateTime currentTime = LocalDateTime.now();

            List<TriggerKey> triggerKeysToRemove = ticketingAlert.deleteAlertAts().stream()
                .map(alertTime -> getTriggerKey(ticketingAlert, currentTime, alertTime))
                .toList();
            ticketingAlertScheduler.unscheduleJobs(triggerKeysToRemove);

            List<LocalDateTime> currentTimePlusSeconds = List.of(
                currentTime.minusSeconds(5),
                currentTime.minusSeconds(10),
                currentTime.minusSeconds(30),
                currentTime.minusSeconds(60)
            );

            for (LocalDateTime alertTime : currentTimePlusSeconds) {
                Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey(ticketingAlert, currentTime, alertTime))
                    .startAt(Date.from(alertTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .forJob(jobKey)
                    .build();

                ticketingAlertScheduler.scheduleJob(trigger);
            }
        } catch (SchedulerException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private TriggerKey getTriggerKey(
        TicketingAlertServiceRequest ticketingAlert,
        LocalDateTime currentTime,
        LocalDateTime alertTime
    ) {
        return TriggerKey.triggerKey(
            ticketingAlert.userFcmToken() + " : "
                + ticketingAlert.showId() + " : "
                + alertTime,
            calculateAlertMinutes(alertTime, currentTime)
        );
    }

    private String calculateAlertMinutes(LocalDateTime alertTime, LocalDateTime ticketingAt) {
        return String.valueOf(Duration.between(alertTime, ticketingAt).toSeconds());
    }

    private JobDetail getJobDetail(TicketingAlertServiceRequest ticketingAlert) {
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

    private JobKey getJobKey(TicketingAlertServiceRequest ticketingAlert) {
        return new JobKey(
            ticketingAlert.userFcmToken() + " : "
                + ticketingAlert.showId()
        );
    }
}
