package io.nerd.demail.emaillist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@PrimaryKeyClass
public class EmailListItemKey {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;
    @PrimaryKeyColumn(name = "label", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String label;
    @PrimaryKeyColumn(name = "created_time_uuid)", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private UUID timeUUID;

}
