package com.example.boardservice.controller;

import com.example.boardservice.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class OAuthController {

    @Autowired
    OAuthService oAuthService;

    @GetMapping("/do")
    public String loginPage() {
        return "loginForm";
    }

    @GetMapping("/kakao")
    public String getCI(@RequestParam(value = "code") String code, Model model) throws IOException {
        System.out.println("code = " + code);
        String access_token = oAuthService.getAccessToken(code);
        Map<String, Object> userInfo = oAuthService.getUserInfo(access_token);
        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);

        return "main";
    }

}