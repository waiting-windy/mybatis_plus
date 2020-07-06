package com.ze.mybatis_plus_chapter1;
import	java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ze.mybatis_plus_chapter1.mapper.UserMapper;
import com.ze.mybatis_plus_chapter1.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisPlusChapter1ApplicationTests {

    @Autowired
    private UserMapper mapper;
    @Test
    void contextLoads() {
        mapper.selectList(null).forEach(user -> System.out.println(user));
    }
    @Test
    void testInsert() {
        User user = new User();
        user.setName("lizeyu").setAge(22).setEmail("964879015@qq.com");
        mapper.insert(user);
    }
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(11L).setName("liziwei").setAge(22).setEmail("964879015@qq.com");
        mapper.updateById(user);
    }

    @Test
    //测试乐观锁成功
    void testOptimisticLocker1() {
        //1.查询用户信息
        User user = mapper.selectById(1L);
        //2.修改用户信息
        user.setName("liulilin");
        user.setEmail("lizeyuwaiting@gmail.com");
        //3.执行更新操作
        mapper.updateById(user);

    }
    @Test
    //测试乐观锁失败
    void testOptimisticLocker2() {
        //线程1
        User user = mapper.selectById(2L);
        user.setName("liulilin");
        user.setEmail("lizeyuwaiting@gmail.com");

        //模拟另外一个线程执行了插队操作
        User user2 = mapper.selectById(2L);
        user2.setName("shihao");
        user2.setEmail("lizeyuwaiting@gmail.com");
        mapper.updateById(user2);

        mapper.updateById(user);//如果没有乐观锁就会覆盖插队线程的值
    }
    //测试id查询
    @Test
    void testSelectById() {
        User user = mapper.selectById(1);
        System.out.println(user);
    }
    //测试批量查询
    @Test
    void testSelectByIds() {
        mapper.selectBatchIds(Arrays.asList(1, 2, 3)).forEach(user->System.out.println(user));
    }

    //测试条件查询
    @Test
    void testSelectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lizeyu");
        mapper.selectByMap(map).forEach(user -> System.out.println(user));
    }

    //测试分页查询
    @Test
    void testPage() {
        Page<User> page = new Page<>(2,5);
        mapper.selectPage(page, null).getRecords().forEach(System.out::println);

    }

    //测试删除
    @Test
    void testDeleteById() {
        mapper.deleteById(11);
    }

}
