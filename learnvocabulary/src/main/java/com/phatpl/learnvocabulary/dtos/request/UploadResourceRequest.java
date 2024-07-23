package com.phatpl.learnvocabulary.dtos.request;

import com.phatpl.learnvocabulary.utils.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResourceRequest {
    @NotNull(message = "Title " + Constant.NOT_NULL)
    public String title;

    @NotNull(message = "Video " + Constant.NOT_NULL)
    public MultipartFile video;

    @NotNull(message = "EngSub " + Constant.NOT_NULL)
    public MultipartFile enSub;

    @NotNull(message = "VieSub " + Constant.NOT_NULL)
    public MultipartFile viSub;

    public MultipartFile thumbnail;

    @NotNull(message = "isPrivate " + Constant.NOT_NULL)
    public Boolean isPrivate;
}
