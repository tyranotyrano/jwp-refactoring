package kitchenpos.common.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.tablegroup.TableGroup;
import kitchenpos.common.dto.table.OrderTableResponse;
import kitchenpos.common.utils.StreamUtils;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {}

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup.getId(),
                                      tableGroup.getCreatedDate(),
                                      StreamUtils.mapToList(orderTables, OrderTableResponse::from));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TableGroupResponse that = (TableGroupResponse)o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate)
            && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, orderTables);
    }
}
