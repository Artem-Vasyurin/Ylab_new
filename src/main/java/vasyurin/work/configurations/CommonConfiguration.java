package vasyurin.work.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import vasyurin.work.utilites.YamlPropertySourceFactory;

@Configuration
@ComponentScan(basePackages = "vasyurin.work")
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
@EnableAspectJAutoProxy
public class CommonConfiguration {
}
