package ru.akalavan.cashflowservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.akalavan.cashflowservice.model.dto.NewCategoryDto;
import ru.akalavan.cashflowservice.model.entity.Category;
import ru.akalavan.cashflowservice.service.CategoryService;

import java.util.Locale;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cash-flow-api/categories")
public class CategoriesRestController {

    private final CategoryService categoryService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody NewCategoryDto payload,
                                                   BindingResult bindingResult,
                                                   UriComponentsBuilder uriComponentsBuilder,
                                                   @RequestHeader(value = "Accept-Language", required = false) Locale locale) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Category category = categoryService.createCategory(payload.name(), payload.description());
            category.add(
                linkTo(methodOn(CategoriesRestController.class)
                    .createCategory(payload, bindingResult, uriComponentsBuilder, locale))
                    .withSelfRel(),
                linkTo(methodOn(CategoriesRestController.class)
                    .findAllCategory())
                    .withRel("findAllCategory"));
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("cash-flow-api/categories/{categoryId}")
                            .build(Map.of("categoryId", category.getId())))
                    .body(category);
        }
    }

    @GetMapping
    public Iterable<Category> findAllCategory() {
        return categoryService.findAllCategories();
    }

}
