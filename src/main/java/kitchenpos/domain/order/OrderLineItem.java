package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.domain.menu.Menu;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Orders orders, Menu menu, long quantity) {
        this(menu, quantity);
        this.seq = seq;
        this.orders = orders;
    }

    private OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Orders orders, Menu menu, long quantity) {
        return new OrderLineItem(seq, orders, menu, quantity);
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public void assignOrders(Orders orders) {
        this.orders = orders;
    }

    public Long getSeq() {
        return seq;
    }

    public Orders getOrders() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
