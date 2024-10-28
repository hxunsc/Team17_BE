package homeTry.product.model.entity;

import homeTry.common.entity.BaseEntity;
import homeTry.product.model.vo.ProductTagName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private ProductTagName name;

    protected ProductTag() {

    }

    public ProductTag(String name) {
        this.name = new ProductTagName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

}
