package com.lyh.yingdingtong.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyh.yingdingtong.model.entity.CinemaMovie;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 影院电影表视图
 *

 */
@Data
public class CinemaMovieVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 创建用户 id
     */
    private Long userId;

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
     * 影院id
     */
    private Long cinemaId;

    /**
     * 电影 id
     */
    private Long movieId;

    /**
     * 影院信息
     */
    private CinemaVO cinema;

    /**
     * 电影信息
     */
    private List<MovieVO> movieVOList;


    /**
     * 封装类转对象
     *
     * @param cinemaMovieVO
     * @return
     */
    public static CinemaMovie voToObj(CinemaMovieVO cinemaMovieVO) {
        if (cinemaMovieVO == null) {
            return null;
        }
        CinemaMovie cinemaMovie = new CinemaMovie();
        BeanUtils.copyProperties(cinemaMovieVO, cinemaMovie);
        return cinemaMovie;
    }

    /**
     * 对象转封装类
     *
     * @param cinemaMovie
     * @return
     */
    public static CinemaMovieVO objToVo(CinemaMovie cinemaMovie) {
        if (cinemaMovie == null) {
            return null;
        }
        CinemaMovieVO cinemaMovieVO = new CinemaMovieVO();
        BeanUtils.copyProperties(cinemaMovie, cinemaMovieVO);
        return cinemaMovieVO;
    }
}
