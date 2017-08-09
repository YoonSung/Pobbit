package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({
		WebConfig.class, DbConfig.class,
        BatchJobConfig.class, ClientConfig.class
})
@ComponentScan("service")
public class AppConfig {
}
