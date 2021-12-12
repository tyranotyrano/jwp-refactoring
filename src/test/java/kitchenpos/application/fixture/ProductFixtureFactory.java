package kitchenpos.application.fixture;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductFixtureFactory {

    private ProductFixtureFactory() {}

    public static Product create(long id, String name, long price) {
        return Product.of(id, name, BigDecimal.valueOf(price));
    }
}
