package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    AlarmDomainConfig.class
})
@ComponentScan(basePackages = "org.example")
@RequiredArgsConstructor
public class AlarmApiConfig {

}
