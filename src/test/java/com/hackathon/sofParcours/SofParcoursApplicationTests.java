package com.hackathon.sofParcours;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration"
})
class SofParcoursApplicationTests {

	@Test
	void contextLoads() {
		// Test basique - vérifie juste que le contexte Spring se charge
	}

}
