package homeTry.adminTag.teamTag.model.entity;

import homeTry.adminTag.teamTag.model.vo.TeamTagAttribute;
import homeTry.adminTag.teamTag.model.vo.TeamTagName;
import homeTry.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class TeamTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "team_tag_name", nullable = false, length = 15))
    private TeamTagName teamTagName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "team_tag_attribute", nullable = false))
    private TeamTagAttribute teamTagAttribute;

    protected TeamTag() {}

    public TeamTag(String teamTagName, String teamTagAttribute) {
        this.teamTagName = new TeamTagName(teamTagName);
        this.teamTagAttribute = new TeamTagAttribute(teamTagAttribute);
    }

    public Long getId() {
        return id;
    }

    public TeamTagName getTeamTagName() {
        return teamTagName;
    }

    public TeamTagAttribute getTeamTagAttribute() {
        return teamTagAttribute;
    }
}