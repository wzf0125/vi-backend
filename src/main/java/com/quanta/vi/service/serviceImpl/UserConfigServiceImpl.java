package com.quanta.vi.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.entity.UserConfig;
import com.quanta.vi.mapper.UserConfigMapper;
import com.quanta.vi.service.UserConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {

}
