package ru.akalavan.cashflowservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.akalavan.cashflowservice.model.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
