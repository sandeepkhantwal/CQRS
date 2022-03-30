package my.playground.cqrs.ProductService.command.api.aggregate;

import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.playground.cqrs.ProductService.command.api.commands.CreateProductCommand;
import my.playground.cqrs.ProductService.command.api.data.IProductRepository;
import my.playground.cqrs.ProductService.command.api.events.ProductCreatedEvent;
import my.playground.cqrs.ProductService.command.api.model.AddProductReponse;
import my.playground.cqrs.ProductService.command.api.model.ProductRestModel;
import my.playground.cqrs.ProductService.command.api.service.ProductService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcedAggregate;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.Id;
import java.math.BigDecimal;

@Aggregate
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String scanRefNo;

    @Autowired
    @Qualifier("ProductRepository")
    private IProductRepository productRepository;

    @Autowired
    @Qualifier("productService")
    private ProductService productService;

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    public AddProductReponse handle(CreateProductCommand createProductCommand, MetaData metaData) throws Exception {
        log.info("addProduct before IN command handler");
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
       /* if(true)
            throw new Exception("Exception while validating in Command Handler");*/
        ProductAggregate aggregate = (ProductAggregate)((EventSourcedAggregate)AggregateLifecycle.apply(productCreatedEvent, metaData)).getAggregateRoot();

        log.info("addProduct before OUT command handler");

        return AddProductReponse
                .builder()
                .scanRefNo(aggregate.scanRefNo)
                .build();

    }

    @ExceptionHandler(payloadType = ProductCreatedEvent.class)
    public void handleProductCreatedEventException(Exception exception) throws Exception {
        log.error(exception.getMessage() + " <---------------->");
        throw exception;
    }

   /* @ExceptionHandler(payloadType = CreateProductCommand.class)
    public void handleCreateProductCommandException(Exception exception) throws Exception {
        log.error(exception.getMessage() + " <$$$$$$$$$>");
        throw exception;
    }*/

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent, MetaData metaData) throws Exception {
        log.info("addProduct before IN EventSourcingHandler ");
        this.name = productCreatedEvent.getName();
        this.price = productCreatedEvent.getPrice();
        this.productId = productCreatedEvent.getProductId();
        this.quantity = productCreatedEvent.getQuantity();
        this.scanRefNo = "ASGSG000000089";
        log.info("addProduct before OUT EventSourcingHandler ");
        //throw new Exception("Exception from EventSourcingHandler");
    }
}
