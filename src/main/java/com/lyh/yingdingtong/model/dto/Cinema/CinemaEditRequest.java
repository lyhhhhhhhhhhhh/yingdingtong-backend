package com.lyh.yingdingtong.model.dto.Cinema;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑影院请求
 *

 */
@Data
public class CinemaEditRequest implements Serializable {

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
     * 起始金额
     */
    private String startingPrice;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * id
     */
    private Long id;


    private static final long serialVersionUID = 1L;
}