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
import com.lyh.yingdingtong.model.dto.cinemaMovie.CinemaMovieAddRequest;
import com.lyh.yingdingtong.model.dto.cinemaMovie.CinemaMovieEditRequest;
import com.lyh.yingdingtong.model.dto.cinemaMovie.CinemaMovieQueryRequest;
import com.lyh.yingdingtong.model.dto.cinemaMovie.CinemaMovieUpdateRequest;
import com.lyh.yingdingtong.model.entity.CinemaMovie;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.vo.CinemaMovieVO;
import com.lyh.yingdingtong.service.CinemaMovieService;
import com.lyh.yingdingtong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 影院电影表接口
 *

 */
@RestController
@RequestMapping("/cinemaMovie")
@Slf4j
public class CinemaMovieController {

    @Resource
    private CinemaMovieService cinemaMovieService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建影院电影表
     *
     * @param cinemaMovieAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCinemaMovie(@RequestBody CinemaMovieAddRequest cinemaMovieAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(cinemaMovieAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        CinemaMovie cinemaMovie = new CinemaMovie();
        BeanUtils.copyProperties(cinemaMovieAddRequest, cinemaMovie);
        // 数据校验
        cinemaMovieService.validCinemaMovie(cinemaMovie, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        cinemaMovie.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = cinemaMovieService.save(cinemaMovie);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCinemaMovieId = cinemaMovie.getId();
        return ResultUtils.success(newCinemaMovieId);
    }

    /**
     * 删除影院电影表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCinemaMovie(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        CinemaMovie oldCinemaMovie = cinemaMovieService.getById(id);
        ThrowUtils.throwIf(oldCinemaMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldCinemaMovie.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = cinemaMovieService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新影院电影表（仅管理员可用）
     *
     * @param cinemaMovieUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCinemaMovie(@RequestBody CinemaMovieUpdateRequest cinemaMovieUpdateRequest) {
        if (cinemaMovieUpdateRequest == null || cinemaMovieUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        CinemaMovie cinemaMovie = new CinemaMovie();
        BeanUtils.copyProperties(cinemaMovieUpdateRequest, cinemaMovie);
        // 数据校验
        cinemaMovieService.validCinemaMovie(cinemaMovie, false);
        // 判断是否存在
        long id = cinemaMovieUpdateRequest.getId();
        CinemaMovie oldCinemaMovie = cinemaMovieService.getById(id);
        ThrowUtils.throwIf(oldCinemaMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = cinemaMovieService.updateById(cinemaMovie);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取影院电影表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CinemaMovieVO> getCinemaMovieVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        CinemaMovie cinemaMovie = cinemaMovieService.getById(id);
        ThrowUtils.throwIf(cinemaMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(cinemaMovieService.getCinemaMovieVO(cinemaMovie, request));
    }

    /**
     * 分页获取影院电影表列表（仅管理员可用）
     *
     * @param cinemaMovieQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<CinemaMovie>> listCinemaMovieByPage(@RequestBody CinemaMovieQueryRequest cinemaMovieQueryRequest) {
        long current = cinemaMovieQueryRequest.getCurrent();
        long size = cinemaMovieQueryRequest.getPageSize();
        // 查询数据库
        Page<CinemaMovie> cinemaMoviePage = cinemaMovieService.page(new Page<>(current, size),
                cinemaMovieService.getQueryWrapper(cinemaMovieQueryRequest));
        return ResultUtils.success(cinemaMoviePage);
    }

    /**
     * 分页获取影院电影表列表（封装类）
     *
     * @param cinemaMovieQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CinemaMovieVO>> listCinemaMovieVOByPage(@RequestBody CinemaMovieQueryRequest cinemaMovieQueryRequest,
                                                               HttpServletRequest request) {
        /**
         *
        首先 获取分页参数
        这里的分页数据可添加可不添加
        因为我们的字段默认是根据创造时间来排序的 一般查询都会传输影院ID
        并且一般同一时间影院上架电影不超过十部
        所以我们这里还是添加上分页
         */
        long current = cinemaMovieQueryRequest.getCurrent();
        long size = cinemaMovieQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        //获取到数据
        //根据查询条件 查询数据
        Page<CinemaMovie> cinemaMoviePage = cinemaMovieService.page(new Page<>(current, size),
                cinemaMovieService.getQueryWrapper(cinemaMovieQueryRequest));
        // 获取封装类
        return ResultUtils.success(cinemaMovieService.getCinemaMovieVOPage(cinemaMoviePage, request));
    }

    /**
     * 分页获取当前登录用户创建的影院电影表列表
     *
     * @param cinemaMovieQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CinemaMovieVO>> listMyCinemaMovieVOByPage(@RequestBody CinemaMovieQueryRequest cinemaMovieQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(cinemaMovieQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        cinemaMovieQueryRequest.setUserId(loginUser.getId());
        long current = cinemaMovieQueryRequest.getCurrent();
        long size = cinemaMovieQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CinemaMovie> cinemaMoviePage = cinemaMovieService.page(new Page<>(current, size),
                cinemaMovieService.getQueryWrapper(cinemaMovieQueryRequest));
        // 获取封装类
        return ResultUtils.success(cinemaMovieService.getCinemaMovieVOPage(cinemaMoviePage, request));
    }

    /**
     * 编辑影院电影表（给用户使用）
     *
     * @param cinemaMovieEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editCinemaMovie(@RequestBody CinemaMovieEditRequest cinemaMovieEditRequest, HttpServletRequest request) {
        if (cinemaMovieEditRequest == null || cinemaMovieEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        CinemaMovie cinemaMovie = new CinemaMovie();
        BeanUtils.copyProperties(cinemaMovieEditRequest, cinemaMovie);
        // 数据校验
        cinemaMovieService.validCinemaMovie(cinemaMovie, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = cinemaMovieEditRequest.getId();
        CinemaMovie oldCinemaMovie = cinemaMovieService.getById(id);
        ThrowUtils.throwIf(oldCinemaMovie == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldCinemaMovie.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = cinemaMovieService.updateById(cinemaMovie);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
