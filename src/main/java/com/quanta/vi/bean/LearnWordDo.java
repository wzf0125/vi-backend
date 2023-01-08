package com.quanta.vi.bean;

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
public class LearnWordDo {
    // 单词
    Word word;

    // 混淆选项
    Word[] option;

    // 正确选项下标
    int correct;
}
