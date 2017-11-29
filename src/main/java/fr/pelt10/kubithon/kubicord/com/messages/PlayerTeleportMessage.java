package fr.pelt10.kubithon.kubicord.com.messages;

import fr.pelt10.kubithon.kubicord.com.CommunicationMessage;
import fr.pelt10.kubithon.kubicord.utils.RedisKeys;
import fr.pelt10.kubithon.kubicord.utils.ServerInstance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class PlayerTeleportMessage implements CommunicationMessage {
    @Override
    public void receive(String[] args) {

        if (args.length == 2) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(args[0]));

            if (player != null && player.isConnected()) {
                ServerInstance server = ServerInstance.deserialize(args[1]);
                player.connect(server.getAsServerInfo());
            }
        }
    }

    @Override
    public String redisKeys() {
        return RedisKeys.COMM_PLAYER_TP;
    }
}
