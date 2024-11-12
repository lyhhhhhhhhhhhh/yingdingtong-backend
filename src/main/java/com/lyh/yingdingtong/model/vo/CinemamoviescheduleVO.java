package com.lyh.yingdingtong.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyh.yingdingtong.model.entity.Cinemamovieschedule;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 影院电影表视图
 *

 */
@Data
public class CinemamoviescheduleVO implements Serializable {

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
     * 这里是个大坑
     *      数据库设置时间为东八区
     *      查看数据库查询日志 发现查询的时间与电脑时间一致
     *      但是返回给前端的JSON串会 -8 小时 也就是数据库默认时区
     *      所以需要设置时区 来达到目的
     *      天坑！！！！！！！！！！！！
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param cinemamoviescheduleVO
     * @return
     */
    public static Cinemamovieschedule voToObj(CinemamoviescheduleVO cinemamoviescheduleVO) {
        if (cinemamoviescheduleVO == null) {
            return null;
        }
        Cinemamovieschedule cinemamovieschedule = new Cinemamovieschedule();
        BeanUtils.copyProperties(cinemamoviescheduleVO, cinemamovieschedule);
        return cinemamovieschedule;
    }

    /**
     * 对象转封装类
     *
     * @param cinemamovieschedule
     * @return
     */
    public static CinemamoviescheduleVO objToVo(Cinemamovieschedule cinemamovieschedule) {
        if (cinemamovieschedule == null) {
            return null;
        }
        CinemamoviescheduleVO cinemamoviescheduleVO = new CinemamoviescheduleVO();
        BeanUtils.copyProperties(cinemamovieschedule, cinemamoviescheduleVO);
        return cinemamoviescheduleVO;
    }
}
