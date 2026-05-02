package bsh.loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BshPluginLoader extends ClassLoader {
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    public BshPluginLoader(ClassLoader parent) {
        super(parent);
    }

    public void addClass(String name, Class<?> c) {
        classes.put(name, c);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> c = classes.get(name);
        if (c != null) return c;
        throw new ClassNotFoundException(name);
    }
}