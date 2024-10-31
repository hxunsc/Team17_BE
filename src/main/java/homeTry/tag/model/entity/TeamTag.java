package homeTry.tag.model.entity;

import homeTry.team.model.vo.Name;
import jakarta.persistence.*;

@Entity
public class TeamTag extends Tag {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tag_attribute", nullable = false))
    private Name tagAttribute;

    protected TeamTag() {
        super();
    }

    public TeamTag(Name tagName, Name tagAttribute) {
        super(tagName);
        this.tagAttribute = tagAttribute;
    }

    public TeamTag(String tagName, String tagAttribute) {
        this(new Name(tagName), new Name(tagAttribute));
    }

    public Name getTagAttribute() {
        return tagAttribute;
    }

    public void updateAttribute(String tagAttribute) {
        this.tagAttribute = new Name(tagAttribute);
    }
}

