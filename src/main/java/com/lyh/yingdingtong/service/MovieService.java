package com.lyh.yingdingtong.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.yingdingtong.model.dto.movie.MovieQueryRequest;
import com.lyh.yingdingtong.model.entity.Movie;
import com.lyh.yingdingtong.model.vo.MovieVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 电影服务
 *

 */
public interface MovieService extends IService<Movie> {

    /**
     * 校验数据
     *
     * @param movie
     * @param add 对创建的数据进行校验
     */
    void validMovie(Movie movie, boolean add);

    /**
     * 获取查询条件
     *
     * @param movieQueryRequest
     * @return
     */
    QueryWrapper<Movie> getQueryWrapper(MovieQueryRequest movieQueryRequest);
    
    /**
     * 获取电影封装
     *
     * @param movie
     * @param request
     * @return
     */
    MovieVO getMovieVO(Movie movie, HttpServletRequest request);

    /**
     * 分页获取电影封装
     *
     * @param moviePage
     * @param request
     * @return
     */
    Page<MovieVO> getMovieVOPage(Page<Movie> moviePage, HttpServletRequest request);
}
