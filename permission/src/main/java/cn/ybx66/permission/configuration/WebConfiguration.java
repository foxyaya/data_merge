package cn.ybx66.permission.configuration;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport{
	/**
	 * 跨域处理
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedHeaders("*")
				.allowedMethods("*")
				.allowedOrigins("*");
	}
	
	@Override
	protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
		configurer.setDefaultTimeout(30000);
	}
}
