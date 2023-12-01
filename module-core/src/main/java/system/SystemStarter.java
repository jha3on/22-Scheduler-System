package system;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import system.share.schedule.JobBeanProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {JobBeanProperties.class})
public class SystemStarter implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SystemStarter.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        // ...
    }
}