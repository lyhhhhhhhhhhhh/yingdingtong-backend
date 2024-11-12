package com.lyh.yingdingtong.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.yingdingtong.common.ErrorCode;
import com.lyh.yingdingtong.constant.CommonConstant;
import com.lyh.yingdingtong.exception.ThrowUtils;
import com.lyh.yingdingtong.mapper.CinemamoviescheduleMapper;
import com.lyh.yingdingtong.model.dto.cinemamovieschedule.CinemamoviescheduleQueryRequest;
import com.lyh.yingdingtong.model.entity.Cinemamovieschedule;
import com.lyh.yingdingtong.model.entity.User;
import com.lyh.yingdingtong.model.vo.CinemamoviescheduleVO;
import com.lyh.yingdingtong.model.vo.UserVO;
import com.lyh.yingdingtong.service.CinemamoviescheduleService;
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
 * 影院电影表服务实现
 *

 */
@Service
@Slf4j
public class CinemamoviescheduleServiceImpl extends ServiceImpl<CinemamoviescheduleMapper, Cinemamovieschedule> implements CinemamoviescheduleService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param cinemamovieschedule
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validCinemamovieschedule(Cinemamovieschedule cinemamovieschedule, boolean add) {
        ThrowUtils.throwIf(cinemamovieschedule == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = cinemamovieschedule.getId();
        Long cinemaId = cinemamovieschedule.getCinemaId();
        Long movieId = cinemamovieschedule.getMovieId();
        Long userId = cinemamovieschedule.getUserId();
        Date showTime = cinemamovieschedule.getShowTime();
        String movieLanguage = cinemamovieschedule.getMovieLanguage();
        String cinemaHallName = cinemamovieschedule.getCinemaHallName();
        BigDecimal moviePrice = cinemamovieschedule.getMoviePrice();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(ObjectUtils.isEmpty(cinemaId), ErrorCode.PARAMS_ERROR, "影院id不能为空");
            ThrowUtils.throwIf(ObjectUtils.isEmpty(movieId), ErrorCode.PARAMS_ERROR, "电影id不能为空");
            ThrowUtils.throwIf(ObjectUtils.isEmpty(showTime), ErrorCode.PARAMS_ERROR, "放映时间不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(movieLanguage), ErrorCode.PARAMS_ERROR, "语言版本不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(cinemaHallName), ErrorCode.PARAMS_ERROR, "影厅不能为空");
            ThrowUtils.throwIf(ObjectUtils.isEmpty(moviePrice), ErrorCode.PARAMS_ERROR, "票价不能为空");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param cinemamoviescheduleQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Cinemamovieschedule> getQueryWrapper(CinemamoviescheduleQueryRequest cinemamoviescheduleQueryRequest) {
        QueryWrapper<Cinemamovieschedule> queryWrapper = new QueryWrapper<>();
        if (cinemamoviescheduleQueryRequest == null) {
            return queryWrapper;
        }

        // 从对象中取值
        Long id = cinemamoviescheduleQueryRequest.getId();
        Long notId = cinemamoviescheduleQueryRequest.getNotId();
        String searchText = cinemamoviescheduleQueryRequest.getSearchText();
        Long cinemaId = cinemamoviescheduleQueryRequest.getCinemaId();
        Long movieId = cinemamoviescheduleQueryRequest.getMovieId();
        Long userId = cinemamoviescheduleQueryRequest.getUserId();
        Date showTime = cinemamoviescheduleQueryRequest.getShowTime(); // 前端传递的查询日期
        String movieLanguage = cinemamoviescheduleQueryRequest.getMovieLanguage();
        String cinemaHallName = cinemamoviescheduleQueryRequest.getCinemaHallName();
        BigDecimal moviePrice = cinemamoviescheduleQueryRequest.getMoviePrice();
        int current = cinemamoviescheduleQueryRequest.getCurrent();
        int pageSize = cinemamoviescheduleQueryRequest.getPageSize();
        String sortField = cinemamoviescheduleQueryRequest.getSortField();
        String sortOrder = cinemamoviescheduleQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(cinemaHallName), "cinemaHallName", cinemaHallName);
        queryWrapper.like(StringUtils.isNotBlank(movieLanguage), "movieLanguage", movieLanguage);

        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(cinemaId), "cinemaId", cinemaId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(movieId), "movieId", movieId);

// 处理 showTime 条件
        if (showTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(showTime);

            // 获取今天的日期
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {

                // 如果 showTime 是今天
                Calendar currentPlus20Min = Calendar.getInstance();
                currentPlus20Min.add(Calendar.MINUTE, 20);

                // 今天结束的时间：23:59:59
                Calendar endOfToday = Calendar.getInstance();
                endOfToday.set(Calendar.HOUR_OF_DAY, 23);
                endOfToday.set(Calendar.MINUTE, 59);
                endOfToday.set(Calendar.SECOND, 59);
                endOfToday.set(Calendar.MILLISECOND, 999);

                // 查询条件：今天当前时间 + 20 分钟 到 今天的结束时间
                queryWrapper.ge("showTime", currentPlus20Min.getTime())
                        .le("showTime", endOfToday.getTime());
            } else {
                // 如果 showTime 不是今天，查询该日期的全天场次
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date startOfDay = calendar.getTime();

                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                Date endOfDay = calendar.getTime();

                // 查询 showTime 在指定日期的范围内
                queryWrapper.between("showTime", startOfDay, endOfDay);
            }
        }

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取影院电影表封装
     *
     * @param cinemamovieschedule
     * @param request
     * @return
     */
    @Override
    public CinemamoviescheduleVO getCinemamoviescheduleVO(Cinemamovieschedule cinemamovieschedule, HttpServletRequest request) {
        // 对象转封装类
        CinemamoviescheduleVO cinemamoviescheduleVO = CinemamoviescheduleVO.objToVo(cinemamovieschedule);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = cinemamovieschedule.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        cinemamoviescheduleVO.setUser(userVO);
        // endregion

        return cinemamoviescheduleVO;
    }

    /**
     * 分页获取影院电影表封装
     *
     * @param cinemamovieschedulePage
     * @param request
     * @return
     */
    @Override
    public Page<CinemamoviescheduleVO> getCinemamoviescheduleVOPage(Page<Cinemamovieschedule> cinemamovieschedulePage, HttpServletRequest request) {
        List<Cinemamovieschedule> cinemamoviescheduleList = cinemamovieschedulePage.getRecords();
        Page<CinemamoviescheduleVO> cinemamoviescheduleVOPage = new Page<>(cinemamovieschedulePage.getCurrent(), cinemamovieschedulePage.getSize(), cinemamovieschedulePage.getTotal());
        if (CollUtil.isEmpty(cinemamoviescheduleList)) {
            return cinemamoviescheduleVOPage;
        }
        // 对象列表 => 封装对象列表
        List<CinemamoviescheduleVO> cinemamoviescheduleVOList = cinemamoviescheduleList.stream().map(cinemamovieschedule -> {
            return CinemamoviescheduleVO.objToVo(cinemamovieschedule);
        }).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = cinemamoviescheduleList.stream().map(Cinemamovieschedule::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // endregion

        cinemamoviescheduleVOPage.setRecords(cinemamoviescheduleVOList);
        return cinemamoviescheduleVOPage;
    }

}
