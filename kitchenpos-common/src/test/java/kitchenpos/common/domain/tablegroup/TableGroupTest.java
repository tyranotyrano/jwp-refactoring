package kitchenpos.common.domain.tablegroup;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.common.domain.table.OrderTableValidator;
import kitchenpos.common.fixture.OrderTableFixtureFactory;
import kitchenpos.common.fixture.TableGroupFixtureFactory;
import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTableRepository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class TableGroupTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableValidator otderTableValidator;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private TableGroup 단체_테이블그룹;

    @BeforeEach
    void setUp() {
        테이블1 = OrderTableFixtureFactory.createWithGuests(1L, 0, true);
        테이블2 = OrderTableFixtureFactory.createWithGuests(2L, 0, true);
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);

        테이블1 = orderTableRepository.save(테이블1);
        테이블2 = orderTableRepository.save(테이블2);
        단체_테이블그룹 = tableGroupRepository.save(단체_테이블그룹);
    }

    @DisplayName("TableGroup 생성 시, 최소 2개 이상의 빈 OrderTables 이 필요하다.")
    @Test
    void create1() {
        // given
        List<OrderTable> orderTables = Arrays.asList(테이블1, 테이블2);

        // when & then
        otderTableValidator.validateGroupOrderTables(Arrays.asList(테이블1.getTableGroupId(), 테이블2.getId()), orderTables);
    }

    @DisplayName("TableGroup 생성 시, OrderTables 가 1개만 존재하면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        List<OrderTable> orderTableIds = Arrays.asList(테이블1);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                                                () -> otderTableValidator.validateGroupOrderTables(Arrays.asList(테이블1.getId()),
                                                                                                   orderTableIds))
                                            .withMessageContaining("최소 2개 이상의 OrderTable 이 존재해야합니다.");
    }

    @DisplayName("TableGroup 생성 시, OrderTables 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                                                () -> otderTableValidator.validateGroupOrderTables(Collections.emptyList(),
                                                                                                   Collections.emptyList()))
                                            .withMessageContaining("최소 2개 이상의 OrderTable 이 존재해야합니다.");
    }

    @DisplayName("TableGroup 생성 시, 비어있지 않은 OrderTable 가 있으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        테이블1.updateEmpty(false);
        List<OrderTable> orderTables = Arrays.asList(테이블1, 테이블2);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                                                () -> otderTableValidator.validateGroupOrderTables(Arrays.asList(테이블1.getTableGroupId(), 테이블2.getId()),
                                                                                                   orderTables))
                                            .withMessageContaining("OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.");
    }

    @DisplayName("TableGroup 생성 시, 이미 특정 TableGroup 에 속해있는 OrderTable 가 있으면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        테이블1.alignTableGroup(단체_테이블그룹.getId());
        List<OrderTable> orderTables = Arrays.asList(테이블1, 테이블2);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                                                () -> otderTableValidator.validateGroupOrderTables(Arrays.asList(테이블1.getTableGroupId(), 테이블2.getId()),
                                                                                                   orderTables))
                                            .withMessageContaining("OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.");
    }
}