package cn.afternode.craftmp.spark.mirai;

import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;

import java.util.HashMap;
import java.util.Map;

public class MiraiPlayerPingProvider implements PlayerPingProvider {
    @Override
    public Map<String, Integer> poll() {
        final Map<String, Integer> fake = new HashMap<>();
        for (Bot instance : Bot.getInstances()) {
            for (Group group : instance.getGroups()) {
                fake.put("g" + group.getId(), 0);
            }
            for (Friend friend : instance.getFriends()) {
                fake.put("f" + friend.getId(), 0);
            }
        }
        return fake;
    }
}
