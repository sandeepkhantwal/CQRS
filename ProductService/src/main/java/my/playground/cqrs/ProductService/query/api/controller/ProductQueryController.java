package my.playground.cqrs.ProductService.query.api.controller;

import my.playground.cqrs.ProductService.command.api.model.ProductRestModel;
import my.playground.cqrs.ProductService.query.api.queries.GetProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getAllProducts() {
        GetProductsQuery getProductsQuery = new GetProductsQuery();

        List<ProductRestModel> productRestModelList =
            queryGateway.query(getProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
        return productRestModelList;
    }
}
