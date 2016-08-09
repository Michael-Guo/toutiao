package com.michael.dao;

import com.michael.model.News;
import com.michael.model.User;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.ibatis.annotations.*;

import javax.annotation.ManagedBean;

import java.util.List;

import static com.michael.dao.NewsDAO.INSERT_FIELDS;
import static com.michael.dao.NewsDAO.TABLE_NAME;

/**
 * Created by GWC on 2016/7/6.
 */
/*
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, created_date, user_id ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    //驼峰命名规则
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title}, #{link}, #{image}, #{likeCount}, #{commentCount}, #{createdDate}, #{userId})"})
    int addNew(News news);

    //拉取咨询，offset是为了分页显示，对应数据库中的limit，这个演示了xml读取方式
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
*/
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    /*
    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id = #{id}"})
    int updateLikeCount(@Param("id") int id, @Param("like_count") int likeCount);
    */
    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
