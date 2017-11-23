package fr.pelt10.kubithon.kubicord.loadbalancing;

import fr.pelt10.kubithon.kubicord.KubiCord;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;

import java.util.LinkedList;
import java.util.logging.Logger;

public class LoadBalancing {
    private static LinkedList<HubInstance> hubList = new LinkedList<>();
    private static int RoundRobin = 0;
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
    }
}
