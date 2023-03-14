package io.nerd.demail.controller;

import io.nerd.demail.email.Email;
import io.nerd.demail.email.EmailRepository;
import io.nerd.demail.email.EmailService;
import io.nerd.demail.folder.Folder;
import io.nerd.demail.folder.FolderRepository;
import io.nerd.demail.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class EmailViewController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    FolderService folderService;
    @Autowired
    EmailService emailService;

    @GetMapping("/emails/{id}")
    public String emailView(@PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        //fetch all folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.fetchDefaultFolder(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        model.addAttribute("stats", folderService.mapCountToLabel(userId));
        model.addAttribute("userName",principal.getAttribute("name"));


        Optional<Email> optionalEmail = emailRepository.findById(id);
        if (optionalEmail.isEmpty()) {
            return "inbox-page";
        }
        var email = optionalEmail.get();
        var toIds = String.join(",", email.getTo());
        // ->
        if (!userId.equals(email.getFrom()) && !email.getTo().contains(userId)){
            return "redirect:/";
        }

        // ->
        model.addAttribute("email", email);
        model.addAttribute("toIds", toIds);
        return "email-page";
    }
}
