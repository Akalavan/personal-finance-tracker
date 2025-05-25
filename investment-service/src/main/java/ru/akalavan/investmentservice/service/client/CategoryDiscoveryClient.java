package ru.akalavan.investmentservice.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.akalavan.investmentservice.model.entity.Category;

import java.util.List;

/**
 * Discovery Client следует использовать, только когда службе нужно обратиться
 * к балансировщику нагрузки, чтобы выяснить, какие службы
 * и экземпляры зарегистрированы в нем имеет несколько проблем, в том числе:
 * - не использует преимущества балансировки нагрузки на сто
 * роне клиента. Вызывая Discovery Client напрямую, вы получаете
 * список служб и должны сами решить, какой экземпляр вызывать;
 * - делает слишком много работы. Код должен создать URL, чтобы
 * вызвать службу. Это мелочь, но, избавляясь от таких фрагментов
 * кода, вы уменьшаете объем кода, который придется отлаживать.
 */
@Component
@RequiredArgsConstructor
public class CategoryDiscoveryClient {

    private final DiscoveryClient discoveryClient;

    public Category getCategory(final Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("cash-flow-service");

        if (instances == null || instances.isEmpty()) {
            return null;
        }

        String serviceUrl = String.format("%s/v1/cash-flow-api/categories/%d", instances.getFirst().getUri().toString(), id);

        ResponseEntity<Category> response =
            restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                null,
                Category.class,
                id);

        return response.getBody();
    }
}
