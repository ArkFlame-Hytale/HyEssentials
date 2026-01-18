package com.arkflame.hyessentials.managers;

import java.io.File;

import com.arkflame.hyessentials.HyEssentials;
import com.hypixel.hytale.builtin.hytalegenerator.fields.FastNoiseLite.Vector3;

public class SpawnManager {
    private final HyEssentials plugin;
    private Vector3 spawn;
    private final File spawnFile;
    
    public SpawnManager(HyEssentials plugin) {
        this.plugin = plugin;
        this.spawnFile = new File(plugin.getDataDirectory(), "spawn.yml");
    }
    
    public void load() {
        plugin.getTaskRunner().runAsync(() -> {
            if (spawnFile.exists()) {
                // Load spawn from file
            }
        });
    }
    
    public void setSpawn(Vector3 location) {
        this.spawn = location;
    }
    
    public Vector3 getSpawn() {
        return spawn;
    }
}