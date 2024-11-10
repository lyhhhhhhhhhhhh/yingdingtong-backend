package com.lyh.yingdingtong.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.yingdingtong.annotation.AuthCheck;
import com.lyh.yingdingtong.common.BaseResponse;
import com.lyh.yingdingtong.common.DeleteRequest;
import com.lyh.yingdingtong.common.ErrorCode;
import com.lyh.yingdingtong.common.ResultUtils;
import com.lyh.yingdingtong.constant.UserConstant;
import com.lyh.yingdingtong.exception.BusinessException;
import com.lyh.yingdingtong.exception.ThrowUtils;
import com.lyh.yingdingtong.model.dto.movie.MovieAddRequest;
import com.lyh.yingdingtong.model.dto.movie.MovieEditRequest;
import com.lyh.yingdingtong.model.dto.movie.MovieQueryRequest;
import com.lyh.yingdingtong.model.dto.movie.MovieUpdateRequest;
import com.lyh.yingdingtong.model.entity.Movie;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.enums.UserRoleEnum;
import com.lyh.yingdingtong.model.vo.MovieVO;
import com.lyh.yingdingtong.service.MovieService;
import com.lyh.yingdingtong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 电影接口
 *

 */
@RestController
@RequestMapping("/movie")
@Slf4j
public class MovieController {

    @Resource
    private MovieService movieService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建电影
     *
     * @param movieAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addMovie(@RequestBody MovieAddRequest movieAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(movieAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieAddRequest, movie);
        // 数据校验
        movieService.validMovie(movie, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        movie.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = movieService.save(movie);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newMovieId = movie.getId();
        return ResultUtils.success(newMovieId);
    }

    /**
     * 删除电影
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteMovie(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Movie oldMovie = movieService.getById(id);
        ThrowUtils.throwIf(oldMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldMovie.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = movieService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新电影（仅管理员可用）
     *
     * @param movieUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateMovie(@RequestBody MovieUpdateRequest movieUpdateRequest) {
        if (movieUpdateRequest == null || movieUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieUpdateRequest, movie);
        // 数据校验
        movieService.validMovie(movie, false);
        // 判断是否存在
        long id = movieUpdateRequest.getId();
        Movie oldMovie = movieService.getById(id);
        ThrowUtils.throwIf(oldMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = movieService.updateById(movie);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取电影（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<MovieVO> getMovieVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Movie movie = movieService.getById(id);
        ThrowUtils.throwIf(movie == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(movieService.getMovieVO(movie, request));
    }

    /**
     * 分页获取电影列表（仅管理员可用）
     *
     * @param movieQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Movie>> listMovieByPage(@RequestBody MovieQueryRequest movieQueryRequest) {
        long current = movieQueryRequest.getCurrent();
        long size = movieQueryRequest.getPageSize();
        // 查询数据库
        Page<Movie> moviePage = movieService.page(new Page<>(current, size),
                movieService.getQueryWrapper(movieQueryRequest));
        return ResultUtils.success(moviePage);
    }

    /**
     * 分页获取电影列表（封装类）
     *
     * @param movieQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<MovieVO>> listMovieVOByPage(@RequestBody MovieQueryRequest movieQueryRequest,
                                                               HttpServletRequest request) {
        long current = movieQueryRequest.getCurrent();
        long size = movieQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Movie> moviePage = movieService.page(new Page<>(current, size),
                movieService.getQueryWrapper(movieQueryRequest));
        // 获取封装类
        return ResultUtils.success(movieService.getMovieVOPage(moviePage, request));
    }

    /**
     * 分页获取当前登录用户创建的电影列表
     *
     * @param movieQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<MovieVO>> listMyMovieVOByPage(@RequestBody MovieQueryRequest movieQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(movieQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        movieQueryRequest.setUserId(loginUser.getId());
        long current = movieQueryRequest.getCurrent();
        long size = movieQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Movie> moviePage = movieService.page(new Page<>(current, size),
                movieService.getQueryWrapper(movieQueryRequest));
        // 获取封装类
        return ResultUtils.success(movieService.getMovieVOPage(moviePage, request));
    }

    /**
     * 编辑电影（给用户使用）
     *
     * @param movieEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editMovie(@RequestBody MovieEditRequest movieEditRequest, HttpServletRequest request) {
        if (movieEditRequest == null || movieEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieEditRequest, movie);
        // 数据校验
        movieService.validMovie(movie, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = movieEditRequest.getId();
        Movie oldMovie = movieService.getById(id);
        ThrowUtils.throwIf(oldMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldMovie.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = movieService.updateById(movie);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
