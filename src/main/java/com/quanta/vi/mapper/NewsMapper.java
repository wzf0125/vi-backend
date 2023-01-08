package com.quanta.vi.mapper;

import com.quanta.vi.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 网站的新闻 Mapper 接口
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

}
