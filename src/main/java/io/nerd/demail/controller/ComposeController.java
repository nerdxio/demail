package io.nerd.demail.controller;

import io.nerd.demail.email.EmailRepository;
import io.nerd.demail.inbox.Folder;
import io.nerd.demail.inbox.FolderRepository;
import io.nerd.demail.inbox.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ComposeController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    FolderService folderService;

    @GetMapping(value = "/compose")
    public String getCompose(@RequestParam(required = false) String to, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        //fetch all folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderRepository.findAllById(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        if (StringUtils.hasText(to)) {
            var ids = to.split(",");
            var uniqueToIds = Arrays.stream(ids)
                    .map(StringUtils::trimWhitespace)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .collect(Collectors.toList());

            model.addAttribute("toIds", String.join(", ", uniqueToIds));
        }

        return "compose-page";
    }
}
