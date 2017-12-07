package fr.pelt10.kubithon.kubicord.listener;

import fr.pelt10.kubithon.kubicord.KubiCord;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerLeave implements Listener {

    private KubiCord kubiCord;

    public PlayerLeave(KubiCord kubiCord) {
        this.kubiCord = kubiCord;
        kubiCord.getProxy().getPluginManager().registerListener(kubiCord, this);
    }

    @EventHandler
    public void onPlayerJoinListener(PlayerDisconnectEvent event) {

        kubiCord.getProxy().getScheduler().runAsync(kubiCord, () ->
                kubiCord.getJedisUtils().execute(jedis -> {
                    jedis.select(RedisKeys.BUNGEECORD_DB_ID);
                    jedis.srem(RedisKeys.BUNGEECORD_PLAYERLIST, event.getPlayer().getUniqueId().toString());
                })
        );
    }
}
