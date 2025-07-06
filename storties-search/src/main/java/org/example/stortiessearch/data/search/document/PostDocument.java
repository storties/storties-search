package org.example.stortiessearch.data.search.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.List;

@Document(indexName = "storties_post_index")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Keyword)
    private Long userId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(
        type = FieldType.Date_Nanos,
        format = {},
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS||epoch_millis"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    @Field(
        type = FieldType.Date_Nanos,
        format = {},
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS||epoch_millis"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Boolean)
    private Boolean isPublished;

    @Field(type = FieldType.Dense_Vector, dims = 300)
    private float[] vector;
}
