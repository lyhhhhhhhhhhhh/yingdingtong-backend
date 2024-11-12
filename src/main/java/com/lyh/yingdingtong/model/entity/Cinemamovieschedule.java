package com.lyh.yingdingtong.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 影院电影排期表
 * @TableName CinemaMovieSchedule
 */
@TableName(value ="CinemaMovieSchedule")
@Data
public class Cinemamovieschedule implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 影院 id
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

    /**
     * 放映时间
     */
    private Date showTime;

    /**
     * 语言(如中文、英文)
     */
    private String movieLanguage;

    /**
     * 放映厅名称
     */
    private String cinemaHallName;

    /**
     * 电影价格
     */
    private BigDecimal moviePrice;

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
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}