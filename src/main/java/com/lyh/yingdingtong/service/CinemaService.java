package com.lyh.yingdingtong.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.yingdingtong.model.dto.Cinema.CinemaQueryRequest;
import com.lyh.yingdingtong.model.entity.Cinema;
import com.lyh.yingdingtong.model.vo.CinemaVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 影院服务
 *

 */
public interface CinemaService extends IService<Cinema> {

    /**
     * 校验数据
     *
     * @param cinema
     * @param add 对创建的数据进行校验
     */
    void validCinema(Cinema cinema, boolean add);

    /**
     * 获取查询条件
     *
     * @param cinemaQueryRequest
     * @return
     */
    QueryWrapper<Cinema> getQueryWrapper(CinemaQueryRequest cinemaQueryRequest);
    
    /**
     * 获取影院封装
     *
     * @param cinema
     * @param request
     * @return
     */
    CinemaVO getCinemaVO(Cinema cinema, HttpServletRequest request);

    /**
     * 分页获取影院封装
     *
     * @param cinemaPage
     * @param request
     * @return
     */
    Page<CinemaVO> getCinemaVOPage(Page<Cinema> cinemaPage, HttpServletRequest request);
}
