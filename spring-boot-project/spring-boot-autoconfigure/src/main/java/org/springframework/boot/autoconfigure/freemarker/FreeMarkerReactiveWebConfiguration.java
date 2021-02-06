/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.freemarker;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfig;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerViewResolver;

/**
 * Configuration for FreeMarker when used in a reactive web context.
 *
 * @author Zhengsheng Xia
 * @author Brian Clozel
 * @author Andy Wilkinson
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@AutoConfigureAfter(WebFluxAutoConfiguration.class)
class FreeMarkerReactiveWebConfiguration extends AbstractFreeMarkerConfiguration {

	FreeMarkerReactiveWebConfiguration(FreeMarkerProperties properties) {
		super(properties);
	}

	@Bean
	@ConditionalOnMissingBean(FreeMarkerConfig.class)
	FreeMarkerConfigurer freeMarkerConfigurer(freemarker.template.Configuration configuration) {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setConfiguration(configuration);
		applyProperties(configurer);
		return configurer;
	}

	@Bean
	@ConditionalOnMissingBean(freemarker.template.Configuration.class)
	freemarker.template.Configuration freeMarkerConfiguration() {
		// Avoid the configurer(created by xml) itself call the createConfiguration,there
		// is an error of checking template path by afterPropertiesSet in fatjar
		freemarker.template.Configuration fmConfiguration = new freemarker.template.Configuration(
				freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		return fmConfiguration;
	}

	@Bean
	@ConditionalOnMissingBean(name = "freeMarkerViewResolver")
	@ConditionalOnProperty(name = "spring.freemarker.enabled", matchIfMissing = true)
	FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setPrefix(getProperties().getPrefix());
		resolver.setSuffix(getProperties().getSuffix());
		resolver.setRequestContextAttribute(getProperties().getRequestContextAttribute());
		resolver.setViewNames(getProperties().getViewNames());
		return resolver;
	}

}
