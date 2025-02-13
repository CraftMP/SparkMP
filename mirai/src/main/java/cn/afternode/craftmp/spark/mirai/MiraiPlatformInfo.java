package cn.afternode.craftmp.spark.mirai;

import me.lucko.spark.common.platform.PlatformInfo;
import net.mamoe.mirai.console.MiraiConsole;
import net.mamoe.mirai.console.MiraiConsoleImplementation;

public class MiraiPlatformInfo implements PlatformInfo {
    @Override
    public Type getType() {
        return Type.PROXY;
    }

    @Override
    public String getName() {
        return "MiraiConsole";
    }

    @Override
    public String getBrand() {
        return MiraiConsoleImplementation.getInstance().getFrontEndDescription().getName();
    }

    @Override
    public String getVersion() {
        return MiraiConsole.INSTANCE.getVersion().toString();
    }

    @Override
    public String getMinecraftVersion() {
        return "";
    }
}
