package fr.pelt10.kubithon.kubicord.loadbalancing;

import com.google.gson.Gson;
import lombok.Getter;

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

    public static String serialize(HubInstance hubInstance) {
        return gson.toJson(hubInstance);
    }

    public static HubInstance deserialize(String string) {
        return gson.fromJson(string, HubInstance.class);
    }
}
