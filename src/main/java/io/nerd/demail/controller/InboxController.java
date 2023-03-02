package io.nerd.demail.controller;

import io.nerd.demail.inbox.Folder;
import io.nerd.demail.inbox.FolderRepository;
import io.nerd.demail.inbox.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class InboxController {
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    FolderService folderService;

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }


        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders",userFolders);

        List<Folder> defaultFolders = folderRepository.findAllById(userId);
        model.addAttribute("defaultFolders",defaultFolders);

        return "inbox-page";
    }

}
