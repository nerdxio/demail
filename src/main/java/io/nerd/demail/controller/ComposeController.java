package io.nerd.demail.controller;

import io.nerd.demail.email.EmailService;
import io.nerd.demail.folder.Folder;
import io.nerd.demail.folder.FolderRepository;
import io.nerd.demail.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ComposeController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    FolderService folderService;

    @Autowired
    EmailService emailService;


    @GetMapping(value = "/compose")
    public String getCompose(@RequestParam(required = false) String to, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        //fetch all folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        model.addAttribute("stats", folderService.mapCountToLabel(userId));
        model.addAttribute("userName", principal.getAttribute("name"));
        List<Folder> defaultFolders = folderService.fetchDefaultFolder(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        var uniqueToIds = splitIds(to);
        model.addAttribute("toIds", String.join(", ", uniqueToIds));

        return "compose-page";
    }

    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(@RequestBody MultiValueMap<String, String> formData, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("login") == null) {
            return new ModelAndView("redirect:/");
        }

        String from = principal.getAttribute("login");
        var toIds = splitIds(formData.getFirst("toIds"));
        var subject = formData.getFirst("subject");
        var body = formData.getFirst("body");

        emailService.sendEmail(from,toIds, subject, body);


        return new ModelAndView("redirect:/");
    }

    private List<String> splitIds(String to) {
        if (!StringUtils.hasText(to))
            return new ArrayList<String>();

        var ids = to.split(",");
        return Arrays.stream(ids)
                .map(StringUtils::trimWhitespace)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }
}
