package io.nerd.demail;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.nerd.demail.config.DataStaxAstraProperties;
import io.nerd.demail.email.Email;
import io.nerd.demail.email.EmailRepository;
import io.nerd.demail.email.EmailService;
import io.nerd.demail.emaillist.EmailListItem;
import io.nerd.demail.emaillist.EmailListItemKey;
import io.nerd.demail.emaillist.EmailListItemRepository;
import io.nerd.demail.folder.Folder;
import io.nerd.demail.folder.FolderRepository;
import io.nerd.demail.folder.UnreadEmailStatsRepository;
import lombok.RequiredArgsConstructor;
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
    EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(DemailApplication.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("hassanrefaat9", "Work", "blue"));
        folderRepository.save(new Folder("hassanrefaat9", "Home", "green"));
        folderRepository.save(new Folder("hassanrefaat9", "Family", "red"));


        for (int i = 0; i < 10; i++) {
            emailService.sendEmail("hassanrefaat9", Arrays.asList("hassanrefaat9","abc"),"Hello "+i ,"Body");

        }
    }
}
