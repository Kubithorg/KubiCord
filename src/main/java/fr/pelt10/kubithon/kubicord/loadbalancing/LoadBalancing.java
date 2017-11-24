package fr.pelt10.kubithon.kubicord.loadbalancing;

import fr.pelt10.kubithon.kubicord.KubiCord;
import fr.pelt10.kubithon.kubicord.loadbalancing.hub.HubInstance;
import fr.pelt10.kubithon.kubicord.loadbalancing.hub.HubPubSub;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Logger;

public class LoadBalancing {
    private static LinkedList<HubInstance> hubList = new LinkedList<>();
    private static int roundRobin = 0;
    private static HubPubSub hubPubSub;

    public LoadBalancing(KubiCord kubiCord) {
        Logger logger = kubiCord.getLogger();

        kubiCord.getJedisUtils().execute(jedis -> {
            jedis.select(RedisKeys.HUB_DB_ID);
            jedis.ping();
            logger.info("Loading Started Hub...");
            jedis.hgetAll(RedisKeys.HUB_KEY_NAME).values().stream().map(HubInstance::deserialize).forEach(hubInstance -> {
                hubList.add(hubInstance);
                logger.info(" - " + hubInstance.getHubID() + " load.");
            });
        });

        if(hubPubSub == null) {
            hubPubSub = new HubPubSub(kubiCord, hubList);
            new Thread(hubPubSub).start();
        }

        kubiCord.getProxy().getPluginManager().registerListener(kubiCord, new PlayerAskHubEvent(this));
    }

    public Optional<ServerInfo> nextHub() {
        roundRobin++;
        if(roundRobin >= hubList.size()) {
            roundRobin = 0;
        }

        return Optional.of(hubList.size() == 0 ? null : hubList.get(roundRobin).getAsServerInfo());
    }
}
