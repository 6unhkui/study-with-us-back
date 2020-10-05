package switus.user.back.studywithus.repository.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.dto.PostDto;

import static switus.user.back.studywithus.domain.account.QAccount.account;
import static switus.user.back.studywithus.domain.file.QFileGroup.fileGroup;
import static switus.user.back.studywithus.domain.file.QFileInfo.fileInfo;
import static switus.user.back.studywithus.domain.post.QPost.post;
import static switus.user.back.studywithus.domain.post.QPostComment.postComment;

import static switus.user.back.studywithus.domain.member.QMember.member;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;




    @Override
    public Page<Post> findAll(Long roomId, PostDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Post> result = queryFactory.selectFrom(post)
                                                .innerJoin(post.member, member).on(member.room.id.eq(roomId))
//                                                .leftJoin(post.comments, postComment).fetchJoin()
//                                                .leftJoin(post.fileGroup, fileGroup)
//                                                .leftJoin(fileGroup.files, fileInfo)
                                                .where(
                                                        likeKeyword(searchRequest.getKeyword())
                                                )
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .orderBy(post.id.desc())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }



    private BooleanExpression likeKeyword(String keyword) {
        if(StringUtils.isEmpty(keyword))
            return null;
        return post.title.like( '%' + keyword + '%');
    }

}
