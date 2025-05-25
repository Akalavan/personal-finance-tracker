package ru.akalavan.cashflowservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akalavan.cashflowservice.model.entity.Category;
import ru.akalavan.cashflowservice.repository.CategoryRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(String name, String description) {
        return categoryRepository.save(new Category(null, name, description));
    }

    @Override
    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCategory(int categoryId, String name, String description) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(category -> {
                    category.setName(name);
                    category.setDescription(description);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }
}
