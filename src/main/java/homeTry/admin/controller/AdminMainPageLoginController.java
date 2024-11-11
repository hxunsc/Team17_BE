package homeTry.admin.controller;

import homeTry.common.auth.kakaoAuth.config.KakaoAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMainPageLoginController {

    private final KakaoAuthConfig kakaoAuthConfig;

    public AdminMainPageLoginController(KakaoAuthConfig kakaoAuthConfig) {
        this.kakaoAuthConfig = kakaoAuthConfig;
    }

    @GetMapping
    public String redirectToAdminLoginPage(){
        return "redirect:admin/login";
    }

    @GetMapping("/admin/login")
    public String login(Model model) {
        String kakaoAuthUrl =
                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                        + kakaoAuthConfig.restApiKey() + "&redirect_uri="
                        + kakaoAuthConfig.redirectUri();

        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);

        return "adminMainPageLogin";
    }


}
