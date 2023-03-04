package io.nerd.demail.emaillist;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;


@Table(value = "messages_by_user_folder")
@Setter
@Getter
public class EmailListItem {
    @PrimaryKey
    private EmailListItemKey key;
    @CassandraType(type = CassandraType.Name.LIST,typeArguments = CassandraType.Name.TEXT)
    private List<String> to;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String subject;

    private boolean isUnread;

    //its wont be in database
    @Transient
    private String agoTimeString;
}
