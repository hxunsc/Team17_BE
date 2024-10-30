package homeTry.adminTag.productTag.model.entity;

import homeTry.adminTag.productTag.model.vo.ProductTagName;
import homeTry.common.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductTag extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_tag_name", nullable = false, length = 15))
    private ProductTagName productTagName;

    protected ProductTag() {}

    public ProductTag(String productTagName) {
        this.productTagName = new ProductTagName(productTagName);
    }

    public Long getId() {
        return id;
    }

    public ProductTagName getProductTagName() {
        return productTagName;
    }
}
