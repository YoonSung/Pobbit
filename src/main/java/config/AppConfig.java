package config;

import org.springframework.context.annotation.Import;

@Import({WebConfig.class, DbConfig.class})
public class AppConfig {
}
