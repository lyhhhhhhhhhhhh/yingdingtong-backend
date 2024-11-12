package com.lyh.yingdingtong.model.dto.cinemamovieschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyh.yingdingtong.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询影院电影表请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CinemamoviescheduleQueryRequest extends PageRequest implements Serializable {

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
    @JsonFormat(pattern = "yyyy-MM-dd") // 指定解析格式
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


    private static final long serialVersionUID = 1L;
}