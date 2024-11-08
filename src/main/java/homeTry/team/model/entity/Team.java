package homeTry.team.model.entity;

import homeTry.common.entity.BaseEntity;
import homeTry.member.model.entity.Member;
import homeTry.team.exception.NotTeamLeaderException;
import homeTry.team.model.vo.Description;
import homeTry.team.model.vo.Name;
import homeTry.team.model.vo.Participant;
import homeTry.team.model.vo.Password;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "team_name", nullable = false, unique = true, length = 15))
    private Name teamName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "team_description", nullable = false))
    private Description teamDescription;

    @Column(nullable = false)
    private Long leaderId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "max_participants", nullable = false))
    private Participant maxParticipants;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "current_participants", nullable = false))
    private Participant currentParticipants;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;

    protected Team() {
    }

    public Team(String teamName, String teamDescription, Member leader, long maxParticipants,
                long currentParticipants, String password) {
        this.teamName = new Name(teamName);
        this.teamDescription = new Description(teamDescription);
        this.leaderId = leader.getId();
        this.maxParticipants = new Participant(maxParticipants);
        this.currentParticipants = new Participant(currentParticipants);
        this.password = new Password(password);
    }

    public Long getId() {
        return id;
    }

    public Name getTeamName() {
        return teamName;
    }

    public Description getTeamDescription() {
        return teamDescription;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public Participant getMaxParticipants() {
        return maxParticipants;
    }

    public Participant getCurrentParticipants() {
        return currentParticipants;
    }

    public Optional<Password> getPassword() {
        return Optional.ofNullable(password);
    }

    public void decreaseParticipantsByWithdraw() {
        long decreasedParticipants = getCurrentParticipants().value() - 1;
        this.currentParticipants = new Participant(decreasedParticipants);
    }

    public boolean validateIsLeader(long memberId) {
        if (leaderId != memberId) {
            throw new NotTeamLeaderException();
        }

        return true;
    }

    public void updateTeam(String teamName, String teamDescription, Member leader,
                           long maxParticipants, long currentParticipants, String password) {
        this.teamName = new Name(teamName);
        this.teamDescription = new Description(teamDescription);
        this.leaderId = leader.getId();
        this.maxParticipants = new Participant(maxParticipants);
        this.currentParticipants = new Participant(currentParticipants);
        this.password = new Password(password);
    }
}
