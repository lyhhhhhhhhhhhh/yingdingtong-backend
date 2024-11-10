package com.lyh.yingdingtong.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 影院
 * @TableName cinema
 */
@TableName(value ="cinema")
@Data
public class Cinema implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 影院名称
     */
    private String cinemaTitle;

    /**
     * 影院地址
     */
    private String cinemaAddress;

    /**
     * 影院标签
     */
    private String cinemaTags;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 起始金额
     */
    private String startingPrice;

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