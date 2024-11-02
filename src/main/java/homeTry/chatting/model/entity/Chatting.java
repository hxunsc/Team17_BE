package homeTry.chatting.model.entity;

import homeTry.common.entity.BaseEntity;
import homeTry.team.model.entity.TeamMemberMapping;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Chatting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TeamMemberMapping teamMemberMapping;

    @Column(nullable = false)
    private String message;

    protected Chatting() {
    }

    public Chatting(TeamMemberMapping teamMember, String message) {
        this.teamMemberMapping = teamMember;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public TeamMemberMapping getTeamMemberMapping() {
        return teamMemberMapping;
    }

    public String getMessage() {
        return message;
    }
}
