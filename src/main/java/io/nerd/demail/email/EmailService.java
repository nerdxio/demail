package io.nerd.demail.email;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.nerd.demail.emaillist.EmailListItem;
import io.nerd.demail.emaillist.EmailListItemKey;
import io.nerd.demail.emaillist.EmailListItemRepository;
import io.nerd.demail.folder.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class EmailService {

    @Autowired
    private EmailListItemRepository emailsListItemRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void sendEmail(String from, List<String> to, String subject, String body) {

        // Save email entity
        Email email = new Email();
        email.setTo(to);
        email.setFrom(from);
        email.setSubject(subject);
        email.setBody(body);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);

        to.forEach(toId -> {
            EmailListItem item = createEmailListItem(to, subject, email, toId, "Inbox");
            emailsListItemRepository.save(item);
            unreadEmailStatsRepository.incrementUnreadCount(toId,"Inbox");
        });
        EmailListItem sendItemsEntry = createEmailListItem(to, subject, email, from, "Send");
        sendItemsEntry.setUnread(false);
        emailsListItemRepository.save(sendItemsEntry);
    }

    private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String ownerItem, String folder) {

        EmailListItemKey key = new EmailListItemKey();
        key.setId(ownerItem);
        key.setLabel(folder);
        key.setTimeUUID(email.getId());

        EmailListItem item = new EmailListItem();

        item.setKey(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setUnread(true);
        return item;
    }

}