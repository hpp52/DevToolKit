### Redis Memory Management
> 由于Redis的读写都是在内存中完成的，需要考虑内存不够的决策
>
> Redis针对内存管理有3个命令、8种淘汰策略

#### Config命令
```
maxmemory <bytes>：设置内存使用限制，与从机交互占用的内存不算在内
maxmemory-policy noeviction：配置使用的淘汰策略
maxmemory-samples 5：设置淘汰key时的单位组大小。
```

#### 淘汰策略
```
volatile-lru：对带有过期时间的key进行LRU算法淘汰
volatile-lfu：对带有过期时间的key进行LFU算法淘汰
volatile-random：对带有过期时间的key进行随机删除
volatile-ttl：删除最快到期的key
allkeys-lru：对所有的key进行LRU算法淘汰
allkeys-lfu：对所有的key进行LFU算法淘汰
noeviction：不作删除，对写操作返回error
```

#### LFU
**L**east **F**requently **U**sed：淘汰最少访问次数的key

LFU算法相对比较复杂，源码如下：
```
uint8_t LFULogIncr(uint8_t counter) { // counter为当前访问频率
    if (counter == 255) return 255;
    double r = (double)rand()/RAND_MAX; // r为随机小数
    double baseval = counter - LFU_INIT_VAL; // baseval为counter与初始值的差值
    if (baseval < 0) baseval = 0; // 差值小于0时counter++
    double p = 1.0/(baseval*server.lfu_log_factor+1); // 计算counter自加的概率
    if (r < p) counter++; return counter;
}

unsigned long LFUDecrAndReturn(robj *o) {
    unsigned long ldt = o->lru >> 8;
    unsigned long counter = o->lru & 255;
    // num_periods衰减数，由ldt/decay_time得出
    unsigned long num_periods = server.lfu_decay_time ?
                    LFUTimeElapsed(ldt) / server.lfu_decay_time : 0;
    if (num_periods)
        counter = (num_periods > counter) ? 0 : counter - num_periods;
    return counter;
}
```
Redis对象有24bits的空间来记录LRU/LFU信息

在LFU中，前16位记录访问时间（分钟），后8位记录访问频率

访问频率记为counter，最大值2^8-1=255，是一种基于概率的对数计数器

LFU算法会计算p=1/((counter-5)*factor-1)。当p大于一个随机小数时，counter才会自加。

默认情况下对数因子factor=10，8bits 的counter可表示1000w次访问频率。

衰减因子decay_time默认为1，值越大衰减速度越慢

factor和decay-time默认配置为：
```
lfu-log-factor 10
lfu-decay-time 1
```

官方给出的 factor 与 访问次数的关系：
```
+--------+------------+------------+------------+------------+------------+
| factor | 100 hits   | 1000 hits  | 100K hits  | 1M hits    | 10M hits   |
+--------+------------+------------+------------+------------+------------+
| 0      | 104        | 255        | 255        | 255        | 255        |
+--------+------------+------------+------------+------------+------------+
| 1      | 18         | 49         | 255        | 255        | 255        |
+--------+------------+------------+------------+------------+------------+
| 10     | 10         | 18         | 142        | 255        | 255        |
+--------+------------+------------+------------+------------+------------+
| 100    | 8          | 11         | 49         | 143        | 255        |
+--------+------------+------------+------------+------------+------------+
```

#### LRU
**L**east **R**ecenty **U**sed：淘汰最久访问时间的key

Redis中，LRU算法是一种近似淘汰，采样n(通过配置)个keys，在样品池中淘汰访问时间最老的key。

下图为官方给出的各情景下LRU算法的精确度：
![image](http://redis.io/images/redisdoc/lru_comparison.png)

可以看出：
1. Redis 3.0 与 2.8 对比，经过算法的改进，淘汰的精确度有所提高
2. 样品池中放入key越多，结果越准确，但这也会影响Redis的效率与资源占用
3. Redis3.0中，samples=10时结果已经非常接近精确值
