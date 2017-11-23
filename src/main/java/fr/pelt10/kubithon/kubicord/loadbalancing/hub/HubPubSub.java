package fr.pelt10.kubithon.kubicord.loadbalancing.hub;

import fr.pelt10.kubithon.kubicord.KubiCord;
import fr.pelt10.kubithon.kubicord.loadbalancing.hub.HubInstance;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HubPubSub extends JedisPubSub implements Runnable {
    private KubiCord kubiCord;
    private List<HubInstance> hubInstanceList;
    private Logger logger;
    private boolean run = true;

    public HubPubSub(KubiCord kubiCord, List<HubInstance> hubInstanceList) {
        this.kubiCord = kubiCord;
        this.hubInstanceList = hubInstanceList;
        this.logger = kubiCord.getLogger();
    }

    @Override
    public void run() {
        while (run) {
            kubiCord.getJedisUtils().execute(jedis -> {
                jedis.select(RedisKeys.HUB_DB_ID);
                jedis.subscribe(this, RedisKeys.HUB_PUBSUB_CHANNEL);
            });
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        String command = message.split(" ")[0];
        String[] args = message.replace(command + " ", "").split(" ");

        switch (command) {
            case RedisKeys.PUBSUB_CMD_NEW_HUB:
                HubInstance hubInstance = HubInstance.deserialize(args[0]);
                hubInstanceList.add(hubInstance);
                logger.info("New Hub add : " + hubInstance.getHubID());
                break;
            case RedisKeys.PUBSUB_CMD_DELETE_HUB:
                hubInstanceList  = hubInstanceList.stream().filter(hub -> hub.getHubID().equals(args[0])).collect(Collectors.toList());
                logger.info("Remove Hub : " + args[0]);
                break;
            default:
                //TODO Log ?
                break;
        }
    }

    @Override
    public void unsubscribe() {
        run = false;
        super.unsubscribe();
    }
}
