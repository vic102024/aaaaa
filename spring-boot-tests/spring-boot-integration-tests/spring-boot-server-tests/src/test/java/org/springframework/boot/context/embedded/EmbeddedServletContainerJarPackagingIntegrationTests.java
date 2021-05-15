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

package org.springframework.boot.context.embedded;

import org.junit.jupiter.api.TestTemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Spring Boot's embedded servlet container support using jar
 * packaging.
 *
 * @author Andy Wilkinson
 */
@EmbeddedServletContainerTest(packaging = "jar",
		launchers = { PackagedApplicationLauncher.class, ExplodedApplicationLauncher.class })
public class EmbeddedServletContainerJarPackagingIntegrationTests {

	@TestTemplate
	public void nestedMetaInfResourceIsAvailableViaHttp(RestTemplate rest) {
		ResponseEntity<String> entity = rest.getForEntity("/nested-meta-inf-resource.txt", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@TestTemplate
	public void nestedMetaInfResourceIsAvailableViaServletContext(RestTemplate rest) {
		ResponseEntity<String> entity = rest.getForEntity("/servletContext?/nested-meta-inf-resource.txt",
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@TestTemplate
	public void nestedJarIsNotAvailableViaHttp(RestTemplate rest) {
		ResponseEntity<String> entity = rest.getForEntity("/BOOT-INF/lib/resources-1.0.jar", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@TestTemplate
	public void applicationClassesAreNotAvailableViaHttp(RestTemplate rest) {
		ResponseEntity<String> entity = rest
				.getForEntity("/BOOT-INF/classes/com/example/ResourceHandlingApplication.class", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@TestTemplate
	public void launcherIsNotAvailableViaHttp(RestTemplate rest) {
		ResponseEntity<String> entity = rest.getForEntity("/org/springframework/boot/loader/Launcher.class",
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
