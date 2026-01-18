package com.arkflame.hyessentials;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.arkflame.hyessentials.config.ConfigManager;
import com.arkflame.hyessentials.config.LanguageManager;
import com.arkflame.hyessentials.economy.DummyEconomy;
import com.arkflame.hyessentials.economy.IEconomy;
import com.arkflame.hyessentials.managers.*;
import com.arkflame.hyessentials.modules.*;
import com.arkflame.hyessentials.tasks.TaskRunner;
import com.arkflame.hyessentials.commands.*;

/**
 * HyEssentials - A comprehensive Essentials plugin for Hytale
 * 
 * @author ArkFlame Development
 * @version 1.0.0
 */
public class HyEssentials extends JavaPlugin {
    private static HyEssentials instance;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    // Core Systems
    private TaskRunner taskRunner;
    private ConfigManager configManager;
    private LanguageManager languageManager;

    // Managers
    private PermissionManager permissionManager;
    private UserDataManager userDataManager;
    private TeleportManager teleportManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private SpawnManager spawnManager;
    private KitManager kitManager;
    private ChatManager chatManager;
    private MuteManager muteManager;
    private IgnoreManager ignoreManager;
    private BackManager backManager;

    // Economy
    private IEconomy economy;

    // Module System
    private ModuleManager moduleManager;

    public HyEssentials(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;

        long startTime = System.currentTimeMillis();
        LOGGER.atInfo().log("===================================");
        LOGGER.atInfo().log("  HyEssentials v" + this.getManifest().getVersion().toString());
        LOGGER.atInfo().log("  by ArkFlame Development");
        LOGGER.atInfo().log("===================================");

        // Initialize core systems
        initializeCoreSystem();

        // Initialize managers
        initializeManagers();

        // Initialize economy
        initializeEconomy();

        // Load modules
        initializeModules();

        // Register commands
        registerCommands();

        // Register events
        registerEvents();

        long loadTime = System.currentTimeMillis() - startTime;
        LOGGER.atInfo().log("HyEssentials loaded successfully in " + loadTime + "ms!");
        LOGGER.atInfo().log("===================================");
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Shutting down HyEssentials...");

        // Save all data
        if (userDataManager != null) {
            userDataManager.saveAll();
        }

        // Disable all modules
        if (moduleManager != null) {
            moduleManager.disableAll();
        }

        // Shutdown task runner
        if (taskRunner != null) {
            taskRunner.shutdown();
        }

        LOGGER.atInfo().log("HyEssentials disabled successfully!");
    }

    private void initializeCoreSystem() {
        LOGGER.atInfo().log("Initializing core systems...");

        // Task runner for async operations
        this.taskRunner = new TaskRunner(this);

        // Configuration manager
        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();

        // Language manager
        this.languageManager = new LanguageManager(this);
        this.languageManager.loadLanguages();

        LOGGER.atInfo().log("Core systems initialized!");
    }

    private void initializeManagers() {
        LOGGER.atInfo().log("Initializing managers...");

        this.permissionManager = new PermissionManager(this);
        this.userDataManager = new UserDataManager(this);
        this.teleportManager = new TeleportManager(this);
        this.homeManager = new HomeManager(this);
        this.warpManager = new WarpManager(this);
        this.spawnManager = new SpawnManager(this);
        this.kitManager = new KitManager(this);
        this.chatManager = new ChatManager(this);
        this.muteManager = new MuteManager(this);
        this.ignoreManager = new IgnoreManager(this);
        this.backManager = new BackManager(this);

        // Load data
        permissionManager.load();
        warpManager.load();
        spawnManager.load();
        kitManager.load();

        LOGGER.atInfo().log("Managers initialized!");
    }

    private void initializeEconomy() {
        LOGGER.atInfo().log("Initializing economy system...");

        // For now, use dummy economy until Vault is available
        this.economy = new DummyEconomy();

        LOGGER.atInfo().log("Economy system initialized (Dummy mode - Vault not available)");
    }

    private void initializeModules() {
        LOGGER.atInfo().log("Loading modules...");

        this.moduleManager = new ModuleManager(this);

        // Register all modules
        moduleManager.registerModule(new JoinLeaveModule(this));
        moduleManager.registerModule(new GamemodeModule(this));
        moduleManager.registerModule(new GiveModule(this));
        moduleManager.registerModule(new FlyModule(this));
        moduleManager.registerModule(new RepairModule(this));
        moduleManager.registerModule(new TeleportModule(this));
        moduleManager.registerModule(new HomeModule(this));
        moduleManager.registerModule(new WarpModule(this));
        moduleManager.registerModule(new SpawnModule(this));
        moduleManager.registerModule(new BackModule(this));
        moduleManager.registerModule(new ChatModule(this));
        moduleManager.registerModule(new MessageModule(this));
        moduleManager.registerModule(new KitModule(this));
        moduleManager.registerModule(new BasicCommandsModule(this));
        moduleManager.registerModule(new TimeModule(this));

        // Enable all modules
        moduleManager.enableAll();

        LOGGER.atInfo().log("Modules loaded: " + moduleManager.getEnabledModules().size());
    }

