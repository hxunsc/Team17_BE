package homeTry.team.controller;

import homeTry.common.annotation.DateValid;
import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import homeTry.team.dto.request.TeamCreateRequest;
import homeTry.team.dto.request.CheckingPasswordRequest;
import homeTry.team.dto.response.RankingResponse;
import homeTry.team.dto.response.TagListResponse;
import homeTry.team.dto.response.TeamResponse;
import homeTry.team.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    //팀 생성 api
    @PostMapping
    public ResponseEntity<Void> addTeam(@LoginMember MemberDTO memberDTO,
                                        @Valid @RequestBody TeamCreateRequest teamCreateRequest) {
        teamService.addTeam(memberDTO, teamCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //팀 삭제 api
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@LoginMember MemberDTO memberDTO,
                                           @PathVariable("teamId") Long teamID) {
        teamService.deleteTeam(memberDTO, teamID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //팀 검색 api (페이징 적용)
    @GetMapping
    public ResponseEntity<Slice<TeamResponse>> searchingTeam(
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "tagIdList", required = false) List<Long> tagIdList,
            @RequestParam(name = "teamName", required = false) String teamName,
            @LoginMember MemberDTO memberDTO) {
        Slice<TeamResponse> searchedTeamList = teamService.getSearchedTeamList(pageable, tagIdList, teamName, memberDTO);
        return ResponseEntity.ok(searchedTeamList);
    }

    //그룹 검색 화면에 태그들을 보여주는 api
    @GetMapping("/teamTags")
    public ResponseEntity<TagListResponse> getTeamTagList(
            @LoginMember MemberDTO memberDTO) {
        TagListResponse response = teamService.getAllTeamTagList(memberDTO);
        return ResponseEntity.ok(response);
    }

    //팀 내 랭킹을 조회하는 api (페이징 적용)
    @GetMapping("/{teamId}/ranking")
    public ResponseEntity<RankingResponse> getTeamRanking(
            @LoginMember MemberDTO memberDTO,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 8, sort = "totalExerciseTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "date") @DateValid @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        RankingResponse rankingSlice = teamService.getTeamRanking(memberDTO, teamId, pageable, date);
        return ResponseEntity.ok(rankingSlice);
    }

    // 팀에 가입
    @PostMapping("/join/{teamId}")
    public ResponseEntity<Void> joinTeam(
            @LoginMember MemberDTO memberDTO,
            @PathVariable("teamId") Long teamId) {
        teamService.joinTeam(memberDTO, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //팀에서 탈퇴
    @DeleteMapping("/withdraw/{teamId}")
    public ResponseEntity<Void> withdrawTeam(
            @LoginMember MemberDTO memberDTO,
            @PathVariable("teamId") Long teamId) {
        teamService.withDrawTeam(memberDTO, teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //비밀번호 일치 검사 api
    @PostMapping("/checking/{teamId}")
    public ResponseEntity<Void> verifyPassword(
            @PathVariable("teamId") Long teamId,
            @RequestBody @Valid CheckingPasswordRequest checkingPasswordRequest) {
        teamService.checkPassword(teamId, checkingPasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/joined")
    public ResponseEntity<Slice<TeamResponse>> getJoinedTeam(
            @LoginMember MemberDTO memberDTO,
            @PageableDefault(size = 8, sort = "teamId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Slice<TeamResponse> responseSlice = teamService.getMyTeamList(memberDTO, pageable);
        return ResponseEntity.ok(responseSlice);
    }
}