package com.lyh.yingdingtong.model.dto.cinemamovieschedule;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 编辑影院电影表请求
 *

 */
@Data
public class CinemamoviescheduleEditRequest implements Serializable {

    /**
     * id
     */
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

    private static final long serialVersionUID = 1L;
}