package com.quanta.vi.vo;

import com.quanta.vi.bean.LearnWordDo;
import com.quanta.vi.entity.Word;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/12/2
 */
@Data
public class LearnDataInfoVo {
    // 待学习单词
    List<LearnWordDo> learnWord;

}
