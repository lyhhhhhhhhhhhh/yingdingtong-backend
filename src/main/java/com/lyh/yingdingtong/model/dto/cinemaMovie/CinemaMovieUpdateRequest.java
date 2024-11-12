package com.lyh.yingdingtong.model.dto.cinemaMovie;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新影院电影表请求
 *

 */
@Data
public class CinemaMovieUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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