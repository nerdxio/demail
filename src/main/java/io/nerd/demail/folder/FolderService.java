package io.nerd.demail.folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;

    public List<Folder> fetchDefaultFolder(String userId) {
        return Arrays.asList(
                new Folder(userId, "Inbox", "white"),
                new Folder(userId, "Send", "green"),
                new Folder(userId, "Important", "red")
        );
    }

    public Map<String, Integer> mapCountToLabel(String userId) {
        var stats = unreadEmailStatsRepository.findAllById(userId);
        return stats.
                stream().
                collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
    }
}
