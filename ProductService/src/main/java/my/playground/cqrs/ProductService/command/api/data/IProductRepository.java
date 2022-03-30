package my.playground.cqrs.ProductService.command.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component("ProductRepository")
public interface IProductRepository extends JpaRepository<Product, String> {
}
