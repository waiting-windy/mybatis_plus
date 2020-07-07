# Mybatis-plus

## 1. MybatisPlus概述

> 简介

是什么？

官网： [https://mp.baomidou.com/guide/quick-start.html#%E5%88%9D%E5%A7%8B%E5%8C%96%E5%B7%A5%E7%A8%8B](https://mp.baomidou.com/guide/quick-start.html#初始化工程) 

### 特性

- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- **损耗小**：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- **支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- **支持主键自动生成**：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- **支持 ActiveRecord 模式**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- **支持自定义全局通用操作**：支持全局通用方法注入（ Write once, use anywhere ）
- **内置代码生成器**：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- **内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- **分页插件支持多种数据库**：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- **内置性能分析插件**：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

### 支持数据库

- mysql 、 mariadb 、 oracle 、 db2 、 h2 、 hsql 、 sqlite 、 postgresql 、 sqlserver 、 presto
- 达梦数据库 、 虚谷数据库 、 人大金仓数据库

### 框架结构

![framework](https://mp.baomidou.com/img/mybatis-plus-framework.jpg) 



## 2. 快速入门

[官方文档地址](https://mp.baomidou.com/guide/quick-start.html)

> 依赖

```xml
	<dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.3.2</version>
    </dependency>
```

> 配置文件

```yaml
spring:
  datasource:
    username: root
    password: 111111
    url: jdbc:mysql://localhost:3306/mybatis+?useSSl=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
```

> pojo

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

> mapper

```java
public interface UserMapper extends BaseMapper<User> {

}
```

**注意点：**我们需要在主启动类上扫描mapper中的所有接口

```java
@MapperScan("com.ze.mybatis_plus_chapter1.mapper")
```

> 测试

```java
	@Autowired
    private UserMapper mapper;
    @Test
    void contextLoads() {
        mapper.selectList(null).forEach(user -> System.out.println(user));
    }
```



> 配置日志



```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

## 3. CRUD

### 插入操作

```java
    @Test
    void testInsert() {
        User user = new User();
        user.setName("lizeyu").setAge(22).setEmail("964879015@qq.com");
        mapper.insert(user);
    }

INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? ) 
Parameters: 1279295724784742402(Long), lizeyu(String), 22(Integer), 964879015@qq.com(String)
```

#### 主键生成策略

雪花算法：

>第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)，然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点） ，最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）一共加起来刚好64位，为一个Long型。(转换成字符串后长度最多19)。
>
>snowflake生成的ID整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和workerId作区分），并且效率较高。经测试snowflake每秒能够产生26万个ID。

###   更新操作

```java
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(6L).setName("liziwei").setAge(22).setEmail("964879015@qq.com");
        mapper.updateById(user);
    }

==>  Preparing: UPDATE user SET name=?, age=?, email=? WHERE id=? 
==> Parameters: liziwei(String), 22(Integer), 964879015@qq.com(String), 6(Long)

```

### 自动填充

创建时间、修改时间！这些操作一般都是自动化完成的，我们不希望手动更新！

> 代码级别

1. 在实体类字段属性上增加注解

```java
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

```

2. 编写处理器来处理这个注解

```java
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill");
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}

```

3. 测试

```java
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

```

![1594000038073](C:\Users\96487\AppData\Roaming\Typora\typora-user-images\1594000038073.png)

### 乐观锁

> 乐观锁：顾名思义十分乐观，它总是认为不会出现问题，无论干什么都不去上锁！如果出现了问题，再次更新值测试
>
> 悲观锁：十分悲观，它总是认为总是出现问题，无论干什么都会上锁！

乐观锁实现方式：

+ 取出记录时，获取当前的version
+ 更新时，带上这个version
+ 执行更新时，set version = newVersion where version = oldVersion
+ 如果version不对，就更新失败

```java
乐观锁：1、先查询，获取版本号 version = 1
-- A
update user set name = “kuangshen”,version = version+1
where id = 2 and version = 1
    
-- B 线程抢先完成，这个时候 version = 2，会导致A修改失败！
update user set name = “kuangshen”,version = version+1
where id = 2 and version = 1

