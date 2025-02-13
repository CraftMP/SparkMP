package cn.afternode.craftmp.spark.mirai;

import me.lucko.spark.common.command.sender.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.ConsoleCommandSender;

import java.util.UUID;

public class MiraiCommandSender extends AbstractCommandSender<ConsoleCommandSender> {
    private static final ComponentLogger logger = ComponentLogger.logger("spark");
    private static final UUID uuid = new UUID(0, 0);

    public MiraiCommandSender() {
        super(ConsoleCommandSender.INSTANCE);
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public UUID getUniqueId() {
        return MiraiCommandSender.uuid;
    }

    @Override
    public void sendMessage(Component message) {
        MiraiCommandSender.logger.info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
