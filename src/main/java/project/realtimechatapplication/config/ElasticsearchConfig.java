package project.realtimechatapplication.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
    basePackages = "project.realtimechatapplication.repository.elasticsearch",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "project.realtimechatapplication.repository.jpa.*")
)
public class ElasticsearchConfig extends ElasticsearchConfiguration {

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder()
        .connectedTo("localhost:9200")
        .build();
  }
}
