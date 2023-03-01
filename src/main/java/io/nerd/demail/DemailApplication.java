package io.nerd.demail;

import io.nerd.demail.config.DataStaxAstraProperties;
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

@SpringBootApplication
@RestController
public class DemailApplication {

    @Autowired
    FolderRepository folderRepository;
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

    }
}
