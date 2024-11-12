package com.lyh.yingdingtong.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.yingdingtong.common.ErrorCode;
import com.lyh.yingdingtong.constant.CommonConstant;
import com.lyh.yingdingtong.exception.ThrowUtils;
import com.lyh.yingdingtong.mapper.CinemaMovieMapper;
import com.lyh.yingdingtong.model.dto.cinemaMovie.CinemaMovieQueryRequest;
import com.lyh.yingdingtong.model.entity.CinemaMovie;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.entity.Cinema;
import com.lyh.yingdingtong.model.entity.Movie;
import com.lyh.yingdingtong.model.vo.CinemaMovieVO;
import com.lyh.yingdingtong.model.vo.CinemaVO;
import com.lyh.yingdingtong.model.vo.MovieVO;
import com.lyh.yingdingtong.model.vo.UserVO;
import com.lyh.yingdingtong.service.CinemaMovieService;
import com.lyh.yingdingtong.service.CinemaService;
import com.lyh.yingdingtong.service.MovieService;
import com.lyh.yingdingtong.service.UserService;
import com.lyh.yingdingtong.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 影院电影表服务实现
 *

 */
@Service
@Slf4j
public class CinemaMovieServiceImpl extends ServiceImpl<CinemaMovieMapper, CinemaMovie> implements CinemaMovieService {

    @Resource
    private UserService userService;

    @Resource
    private CinemaService cinemaService;

    @Resource
    private MovieService movieService;

    /**
     * 校验数据
     *
     * @param cinemaMovie
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validCinemaMovie(CinemaMovie cinemaMovie, boolean add) {
        ThrowUtils.throwIf(cinemaMovie == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        Long id = cinemaMovie.getId();
        Long cinemaId = cinemaMovie.getCinemaId();
        Long movieId = cinemaMovie.getMovieId();
        Long userId = cinemaMovie.getUserId();
        Date createTime = cinemaMovie.getCreateTime();
        Date updateTime = cinemaMovie.getUpdateTime();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(cinemaId == null, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(movieId == null, ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        //  补充校验规则
//        if (StringUtils.isNotBlank()) {
//            //这里添加校验规则
//            //
//        }
    }

    /**
     * 获取查询条件
     *
     * @param cinemaMovieQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<CinemaMovie> getQueryWrapper(CinemaMovieQueryRequest cinemaMovieQueryRequest) {
        QueryWrapper<CinemaMovie> queryWrapper = new QueryWrapper<>();
        if (cinemaMovieQueryRequest == null) {
            return queryWrapper;
        }
        //  从对象中取值
        Long id = cinemaMovieQueryRequest.getId();
        Long notId = cinemaMovieQueryRequest.getNotId();
        String searchText = cinemaMovieQueryRequest.getSearchText();
        String sortField = cinemaMovieQueryRequest.getSortField();
        String sortOrder = cinemaMovieQueryRequest.getSortOrder();
        Long userId = cinemaMovieQueryRequest.getUserId();
        Long cinemaId = cinemaMovieQueryRequest.getCinemaId();
        Long movieId = cinemaMovieQueryRequest.getMovieId();
        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            //queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        // JSON 数组查询
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(cinemaId), "cinemaId", cinemaId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(movieId), "movieId", movieId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取影院电影表封装
     *
     * @param cinemaMovie
     * @param request
     * @return
     */
    @Override
    public CinemaMovieVO getCinemaMovieVO(CinemaMovie cinemaMovie, HttpServletRequest request) {
        // 对象转封装类
        CinemaMovieVO cinemaMovieVO = CinemaMovieVO.objToVo(cinemaMovie);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = cinemaMovie.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        cinemaMovieVO.setUser(userVO);
        // endregion

        return cinemaMovieVO;
    }

    /**
     * 获取影院电影表分页封装
     *
     * @param cinemaMoviePage
     * @param request
     * @return
     */
    @Override
    public Page<CinemaMovieVO> getCinemaMovieVOPage(Page<CinemaMovie> cinemaMoviePage, HttpServletRequest request) {
        // 获取查询结果
        // CinemaMovie对应的结果里面是没有 影院信息 以及 对应电影的详细信息的
        List<CinemaMovie> cinemaMovieList = cinemaMoviePage.getRecords();
        // 创建分页对象
        Page<CinemaMovieVO> cinemaMovieVOPage = new Page<>(cinemaMoviePage.getCurrent(), cinemaMoviePage.getSize(), cinemaMoviePage.getTotal());

        if (CollUtil.isEmpty(cinemaMovieList)) {
            return cinemaMovieVOPage;
        }

        // 按 cinemaId 对 cinemaMovieList 进行分组
        Map<Long, List<CinemaMovie>> cinemaGroupedByCinemaId = cinemaMovieList.stream()
                .collect(Collectors.groupingBy(CinemaMovie::getCinemaId));

        List<CinemaMovieVO> cinemaMovieVOList = new ArrayList<>();

        // 获取所有 userId、cinemaId 和 movieId 集合，用于后续批量查询
        Set<Long> userIdSet = cinemaMovieList.stream().map(CinemaMovie::getUserId).collect(Collectors.toSet());
        Set<Long> cinemaIdSet = cinemaGroupedByCinemaId.keySet();
        Set<Long> movieIdSet = cinemaMovieList.stream().map(CinemaMovie::getMovieId).collect(Collectors.toSet());

        // 批量查询用户、影院和电影信息
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        Map<Long, CinemaVO> cinemaIdCinemaMap = cinemaService.listByIds(cinemaIdSet).stream()
                .collect(Collectors.toMap(Cinema::getId, CinemaVO::objToVo));
        Map<Long, MovieVO> movieIdMovieMap = movieService.listByIds(movieIdSet).stream()
                .collect(Collectors.toMap(Movie::getId, MovieVO::objToVo));

        // 遍历分组数据，生成 CinemaMovieVO 列表
        cinemaGroupedByCinemaId.forEach((cinemaId, groupedCinemaMovies) -> {
            // 生成 CinemaMovieVO 对象
            CinemaMovieVO cinemaMovieVO = new CinemaMovieVO();
            CinemaMovie firstCinemaMovie = groupedCinemaMovies.get(0);

            // 设置基本属性
            cinemaMovieVO.setId(firstCinemaMovie.getId());
            cinemaMovieVO.setCinemaId(cinemaId);
            cinemaMovieVO.setUserId(firstCinemaMovie.getUserId());
            cinemaMovieVO.setCreateTime(firstCinemaMovie.getCreateTime());
            cinemaMovieVO.setUpdateTime(firstCinemaMovie.getUpdateTime());

            // 设置影院信息
            if (cinemaIdCinemaMap.containsKey(cinemaId)) {
                cinemaMovieVO.setCinema(cinemaIdCinemaMap.get(cinemaId));
            }

            // 设置用户信息
            Long userId = firstCinemaMovie.getUserId();
            if (userIdUserListMap.containsKey(userId)) {
                User user = userIdUserListMap.get(userId).get(0);
                cinemaMovieVO.setUser(userService.getUserVO(user));
            }

            // 设置电影信息列表
            List<MovieVO> movieVOList = groupedCinemaMovies.stream()
                    .map(cinemaMovie -> movieIdMovieMap.get(cinemaMovie.getMovieId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            cinemaMovieVO.setMovieVOList(movieVOList);

            cinemaMovieVOList.add(cinemaMovieVO);
        });

        cinemaMovieVOPage.setRecords(cinemaMovieVOList);
        return cinemaMovieVOPage;
    }



}
