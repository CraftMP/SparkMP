package cn.afternode.craftmp.spark.mirai;

import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import net.mamoe.mirai.console.plugin.Plugin;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

import java.util.HashMap;
import java.util.Map;

public class MiraiClassSourceLookup implements ClassSourceLookup {
    private static final Map<Class<?>, String> cache = new HashMap<>();

    private final Map<ClassLoader, String> plugins = new HashMap<>();

    public MiraiClassSourceLookup() {
        for (Plugin plugin : PluginManager.INSTANCE.getPlugins()) {
            if (plugin instanceof JvmPlugin jvm) {
                plugins.put(plugin.getClass().getClassLoader(), jvm.getDescription().getName());
            }
        }
    }

    @Override
    public String identify(Class<?> clazz) {
        if (cache.containsKey(clazz))
            return cache.get(clazz);

        String name = null;
        ClassLoader ldr = clazz.getClassLoader();
        while (name == null) {
            name = this.search(ldr);    // search for source until found

            try {
                ldr = ldr.getParent();
            } catch (SecurityException se) {
                break;  // no access to parent
            }
        }

        if (name != null)   // put cache
            cache.put(clazz, name);

        return name;
    }

    private String search(ClassLoader loader) {

        if (plugins.containsKey(loader))
            return plugins.get(loader);
        return null;
    }
}
