package com.lyh.yingdingtong.model.dto.movie;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 创建电影请求
 *

 */
@Data
public class MovieAddRequest implements Serializable {

    /**
     * 电影名称
     */
    private String movieTitle;

    /**
     * 电影类型
     */
    private String movieType;

    /**
     * 电影时长
     */
    private Integer movieDuration;

    /**
     * 电影图片
     */
    private String moviePicture;

    /**
     * 电影评分
     */
    private BigDecimal movieRating;

    /**
     * 电影地区
     */
    private String movieRegion;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}