package homeTry.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "is_deprecated")
    private boolean isDeprecated;

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void markAsDeprecated() {
        this.isDeprecated = true;
    }

}
