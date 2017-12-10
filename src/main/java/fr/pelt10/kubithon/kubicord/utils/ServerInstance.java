package fr.pelt10.kubithon.kubicord.utils;

import com.google.gson.Gson;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class ServerInstance {
    private static Gson gson = new Gson();
    @Getter
    private String hubID;
    @Getter
    private String ip;
    @Getter
    private int port;

    public ServerInstance(String hubID, String ip, int port) {
        this.hubID = hubID;
        this.ip = ip;
        this.port = port;
    }

    public ServerInfo getAsServerInfo() {
        return ProxyServer.getInstance().constructServerInfo(getHubID(), new InetSocketAddress(ip, port), "Hub generate by KubiCord", false);
    }

    public static String serialize(ServerInstance hubInstance) {
        return gson.toJson(hubInstance);
    }

    public static ServerInstance deserialize(String string) {
        return gson.fromJson(string, ServerInstance.class);
    }
}
