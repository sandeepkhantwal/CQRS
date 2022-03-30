package my.playground.cqrs.ProductService.command.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductReponse {

    private String scanRefNo;
}
