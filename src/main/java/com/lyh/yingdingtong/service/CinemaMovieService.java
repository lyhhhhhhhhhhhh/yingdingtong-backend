package com.lyh.yingdingtong.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.yingdingtong.model.dto.cinemaMovie.CinemaMovieQueryRequest;
import com.lyh.yingdingtong.model.entity.CinemaMovie;
import com.lyh.yingdingtong.model.vo.CinemaMovieVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 影院电影表服务
 *

 */
public interface CinemaMovieService extends IService<CinemaMovie> {

    /**
     * 校验数据
     *
     * @param cinemaMovie
     * @param add 对创建的数据进行校验
     */
    void validCinemaMovie(CinemaMovie cinemaMovie, boolean add);

    /**
     * 获取查询条件
     *
     * @param cinemaMovieQueryRequest
     * @return
     */
    QueryWrapper<CinemaMovie> getQueryWrapper(CinemaMovieQueryRequest cinemaMovieQueryRequest);
    
    /**
     * 获取影院电影表封装
     *
     * @param cinemaMovie
     * @param request
     * @return
     */
    CinemaMovieVO getCinemaMovieVO(CinemaMovie cinemaMovie, HttpServletRequest request);

    /**
     * 分页获取影院电影表封装
     *
     * @param cinemaMoviePage
     * @param request
     * @return
     */
    Page<CinemaMovieVO> getCinemaMovieVOPage(Page<CinemaMovie> cinemaMoviePage, HttpServletRequest request);
}
