package com.lyh.yingdingtong.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.yingdingtong.model.dto.cinemamovieschedule.CinemamoviescheduleQueryRequest;
import com.lyh.yingdingtong.model.entity.Cinemamovieschedule;
import com.lyh.yingdingtong.model.vo.CinemamoviescheduleVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 影院电影表服务
 *

 */
public interface CinemamoviescheduleService extends IService<Cinemamovieschedule> {

    /**
     * 校验数据
     *
     * @param cinemamovieschedule
     * @param add 对创建的数据进行校验
     */
    void validCinemamovieschedule(Cinemamovieschedule cinemamovieschedule, boolean add);

    /**
     * 获取查询条件
     *
     * @param cinemamoviescheduleQueryRequest
     * @return
     */
    QueryWrapper<Cinemamovieschedule> getQueryWrapper(CinemamoviescheduleQueryRequest cinemamoviescheduleQueryRequest);
    
    /**
     * 获取影院电影表封装
     *
     * @param cinemamovieschedule
     * @param request
     * @return
     */
    CinemamoviescheduleVO getCinemamoviescheduleVO(Cinemamovieschedule cinemamovieschedule, HttpServletRequest request);

    /**
     * 分页获取影院电影表封装
     *
     * @param cinemamovieschedulePage
     * @param request
     * @return
     */
    Page<CinemamoviescheduleVO> getCinemamoviescheduleVOPage(Page<Cinemamovieschedule> cinemamovieschedulePage, HttpServletRequest request);
}
