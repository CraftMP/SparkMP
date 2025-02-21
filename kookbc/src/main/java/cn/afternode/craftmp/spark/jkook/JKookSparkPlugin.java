package cn.afternode.craftmp.spark.jkook;

import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.slf4j.Logger;
import snw.jkook.JKook;
import snw.jkook.command.ConsoleCommandSender;
import snw.jkook.command.JKookCommand;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.plugin.BasePlugin;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Stream;

public class JKookSparkPlugin extends BasePlugin implements SparkPlugin {
    private final Map<UUID, CommandSender> createdSenders = new HashMap<>();

    private JKookConsoleCommandSender console;
    private ComponentLogger componentLogger;
    private SparkPlatform platform;

    @Override
    public void onLoad() {
        this.componentLogger = ComponentLogger.logger(this.getLogger().getName());
        this.platform = new SparkPlatform(this);
    }

    @Override
    public void onEnable() {
        this.console = new JKookConsoleCommandSender(this, JKook.getConsoleCommandSender());

        this.platform.enable();

        JKookSparkCommandImpl executor = new JKookSparkCommandImpl(this);
        JKook.getCommandManager().registerCommand(this, new JKookCommand("spark").executesConsole(executor).executesUser(executor));
    }

    public ComponentLogger getComponentLogger() {
        return componentLogger;
    }

    @Override
    public String getVersion() {
        return this.getDescription().getVersion();
    }

    @Override
    public Path getPluginDirectory() {
        return this.getDataFolder().toPath();
    }

    @Override
    public String getCommandName() {
        return "spark";
    }

    @Override
    public Stream<? extends CommandSender> getCommandSenders() {
        return Stream.of(this.console);
    }

    @Override
    public void executeAsync(Runnable task) {
        JKook.getScheduler().runTask(this, task);
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new JKookPlatformInfo();
    }

    @Override
    public void log(Level level, String msg) {
        Logger logger = this.getLogger();
        switch (level.getName()) {
            case "SEVERE", "ERROR" -> logger.error(msg);
            case "WARNING" -> logger.warn(msg);
            case "INFO" -> logger.info(msg);
            case "CONFIG" -> logger.debug(msg);
            case "FINE", "FINER", "FINEST" -> logger.trace(msg);
        }
    }

    @Override
    public void log(Level level, String msg, Throwable throwable) {
        Logger logger = this.getLogger();
        switch (level.getName()) {
            case "SEVERE", "ERROR" -> logger.error(msg, throwable);
            case "WARNING" -> logger.warn(msg, throwable);
            case "INFO" -> logger.info(msg, throwable);
            case "CONFIG" -> logger.debug(msg, throwable);
            case "FINE", "FINER", "FINEST" -> logger.trace(msg, throwable);
        }
    }

    @Override
    public ClassSourceLookup createClassSourceLookup() {
        return new JKookClassSourceLookup();
    }

    public CommandSender asSparkSender(TextChannel channel, snw.jkook.command.CommandSender sender) {
        UUID id = this.getSenderUUID(sender);
        if (id == null) {   // unsupported sender
            return null;
        }

        CommandSender created = this.createdSenders.get(id);
        if (sender instanceof ConsoleCommandSender console) {
            created = new JKookConsoleCommandSender(this, console);
        } else if (sender instanceof User user) {
            if (channel == null)
                created = new JKookUserCommandSender(this, user);
            else
                created = new JKookTextChannelUserCommandSender(this, channel, user);
        }
        this.createdSenders.put(id, created);

        return created;
    }

    public UUID getSenderUUID(snw.jkook.command.CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return new UUID(0, 0);
        } else if (sender instanceof User user) {
            return this.getUserUUID(user);
        }
        return null;
    }

    public UUID getUserUUID(User user) {
        return new UUID(0, user.getIdentifyNumber());
    }

    public SparkPlatform getPlatform() {
        return platform;
    }
}
