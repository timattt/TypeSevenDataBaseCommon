package org.shlimtech.typesevendatabasecommon;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {MetadataService.class})
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=sa",
        "spring.profiles.active=debug",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://google.com"
})
@RequiredArgsConstructor
@ComponentScan(basePackages = {"org.shlimtech"})
@EnableJpaRepositories(basePackages = "org.shlimtech")
@ComponentScan(basePackages = "org.shlimtech")
@EntityScan(basePackages = "org.shlimtech")
@EnableAutoConfiguration
class TypeSevenDatabaseCommonApplicationTests {

    @Test
    void contextLoads() {
    }

}
