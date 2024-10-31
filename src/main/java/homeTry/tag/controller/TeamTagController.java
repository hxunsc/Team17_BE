package homeTry.tag.controller;

import homeTry.tag.service.TeamTagService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//추후 관리자 쪽에서 api 추가 예정

@RestController
@RequestMapping("/api/admin/tag")
public class TeamTagController {

    private final TeamTagService teamTagService;

    public TeamTagController(TeamTagService teamTagService) {
        this.teamTagService = teamTagService;
    }
}
