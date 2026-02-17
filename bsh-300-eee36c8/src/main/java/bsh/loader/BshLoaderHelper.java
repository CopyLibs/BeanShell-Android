package bsh.loader;

import java.util.HashMap;

public class BshLoaderHelper {
    private static final HashMap<String, ClassLoader> dexLoaderMap = new HashMap<>();

    public static ClassLoader getLoaderByDex(String dexPath, ClassLoader parentLoader) {
        String dexMd5 = DataUtil.getFileMD5(dexPath);
        if (dexLoaderMap.containsKey(dexMd5)) return dexLoaderMap.get(dexMd5);
        try {
            ClassLoader dexLoader = new BshConvertHelper().convertDexToLoader(dexPath, parentLoader);
            dexLoaderMap.put(dexMd5, dexLoader);
            return dexLoader;
        } catch (Exception e) {
            return null;
        }
    }
}
