package com.arkflame.hyessentials.managers;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.arkflame.hyessentials.HyEssentials;

public class PermissionManager {
    private final HyEssentials plugin;
    private final Map<String, PermissionGroup> groups = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerPermissions = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerGroups = new ConcurrentHashMap<>();
    private final File groupsFile;
    
    public PermissionManager(HyEssentials plugin) {
        this.plugin = plugin;
        this.groupsFile = new File(plugin.getDataFolder(), "groups.yml");
    }
    
    public void load() {
        plugin.getTaskRunner().runAsync(() -> {
            // Load groups from file
            if (!groupsFile.exists()) {
                createDefaultGroups();
            }
            // Load logic here using YML parser
        });
    }
    
    private void createDefaultGroups() {
        PermissionGroup defaultGroup = new PermissionGroup("default", 0);
        defaultGroup.addPermission("essentials.help");
        groups.put("default", defaultGroup);
        
        PermissionGroup vip = new PermissionGroup("vip", 10);
        vip.addPermission("essentials.fly");
        vip.setPrefix("&6[VIP]&r ");
        groups.put("vip", vip);
        
        PermissionGroup admin = new PermissionGroup("admin", 100);
        admin.addPermission("essentials.*");
        admin.setPrefix("&c[Admin]&r ");
        groups.put("admin", admin);
    }
    
    public boolean hasPermission(UUID uuid, String permission) {
        // Check player-specific permissions
        Set<String> perms = playerPermissions.getOrDefault(uuid, new HashSet<>());
        if (perms.contains(permission) || perms.contains("*")) {
            return true;
        }
        
        // Check group permissions
        String groupName = playerGroups.get(uuid);
        if (groupName != null) {
            PermissionGroup group = groups.get(groupName);
            if (group != null && group.hasPermission(permission)) {
                return true;
            }
        }
        
        // Check wildcard permissions
        String[] parts = permission.split("\\.");
        StringBuilder wildcard = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            wildcard.append(parts[i]).append(".");
            if (perms.contains(wildcard + "*")) {
                return true;
            }
        }
        
        return false;
    }
    
    public void addPermission(UUID uuid, String permission) {
        playerPermissions.computeIfAbsent(uuid, k -> new HashSet<>()).add(permission);
    }
    
    public void removePermission(UUID uuid, String permission) {
        Set<String> perms = playerPermissions.get(uuid);
        if (perms != null) {
            perms.remove(permission);
        }
    }
    
    public void setGroup(UUID uuid, String groupName) {
        if (groups.containsKey(groupName)) {
            playerGroups.put(uuid, groupName);
        }
    }
    
    public String getGroup(UUID uuid) {
        return playerGroups.getOrDefault(uuid, "default");
    }
    
    public PermissionGroup getGroupObject(String name) {
        return groups.get(name);
    }
    
    public void createGroup(String name, int priority) {
        groups.put(name, new PermissionGroup(name, priority));
    }
    
    public void deleteGroup(String name) {
        groups.remove(name);
    }
    
    public Collection<PermissionGroup> getAllGroups() {
        return groups.values();
    }
}