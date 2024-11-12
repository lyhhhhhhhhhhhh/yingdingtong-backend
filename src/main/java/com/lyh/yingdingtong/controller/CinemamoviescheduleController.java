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
import com.lyh.yingdingtong.model.dto.cinemamovieschedule.CinemamoviescheduleAddRequest;
import com.lyh.yingdingtong.model.dto.cinemamovieschedule.CinemamoviescheduleEditRequest;
import com.lyh.yingdingtong.model.dto.cinemamovieschedule.CinemamoviescheduleQueryRequest;
import com.lyh.yingdingtong.model.dto.cinemamovieschedule.CinemamoviescheduleUpdateRequest;
import com.lyh.yingdingtong.model.entity.Cinemamovieschedule;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.vo.CinemamoviescheduleVO;
import com.lyh.yingdingtong.service.CinemamoviescheduleService;
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
@RequestMapping("/cinemamovieschedule")
@Slf4j
public class CinemamoviescheduleController {

    @Resource
    private CinemamoviescheduleService cinemamoviescheduleService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建影院电影表
     *
     * @param cinemamoviescheduleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCinemamovieschedule(@RequestBody CinemamoviescheduleAddRequest cinemamoviescheduleAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(cinemamoviescheduleAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Cinemamovieschedule cinemamovieschedule = new Cinemamovieschedule();
        BeanUtils.copyProperties(cinemamoviescheduleAddRequest, cinemamovieschedule);
        // 数据校验
        cinemamoviescheduleService.validCinemamovieschedule(cinemamovieschedule, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        cinemamovieschedule.setUserId(loginUser.getId());
        //这里其实没有做判断 就是判断cinemaId movieId是否存在
        //TODO 这里其实最好
        // 写入数据库
        boolean result = cinemamoviescheduleService.save(cinemamovieschedule);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCinemamoviescheduleId = cinemamovieschedule.getId();
        return ResultUtils.success(newCinemamoviescheduleId);
    }

    /**
     * 删除影院电影表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCinemamovieschedule(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Cinemamovieschedule oldCinemamovieschedule = cinemamoviescheduleService.getById(id);
        ThrowUtils.throwIf(oldCinemamovieschedule == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldCinemamovieschedule.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = cinemamoviescheduleService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新影院电影表（仅管理员可用）
     *
     * @param cinemamoviescheduleUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCinemamovieschedule(@RequestBody CinemamoviescheduleUpdateRequest cinemamoviescheduleUpdateRequest) {
        if (cinemamoviescheduleUpdateRequest == null || cinemamoviescheduleUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Cinemamovieschedule cinemamovieschedule = new Cinemamovieschedule();
        BeanUtils.copyProperties(cinemamoviescheduleUpdateRequest, cinemamovieschedule);
        // 数据校验
        cinemamoviescheduleService.validCinemamovieschedule(cinemamovieschedule, false);
        // 判断是否存在
        long id = cinemamoviescheduleUpdateRequest.getId();
        Cinemamovieschedule oldCinemamovieschedule = cinemamoviescheduleService.getById(id);
        ThrowUtils.throwIf(oldCinemamovieschedule == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = cinemamoviescheduleService.updateById(cinemamovieschedule);
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
    public BaseResponse<CinemamoviescheduleVO> getCinemamoviescheduleVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Cinemamovieschedule cinemamovieschedule = cinemamoviescheduleService.getById(id);
        ThrowUtils.throwIf(cinemamovieschedule == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(cinemamoviescheduleService.getCinemamoviescheduleVO(cinemamovieschedule, request));
    }

    /**
     * 分页获取影院电影表列表（仅管理员可用）
     *
     * @param cinemamoviescheduleQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Cinemamovieschedule>> listCinemamoviescheduleByPage(@RequestBody CinemamoviescheduleQueryRequest cinemamoviescheduleQueryRequest) {
        long current = cinemamoviescheduleQueryRequest.getCurrent();
        long size = cinemamoviescheduleQueryRequest.getPageSize();
        // 查询数据库
        Page<Cinemamovieschedule> cinemamovieschedulePage = cinemamoviescheduleService.page(new Page<>(current, size),
                cinemamoviescheduleService.getQueryWrapper(cinemamoviescheduleQueryRequest));
        return ResultUtils.success(cinemamovieschedulePage);
    }

    /**
     * 分页获取影院电影表列表（封装类）
     *
     * @param cinemamoviescheduleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CinemamoviescheduleVO>> listCinemamoviescheduleVOByPage(@RequestBody CinemamoviescheduleQueryRequest cinemamoviescheduleQueryRequest,
                                                               HttpServletRequest request) {
        long current = cinemamoviescheduleQueryRequest.getCurrent();
        long size = cinemamoviescheduleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Cinemamovieschedule> cinemamovieschedulePage = cinemamoviescheduleService.page(new Page<>(current, size),
                cinemamoviescheduleService.getQueryWrapper(cinemamoviescheduleQueryRequest));
        // 获取封装类
        return ResultUtils.success(cinemamoviescheduleService.getCinemamoviescheduleVOPage(cinemamovieschedulePage, request));
    }

    /**
     * 分页获取当前登录用户创建的影院电影表列表
     *
     * @param cinemamoviescheduleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CinemamoviescheduleVO>> listMyCinemamoviescheduleVOByPage(@RequestBody CinemamoviescheduleQueryRequest cinemamoviescheduleQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(cinemamoviescheduleQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        cinemamoviescheduleQueryRequest.setUserId(loginUser.getId());
        long current = cinemamoviescheduleQueryRequest.getCurrent();
        long size = cinemamoviescheduleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Cinemamovieschedule> cinemamovieschedulePage = cinemamoviescheduleService.page(new Page<>(current, size),
                cinemamoviescheduleService.getQueryWrapper(cinemamoviescheduleQueryRequest));
        // 获取封装类
        return ResultUtils.success(cinemamoviescheduleService.getCinemamoviescheduleVOPage(cinemamovieschedulePage, request));
    }

    /**
     * 编辑影院电影表（给用户使用）
     *
     * @param cinemamoviescheduleEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editCinemamovieschedule(@RequestBody CinemamoviescheduleEditRequest cinemamoviescheduleEditRequest, HttpServletRequest request) {
        if (cinemamoviescheduleEditRequest == null || cinemamoviescheduleEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Cinemamovieschedule cinemamovieschedule = new Cinemamovieschedule();
        BeanUtils.copyProperties(cinemamoviescheduleEditRequest, cinemamovieschedule);
        // 数据校验
        cinemamoviescheduleService.validCinemamovieschedule(cinemamovieschedule, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = cinemamoviescheduleEditRequest.getId();
        Cinemamovieschedule oldCinemamovieschedule = cinemamoviescheduleService.getById(id);
        ThrowUtils.throwIf(oldCinemamovieschedule == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldCinemamovieschedule.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = cinemamoviescheduleService.updateById(cinemamovieschedule);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
