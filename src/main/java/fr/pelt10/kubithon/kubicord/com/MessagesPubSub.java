package fr.pelt10.kubithon.kubicord.com;

import fr.pelt10.kubithon.kubicord.utils.JedisUtils;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;
import redis.clients.jedis.JedisPubSub;

public class MessagesPubSub extends JedisPubSub implements Runnable {
    private boolean run = true;
    private JedisUtils jedisUtils;
    private CommunicationManager communicationManager;

    public MessagesPubSub(JedisUtils jedisUtils, CommunicationManager communicationManager) {
        this.jedisUtils = jedisUtils;
        this.communicationManager = communicationManager;
    }

    @Override
    public void run() {
        while (run) {
            jedisUtils.execute(jedis -> {
                jedis.select(RedisKeys.HUB_DB_ID);
                jedis.subscribe(this, RedisKeys.COMM_PUBSUB_CHANNEL);
            });
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        String command = message.split(" ")[0];
        String[] args = message.replace(command + " ", "").split(" ");

        communicationManager.executeMessage(command, args);
    }

    @Override
    public void unsubscribe() {
        run = false;
        super.unsubscribe();
    }
}
