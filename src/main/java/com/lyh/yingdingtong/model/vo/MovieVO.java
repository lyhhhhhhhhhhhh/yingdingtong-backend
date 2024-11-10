package com.lyh.yingdingtong.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyh.yingdingtong.model.entity.Movie;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 电影视图
 *

 */
@Data
public class MovieVO implements Serializable {


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
     * 创建用户 id
     */
    private Long userId;

    /**
     * 电影评分
     */
    private BigDecimal movieRating;

    /**
     * 电影地区
     */
    private String movieRegion;

    /**
     * id
     */
    private Long id;

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
     * @param movieVO
     * @return
     */
    public static Movie voToObj(MovieVO movieVO) {
        if (movieVO == null) {
            return null;
        }
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieVO, movie);
        return movie;
    }

    /**
     * 对象转封装类
     *
     * @param movie
     * @return
     */
    public static MovieVO objToVo(Movie movie) {
        if (movie == null) {
            return null;
        }
        MovieVO movieVO = new MovieVO();
        BeanUtils.copyProperties(movie, movieVO);
        return movieVO;
    }
}
