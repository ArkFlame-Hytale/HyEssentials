package com.arkflame.hyessentials.managers;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataManager {
    private final HyEssentials plugin;
    private final File userDataFolder;
    private final Map<UUID, UserData> loadedUsers = new ConcurrentHashMap<>();
    
    public UserDataManager(HyEssentials plugin) {
        this.plugin = plugin;
        this.userDataFolder = new File(plugin.getDataFolder(), "userdata");
        userDataFolder.mkdirs();
    }
    
    public CompletableFuture<UserData> loadUser(UUID uuid) {
        return plugin.getTaskRunner().supplyAsync(() -> {
            if (loadedUsers.containsKey(uuid)) {
                return loadedUsers.get(uuid);
            }
            
            File userFile = new File(userDataFolder, uuid.toString() + ".yml");
            UserData data = new UserData(uuid);
            
            if (userFile.exists()) {
                // Load from file using YML parser
                // data.load(userFile);
            }
            
            loadedUsers.put(uuid, data);
            return data;
        });
    }
    
    public void saveUser(UUID uuid) {
        UserData data = loadedUsers.get(uuid);
        if (data != null) {
            plugin.getTaskRunner().runAsync(() -> {
                File userFile = new File(userDataFolder, uuid.toString() + ".yml");
                // Save to file using YML writer
                // data.save(userFile);
            });
        }
    }
    
    public void saveAll() {
        for (UUID uuid : loadedUsers.keySet()) {
            saveUser(uuid);
        }
    }
    
    public UserData getUserData(UUID uuid) {
        return loadedUsers.get(uuid);
    }
    
    public boolean isFirstJoin(UUID uuid) {
        UserData data = loadedUsers.get(uuid);
        return data == null || data.isFirstJoin();
    }
    
    public void setFirstJoin(UUID uuid, boolean firstJoin) {
        UserData data = loadedUsers.get(uuid);
        if (data != null) {
            data.setFirstJoin(firstJoin);
        }
    }
}