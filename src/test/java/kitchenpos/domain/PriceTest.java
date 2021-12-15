package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.exception.NegativePriceException;

class PriceTest {

    @DisplayName("Price 는 0원 이상으로 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100})
    void create1(int price) {
        // when & then
        assertThatNoException().isThrownBy(() -> Price.from(BigDecimal.valueOf(price)));
    }

    @DisplayName("Price 를 null 값으로 만들면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void creat2(BigDecimal price) {
        // when & then
        assertThrows(NegativePriceException.class, () -> Price.from(price));
    }

    @DisplayName("Price 를 0원 미만의 음수값으로 만들면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int price) {
        // when & then
        assertThrows(NegativePriceException.class, () -> Price.from(BigDecimal.valueOf(price)));
    }
}