### Code in Java
> 接下来介绍一下**Redis**在**Java**中的使用。
>
> 此节中的Demo是基于**Spring Boot**编写的**单元测试**。

#### 准备工作

***1. Maven依赖***

```
<!-- Sping Boot starter 中已集成Redis，依赖方便 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- 我使用的Jedis客户端，可满足基础的业务操作需求 -->
<!-- 其他客户端可查看官网：https://redis.io/clients-->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.9.0</version>
</dependency>

<!-- 这里也用到了阿里的fastjson，用于Object与Json的转换 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.49</version>
</dependency>
```

***2. 注入灵魂：Redis配置***

```
/**
 * 描述: Redis配置类
 *
 * @author hk
 * @create 2018-10-30 16:14
 */
@Configuration
public class RedisConfig {

  @Bean(name = "demoRedisTemplate")
  public StringRedisTemplate redisTemplate(
      @Value("${redis.host}") String host,
      @Value("${redis.port}") int port,
      @Value("${redis.password}") String password,
      @Value("${redis.database}") int database,
      @Value("${redis.pool.max-idle}") int maxIdle,
      @Value("${redis.pool.max-wait}") long maxWaitMillis) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(
        connectionFactory(host, port, password, database, maxIdle, maxWaitMillis));
    return template;
  }

  private RedisConnectionFactory connectionFactory(String host, int port, String password,
      int database, int maxIdle, long maxWaitMillis) {
    JedisConnectionFactory jedis = new JedisConnectionFactory();
    jedis.setHostName(host);
    jedis.setPort(port);
    if (StringUtils.isNotBlank(password)) {
      jedis.setPassword(password);
    }
    if (database != 0) {
      jedis.setDatabase(database);
    }
    jedis.setPoolConfig(poolConfig(maxIdle, maxWaitMillis));
    jedis.afterPropertiesSet();
    RedisConnectionFactory factory = jedis;
    return factory;
  }

  private JedisPoolConfig poolConfig(int maxIdle, long maxWaitMillis) {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxIdle(maxIdle);
    poolConfig.setMaxWaitMillis(maxWaitMillis);
    return poolConfig;
  }

}
```

***3. 单元测试注释***
```
/**
 * 描述: Redis操作单元测试
 *
 *     该类包含了操作Redis的基本业务方法。
 *     旨在提供一个可读性高的操作示例协助日常业务开发。
 *     由于Redis是Key-Value型的数据库，
 *     使用Java对其进行读写操作相对其他类型的数据库更简单。
 *
 * @author hk
 * @create 2018-10-30 16:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisOperationTest {

  @Resource(name = "demoRedisTemplate")
  private RedisTemplate redisTemplate;

  private static final long EXPIRE_TIME = 100;

  @Test
  public void testString() {...}

  @Test
  public void testList() {...}

  @Test
  public void testSet() {...}

  @Test
  public void testZSet() {...}

  @Test
  public void testHash() {...}

}
```

#### 实操5大Redis类型

