package config;

import org.springframework.context.annotation.Import;

@Import({
		WebConfig.class, DbConfig.class,
        BatchJobConfig.class, ClientConfig.class
})
public class AppConfig {
}
