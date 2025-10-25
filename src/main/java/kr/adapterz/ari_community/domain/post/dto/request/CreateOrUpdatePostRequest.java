package kr.adapterz.ari_community.domain.post.dto.request;

import lombok.Getter;

@Getter
public class CreateOrUpdatePostRequest {

    private String title;

    private String content;

    private String imageUrl;

}
