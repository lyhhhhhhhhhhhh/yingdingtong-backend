package com.lyh.yingdingtong.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyh.yingdingtong.model.entity.Cinema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 影院视图
 *

 */
@Data
public class CinemaVO implements Serializable {

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
     * id
     */
    private Long id;

    /**
     * 起始金额
     */
    private String startingPrice;


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
     * @param cinemaVO
     * @return
     */
    public static Cinema voToObj(CinemaVO cinemaVO) {
        if (cinemaVO == null) {
            return null;
        }
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(cinemaVO, cinema);
        return cinema;
    }

    /**
     * 对象转封装类
     *
     * @param cinema
     * @return
     */
    public static CinemaVO objToVo(Cinema cinema) {
        if (cinema == null) {
            return null;
        }
        CinemaVO cinemaVO = new CinemaVO();
        BeanUtils.copyProperties(cinema, cinemaVO);
        return cinemaVO;
    }
}
