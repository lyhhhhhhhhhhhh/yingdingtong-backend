package com.lyh.yingdingtong.model.dto.Cinema;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyh.yingdingtong.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询影院请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CinemaQueryRequest extends PageRequest implements Serializable {


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


    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}