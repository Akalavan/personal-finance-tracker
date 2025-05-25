package ru.akalavan.investmentservice.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akalavan.investmentservice.model.entity.Category;
import ru.akalavan.investmentservice.model.entity.Stock;
import ru.akalavan.investmentservice.repository.StockRepository;
import ru.akalavan.investmentservice.service.client.CategoryDiscoveryClient;
import ru.akalavan.investmentservice.service.client.CategoryFeignClient;
import ru.akalavan.investmentservice.service.client.CategoryRestTemplateClient;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Service
public class DefaultStockService implements StockService {

    private final StockRepository stockRepository;

    private final CategoryFeignClient categoryFeignClient;
    private final CategoryRestTemplateClient categoryRestTemplateClient;
    private final CategoryDiscoveryClient categoryDiscoveryClient;

    private static final Logger logger = LoggerFactory.getLogger(DefaultStockService.class);

    @Override
    @Transactional
    public Stock createStock(String name, String description) {
        return stockRepository.save(new Stock(null, name, new BigDecimal(10) ,description, 1, "div"));
    }

    @Override
    public Iterable<Stock> findAllStock() {
        return stockRepository.findAll();
    }

    @Override
    // @CircuitBreaker обертывает метод findById() реализацией шаблона размыкателя цепи из Resilience4j. fallbackMethod - резервная реализация
    @CircuitBreaker(name = "stockService")
    public Optional<Stock> findById(Integer id) throws TimeoutException {
        return stockRepository.findById(id);
    }

    private Stock buildFallbackStockLis(Integer id, Integer categoryId, String clientType, Throwable t) {
        logger.error(t.getMessage());
        Stock stock = new Stock();

        stock.setId(id);
        stock.setName("Undefined");
        stock.setDescription("Sorry no stock information currently available");
        return stock;
    }

    @Override
    @CircuitBreaker(name = "stockServiceWithOrg",
        fallbackMethod = "buildFallbackStockLis") // Имя экземпляра и резервный метод для шаблона герметичных отсеков
    @Retry(name = "retryStockService", fallbackMethod = "buildFallbackStockLis") // Имя экземпляра и резервный метод для шаблона повторных попыток
    @Bulkhead(name = "bulkheadStockService",
        fallbackMethod = "buildFallbackStockLis",  // Имя экземпляра и резервный метод для шаблона герметичных отсеков
        type = Bulkhead.Type.SEMAPHORE) // Тип реализации шаблона герметичных отсеков (пул поток или семафор, по умолчанию семафор)
    public Stock getStock(Integer stockId, Integer categoryId, String clientType) {
        Stock stock = stockRepository.findByIdAndCategoriesId(stockId, categoryId);

        Category category = retrieveCategoryInfo(categoryId, clientType);

        if (category != null) {
            stock.setCategoryName(category.getName());
        }

        return stock;
    }

    @Override
    @Transactional
    public void deleteStock(Integer id) {
        stockRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStock(int stockId, String name, String description) {
        stockRepository.findById(stockId)
                .ifPresentOrElse(category -> {
                    category.setName(name);
                    category.setDescription(description);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    private Category retrieveCategoryInfo(Integer categoryId, String clientType) {
        Category category = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                category = categoryFeignClient.getCategory(categoryId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                category = categoryRestTemplateClient.getCategory(categoryId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                category = categoryDiscoveryClient.getCategory(categoryId);
                break;
            default:
                category = categoryRestTemplateClient.getCategory(categoryId);
        }

        return category;
    }
}
