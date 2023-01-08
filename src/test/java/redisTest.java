import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quanta.vi.ViApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description
 * Param
 * return
 * Author:86184
 * Date: 2022/12/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViApplication.class)
public class redisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws IOException {
        JSONArray jsonArray = JSONArray.parseObject(new FileInputStream("website.json"), JSONArray.class);
        for(Object jsonArray1:jsonArray){
            JSONObject jsonArray2=(JSONObject)jsonArray1;
            ArrayList<String> list = new ArrayList<>();
            list.add((String) jsonArray2.get("url"));
            list.add((String) jsonArray2.get("name"));
            System.out.println(list);
            redisTemplate.delete(String.valueOf(jsonArray2.get("website_id")));
            redisTemplate.boundListOps("website_"+String.valueOf(jsonArray2.get("website_id"))).rightPushAll(list);
            redisTemplate.boundValueOps("website_"+String.valueOf(jsonArray2.get("website_id"))).expire(30, TimeUnit.DAYS);
        }
    }
}
