package cn.afternode.craftmp.spark.mirai;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

public class MiraiSparkCommandImpl extends JRawCommand {
    private final MiraiSparkPlugin plugin;

    public MiraiSparkCommandImpl(MiraiSparkPlugin plugin) {
        super(MiraiSparkPlugin.INSTANCE, "spark");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        this.plugin.getPlatform().executeCommand(this.plugin.getCommandSender(), args.contentToString().split(" "));
    }
}
