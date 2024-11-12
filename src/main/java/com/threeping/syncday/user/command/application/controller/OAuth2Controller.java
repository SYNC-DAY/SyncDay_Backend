package com.threeping.syncday.user.command.application.controller;

import com.threeping.syncday.user.command.application.service.OAuth2Service;
import com.threeping.syncday.user.command.application.service.UserCommandService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/user/oauth2")
public class OAuth2Controller {

    private final ModelMapper modelMapper;
    private final UserCommandService userService;
    private final OAuth2Service oAuth2Service;

    @Autowired
    public OAuth2Controller(UserCommandService userService
            , ModelMapper modelMapper
            , OAuth2Service oAuth2Service) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.oAuth2Service = oAuth2Service;
    }

    @GetMapping("/github")
    public String github(@RequestParam String code) {
        log.info("code: {}", code);
        String githubAccessToken = oAuth2Service.getGithubAccessToken(code);
        log.info("githubAccessToken: {}", githubAccessToken);
        return null;
    }

}