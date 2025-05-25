package ru.akalavan.investmentservice.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.akalavan.investmentservice.model.entity.Category;

@Component
@RequiredArgsConstructor
public class CategoryRestTemplateClient {

    private final RestTemplate restTemplate;

    public Category getCategory(Integer categoryId) {
        ResponseEntity<Category> response =
            restTemplate.exchange(
                "http://cash-flow-service/v1/cash-flow-api/categories/{categoryId}",
                HttpMethod.GET,
                null,
                Category.class,
                categoryId
            );

        return response.getBody();
    }
}
