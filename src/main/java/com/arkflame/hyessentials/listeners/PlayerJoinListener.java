package com.arkflame.hyessentials.listeners;

import java.util.UUID;

import com.arkflame.hyessentials.managers.UserDataManager;
import com.arkflame.hyessentials.tasks.TaskRunner;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

public class PlayerJoinListener {
    private TaskRunner taskRunner;
    private UserDataManager userDataManager;
    private ConfigManager configManager;
    private LanguageManager languageManager;
    
    public PlayerJoinListener(TaskRunner taskRunner, UserDataManager userDataManager, ConfigManager configManager, LanguageManager languageManager) {
        this.taskRunner = taskRunner;
        this.userDataManager = userDataManager;
        this.configManager = configManager;
        this.languageManager = languageManager;
    }

    private void onPlayerJoin(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUuid();
        
        // Load player data asynchronously
        taskRunner.runAsync(() -> {
            userDataManager.loadUser(uuid);
            
            // Check if first join
            boolean isFirstJoin = userDataManager.isFirstJoin(uuid);
            
            // Sync back to main thread for messages
            taskRunner.runSync(() -> {
                // Custom join message
                if (configManager.isJoinMessageEnabled()) {
                    String joinMsg = languageManager.getMessage(player, "join_message")
                        .replace("{player}", player.getName());
                    broadcastMessage(joinMsg);
                }
                
                // First join message
                if (isFirstJoin && configManager.isFirstJoinEnabled()) {
                    String firstJoinMsg = languageManager.getMessage(player, "first_join")
                        .replace("{player}", player.getName());
                    broadcastMessage(firstJoinMsg);
                    userDataManager.setFirstJoin(uuid, false);
                }
                
                // MOTD
                if (configManager.isMotdEnabled()) {
                    String motd = languageManager.getMessage(player, "motd");
                    player.sendMessage(motd);
                }
            });
        });
    }
}
