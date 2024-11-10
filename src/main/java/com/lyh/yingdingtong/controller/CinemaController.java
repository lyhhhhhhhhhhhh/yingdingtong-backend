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
import com.lyh.yingdingtong.model.dto.Cinema.CinemaAddRequest;
import com.lyh.yingdingtong.model.dto.Cinema.CinemaEditRequest;
import com.lyh.yingdingtong.model.dto.Cinema.CinemaQueryRequest;
import com.lyh.yingdingtong.model.dto.Cinema.CinemaUpdateRequest;
import com.lyh.yingdingtong.model.entity.Cinema;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.vo.CinemaVO;
import com.lyh.yingdingtong.service.CinemaService;
import com.lyh.yingdingtong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 影院接口
 *

 */
@RestController
@RequestMapping("/cinema")
@Slf4j
public class CinemaController {

    @Resource
    private CinemaService cinemaService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建影院
     *
     * @param cinemaAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCinema(@RequestBody CinemaAddRequest cinemaAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(cinemaAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(cinemaAddRequest, cinema);
        // 数据校验
        cinemaService.validCinema(cinema, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        cinema.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = cinemaService.save(cinema);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCinemaId = cinema.getId();
        return ResultUtils.success(newCinemaId);
    }

    /**
     * 删除影院
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCinema(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Cinema oldCinema = cinemaService.getById(id);
        ThrowUtils.throwIf(oldCinema == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldCinema.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = cinemaService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新影院（仅管理员可用）
     *
     * @param cinemaUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCinema(@RequestBody CinemaUpdateRequest cinemaUpdateRequest) {
        if (cinemaUpdateRequest == null || cinemaUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(cinemaUpdateRequest, cinema);
        // 数据校验
        cinemaService.validCinema(cinema, false);
        // 判断是否存在
        long id = cinemaUpdateRequest.getId();
        Cinema oldCinema = cinemaService.getById(id);
        ThrowUtils.throwIf(oldCinema == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = cinemaService.updateById(cinema);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取影院（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CinemaVO> getCinemaVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Cinema cinema = cinemaService.getById(id);
        ThrowUtils.throwIf(cinema == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(cinemaService.getCinemaVO(cinema, request));
    }

    /**
     * 分页获取影院列表（仅管理员可用）
     *
     * @param cinemaQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Cinema>> listCinemaByPage(@RequestBody CinemaQueryRequest cinemaQueryRequest) {
        long current = cinemaQueryRequest.getCurrent();
        long size = cinemaQueryRequest.getPageSize();
        // 查询数据库
        Page<Cinema> cinemaPage = cinemaService.page(new Page<>(current, size),
                cinemaService.getQueryWrapper(cinemaQueryRequest));
        return ResultUtils.success(cinemaPage);
    }

    /**
     * 分页获取影院列表（封装类）
     *
     * @param cinemaQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CinemaVO>> listCinemaVOByPage(@RequestBody CinemaQueryRequest cinemaQueryRequest,
                                                               HttpServletRequest request) {
        long current = cinemaQueryRequest.getCurrent();
        long size = cinemaQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Cinema> cinemaPage = cinemaService.page(new Page<>(current, size),
                cinemaService.getQueryWrapper(cinemaQueryRequest));
        // 获取封装类
        return ResultUtils.success(cinemaService.getCinemaVOPage(cinemaPage, request));
    }

    /**
     * 分页获取当前登录用户创建的影院列表
     *
     * @param cinemaQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CinemaVO>> listMyCinemaVOByPage(@RequestBody CinemaQueryRequest cinemaQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(cinemaQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        cinemaQueryRequest.setUserId(loginUser.getId());
        long current = cinemaQueryRequest.getCurrent();
        long size = cinemaQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Cinema> cinemaPage = cinemaService.page(new Page<>(current, size),
                cinemaService.getQueryWrapper(cinemaQueryRequest));
        // 获取封装类
        return ResultUtils.success(cinemaService.getCinemaVOPage(cinemaPage, request));
    }

    /**
     * 编辑影院（给用户使用）
     *
     * @param cinemaEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editCinema(@RequestBody CinemaEditRequest cinemaEditRequest, HttpServletRequest request) {
        if (cinemaEditRequest == null || cinemaEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(cinemaEditRequest, cinema);
        // 数据校验
        cinemaService.validCinema(cinema, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = cinemaEditRequest.getId();
        Cinema oldCinema = cinemaService.getById(id);
        ThrowUtils.throwIf(oldCinema == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldCinema.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = cinemaService.updateById(cinema);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
