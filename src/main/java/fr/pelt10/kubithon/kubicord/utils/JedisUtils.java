package fr.pelt10.kubithon.kubicord.utils;

import fr.pelt10.kubithon.kubicord.KubiCord;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;

public class JedisUtils {
    private JedisPool jedisPool;

    public JedisUtils(KubiCord kubiCord) {

        Configuration configuration = kubiCord.getConfiguration();

        this.jedisPool = new JedisPool(new JedisPoolConfig(),
                configuration.getString("redis.ip"), //IP Address
                configuration.getInt("redis.port"), //Port
                5000,  //Timeout
                configuration.getString("redis.password"), //Password
                0, // Database
                kubiCord.getProxy().getName());
    }

    public void execute(Consumer<Jedis> consumer) {
        try(Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        }
    }
}
