package bsh.loader;

import java.util.HashMap;

public class BshLoaderHelper {
    private static final HashMap<String, Class<?>> clazzMap = new HashMap<>();
    private static final HashMap<String, ClassLoader> loaderMap = new HashMap<>();

    public static Class<?> loadInternalClass(String name, byte[] code) {
        if (clazzMap.containsKey(name)) return clazzMap.get(name);
        try {
            ClassLoader classLoader = new BshConvertHelper().convertClassToLoader(name, code, BshLoaderHelper.class.getClassLoader());
            Class<?> clazz = classLoader.loadClass(name);
            clazzMap.put(name, clazz);
            return clazz;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static ClassLoader getLoaderByDex(String dexPath, ClassLoader parentLoader) {
        String dexMd5 = DataUtil.getFileMD5(dexPath);
        if (loaderMap.containsKey(dexMd5)) return loaderMap.get(dexMd5);
        try {
            ClassLoader dexLoader = new BshConvertHelper().convertDexToLoader(dexPath, parentLoader);
            loaderMap.put(dexMd5, dexLoader);
            return dexLoader;
        } catch (Exception ignored) {
            return null;
        }
    }
}