    private void registerCommands() {
        LOGGER.atInfo().log("Registering commands...");

        // Permission commands
        getCommandRegistry().registerCommand(new PermissionCommand(this));

        // Teleport commands
        getCommandRegistry().registerCommand(new TpaCommand(this));
        getCommandRegistry().registerCommand(new TpacceptCommand(this));
        getCommandRegistry().registerCommand(new TpadenyCommand(this));
        getCommandRegistry().registerCommand(new TpahereCommand(this));

        // Home commands
        getCommandRegistry().registerCommand(new HomeCommand(this));
        getCommandRegistry().registerCommand(new SethomeCommand(this));
        getCommandRegistry().registerCommand(new DelhomeCommand(this));
        getCommandRegistry().registerCommand(new HomesCommand(this));

        // Warp commands
        getCommandRegistry().registerCommand(new WarpCommand(this));
        getCommandRegistry().registerCommand(new SetwarpCommand(this));
        getCommandRegistry().registerCommand(new DelwarpCommand(this));

        // Spawn commands
        getCommandRegistry().registerCommand(new SpawnCommand(this));
        getCommandRegistry().registerCommand(new SetspawnCommand(this));

        // Back command
        getCommandRegistry().registerCommand(new BackCommand(this));

        // Gamemode command
        getCommandRegistry().registerCommand(new GamemodeCommand(this));

        // Give command
        getCommandRegistry().registerCommand(new GiveCommand(this));

        // Fly command
        getCommandRegistry().registerCommand(new FlyCommand(this));

        // Repair command
        getCommandRegistry().registerCommand(new RepairCommand(this));

        // Time command
        getCommandRegistry().registerCommand(new TimeCommand(this));

        // Chat commands
        getCommandRegistry().registerCommand(new MsgCommand(this));
        getCommandRegistry().registerCommand(new ReplyCommand(this));
        getCommandRegistry().registerCommand(new MuteCommand(this));
        getCommandRegistry().registerCommand(new UnmuteCommand(this));
        getCommandRegistry().registerCommand(new IgnoreCommand(this));

        // Kit commands
        getCommandRegistry().registerCommand(new KitCommand(this));
        getCommandRegistry().registerCommand(new CreatekitCommand(this));
        getCommandRegistry().registerCommand(new DelkitCommand(this));

        // Basic commands
        getCommandRegistry().registerCommand(new HealCommand(this));
        getCommandRegistry().registerCommand(new FeedCommand(this));
        getCommandRegistry().registerCommand(new ClearCommand(this));

        LOGGER.atInfo().log("Commands registered!");
    }

    private void registerEvents() {
        LOGGER.atInfo().log("Registering event listeners...");

        // Player events
        getEventRegistry().registerGlobal(PlayerReadyEvent.class, this::onPlayerJoin);
        getEventRegistry().registerGlobal(PlayerQuitEvent.class, this::onPlayerQuit);
        getEventRegistry().registerGlobal(PlayerChatEvent.class, this::onPlayerChat);
        getEventRegistry().registerGlobal(PlayerMoveEvent.class, this::onPlayerMove);
        getEventRegistry().registerGlobal(EntityDamageEvent.class, this::onEntityDamage);
        getEventRegistry().registerGlobal(PlayerDeathEvent.class, this::onPlayerDeath);
        getEventRegistry().registerGlobal(PlayerRespawnEvent.class, this::onPlayerRespawn);

        LOGGER.atInfo().log("Event listeners registered!");
    }

    // Utility Methods
    private void broadcastMessage(String message) {
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    // Getters
    public static HyEssentials getInstance() { return instance; }
    public TaskRunner getTaskRunner() { return taskRunner; }
    public ConfigManager getConfigManager() { return configManager; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public PermissionManager getPermissionManager() { return permissionManager; }
    public UserDataManager getUserDataManager() { return userDataManager; }
    public TeleportManager getTeleportManager() { return teleportManager; }
    public HomeManager getHomeManager() { return homeManager; }
    public WarpManager getWarpManager() { return warpManager; }
    public SpawnManager getSpawnManager() { return spawnManager; }
    public KitManager getKitManager() { return kitManager; }
    public ChatManager getChatManager() { return chatManager; }
    public MuteManager getMuteManager() { return muteManager; }
    public IgnoreManager getIgnoreManager() { return ignoreManager; }
    public BackManager getBackManager() { return backManager; }
    public IEconomy getEconomy() { return economy; }
    public ModuleManager getModuleManager() { return moduleManager; }
}