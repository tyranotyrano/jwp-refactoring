package kitchenpos.domain.menugroup;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.domain.Name;

@Entity
@Table(name = "menu_group")
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {}

    private MenuGroup(Long id, String name) {
        this(name);
        this.id = id;
    }

    private MenuGroup(String name) {
        this.name = Name.from(name);
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