***1. 操作String***
```
@Test
public void testString() {

  String key1 = "demo:string:key1";
  BoundValueOperations<String, String> bvo1 =
      redisTemplate.boundValueOps(key1);

  System.out.println(
      "1. The key" + key1 + "is persist?  -  "
      + (bvo1.persist() ? "Yes" : "No"));

  bvo1.set("value1", EXPIRE_TIME, TimeUnit.SECONDS);
  System.out.println(
      "2. The key" + key1 + "is persist now?  -  "
       + (bvo1.persist() ? "Yes" : "No"));

  String valueStr = bvo1.get();
  System.out.println("3. Key:" + key1 + " Value:" + valueStr);

  valueStr = bvo1.getAndSet("value2");
  System.out.println("4. Key:" + key1 + " Old Value:" + valueStr);

  bvo1.append(" & append");
  valueStr = bvo1.get();
  System.out.println("5. Key:" + key1 + " New Value:" + valueStr);

  boolean success = bvo1.setIfAbsent("value3");
  if (!success) {
    System.out.println("6. Set failure, the key is already exist!");
  }

  Long expire = bvo1.getExpire();
  System.out.println("7. Key:" + key1 + " will be expire after "
      + expire + " s");

  String key2 = "demo:string:key2";
  BoundValueOperations<String, String> bvo2 =
      redisTemplate.boundValueOps(key2);

  bvo2.set(JsonUtils.obj2json(new Fruit("Banana", 100)),
      EXPIRE_TIME, TimeUnit.SECONDS);
  valueStr = bvo2.get();
  if (StringUtils.isNotBlank(valueStr)) {
    Fruit banana = JsonUtils.json2obj(valueStr, Fruit.class);
    System.out.println("8. " + banana.toString());
  }

  expire = bvo2.getExpire();
  System.out.println("9. Key:" + key2 + " will be expire after "
      + expire + " s");
}
```
Console:
```
1. The keydemo:string:key1is persist?  -  No
2. The keydemo:string:key1is persist now?  -  Yes
3. Key:demo:string:key1 Value:value1
4. Key:demo:string:key1 Old Value:value1
5. Key:demo:string:key1 New Value:value2 & append
6. Set failure, the key is already exist!
7. Key:demo:string:key1 will be expire after -1 s
8. This fruit is Banana, price is 100
9. Key:demo:string:key2 will be expire after 100 s
```

***2. 操作List***
```
@Test
public void testList() {

  String key1 = "demo:list:key1";
  BoundListOperations<String, String> blo = redisTemplate.boundListOps(key1);
  List<String> list;
  blo.rightPushIfPresent("value0");
  blo.rightPush("value1");
  blo.rightPush("value2");
  blo.rightPush("value3");
  list = blo.range(0, -1);
  System.out.println("1. list's members:" + list);
  int i = 1;
  String temp = blo.index(i);
  System.out.println("2. list[" + i + "] = " + temp);
  System.out.println("3. list's size: " + blo.size());
  temp = blo.rightPop();

  System.out.println("4. rpop: " + temp);
  blo.rightPushIfPresent("value4");
  list = blo.range(0, -1);
  System.out.println("5. list's members:" + list);

  String[] arr = {"value5", "value6", "value7"};
  blo.rightPushAll(arr);
  list = blo.range(0, -1);
  System.out.println("6. list's members:" + list);
}
```
Console:
```
1. list's members:[value1, value2, value3]
2. list[1] = value2
3. list's size: 3
4. rpop: value3
5. list's members:[value1, value2, value4]
6. list's members:[value1, value2, value4, value5, value6, value7]
```

