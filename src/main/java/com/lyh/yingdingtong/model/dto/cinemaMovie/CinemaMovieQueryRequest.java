package com.lyh.yingdingtong.model.dto.cinemaMovie;

import com.lyh.yingdingtong.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询影院电影表请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CinemaMovieQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 影院id
     */
    private Long cinemaId;

    /**
     * 电影 id
     */
    private Long movieId;

    /**
     * 创建用户 id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}