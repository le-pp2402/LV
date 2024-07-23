package com.phatpl.learnvocabulary.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadResourceRequest {
    @NotNull(message = "title must not be null")
    String title;
    @NotNull(message = "resources must not be null")
    MultipartFile source;
    MultipartFile en_sub;
    @NotNull(message = "isPrivate must not be null")
    Boolean isPrivate;
}