```

> 测试一下MP的乐观锁插件

1.给数据库增加version字段！

2.我们实体类增加相应的字段

```java
    @Version
    private Integer version;

```

3.注册一个拦截器

```java
@Configuration
@MapperScan("com.ze.mybatis_plus_chapter1.mapper")
@EnableTransactionManagement
public class MyBatisPlusConfig {
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}

```

4.测试乐观锁

```java
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

```

![1594019556195](C:\Users\96487\AppData\Roaming\Typora\typora-user-images\1594019556195.png)

### 查询操作

```java
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

```

### 分页查询

1、原始的limit进行分页

2、pageHelper第三方插件

3、MP其实也内置了分页插件

> 如何使用

1、配置拦截器组件即可

```java
@Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

```



2、直接使用Page对象

```java
    //测试分页查询
    @Test
    void testPage() {
        Page<User> page = new Page<>(2,5);
        mapper.selectPage(page, null).getRecords().forEach(System.out::println);

    }

```

### 逻辑删除

1、在数据表中增加is_deleted字段

![1594022884014](C:\Users\96487\AppData\Roaming\Typora\typora-user-images\1594022884014.png)

2、实体类中增加属性

```java
    @TableLogic
    private Integer isDeleted;

```

3、配置文件配置删除字段值

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-value: 1
      logic-not-delete-value: 0

```

![1594023772397](C:\Users\96487\AppData\Roaming\Typora\typora-user-images\1594023772397.png)

**注意：**

被删除字段没有真的删除，而是逻辑删除字段变为1。再次查询时不存在，MP自动为我们查询时加入了id_deleted=0字段

## 4. 条件构造器

```java
@Test
void testWrapper() {
    //查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.isNotNull("name").isNotNull("email").ge("age", 12);
    mapper.selectList(wrapper).forEach(System.out::println);
}

```

## 5. 代码自动生成器

 AutoGenerator 是 MyBatis-Plus 的代码生成器，通过 AutoGenerator 可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码，极大的提升了开发效率。 

```java
public static void main(String[] args) {

    //需要构建一个代码自动生成器对象
    AutoGenerator generator = new AutoGenerator();
    //配置策略

    //1、全局策略
    GlobalConfig gc = new GlobalConfig();
    String projectPath = System.getProperty("user.dir");
    gc.setOutputDir(projectPath + "/src/main/java");
    gc.setAuthor("lizeyu");
    gc.setServiceName("%sService");
    gc.setFileOverride(false);
    gc.setSwagger2(true);
    generator.setGlobalConfig(gc);

    //2、设置数据源
    DataSourceConfig dsc = new DataSourceConfig();
    dsc.setUrl("jdbc:mysql://localhost:3306/mybatis?useSSl=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
    dsc.setDriverName("com.mysql.cj.jdbc.Driver");
    dsc.setUsername("root");
    dsc.setPassword("111111");
    dsc.setDbType(DbType.MYSQL);
    generator.setDataSource(dsc);

    //3、包的设置
    PackageConfig pc = new PackageConfig();
    pc.setModuleName("blog");
    pc.setParent("com.ze");
    pc.setEntity("entity");
    pc.setMapper("mapper");
    pc.setService("service");
    pc.setController("controller");
    generator.setPackageInfo(pc);

    //4、策略配置
    StrategyConfig strategyConfig = new StrategyConfig();
    strategyConfig.setInclude("user");
    strategyConfig.setNaming(NamingStrategy.underline_to_camel);
    strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
    strategyConfig.setSuperEntityClass("父类实体");
    strategyConfig.setEntityLombokModel(true);
    strategyConfig.setRestControllerStyle(true);
    strategyConfig.setLogicDeleteFieldName("isDeleted");
    TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
    TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
    ArrayList<TableFill> tableFills = new ArrayList<>();
    tableFills.add(createTime);
    tableFills.add(updateTime);
    strategyConfig.setTableFillList(tableFills);
    strategyConfig.setVersionFieldName("version");
    strategyConfig.setControllerMappingHyphenStyle(true);
    generator.setStrategy(strategyConfig);

    generator.execute();

}

```

