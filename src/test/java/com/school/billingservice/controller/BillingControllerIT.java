package com.school.billingservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({@Sql(scripts = "classpath:/populate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:/truncate-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class BillingControllerIT {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${local.server.port}")
    private int port;

    @Test
    @SneakyThrows
    public void shouldReturnAReportForASchool() {
        // Given
        String url = "http://localhost:" + port + "/api/billings/schools/2?year=2024&month=1";

        // When
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals(responseEntity.getBody(), getFileContent("school-report.json"),
                JSONCompareMode.STRICT);
    }

    @Test
    @SneakyThrows
    public void shouldReturnAReportForParent() {
        // Given
        String url = "http://localhost:" + port + "/api/billings/schools/1/parents/100?year=2024&month=1";

        // When
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals(responseEntity.getBody(), getFileContent("parent-report.json"),
                JSONCompareMode.STRICT);
    }

    @Test
    void shouldReturnSchoolNotFoundError() {
        // Given
        String url = "http://localhost:" + port + "/api/billings/schools/999?year=2024&month=1";

        // When
        ResponseEntity<String> responseEntity = getForResponse(url);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody())
                .isEqualTo("{\"status\":\"NOT_FOUND\",\"message\":\"School not found\"}");
    }

    @Test
    void shouldReturnDayTimeError() {
        // Given
        String url = "http://localhost:" + port + "/api/billings/schools/2/parents/100?year=2024&month=1111";

        // When
        ResponseEntity<String> responseEntity = getForResponse(url);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody())
                .isEqualTo("{\"status\":\"BAD_REQUEST\",\"message\":\"Invalid value for MonthOfYear (valid values 1 - 12): 1111\"}");
    }

    private String getFileContent(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource(fileName).getFile()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file " + fileName, e);
        }
    }

    private ResponseEntity<String> getForResponse(String url) {
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}

