package kr.adapterz.ari_community.domain.post.dto.request;

import lombok.Getter;

@Getter
public class CreateOrUpdatePostRequest {

    private Integer user_id;

    private String title;

    private String content;

}
