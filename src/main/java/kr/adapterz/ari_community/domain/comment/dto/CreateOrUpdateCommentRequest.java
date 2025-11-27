package kr.adapterz.ari_community.domain.comment.dto;

import kr.adapterz.ari_community.domain.comment.Comment;
import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.user.User;

public record CreateOrUpdateCommentRequest(

    Integer userId,

    String content

) {

    public Comment toEntity(Post post, User user) {
        return new Comment(post, user, content);
    }
    
}