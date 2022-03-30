package my.playground.cqrs.ProductService.command.api.controller;

import lombok.extern.slf4j.Slf4j;
import my.playground.cqrs.ProductService.command.api.commands.CreateProductCommand;
import my.playground.cqrs.ProductService.command.api.model.AddProductReponse;
import my.playground.cqrs.ProductService.command.api.model.ProductRestModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductCommandController {

    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    public ProductCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String addProduct(@RequestBody ProductRestModel productRestModel) {
        log.info("addProduct before command handler");
        CreateProductCommand createProductCommand =
                CreateProductCommand.builder()
                        .productId(UUID.randomUUID().toString())
                        .name(productRestModel.getName())
                        .price(productRestModel.getPrice())
                        .quantity(productRestModel.getQuantity())
                        .build();

        AddProductReponse result = null;
        try {
            /*commandGateway
                    .send(createProductCommand, new CommandCallback<CreateProductCommand, Object>() {

                        @Override
                        public void onResult(CommandMessage<? extends CreateProductCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                            log.error(commandResultMessage.exceptionDetails().toString());
                        }
                    });*/

            MetaData metaData = MetaData.with("CORR_ID", UUID.randomUUID());
            result = (AddProductReponse) commandGateway.sendAndWait(createProductCommand, metaData);
            log.info("addProduct after command handler");
        } catch (Throwable e) {
            log.error(ExceptionUtils.getRootCauseMessage(e));
            //log.error(e.getMessage());
            /*while (e.getCause() != null) {
                e = (Exception) e.getCause();
                log.error(e.getMessage());
            }
            log.error(e.getMessage());*/
            return null;
        }
        return result.getScanRefNo();
    }
}
