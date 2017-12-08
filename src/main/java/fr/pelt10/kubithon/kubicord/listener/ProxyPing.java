package fr.pelt10.kubithon.kubicord.listener;

import fr.pelt10.kubithon.kubicord.KubiCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPing implements Listener {
    private KubiCord kubiCord;

    public ProxyPing(KubiCord kubiCord) {
        this.kubiCord = kubiCord;
        kubiCord.getProxy().getPluginManager().registerListener(kubiCord, this);
    }

    @EventHandler
    public void ProxyPingEvent(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();

        ServerPing.Players players = serverPing.getPlayers();

        players.setOnline(kubiCord.getSyncManager().getOnlinePlayer());
        players.setMax(kubiCord.getSyncManager().getMaxPlayer());

        serverPing.setPlayers(players);

        event.setResponse(serverPing);
    }
}
