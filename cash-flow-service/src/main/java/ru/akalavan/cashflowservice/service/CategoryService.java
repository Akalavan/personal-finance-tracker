package ru.akalavan.cashflowservice.service;


import ru.akalavan.cashflowservice.model.entity.Category;

import java.util.Optional;

public interface CategoryService {

    Category createCategory(String name, String description);

    Iterable<Category> findAllCategories();

    Optional<Category> findById(Integer id);

    void deleteCategory(Integer id);

    void updateCategory(int categoryId, String name, String description);
}

