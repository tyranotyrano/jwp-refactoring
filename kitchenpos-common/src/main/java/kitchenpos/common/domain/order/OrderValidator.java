package kitchenpos.common.domain.order;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.menu.MenuRepository;
import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTableRepository;
import kitchenpos.common.utils.StreamUtils;

@Component
public class OrderValidator {
    private static final String EMPTY_ORDER_TABLE = "OrderTable 이 비어있습니다.";
    private static final String NOT_EXIST_ORDER_TABLE = "OrderTable 이 존재하지 않습니다.";
    private static final String INVALID_ORDER_LINE_ITEM_COUNT = "최소 1개 이상의 OrderLineItem 이 존재해야합니다.";
    private static final int MIN_ORDER_LINE_ITEM_COUNT = 1;

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validateOrderLineItems(order.getOrderLineItems());
        validateExistMenus(order.getOrderLineItems());
        validateOrderTable(order.getOrderTableId());
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (orderLineItems.size() < MIN_ORDER_LINE_ITEM_COUNT) {
            throw new IllegalArgumentException(INVALID_ORDER_LINE_ITEM_COUNT);
        }
    }

    private void validateExistMenus(OrderLineItems orderLineItems) {
        List<Long> menuIds = StreamUtils.mapToList(orderLineItems.getValues(), OrderLineItem::getMenuId);
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new EntityNotFoundException();
        }
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = findOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE);
        }
    }

    private OrderTable findOrderTable(Long id) {
        return orderTableRepository.findById(id)
                                   .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_ORDER_TABLE));
    }
}
