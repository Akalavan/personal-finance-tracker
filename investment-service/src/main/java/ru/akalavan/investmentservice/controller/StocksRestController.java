package ru.akalavan.investmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.akalavan.investmentservice.model.dto.NewStockDto;
import ru.akalavan.investmentservice.model.entity.Stock;
import ru.akalavan.investmentservice.service.StockService;

import java.util.Locale;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/investment-api/stocks")
public class StocksRestController {

    private final StockService stockService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody NewStockDto payload,
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
            Stock stock = stockService.createStock(payload.name(), payload.description());
            stock.add(
                linkTo(methodOn(StocksRestController.class)
                    .createStock(payload, bindingResult, uriComponentsBuilder, locale))
                    .withSelfRel(),
                linkTo(methodOn(StocksRestController.class)
                    .findAllStock())
                    .withRel("findAllStock"));
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("investment-api/stocks/{stockId}")
                            .build(Map.of("stockId", stock.getId())))
                    .body(stock);
        }
    }

    @GetMapping
    public Iterable<Stock> findAllStock() {
        return stockService.findAllStock();
    }

}
