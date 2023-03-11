package io.nerd.demail.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.nerd.demail.emaillist.EmailListItemRepository;
import io.nerd.demail.folder.Folder;
import io.nerd.demail.folder.FolderRepository;
import io.nerd.demail.folder.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class InboxController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;
    @Autowired
    FolderService folderService;

    @GetMapping("/")
    public String homePage(@RequestParam(required = false)String folder, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        //fetch all folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderRepository.findAllById(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        //fetch all messages
        if (!StringUtils.hasText(folder)){
            folder ="Inbox";
        }
        var emailList = emailListItemRepository.findAllByKey_IdAndKey_label(userId, folder);

        PrettyTime p = new PrettyTime();
        emailList.stream().forEach(item -> {
                    UUID timeUuid = item.getKey().getTimeUUID();
                    var emailDataTime = new Date(Uuids.unixTimestamp(timeUuid));
                    item.setAgoTimeString(p.format(emailDataTime));
                }
        );

        model.addAttribute("emailList", emailList);
        model.addAttribute("folderName", folder);
        return "inbox-page";
    }

}
