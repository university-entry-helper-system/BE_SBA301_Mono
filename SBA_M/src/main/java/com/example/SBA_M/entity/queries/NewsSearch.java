package com.example.SBA_M.entity.queries;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "news_search")
@EqualsAndHashCode(callSuper = true)
public class NewsSearch extends AbstractElasticsearchDocument<Long> {

    @Field(type = FieldType.Nested)
    private UniSearch university;

    @Field(type = FieldType.Text)
    private String summary;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String imageUrl;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Integer)
    private Integer viewCount;

    @Field(type = FieldType.Keyword)
    private String newsStatus;

    @Field(type = FieldType.Date)
    private Instant publishedAt;

    @Field(type = FieldType.Date)
    private Instant releaseDate;
}
