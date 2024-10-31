package homeTry.tag.model.entity;

import homeTry.team.model.vo.Name;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tag_name", nullable = false, length = 15))
    private Name tagName;

    protected Tag() {
    }

    public Tag(Name tagName) {
        this.tagName = tagName;
    }

    public Name getTagName() {
        return tagName;
    }

    public Long getId() {
        return id;
    }

    public void updateTagName(String tagName) {
        this.tagName = new Name(tagName);
    }
}
