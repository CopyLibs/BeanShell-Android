package bsh.loader;

import java.util.HashMap;

public class BshLoaderHelper {
    private static final HashMap<String, Class<?>> clazzMap = new HashMap<>();
    private static final HashMap<String, ClassLoader> loaderMap = new HashMap<>();

    public static Class<?> loadInternalClass(String name, byte[] code) {
        try {
            String key = name + DataUtil.getMd5ByBytes(code);
            if (clazzMap.containsKey(key)) return clazzMap.get(key);
            ClassLoader parentLoader = BshLoaderHelper.class.getClassLoader();
            ClassLoader classLoader = new BshConvertHelper().convertClassToLoader(name, code, parentLoader);
            Class<?> clazz = classLoader.loadClass(name);
            clazzMap.put(key, clazz);
            return clazz;
        } catch (Exception e) {
            System.err.println("[BeanShell] BshLoaderHelper loadInternalClass: " + e);
            return null;
        }
    }

    public static ClassLoader getLoaderByDex(String dexPath, ClassLoader parentLoader) {
        try {
            String key = DataUtil.getMd5ByFilePath(dexPath);
            if (loaderMap.containsKey(key)) return loaderMap.get(key);
            ClassLoader dexLoader = new BshConvertHelper().convertDexToLoader(dexPath, parentLoader);
            loaderMap.put(key, dexLoader);
            return dexLoader;
        } catch (Exception e) {
            System.err.println("[BeanShell] BshLoaderHelper getLoaderByDex: " + e);
            throw new RuntimeException("BshLoaderHelper getLoaderByDex", e);
        }
    }

    public static ClassLoader getLoaderByJar(String jarPath, ClassLoader parentLoader) {
        try {
            String key = DataUtil.getMd5ByFilePath(jarPath);
            if (loaderMap.containsKey(key)) return loaderMap.get(key);
            ClassLoader dexLoader = new BshConvertHelper().convertJarToLoader(jarPath, parentLoader);
            loaderMap.put(key, dexLoader);
            return dexLoader;
        } catch (Exception e) {
            System.err.println("[BeanShell] BshLoaderHelper getLoaderByJar: " + e);
            throw new RuntimeException("BshLoaderHelper getLoaderByJar", e);
        }
    }
}
