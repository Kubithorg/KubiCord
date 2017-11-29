package fr.pelt10.kubithon.kubicord;

import fr.pelt10.kubithon.kubicord.com.CommunicationManager;
import fr.pelt10.kubithon.kubicord.com.messages.PlayerTeleportMessage;
import fr.pelt10.kubithon.kubicord.loadbalancing.LoadBalancing;
import fr.pelt10.kubithon.kubicord.utils.JedisUtils;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class KubiCord extends Plugin {
    @Getter
    private Configuration configuration;
    @Getter
    private JedisUtils jedisUtils;

    private LoadBalancing loadBalancing;
    private CommunicationManager communicationManager;

    @Override
    public void onEnable() {
        try {

            if (!getDataFolder().exists())
                getDataFolder().mkdir();

            File file = new File(getDataFolder(), "config.yml");


            if (!file.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            jedisUtils = new JedisUtils(this);
            communicationManager = new CommunicationManager(this);
            communicationManager.registerMessage(new PlayerTeleportMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadBalancing = new LoadBalancing(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
