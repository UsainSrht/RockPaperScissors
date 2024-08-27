package me.usainsrht.rockpaperscissors;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RockPaperScissors extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(getPluginMeta(), RPSCommand.crateCommand(this), "rockpaperscissors", List.of());
        });
    }

    @Override
    public void onDisable() {
    }

    public void reload() {
        reloadConfig();
        getLogger().info("reloaded");
    }
}
