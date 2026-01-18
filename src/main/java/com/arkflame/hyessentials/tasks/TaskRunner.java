package com.arkflame.hyessentials.tasks;

import com.arkflame.hyessentials.HyEssentials;

import java.util.concurrent.*;

/**
 * TaskRunner - Handles all asynchronous operations
 * Uses Hytale's scheduler if available, otherwise uses a custom thread pool
 */
public class TaskRunner {
    
    private final HyEssentials plugin;
    private final ScheduledExecutorService asyncExecutor;
    private final ExecutorService syncExecutor;
    private boolean useHytaleScheduler;
    
    public TaskRunner(HyEssentials plugin) {
        this.plugin = plugin;
        
        // Try to use Hytale's built-in scheduler
        try {
            // Attempt to get Hytale's scheduler
            this.useHytaleScheduler = plugin.getServer().getScheduler() != null;
        } catch (Exception e) {
            this.useHytaleScheduler = false;
        }
        
        if (!useHytaleScheduler) {
            // Create custom thread pool
            int cores = Runtime.getRuntime().availableProcessors();
            this.asyncExecutor = Executors.newScheduledThreadPool(
                Math.max(2, cores / 2),
                new ThreadFactory() {
                    private int counter = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, "HyEssentials-Async-" + counter++);
                        thread.setDaemon(true);
                        return thread;
                    }
                }
            );
            
            this.syncExecutor = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r, "HyEssentials-Sync");
                thread.setDaemon(true);
                return thread;
            });
            
            plugin.getLogger().info("Using custom thread pool for async operations");
        } else {
            this.asyncExecutor = null;
            this.syncExecutor = null;
            plugin.getLogger().info("Using Hytale's native scheduler");
        }
    }
    
    /**
     * Run a task asynchronously
     */
    public CompletableFuture<Void> runAsync(Runnable task) {
        if (useHytaleScheduler) {
            return CompletableFuture.runAsync(task, 
                plugin.getServer().getScheduler().async());
        } else {
            return CompletableFuture.runAsync(task, asyncExecutor);
        }
    }
    
    /**
     * Run a task synchronously (on main thread)
     */
    public void runSync(Runnable task) {
        if (useHytaleScheduler) {
            plugin.getServer().getScheduler().sync().execute(task);
        } else {
            syncExecutor.execute(task);
        }
    }
    
    /**
     * Run a task after a delay
     */
    public void runDelayed(Runnable task, long delay, TimeUnit unit) {
        if (useHytaleScheduler) {
            plugin.getServer().getScheduler().sync()
                .schedule(task, delay, unit);
        } else {
            asyncExecutor.schedule(() -> runSync(task), delay, unit);
        }
    }
    
    /**
     * Run a task asynchronously after a delay
     */
    public void runAsyncDelayed(Runnable task, long delay, TimeUnit unit) {
        if (useHytaleScheduler) {
            plugin.getServer().getScheduler().async()
                .schedule(task, delay, unit);
        } else {
            asyncExecutor.schedule(task, delay, unit);
        }
    }
    
    /**
     * Run a task repeatedly
     */
    public ScheduledFuture<?> runRepeating(Runnable task, long initialDelay, long period, TimeUnit unit) {
        if (useHytaleScheduler) {
            return plugin.getServer().getScheduler().sync()
                .scheduleAtFixedRate(task, initialDelay, period, unit);
        } else {
            return asyncExecutor.scheduleAtFixedRate(() -> runSync(task), initialDelay, period, unit);
        }
    }
    
    /**
     * Run a callable asynchronously and return result
     */
    public <T> CompletableFuture<T> supplyAsync(Callable<T> task) {
        if (useHytaleScheduler) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, plugin.getServer().getScheduler().async());
        } else {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, asyncExecutor);
        }
    }
    
    /**
     * Shutdown the task runner
     */
    public void shutdown() {
        if (!useHytaleScheduler) {
            asyncExecutor.shutdown();
            syncExecutor.shutdown();
            
            try {
                if (!asyncExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    asyncExecutor.shutdownNow();
                }
                if (!syncExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    syncExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                asyncExecutor.shutdownNow();
                syncExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
