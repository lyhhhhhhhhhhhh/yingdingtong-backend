package com.lyh.yingdingtong.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.yingdingtong.common.ErrorCode;
import com.lyh.yingdingtong.constant.CommonConstant;
import com.lyh.yingdingtong.exception.ThrowUtils;
import com.lyh.yingdingtong.mapper.MovieMapper;
import com.lyh.yingdingtong.model.dto.movie.MovieQueryRequest;
import com.lyh.yingdingtong.model.entity.Movie;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.vo.MovieVO;
import com.lyh.yingdingtong.model.vo.UserVO;
import com.lyh.yingdingtong.service.MovieService;
import com.lyh.yingdingtong.service.UserService;
import com.lyh.yingdingtong.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 电影服务实现
 *

 */
@Service
@Slf4j
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param movie
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validMovie(Movie movie, boolean add) {
        ThrowUtils.throwIf(movie == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String movieTitle = movie.getMovieTitle();
        String movieType = movie.getMovieType();
        Integer movieDuration = movie.getMovieDuration();
        String moviePicture = movie.getMoviePicture();
        Long userId = movie.getUserId();
        BigDecimal movieRating = movie.getMovieRating();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(movieTitle), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(movieType), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(movieDuration == null, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(moviePicture), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(movieRating == null, ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(movieTitle)) {
            ThrowUtils.throwIf(movieTitle.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (movieRating != null) {
            ThrowUtils.throwIf(movieRating.compareTo(BigDecimal.TEN) > 0, ErrorCode.PARAMS_ERROR, "评分不能大于10分");
            ThrowUtils.throwIf(movieRating.compareTo(BigDecimal.ZERO) < 0, ErrorCode.PARAMS_ERROR, "评分不能小于0分");
        }
    }

    /**
     * 获取查询条件
     *
     * @param movieQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Movie> getQueryWrapper(MovieQueryRequest movieQueryRequest) {
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        if (movieQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        String movieTitle = movieQueryRequest.getMovieTitle();
        String movieType = movieQueryRequest.getMovieType();
        Integer movieDuration = movieQueryRequest.getMovieDuration();
        String moviePicture = movieQueryRequest.getMoviePicture();
        Long userId = movieQueryRequest.getUserId();
        Long id = movieQueryRequest.getId();
        Long notId = movieQueryRequest.getNotId();
        String searchText = movieQueryRequest.getSearchText();
        int current = movieQueryRequest.getCurrent();
        int pageSize = movieQueryRequest.getPageSize();
        String sortField = movieQueryRequest.getSortField();
        String sortOrder = movieQueryRequest.getSortOrder();
        String movieYear = movieQueryRequest.getMovieYear();
        String movieRegion = movieQueryRequest.getMovieRegion();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("movieTitle", searchText).or().like("movieType", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(movieTitle), "movieTitle", movieTitle);
        queryWrapper.like(StringUtils.isNotBlank(movieType), "movieType", movieType);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StringUtils.isNotBlank(movieRegion), "movieRegion", movieRegion);
        //年份查询
        if (movieYear != null) {
            if (movieYear != ""){
                String startDate = movieYear + "-01-01 00:00:00";
                String endDate = movieYear + "-12-31 23:59:59";
                queryWrapper.between("createTime", startDate, endDate);
            }else {
             movieYear = null;
            }
        }

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取电影封装
     *
     * @param movie
     * @param request
     * @return
     */
    @Override
    public MovieVO getMovieVO(Movie movie, HttpServletRequest request) {
        // 对象转封装类
        MovieVO movieVO = MovieVO.objToVo(movie);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = movie.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        movieVO.setUser(userVO);
        // endregion

        return movieVO;
    }

    /**
     * 分页获取电影封装
     *
     * @param moviePage
     * @param request
     * @return
     */
    @Override
    public Page<MovieVO> getMovieVOPage(Page<Movie> moviePage, HttpServletRequest request) {
        List<Movie> movieList = moviePage.getRecords();
        Page<MovieVO> movieVOPage = new Page<>(moviePage.getCurrent(), moviePage.getSize(), moviePage.getTotal());
        if (CollUtil.isEmpty(movieList)) {
            return movieVOPage;
        }
        // 对象列表 => 封装对象列表
        List<MovieVO> movieVOList = movieList.stream().map(movie -> {
            return MovieVO.objToVo(movie);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = movieList.stream().map(Movie::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // endregion

        movieVOPage.setRecords(movieVOList);
        return movieVOPage;
    }

}
