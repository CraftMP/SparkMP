package cn.afternode.craftmp.spark.jkook;

import net.kyori.adventure.text.Component;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.TextChannel;

public class JKookTextChannelUserCommandSender extends JKookUserCommandSender {
    private final TextChannel channel;

    public JKookTextChannelUserCommandSender(JKookSparkPlugin plugin, TextChannel channel, User delegate) {
        super(plugin, delegate);
        this.channel = channel;
    }

    @Override
    public void sendMessage(Component message) {
        channel.sendComponent(this.serializer.serialize(message));
    }

    public TextChannel getChannel() {
        return channel;
    }
}
