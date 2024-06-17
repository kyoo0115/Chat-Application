package project.realtimechatapplication.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = "project.realtimechatapplication.repository.jpa",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "project.realtimechatapplication.repository.elasticsearch.*")
)
public class JpaConfig {

}
