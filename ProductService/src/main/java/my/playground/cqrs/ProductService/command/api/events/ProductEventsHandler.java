package my.playground.cqrs.ProductService.command.api.events;

import lombok.extern.slf4j.Slf4j;
import my.playground.cqrs.ProductService.command.api.data.Product;
import my.playground.cqrs.ProductService.command.api.data.IProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product")
@Slf4j
public class ProductEventsHandler {

    @Autowired
    private IProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent, MetaData metaData) throws Exception {
        log.info("addProduct before IN EventHandler ");
        Product product = new Product();
        BeanUtils.copyProperties(productCreatedEvent, product);

        productRepository.save(product);
        log.info("addProduct before OUT EventHandler ");
//        throw new Exception("Exception occurred during Product create");
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception{
        throw exception;
    }
}
