package com.quanta.vi.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.entity.NewsCollection;
import com.quanta.vi.service.NewsCollectionService;
import com.quanta.vi.mapper.NewsCollectionMapper;
import org.springframework.stereotype.Service;

/**
* @author 86184
* @description 针对表【news_collection】的数据库操作Service实现
* @createDate 2022-12-06 10:58:45
*/
@Service
public class NewsCollectionServiceImpl extends ServiceImpl<NewsCollectionMapper, NewsCollection>
    implements NewsCollectionService{

}




