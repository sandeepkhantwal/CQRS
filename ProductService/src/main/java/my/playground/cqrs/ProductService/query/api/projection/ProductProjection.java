package my.playground.cqrs.ProductService.query.api.projection;

import my.playground.cqrs.ProductService.command.api.data.IProductRepository;
import my.playground.cqrs.ProductService.command.api.model.ProductRestModel;
import my.playground.cqrs.ProductService.query.api.queries.GetProductsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductProjection {

    @Autowired
    private IProductRepository productRepository;

    @QueryHandler
    public List<ProductRestModel> handle(GetProductsQuery getProductsQuery) {
        return productRepository.findAll()
                    .stream()
                    .map(product -> ProductRestModel
                            .builder()
                            .name(product.getName())
                            .price(product.getPrice())
                            .quantity(product.getQuantity())
                            .build())
                    .collect(Collectors.toList());
    }
}
