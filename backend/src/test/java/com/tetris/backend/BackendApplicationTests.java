package com.tetris.backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class BackendApplicationTests {

	@Test
	void applicationClassIsMarkedAsSpringBootApplication() {
		assertTrue(BackendApplication.class.isAnnotationPresent(SpringBootApplication.class));
	}

	@Test
	void mainMethodExistsAsPublicStaticEntryPoint() throws NoSuchMethodException {
		Method mainMethod = BackendApplication.class.getMethod("main", String[].class);

		assertTrue(Modifier.isPublic(mainMethod.getModifiers()));
		assertTrue(Modifier.isStatic(mainMethod.getModifiers()));
		assertTrue(mainMethod.getReturnType().equals(Void.TYPE));
	}

	@Test
	void applicationCanBeInstantiated() {
		assertDoesNotThrow(BackendApplication::new);
	}
}
