package com.lyh.yingdingtong.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.yingdingtong.common.ErrorCode;
import com.lyh.yingdingtong.constant.CommonConstant;
import com.lyh.yingdingtong.exception.ThrowUtils;
import com.lyh.yingdingtong.mapper.CinemaMapper;
import com.lyh.yingdingtong.model.dto.Cinema.CinemaQueryRequest;
import com.lyh.yingdingtong.model.entity.Cinema;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.vo.CinemaVO;
import com.lyh.yingdingtong.model.vo.UserVO;
import com.lyh.yingdingtong.service.CinemaService;
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
 * 影院服务实现
 *

 */
@Service
@Slf4j
public class CinemaServiceImpl extends ServiceImpl<CinemaMapper, Cinema> implements CinemaService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param cinema
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validCinema(Cinema cinema, boolean add) {
        ThrowUtils.throwIf(cinema == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = cinema.getId();
        String cinemaTitle = cinema.getCinemaTitle();
        String cinemaAddress = cinema.getCinemaAddress();
        String cinemaTags = cinema.getCinemaTags();
        Long userId = cinema.getUserId();
        Date editTime = cinema.getEditTime();
        Date createTime = cinema.getCreateTime();
        Date updateTime = cinema.getUpdateTime();
        Integer isDelete = cinema.getIsDelete();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(cinemaTitle), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(cinemaAddress), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(cinemaTags), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(cinemaTitle)) {
            ThrowUtils.throwIf(cinemaTitle.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param cinemaQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Cinema> getQueryWrapper(CinemaQueryRequest cinemaQueryRequest) {
        QueryWrapper<Cinema> queryWrapper = new QueryWrapper<>();
        if (cinemaQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        String cinemaTitle = cinemaQueryRequest.getCinemaTitle();
        String cinemaAddress = cinemaQueryRequest.getCinemaAddress();
        String cinemaTags = cinemaQueryRequest.getCinemaTags();
        Long userId = cinemaQueryRequest.getUserId();
        Long id = cinemaQueryRequest.getId();
        Long notId = cinemaQueryRequest.getNotId();
        String searchText = cinemaQueryRequest.getSearchText();
        int current = cinemaQueryRequest.getCurrent();
        int pageSize = cinemaQueryRequest.getPageSize();
        String sortField = cinemaQueryRequest.getSortField();
        String sortOrder = cinemaQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("cinemaTitle", searchText).or().like("cinemaAddress", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(cinemaTitle), "cinemaTitle", cinemaTitle);
        queryWrapper.like(StringUtils.isNotBlank(cinemaAddress), "cinemaAddress", cinemaAddress);
        queryWrapper.like(StringUtils.isNotBlank(cinemaTags), "cinemaTags", cinemaTags);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取影院封装
     *
     * @param cinema
     * @param request
     * @return
     */
    @Override
    public CinemaVO getCinemaVO(Cinema cinema, HttpServletRequest request) {
        // 对象转封装类
        CinemaVO cinemaVO = CinemaVO.objToVo(cinema);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = cinema.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        cinemaVO.setUser(userVO);
        // endregion

        return cinemaVO;
    }

    /**
     * 分页获取影院封装
     *
     * @param cinemaPage
     * @param request
     * @return
     */
    @Override
    public Page<CinemaVO> getCinemaVOPage(Page<Cinema> cinemaPage, HttpServletRequest request) {
        List<Cinema> cinemaList = cinemaPage.getRecords();
        Page<CinemaVO> cinemaVOPage = new Page<>(cinemaPage.getCurrent(), cinemaPage.getSize(), cinemaPage.getTotal());
        if (CollUtil.isEmpty(cinemaList)) {
            return cinemaVOPage;
        }
        // 对象列表 => 封装对象列表
        List<CinemaVO> cinemaVOList = cinemaList.stream().map(cinema -> {
            return CinemaVO.objToVo(cinema);
        }).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = cinemaList.stream().map(Cinema::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        cinemaVOList.forEach(cinemaVO -> {
            Long userId = cinemaVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            cinemaVO.setUser(userService.getUserVO(user));
        });
        // endregion

        cinemaVOPage.setRecords(cinemaVOList);
        return cinemaVOPage;
    }

}
