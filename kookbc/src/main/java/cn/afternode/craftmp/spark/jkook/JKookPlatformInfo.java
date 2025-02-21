package cn.afternode.craftmp.spark.jkook;

import me.lucko.spark.common.platform.PlatformInfo;
import snw.jkook.JKook;

public class JKookPlatformInfo implements PlatformInfo {
    @Override
    public Type getType() {
        return Type.PROXY;
    }

    @Override
    public String getName() {
        return JKook.getImplementationName();
    }

    @Override
    public String getBrand() {
        return JKook.getImplementationName();
    }

    @Override
    public String getVersion() {
        return JKook.getImplementationVersion();
    }

    @Override
    public String getMinecraftVersion() {
        return "";
    }
}
