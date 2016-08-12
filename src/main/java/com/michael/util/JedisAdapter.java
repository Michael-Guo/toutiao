package com.michael.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by GWC on 2016/7/18.
 */

@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    /*
        public static void main(String[] argv) {
            Jedis jedis = new Jedis();
            jedis.flushAll();//delete database

            jedis.set("hello", "world");
            print(1, jedis.get("hello"));
            jedis.rename("hello", "newhello");
            print(1, jedis.get("newhello"));
            jedis.setex("hello2", 15, "world");//set exprired time, can be used in identified code

            jedis.set("pv", "100");
            jedis.incr("pv");
            print(2, jedis.get("pv"));
            jedis.incrBy("pv", 5);
            print(3, jedis.get("pv"));

            //list operation
            String listName = "listA";
            for (int i = 0; i < 10; i++) {
                jedis.lpush(listName, "a" + String.valueOf(i));// add an element to the start of list
            }
            print(3, jedis.lrange(listName, 0, -1));
            print(3, jedis.llen(listName));
            print(3, jedis.lpop(listName));
            print(3, jedis.llen(listName));

            print(4, jedis.lindex(listName, 3));
            print(4, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
            print(5, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
            print(3, jedis.lrange(listName, 0, -1));

            //hashset
            //hashset is suit for extending fields
            String userKey = "user12";
            jedis.hset(userKey, "name", "jim");
            jedis.hset(userKey, "age", "12");
            jedis.hset(userKey, "phone", "18645646");

            print(5, jedis.hget(userKey, "name"));
            print(6, jedis.hgetAll(userKey));
            jedis.hdel(userKey, "phone");
            print(6, jedis.hgetAll(userKey));
            print(6, jedis.hkeys(userKey));
            print(6, jedis.hvals(userKey));
            print(6, jedis.hexists(userKey, "email"));
            print(6, jedis.hexists(userKey, "age"));

            jedis.hsetnx(userKey, "school", "xju");
            jedis.hsetnx(userKey, "name", "gwc");
            print(6, jedis.hgetAll(userKey));

            //set
            String likeKey1 = "newsLike1";
            String likeKey2 = "newsLike2";
            for (int i = 0; i< 10; i++) {
                jedis.sadd(likeKey1, String.valueOf(i));
                jedis.sadd(likeKey2, String.valueOf(i*2));
            }

            print(7, jedis.smembers(likeKey1));
            print(7, jedis.smembers(likeKey2));
            print(8, jedis.sinter(likeKey1, likeKey2));
            print(8, jedis.sunion(likeKey1, likeKey2));
            print(8, jedis.sdiff(likeKey1, likeKey2));

            print(9, jedis.sismember(likeKey1, "5"));
            jedis.srem(likeKey1, "5");
            print(9, jedis.smembers(likeKey1));

            print(10, jedis.scard(likeKey1));// the length of the set
            jedis.smove(likeKey2, likeKey1, "14");
            print(10, jedis.scard(likeKey1));

            //sorted set
            //applied to rank band排行榜 特别适用于做排名
            //本质为优先队列
            String rankKey = "rankKey";
            jedis.zadd(rankKey, 15, "jim");
            jedis.zadd(rankKey, 60, "ben");
            jedis.zadd(rankKey, 90, "lee");
            jedis.zadd(rankKey, 80, "mei");
            jedis.zadd(rankKey, 75, "lucy");
            print(11, jedis.zcard(rankKey));
            print(12, jedis.zcount(rankKey, 61, 100));//how many elements in a specified range
            print(12, jedis.zscore(rankKey, "lucy"));
            jedis.zincrby(rankKey, 2, "lu");
            print(12, jedis.zscore(rankKey, "lucy"));
            print(13, jedis.zcount(rankKey, 0, 100));
            print(14, jedis.zrange(rankKey, 1, 3));// sort by min to max
            print(14, jedis.zrevrange(rankKey, 1, 3));

            for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "0", "100")) {
                print(15, tuple.getElement() + ":" + String.valueOf(tuple.getScore()) );
            }

            print(16, jedis.zrank(rankKey, "ben"));
            print(16, jedis.zrevrank(rankKey, "ben"));

            //线程池
            JedisPool pool = new JedisPool();
            for (int i=0; i< 100; i++) {
                Jedis j = pool.getResource();
                j.get("a");
                System.out.println("POOL" + i);
                j.close();
            }
        }
    */
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    private Jedis getJedis() {
        return pool.getResource();
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    //存取对象, 通过json的形式将一个对象序列化和反序列化
    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }

    //list operation
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
