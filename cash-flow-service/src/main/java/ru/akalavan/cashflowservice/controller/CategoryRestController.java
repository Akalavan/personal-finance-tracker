package ru.akalavan.cashflowservice.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akalavan.cashflowservice.model.entity.Category;
import ru.akalavan.cashflowservice.service.CategoryService;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cash-flow-api/categories/{categoryId:\\d+}")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    @RolesAllowed({"ADMIN", "USER"})
    public Category getCategory(@PathVariable int categoryId) {
        /**
         * clientType
         * Discovery – требует использовать для вызова службы организаций
         * клиента Discovery Client и стандартный класс Spring RestTemplate;
         * Rest – требует использовать для вызова службы Load Balancer
         * расширенный шаблон RestTemplate;
         * Feign – требует использовать для вызова службы через Load
         * Balancer клиентскую библиотеку Netflix Feign.
         *
         */
        return categoryService.findById(categoryId).orElseThrow(() -> new NoSuchElementException("Not found"));
    }
}
