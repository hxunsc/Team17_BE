package homeTry.tag.model.entity;

import homeTry.team.model.vo.Name;
import jakarta.persistence.Entity;

@Entity
public class ProductTag extends Tag {

    protected ProductTag() {
        super();
    }

    public ProductTag(String name) {
        super(new Name(name));
    }

    public String getTagNameValue() {
        return getTagName().value();
    }

}
