package com.lyh.yingdingtong.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电影
 * @TableName movie
 */
@TableName(value ="movie")
@Data
public class Movie implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}