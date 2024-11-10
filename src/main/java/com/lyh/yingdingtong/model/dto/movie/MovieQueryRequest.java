package com.lyh.yingdingtong.model.dto.movie;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyh.yingdingtong.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 查询电影请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MovieQueryRequest extends PageRequest implements Serializable {


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
     * 创建用户 id
     */
    private Long userId;

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 电影评分
     */
    private BigDecimal movieRating;

    /**
     *
     * 电影年份
     */
    private String movieYear;

    /**
     * 电影地区
     */
    private String movieRegion;

    /**
     * 搜索词
     */
    private String searchText;



    private static final long serialVersionUID = 1L;
}