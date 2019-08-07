### Before
> 作为第一节，还是想写一点不一样的东西。
>
> 在最初的版本中，这份分享只提供了比较基础的理论知识。
>
> 后来考虑到实用性，结合了部分细节实战操作。
>
> 也是希望可以把分享做得更好。
>
> 如果文中有出现错误或不规范的表达，还望大家多提点提点。
>


---

### What's Redis

- **Re**mote **Di**ctionary **S**ervice : 远程字典服务
- 作者 AntireZ, 来自意大利(*非英语国家*)
- 内存存储的数据结构服务器，Key-Value型的NoSQL
- C语言实现，支持许多主流开发语言，接下来也会介绍部分Redis在Java中的运用
- 大家听说/使用它大多数是当作缓存，其实Redis能做的不仅于此

---

### Installation:

1. 第一步：安装

  ```
  wget http://download.redis.io/releases/redis-5.0.0.tar.gz
  tar xzf redis-5.0.0.tar.gz
  cd redis-5.0.0
  make
  // 几个月前官文下载主页中例举的还是Redis4.*的安装，看来Redis5.0也开始稳定了
  ```

2. 启动服务与调用：
  ```
  src/redis-server // 启动服务，可输入 ps -ef |grep redis 查看进程
  src/redis-cli // 调用客户端连接服务
  redis> set foo bar // 将key "foo" 的值设置为value "bar"
  OK
  redis> get foo // 获取key "foo" 的值
  "bar"
  ```
  
---

### Data Type

Redis有5种常用数据类型：

String/List/Hash/Set/Sorted Set

接下来我会逐一介绍这5种数据类型及使用，也是本节的主要内容。

#### 1. String
由byte数组实现

Redis的key也是String类型，二进制安全，在key-value存储中，value可以为基于二进制的任意类型，甚至是图片，size <= 512M，key的大小限制也是512M

可以对key设置过期时间，到点后自动删除该key->运用于缓存

```
SET key value [EX seconds] [PX milliseconds] [NX|XX]
GET key
DEL key [key ...]
INCR key
DECR key
MSET key value [key value ...]
MGET key value [key value ...]
EXISTS key [key ...]
TTL key

注：
1. 可通过ex(单位为秒)和px(单位为毫秒)设置key的存活时间
2. NX表示只能对key进行添加操作，不修改已有key
3. XX表示只能对key进行修改操作，不添加新key
4. SET默认覆盖key原有内容
5. DEL返回删除的key个数
6. 通过INCR/DECR操作key时，只有操作整形数字才能成功
7. 设置了过期时间后，再次SET将覆盖过期时间
8. 当key没有过期时间时TTL返回-1
9. 当key不存在时TTL返回-2
```

#### 2. List
常见的List有两种实现，ArrayList和LinkedList

Redis的List基于LinkedList来实现，索引定位很慢，push和pop很快，很方便的用作队列(FIFO)和栈(FILO)

优化： 对Blocking List通过阻塞的方式消费消息，防止在队列为空时使用无效的POP命令

使用BRPOP命令，在有新元素时才调用，并可配置等待时间

```
LPUSH key value [value ...]
RPUSH key value [value ...]
LPOP key
RPOP key
LRANGE key start stop
LTRIM key start stop
LLEN key
EXISTS key
BLPOP key [key ...] timeout
BRPOP key [key ...] timeout
RPOPLPUSH source destination

注：
1. LTRIM 可截取指定位置范围内的list
2. 当List中的元素都被POP出后，该key将被回收
3. 通过block的方式POP，仅当List中有值时才被执行，并需要配置等待时间
4. 命令 RPOPLPUSH 中的R和L不可替换
5. 不可将String当成List进行PUSH
6. 可以将List当成String进行SET，SET将覆盖原List
```

#### 3. Hash
相当于Java中的HashMap，一种无序字典，但Redis中字典的值只能为String,另外Redis的rehash是渐进的，可在rehash过程中提供服务

用于存放多个filed-value的键值对

```
HMSET key field value [field value ...]
HMGET key field [field ...]
HSET key field value
HGET key field
HGETALL key
HINCRYBT key field increment

注：
1. 不可将String当成Hash进行HSET
2. 可以将Hash当成String进行SET，SET将覆盖原Hash
```

#### 4. Set
元素唯一的String集合，无序。

SPOP随机POP集合中的元素 -> 抽奖、纸牌游戏

```
SADD key member [member ...]
SMEMBERS key // 返回所有member
SISMEMBER key member // 查询该member是否存在
SPOP key [count] // 随机从set中pop指定个数
SCARD key
SINTER key [key ...] // 求交集
SINTERSTORE destination key [key ...]
SUNION key [key ...] // 求并集
SUNIONSTORE destination key [key ...]

注：
1. 不可将String当成Set进行SADD
2. 可以将Set当成String进行SET，SET将覆盖原Set
```

#### 5. Sorted Set(ZSet)

有序的集合，由 元素+score 组成

Set(元素唯一) + Hash(每个key对应一个value) ~ Sorted Set(元素唯一，每个元素对应一个score)

其中score为浮点数，不要求唯一，Sorted Set可根据score排序

添加元素的复杂度为O(logN)，每次zadd时都需要对score排序 ->可运用于rank排行榜单

```
ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
ZRANGE key start stop [WITHSCORES]
ZREVRANGE key start stop [WITHSCORES]
ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
ZREMRANGEBYSCORE key min max // 删除score在[min,max]的member
ZRANK key member // 获取member的升序排名，第一名为0
ZREVRANK key member // 获取member的逆序排名
ZINCRBY key increment member
ZRANGEBYLEX key min max [LIMIT offset count]
ZREVRANGEBYLEX key min max [LIMIT offset count]
ZREMRANGEBYLEX key min max
ZLEXCOUNT key min max

注：
1. 当ZADD多个值，调用CH参数，返回值被修改的member个数。例：ZADD了10个值，其中4个重复，修改4个score，添加2个，返回值为6
2. ZRANGE是按照score升序排列
3. ZRANGEBYSCORE中可根据offset(从0算起)获取count个数的元素
4. 当score出现相等的情况，可根据ZRANGEBYLEX将member值排序
```

**以上五种是我们使用Redis经常接触的数据类型，但Redis官方还提供了两类String衍生的数据类型**

#### 6. BitMap

运用在特定情境下，极大的节省空间。听闻面试题40个亿就考到了这个数据类型

    科普一下计算机的空间换算：
    8bit=1Byte  2^10Byte=1KB 2^10KB=1MB 2^10MB=1GB

案例：以用户ID为索引，6MB大小的Bitmap可以统计5000w用户的日访问情况

可运用在 0/1 的情景，可作与/或/亦或/非 运算

```
SETBIT key offset value
GETBIT key offset
BITOP operation destkey key [key ...]
BITCOUNT key [start end]

注：
1. SETBIT只能将该key的逐个值设置为0或者1
2. BITOP可计算多个key的与(AND)或(OR)亦或(XOR)的值并保存到destkey中，也可计算单个key的非(NOT)值保存到destkey中
```

#### 7. HyperLogLog

String的另一种拓展，用于计算基数

基数的概念：集合中唯一元素的个数。例：[A,B,C,A,B]的基数为 3

可用 12KB的内存大小计算2^64个元素的基数，有一定误差。

```
PFADD key element [element ...]
PFCOUNT key [key ...]

使用方式：
1. 通过PFADD往key中添加元素
2. 通过PFCOUNT计算一个至多个key的总集合基数
```