***3. 操作Hash***
```
@Test
public void testHash() {
  String key1 = "demo:hash:key1";
  BoundHashOperations bho = redisTemplate.boundHashOps(key1);
  bho.put("name", "hk");
  bho.put("age", "18");
  bho.put("sex", "man");
  if (bho.hasKey("like")) {
    System.out.println("0. hk likes " + bho.get("like"));
  } else {
    bho.put("like", "women");
  }
  Map<String, String> map = bho.entries();
  System.out.print("1. Personal Info:");
  for (Map.Entry<String, String> entry : map.entrySet()) {
    System.out.print(entry.getKey() + " is " + entry.getValue() + ", ");
  }
  System.out.println();
  bho.increment("age", 5);
  Set<String> keys = new HashSet<>();
  List<String> values;
  keys.add("name");
  keys.add("age");
  keys.add("sex");
  values = bho.multiGet(keys);
  System.out.println("2. Keys are : " + keys);
  System.out.println("3. Values are : " + values);
  bho.putIfAbsent("test1", "Error");
  bho.putIfAbsent("age", "99");
  bho.put("name", "Yangyang.Luo");
  bho.put("test2", "Error");
  bho.put("test3", "Error");
  String[] error = {"test2", "test3"};
  bho.delete(error);
  keys = bho.keys();
  values = bho.values();
  System.out.println("4. Keys are : " + keys);
  System.out.println("5. Values are : " + values);
}
```
Console:
```
1. Personal Info:age is 18, name is hk, like is women, sex is man,
2. Keys are : [sex, name, age]
3. Values are : [man, hk, 23]
4. Keys are : [name, age, sex, like, test1]
5. Values are : [Yangyang.Luo, 23, man, women, Error]
```
***4. 操作Set***
```
@Test
  public void testSet() {
    String key1 = "demo:set:key1";
    BoundSetOperations<String, String> bso1 = redisTemplate.boundSetOps(key1);
    bso1.add("value1");
    bso1.add("value1");
    System.out
        .println("1. Does set contains value2 ?   - " + (bso1.isMember("value2") ? "Yes" : "No"));
    bso1.add("value2");
    bso1.add("value3");
    Set<String> set1 = bso1.members();
    System.out.println("2. set1 : " + set1);

    String key2 = "demo:set:key2";
    BoundSetOperations<String, String> bso2 = redisTemplate.boundSetOps(key2);
    bso2.add("value2");
    bso2.add("value3");
    bso2.add("value4");
    Set<String> set2 = bso2.members();
    System.out.println("3. set2 : " + set2);

    Set<String> diff;
    diff = bso1.diff(key2);
    System.out.println("4. set1 - key2 :" + diff);
    // diff(Collection<K> var1)支持传入多个key值

    String key3 = "demo:set:key3";
    bso1.diffAndStore(key2, key3);
    BoundSetOperations<String, String> bso3 =
        redisTemplate.boundSetOps(key3);
    Set<String> set3 = bso3.members();
    System.out.println("5. set3 = set1 - set2 :" + set3);

    Set<String> intersect;
    intersect = bso1.intersect(key2);
    System.out.println("6. set1 ∩ set2 : " + intersect);

    Set<String> union;
    union = bso1.union(key2);
    System.out.println("7. set1 ∪ set2 : " + union);

    bso1.unionAndStore(key2, key1);
    System.out.println("8. Random pop from " + bso1.members() +
        " is " + bso1.pop());

  }
```
Console:
```
1. Does set contains value2 ?   - No
2. set1 : [value1, value2, value3]
3. set2 : [value4, value2, value3]
4. set1 - key2 :[value1]
5. set3 = set1 - set2 :[value1]
6. set1 ∩ set2 : [value2, value3]
7. set1 ∪ set2 : [value2, value3, value1, value4]
8. Random pop from [value2, value3, value1, value4] is value1
```
***5. 操作ZSet***
```
@Test
public void testZSet() {

  String key1 = "demo:zset:key1";
  BoundZSetOperations bzso = redisTemplate.boundZSetOps(key1);
  long count1, rank;
  bzso.add("Mike", 27);
  bzso.add("Make", 27);
  bzso.add("Zue", 22.5);
  bzso.add("Tony", 24);
  bzso.add("Jack", 25.5);

  rank = bzso.rank("Jack");
  System.out.println("1. Rank of Jack: " + rank);

  bzso.incrementScore("Jack", -2);
  rank = bzso.rank("Jack");
  System.out.println("2. Rank of Jack: " + rank);

  count1 = bzso.count(22, 25);
  System.out.println("3. Age between 22 - 25 have " + count1 + " people");

  Set range = bzso.rangeByScore(20, 30);
  System.out.println("4. Rank by age ASC: " + range);

  Set<ZSetOperations.TypedTuple<Object>> range2 =
      bzso.rangeByScoreWithScores(20, 30);
  System.out.print("5. Range with scores: ");
  for (ZSetOperations.TypedTuple<Object> tt : range2){
    System.out.print(tt.getValue() + " is "+tt.getScore()+ ", ");
  }
  System.out.println();

  Set range3 = bzso.reverseRange(0, 5);
  System.out.println("6. Range by age DESC: " + range3);

  bzso.removeRange(0, 1);
  range = bzso.range(0, -1);
  System.out.println("7. After remove Range 0 - 1: " + range);

}
```
Console:
```
1. Rank of Jack: 2
2. Rank of Jack: 1
3. Age between 22 - 25 have 3 people
4. Rank by age ASC: [Zue, Jack, Tony, Make, Mike]
5. Range with scores: Zue is 22.5, Jack is 23.5, Tony is 24.0,
    Make is 27.0, Mike is 27.0,
6. Range by age DESC: [Mike, Make, Tony, Jack, Zue]
7. After remove Range 0 - 1: [Tony, Make, Mike]
```
