package io.nerd.demail;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.nerd.demail.config.DataStaxAstraProperties;
import io.nerd.demail.emaillist.EmailListItem;
import io.nerd.demail.emaillist.EmailListItemKey;
import io.nerd.demail.emaillist.EmailListItemRepository;
import io.nerd.demail.inbox.Folder;
import io.nerd.demail.inbox.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class DemailApplication {

    @Autowired
    FolderRepository folderRepository;
    @Autowired
    EmailListItemRepository emailListItemRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemailApplication.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }
    @PostConstruct
    public void init(){
        folderRepository.save( new Folder("hassanrefaat9","Inbox","blue"));
        folderRepository.save( new Folder("hassanrefaat9","Sent","green"));
        folderRepository.save( new Folder("hassanrefaat9","Important","red"));

        for (int i = 0; i < 10; i++) {
            EmailListItemKey key =new EmailListItemKey();
            key.setId("hassanrefaat9");
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem item = new EmailListItem();
            item.setKey(key);
            item.setTo(List.of("hassanrefaat9"));
            item.setSubject("Subject "+i);
            item.setUnread(true);
             emailListItemRepository.save(item);
        }
    }
}
