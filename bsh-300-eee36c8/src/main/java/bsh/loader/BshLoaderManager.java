package bsh.loader;

import java.util.HashMap;
import java.util.HashSet;

public class BshLoaderManager {
    private final HashMap<String, Class<?>> clazzMap = new HashMap<>();

    public Class<?> loadInternalClass(String name, byte[] code) {
        if (clazzMap.containsKey(name)) return clazzMap.get(name);
        try {
            ClassLoader parentLoader = new BshPluginLoader(getClass().getClassLoader());
            ClassLoader classLoader = new BshConvertHelper().convertClassToLoader(name, code, parentLoader);
            Class<?> clazz = classLoader.loadClass(name);
            clazzMap.put(name, clazz);
            return clazz;
        } catch (Exception ignored) {
            return null;
        }
    }

    private final HashSet<ClassLoader> loaders = new HashSet<>();

    public void addClassLoader(ClassLoader loader) {
        if (loader != null) loaders.add(loader);
    }

    public Class<?> getLoaderClass(String name) {
        for (ClassLoader loader : loaders) {
            try {
                return loader.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return null;
    }
}
