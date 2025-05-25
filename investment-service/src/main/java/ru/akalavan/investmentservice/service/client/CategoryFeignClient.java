package ru.akalavan.investmentservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.akalavan.investmentservice.model.entity.Category;

@FeignClient("cash-flow-service") // Определение службы в Feign
public interface CategoryFeignClient {

    @GetMapping(
        value = "/v1/cash-flow-api/categories/{categoryId}", //Определение пути к конечной точке и операции с ней
        consumes = "application/json")
    Category getCategory(@PathVariable("categoryId") int categoryId);
}
