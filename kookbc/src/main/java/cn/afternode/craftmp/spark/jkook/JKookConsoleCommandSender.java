package cn.afternode.craftmp.spark.jkook;

import me.lucko.spark.common.command.sender.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import snw.jkook.command.ConsoleCommandSender;

import java.util.UUID;

public class JKookConsoleCommandSender extends AbstractCommandSender<ConsoleCommandSender> {
    private static final UUID uuid = new UUID(0, 0);

    private final JKookSparkPlugin plugin;

    public JKookConsoleCommandSender(JKookSparkPlugin plugin, ConsoleCommandSender delegate) {
        super(delegate);
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public void sendMessage(Component message) {
        this.plugin.getComponentLogger().info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
