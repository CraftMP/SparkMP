package cn.afternode.craftmp.spark.jkook;

import me.lucko.spark.common.command.sender.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import snw.jkook.entity.User;

import java.util.UUID;

public class JKookUserCommandSender extends AbstractCommandSender<User> {
    protected final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
    protected final UUID uuid;
    protected final JKookSparkPlugin plugin;

    public JKookUserCommandSender(JKookSparkPlugin plugin, User delegate) {
        super(delegate);
        this.plugin = plugin;
        this.uuid = new UUID(0, delegate.getIdentifyNumber());
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.plugin.getUserUUID(this.delegate);
    }

    @Override
    public void sendMessage(Component message) {
        this.delegate.sendPrivateMessage(serializer.serialize(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return delegate.hasPermission(permission);
    }
}
