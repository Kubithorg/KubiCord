package fr.pelt10.kubithon.kubicord.loadbalancing.hub;

import com.google.gson.Gson;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class HubInstance {
    private static Gson gson = new Gson();
    @Getter
    private String HubID;
    @Getter
    private String ip;
    @Getter
    private int port;

    public HubInstance(String HubID, String ip, int port) {
        this.HubID = HubID;
        this.ip = ip;
        this.port = port;
    }

    public ServerInfo getAsServerInfo() {
        return ProxyServer.getInstance().constructServerInfo(getHubID(), new InetSocketAddress(ip, port), "Hub generate by KubiCord", false);
    }

    public static String serialize(HubInstance hubInstance) {
        return gson.toJson(hubInstance);
    }

    public static HubInstance deserialize(String string) {
        return gson.fromJson(string, HubInstance.class);
    }
}
