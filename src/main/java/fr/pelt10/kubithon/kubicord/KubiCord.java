package fr.pelt10.kubithon.kubicord;

import fr.pelt10.kubithon.kubicord.com.CommunicationManager;
import fr.pelt10.kubithon.kubicord.com.messages.PlayerTeleportMessage;
import fr.pelt10.kubithon.kubicord.listener.PlayerJoin;
import fr.pelt10.kubithon.kubicord.listener.PlayerLeave;
import fr.pelt10.kubithon.kubicord.listener.ProxyPing;
import fr.pelt10.kubithon.kubicord.loadbalancing.LoadBalancing;
import fr.pelt10.kubithon.kubicord.sync.SyncManager;
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
import java.util.logging.Level;

public class KubiCord extends Plugin {
    @Getter
    private Configuration configuration;
    @Getter
    private JedisUtils jedisUtils;

    @Getter
    private SyncManager syncManager;

    @Override
    public void onEnable() {
        try {

            configuration = loadConfig("config.yml");
            jedisUtils = new JedisUtils(this);

            CommunicationManager communicationManager = new CommunicationManager(this);
            communicationManager.registerMessage(new PlayerTeleportMessage());

            new PlayerJoin(this);
            new PlayerLeave(this);
            new ProxyPing(this);

            syncManager = new SyncManager(this);
        } catch (IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "Plugin loading", e);
        }

        new LoadBalancing(this);
        super.onEnable();
    }

    public Configuration loadConfig(String fileName) throws IllegalAccessException {
        try (InputStream in = getResourceAsStream(fileName)) {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();

            File file = new File(getDataFolder(), fileName);


            if (!file.exists()) {
                Files.copy(in, file.toPath());
            }

            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Configuration loading", e);
        }
        throw new IllegalAccessException("Configuration access");
    }

}
