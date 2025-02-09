package org.example.component;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.batch.TicketingAlertBatch;
import org.example.dto.response.TicketingAlertToSchedulerDomainResponse;
import org.example.entity.TicketingAlert;
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
            JobKey jobKey = getJobKey(ticketingAlert.userFcmToken(), ticketingAlert.showId());
            boolean jobExists = ticketingAlertScheduler.checkExists(jobKey);

            if (!jobExists) {
                JobDetail jobDetail = getJobDetail(ticketingAlert.userFcmToken(),
                    ticketingAlert.name(), ticketingAlert.showId());
                ticketingAlertScheduler.addJob(jobDetail, true, true);
            }

            List<TriggerKey> triggerKeysToRemove = ticketingAlert.deleteAlertAts().stream()
                .map(alertTime ->
                    getTriggerKey(ticketingAlert.userFcmToken(), ticketingAlert.showId(),
                        ticketingAlert.ticketingAt(), alertTime))
                .toList();
            ticketingAlertScheduler.unscheduleJobs(triggerKeysToRemove);

            for (LocalDateTime alertTime : ticketingAlert.addAlertAts()) {
                Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(
                        getTriggerKey(ticketingAlert.userFcmToken(), ticketingAlert.showId(),
                            ticketingAlert.ticketingAt(), alertTime))
                    .startAt(Date.from(alertTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .forJob(jobKey)
                    .build();

                ticketingAlertScheduler.scheduleJob(trigger);
            }
        } catch (SchedulerException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void updateTicketingAlerts(
        String previousFcmToken,
        List<TicketingAlert> ticketingAlerts) {
        try {
            List<TriggerKey> triggerKeysToRemove = ticketingAlerts.stream()
                .map(ticketingAlert ->
                    getTriggerKey(previousFcmToken, ticketingAlert.getShowId(),
                        ticketingAlert.getTicketingTime(), ticketingAlert.getAlertTime()))
                .toList();
            ticketingAlertScheduler.unscheduleJobs(triggerKeysToRemove);

            for (TicketingAlert ticketingAlert : ticketingAlerts) {
                JobDetail jobDetail = getJobDetail(ticketingAlert.getUserFcmToken(),
                    ticketingAlert.getName(), ticketingAlert.getShowId());
                ticketingAlertScheduler.addJob(jobDetail, true, true);

                Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(
                        getTriggerKey(ticketingAlert.getUserFcmToken(), ticketingAlert.getShowId(),
                            ticketingAlert.getTicketingTime(), ticketingAlert.getAlertTime()))
                    .startAt(Date.from(
                        ticketingAlert.getAlertTime().atZone(ZoneId.systemDefault()).toInstant()))
                    .forJob(
                        getJobDetail(ticketingAlert.getUserFcmToken(), ticketingAlert.getName(),
                            ticketingAlert.getShowId()))
                    .build();

                System.out.println(ticketingAlert.getUserFcmToken());

                ticketingAlertScheduler.scheduleJob(trigger);
            }
        } catch (SchedulerException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private TriggerKey getTriggerKey(
        String userFcmToken,
        UUID showId,
        LocalDateTime ticketingAt,
        LocalDateTime alertTime
    ) {
        return TriggerKey.triggerKey(
            userFcmToken + " : "
                + showId + " : "
                + alertTime,
            calculateAlertMinutes(alertTime, ticketingAt)
        );
    }

    private String calculateAlertMinutes(LocalDateTime alertTime, LocalDateTime ticketingAt) {
        return String.valueOf(Duration.between(alertTime, ticketingAt).toMinutes());
    }

    private JobDetail getJobDetail(String userFcmToken, String name, UUID showId) {
        JobKey jobKey = getJobKey(userFcmToken, showId);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("userFcmToken", userFcmToken);
        jobDataMap.put("name", name);
        jobDataMap.put("showId", showId.toString());
        jobDataMap.put("retryCount", 0);

        return JobBuilder.newJob(TicketingAlertQuartzJob.class)
            .withIdentity(jobKey)
            .usingJobData(jobDataMap)
            .build();
    }

    private JobKey getJobKey(String userFcmToken, UUID showId) {
        return new JobKey(userFcmToken + " : " + showId);
    }
}
