package fr.pelt10.kubithon.kubicord.loadbalancing;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class PlayerAskHubEvent implements Listener {
    private LoadBalancing loadBalancing;

    public PlayerAskHubEvent(LoadBalancing loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    @EventHandler
    public void onPlayerConnectEvent(ServerConnectEvent event) {
        if(!event.getTarget().getName().equals("CONNECT")) {
            return;
        }

        Optional<ServerInfo> serverInfo = loadBalancing.nextHub();
        if(serverInfo.isPresent()) {
            event.setTarget(serverInfo.get());
        } else {
            event.getPlayer().disconnect(new TextComponent("Please wait..."));
        }
    }
}
