package cn.afternode.craftmp.spark.jkook;

import org.jetbrains.annotations.Nullable;
import snw.jkook.command.*;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;
import snw.jkook.message.component.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class JKookSparkCommandImpl implements ConsoleCommandExecutor, UserCommandExecutor {
    private final JKookSparkPlugin plugin;

    JKookSparkCommandImpl(JKookSparkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(ConsoleCommandSender sender, Object[] arguments) {
        this.plugin.getPlatform().executeCommand(this.plugin.asSparkSender(null, sender), this.transformArguments(arguments));
    }

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (message instanceof TextChannelMessage msg) {
            this.plugin.getPlatform().executeCommand(this.plugin.asSparkSender(msg.getChannel(), sender), this.transformArguments(arguments));
        }
    }

    private String[] transformArguments(Object[] args) {
        List<String> result = new ArrayList<>();

        for (Object arg : args) {
            if (arg instanceof String str)
                result.add(str);
            else if (arg instanceof TextComponent text)
                result.add(text.toString());
        }

        return result.toArray(new String[0]);
    }
}
