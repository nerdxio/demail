package io.nerd.demail.controller;

import io.nerd.demail.inbox.Folder;
import io.nerd.demail.inbox.FolderRepository;
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

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null && principal.getAttribute("login") != null) {
            String loginId = principal.getAttribute("login");
            List<Folder> folders = folderRepository.findAllById(loginId);
            if (folders.size() > 0) {
                model.addAttribute("folders", folders);
            } else {
//                List<Folder> initFolders = InitFolders.init(loginId);
//                initFolders.stream().forEach(folderRepository::save);
//                model.addAttribute("folders", initFolders);
            }

            return "inbox-page";
        }
        return "index";
    }

}
