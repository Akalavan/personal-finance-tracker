package ru.akalavan.investmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akalavan.investmentservice.model.entity.Stock;
import ru.akalavan.investmentservice.service.StockService;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/investment-api/categories/{categoryId:\\d+}/stocks/{stockId:\\d+}")
public class StockRestController {

    private final StockService stockService;

    @GetMapping
    public Stock getStock(@PathVariable int stockId) throws TimeoutException {
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

        return stockService.findById(stockId).orElseThrow(() -> new NoSuchElementException("Not found"));
    }

    @GetMapping("/{clientType}")
    public Stock getStockByEureka(@PathVariable int categoryId,
                                  @PathVariable int stockId,
                                  @PathVariable String clientType) {
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

        return stockService.getStock(stockId, categoryId, clientType);
    }
}
