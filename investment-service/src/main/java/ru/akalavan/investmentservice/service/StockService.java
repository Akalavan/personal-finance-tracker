package ru.akalavan.investmentservice.service;


import ru.akalavan.investmentservice.model.entity.Stock;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

public interface StockService {

    Stock createStock(String name, String description);

    Iterable<Stock> findAllStock();

    Optional<Stock> findById(Integer id) throws TimeoutException;

    Stock getStock(Integer stockId, Integer categoryId, String clientType);

    void deleteStock(Integer id);

    void updateStock(int stockId, String name, String description);
}

