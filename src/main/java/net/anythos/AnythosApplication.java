package net.anythos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.ZonedDateTime;

@Slf4j
@SpringBootApplication
public class AnythosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnythosApplication.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void logStartTime() {
        log.info("#### Application started at: {} ####", ZonedDateTime.now());
    }

}
