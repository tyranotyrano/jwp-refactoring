package kitchenpos.application.order;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.utils.StreamUtils;

@Component
public class OrderValidator {
    private static final String EMPTY_ORDER_TABLE = "OrderTable 이 비어있습니다.";
    private static final String NOT_EXIST_ORDER_TABLE = "OrderTable 이 존재하지 않습니다.";
    private static final String NOT_EXIST_MENU = "Menu 가 존재하지 않습니다.";
    private static final String NOT_EXIST_ORDER = "Order 가 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrder(Order order) {
        validateExistMenus(order.getOrderLineItems());
        validateOrderTable(order.getOrderTableId());
    }

    public void validateOrderLineItem(OrderLineItem orderLineItem) {
        validateExistOrders(orderLineItem.getOrder());
        validateExistMenu(orderLineItem.getMenuId());
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

    private void validateExistMenu(Long menuId) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (!menuOpt.isPresent()) {
            throw new EntityNotFoundException(NOT_EXIST_MENU);
        }
    }

    private static void validateExistOrders(Order order) {
        if (Objects.isNull(order)) {
            throw new EntityNotFoundException(NOT_EXIST_ORDER);
        }
    }

    private OrderTable findOrderTable(Long id) {
        return orderTableRepository.findById(id)
                                   .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_ORDER_TABLE));
    }
}