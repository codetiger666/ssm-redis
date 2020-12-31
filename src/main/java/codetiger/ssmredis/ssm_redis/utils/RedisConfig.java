package codetiger.ssmredis.ssm_redis.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @program: ssm-redis
 * @description:
 * @author: Mr.Nie
 * @create: 2020-12-31 18:35
 **/

@Configuration
@EnableCaching
public class RedisConfig {

        String redishost = "program";
        int port = 6379;

        //配置jedispool
        @Bean("JedisPoolConfig")
        public JedisPoolConfig getJedisPoolConfig(){
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        配置最大连接数
            jedisPoolConfig.setMaxTotal(10);
//        配置最大空闲连接数
            jedisPoolConfig.setMaxIdle(5);
//        配置最小空闲连接数
            jedisPoolConfig.setMinIdle(0);
//        配置是否需要等待
            jedisPoolConfig.setBlockWhenExhausted(true);
//        配置最大等待时间毫秒
            jedisPoolConfig.setMaxWaitMillis(60000);
//        是否进行连接有效性检测
            jedisPoolConfig.setTestOnBorrow(true);
//        是否进行释放有效性检测
            jedisPoolConfig.setTestOnReturn(true);
//        是否开启JMX监控
            jedisPoolConfig.setJmxEnabled(true);
            return jedisPoolConfig;
        }

        //配置连接工厂
        @Bean("JedisConnectionFactory")
        public JedisConnectionFactory getJedisConnectionFactory(@Qualifier("JedisPoolConfig") JedisPoolConfig jedisPoolConfig){
            try {
                //单机版jedis
                RedisStandaloneConfiguration redisStandaloneConfiguration =
                        new RedisStandaloneConfiguration();
                //设置redis服务器的host或者ip地址
                redisStandaloneConfiguration.setHostName(redishost);
                //设置默认使用的数据库
//            redisStandaloneConfiguration.setDatabase(0);
                //设置密码
//            redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));
                //设置redis的服务的端口号
                redisStandaloneConfiguration.setPort(port);
                //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
                JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                        (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
                //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
                jpcb.poolConfig(jedisPoolConfig);
                //通过构造器来构造jedis客户端配置
                JedisClientConfiguration jedisClientConfiguration = jpcb.build();
                //单机配置 + 客户端配置 = jedis连接工厂
                return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
            }
            catch (Exception e){
                return null;
            }
        }


        //配置redis模板
        @Bean
        public RedisTemplate<String, String> redisTemplate(@Qualifier("JedisConnectionFactory") JedisConnectionFactory cf) {
            RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
            redisTemplate.setConnectionFactory(cf);
            return redisTemplate;
        }

        @Bean
        public CacheManager cacheManager(JedisConnectionFactory factory) {
            FastJsonRedisSerializer<Object> fastJsonUtil = new FastJsonRedisSerializer<>(Object.class);
            // 生成两套默认配置，通过 Config 对象即可对缓存进行自定义配置
            RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                    // 设置过期时间 10 分钟
                    .entryTtl(Duration.ofMinutes(10))
                    // 设置缓存前缀
                    .prefixKeysWith("codetiger:")
                    // 禁止缓存 null 值
                    .disableCachingNullValues()
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonUtil));
            // 设置 key 序列化
//                .serializeKeysWith()
//                // 设置 value 序列化
//                .serializeValuesWith();
            // 返回 Redis 缓存管理器
            return RedisCacheManager.builder(factory)
                    .withCacheConfiguration("user", cacheConfig).build();
        }


    }
