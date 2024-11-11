package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SubscriptionApiConfig.class, TicketingApiConfig.class, AlarmApiConfig.class})
public class ApiConfig {

}
