package fr.pelt10.kubithon.kubicord.sync;

import fr.pelt10.kubithon.kubicord.KubiCord;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class SyncManager {

    @Getter
    private int onlinePlayer = 0;

    @Getter
    private int maxPlayer = 0;

    public SyncManager(KubiCord kubiCord) {
        kubiCord.getProxy().getScheduler().schedule(kubiCord, () ->
            kubiCord.getJedisUtils().execute(jedis -> {
                jedis.select(RedisKeys.BUNGEECORD_DB_ID);
                onlinePlayer = jedis.scard(RedisKeys.BUNGEECORD_PLAYERLIST).intValue();
                maxPlayer = onlinePlayer + (100 - (onlinePlayer%100));
            })
        , 0, 10, TimeUnit.SECONDS);
    }
}
