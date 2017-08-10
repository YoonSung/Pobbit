package config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.google.common.base.Charsets;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.cache.TemplateLoader;
import no.api.freemarker.java8.Java8ObjectWrapper;

@Configuration
@ComponentScan(basePackages = "interfaces.rest")
public class WebConfig extends WebMvcConfigurationSupport {


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(customJackson2HttpMessageConverter());
		converters.add(stringHttpMessageConverter());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
	}

	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver("", ".ftl");
		viewResolver.setContentType("text/html;charset=" + CharEncoding.UTF_8);
		viewResolver.setExposeSpringMacroHelpers(true);
		viewResolver.setExposeRequestAttributes(true);
		return viewResolver;
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfig() throws Exception {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setDefaultEncoding(CharEncoding.UTF_8);
		freeMarkerConfigurer.setTemplateLoaderPaths("classpath:/website");
		
		freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
		TemplateLoader[] loaders = new TemplateLoader[] {
				configuration.getTemplateLoader()
		};
		configuration.setTemplateLoader(new MultiTemplateLoader(loaders));
		configuration.setObjectWrapper(new Java8ObjectWrapper(freemarker.template.Configuration.VERSION_2_3_21));
		configuration.setCacheStorage(new NullCacheStorage());
		freeMarkerConfigurer.setConfiguration(configuration);
		return freeMarkerConfigurer;
	}

	private MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		return jsonConverter;
	}

	private StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charsets.UTF_8);
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML));
		return converter;
	}
}