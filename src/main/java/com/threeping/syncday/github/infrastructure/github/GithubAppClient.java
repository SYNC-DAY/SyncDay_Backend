package com.threeping.syncday.github.infrastructure.github;


import com.threeping.syncday.common.exception.CommonException;
import com.threeping.syncday.common.exception.ErrorCode;
import com.threeping.syncday.github.config.GitHubFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubAppClient {

    private final GitHubFactory gitHubFactory;

    public GHAppInstallation getGithubInstallation(Long installationId) {
        try {
            GitHub github = gitHubFactory.createGithub();
            return github.getApp().getInstallationById(installationId);

        } catch (IOException e) {
            throw new CommonException(ErrorCode.GITHUB_APP_INSTALLATION_CLIENT_ERROR);
        }
    }

    public GHAppInstallationToken getInstallationToken(Long installationId) {
        try {
            GitHub github = gitHubFactory.createGithub();
            return github.getApp().getInstallationById(installationId).createToken().create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}