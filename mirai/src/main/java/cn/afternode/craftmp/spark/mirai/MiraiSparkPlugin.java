package cn.afternode.craftmp.spark.mirai;

import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.common.sampler.source.SourceMetadata;
import net.mamoe.mirai.console.MiraiConsole;
import net.mamoe.mirai.console.MiraiConsoleImplementation;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.Plugin;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.utils.MiraiLogger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Stream;

public class MiraiSparkPlugin extends JavaPlugin implements SparkPlugin {
    public static final MiraiSparkPlugin INSTANCE = new MiraiSparkPlugin();

    private Executor executor;
    private MiraiCommandSender commandSender;
    private SparkPlatform platform;

    private Set<SourceMetadata> plugins;

    public MiraiSparkPlugin() {
        super(JvmPluginDescription.loadFromResource("mirai.yml", MiraiSparkPlugin.class.getClassLoader()));
    }

    @Override
    public void onEnable() {
        this.executor = Executors.newCachedThreadPool();
        this.commandSender = new MiraiCommandSender();

        this.platform = new SparkPlatform(this);
        this.platform.enable();

        CommandManager.INSTANCE.registerCommand(new MiraiSparkCommandImpl(this), true);
    }

    @Override
    public void onDisable() {
        this.platform.disable();
    }

    @Override
    public String getVersion() {
        return this.getDescription().getVersion().toString();
    }

    @Override
    public Path getPluginDirectory() {
        return this.getDataFolderPath();
    }

    @Override
    public String getCommandName() {
        return "spark";
    }

    @Override
    public Stream<? extends CommandSender> getCommandSenders() {
        return Stream.of(this.commandSender);    // provides ConsoleCommandSender only
    }

    @Override
    public void executeAsync(Runnable task) {
        this.executor.execute(task);
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new MiraiPlatformInfo();
    }

    @Override
    public void log(Level level, String msg) {
        MiraiLogger logger = this.getLogger();
        switch (level.getName()) {
            case "SEVERE", "ERROR" -> logger.error(msg);
            case "WARNING" -> logger.warning(msg);
            case "INFO" -> logger.info(msg);
            case "CONFIG" -> logger.debug(msg);
            case "FINE", "FINER", "FINEST" -> logger.verbose(msg);
        }
    }

    @Override
    public void log(Level level, String msg, Throwable throwable) {
        MiraiLogger logger = this.getLogger();
        switch (level.getName()) {
            case "SEVERE", "ERROR" -> logger.error(msg, throwable);
            case "WARNING" -> logger.warning(msg, throwable);
            case "INFO" -> logger.info(msg, throwable);
            case "CONFIG" -> logger.debug(msg, throwable);
            case "FINE", "FINER", "FINEST" -> logger.verbose(msg, throwable);
        }
    }

    @Override
    public ClassSourceLookup createClassSourceLookup() {
        return new MiraiClassSourceLookup();
    }

    @Override
    public PlayerPingProvider createPlayerPingProvider() {
        return new MiraiPlayerPingProvider();
    }

    @Override
    public Collection<SourceMetadata> getKnownSources() {
        if (this.plugins == null) { // lazy load
            this.plugins = new HashSet<>();
            for (Plugin plugin : PluginManager.INSTANCE.getPlugins()) {
                if (plugin instanceof JvmPlugin jvm) {
                    JvmPluginDescription desc = jvm.getDescription();
                    this.plugins.add(new SourceMetadata(desc.getName(), desc.getVersion().toString(), desc.getAuthor(), desc.getInfo()));
                }
            }
        }
        return this.plugins;
    }

    public MiraiCommandSender getCommandSender() {
        return commandSender;
    }

    public SparkPlatform getPlatform() {
        return platform;
    }
}
