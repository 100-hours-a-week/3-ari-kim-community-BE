package kr.adapterz.ari_community.domain.post.dto.request;

import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.user.User;

public record CreateOrUpdatePostRequest (
    Integer userId,
    String title,
    String content,
    String imageUrl
) {

    public Post toEntity(User user) {
        return new Post(user, title, content, imageUrl);
    }
    
}