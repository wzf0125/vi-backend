package com.quanta.vi.service;

import com.quanta.vi.entity.Statistics;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quanta.vi.vo.StatisticsVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
public interface StatisticsService extends IService<Statistics> {
    // 获取词书学习进度
    public Statistics getStatistics(Long uid);

    // 获取用户当前待学和待复习单词数
    public StatisticsVO getUnLearnAndReviewCount(Long uid);
}
