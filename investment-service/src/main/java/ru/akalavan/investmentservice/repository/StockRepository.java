package ru.akalavan.investmentservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.akalavan.investmentservice.model.entity.Stock;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

    @Query(value = "SELECT * FROM Stock WHERE id = ?1 and categories_id = ?2", nativeQuery = true)
    Stock findByIdAndCategoriesId(Integer stockId, Integer categoryId);
}
