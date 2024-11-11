package homeTry.common.auth.kakaoAuth.controller;

import homeTry.common.auth.jwt.JwtAuth;
import homeTry.common.auth.kakaoAuth.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/oauth/login/ssr")
public class KakaoAuthSSRController {

    private final KakaoAuthService kakaoAuthService;
    private final JwtAuth jwtAuth;

    @Autowired
    KakaoAuthSSRController(KakaoAuthService kakaoAuthService, JwtAuth jwtAuth) {
        this.kakaoAuthService = kakaoAuthService;
        this.jwtAuth = jwtAuth;
    }

    @GetMapping
    public String loginOrRegisterSSR(@RequestParam(name = "code") String code, Model model) {
        String token = jwtAuth.generateToken(kakaoAuthService.loginOrRegister(code));
        model.addAttribute("access_token", token);
        return "adminKakaoLoginResult";
    }
}
