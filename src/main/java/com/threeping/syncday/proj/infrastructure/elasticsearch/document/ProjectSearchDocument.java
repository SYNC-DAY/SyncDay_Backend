package com.threeping.syncday.proj.infrastructure.elasticsearch.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "project_search")
@Getter
@Builder
public class ProjectSearchDocument {

    @Id
    private Long projectId;
    @Field(type = FieldType.Text, analyzer = "nori_mixed")
    private String projectName;
    @Field(type = FieldType.Text, analyzer = "nori_mixed")
    private String vcsType;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;
}
