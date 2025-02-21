package cn.afternode.craftmp.spark.jkook;

import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import snw.jkook.JKook;
import snw.jkook.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class JKookClassSourceLookup implements ClassSourceLookup {
    private static final Map<Class<?>, String> cache = new HashMap<>();

    private final Map<ClassLoader, String> plugins = new HashMap<>();

    public JKookClassSourceLookup() {
        for (Plugin plugin : JKook.getPluginManager().getPlugins()) {
            this.plugins.put(plugin.getClass().getClassLoader(), plugin.getDescription().getName());
        }
    }

    @Override
    public String identify(Class<?> clazz) throws Exception {
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
