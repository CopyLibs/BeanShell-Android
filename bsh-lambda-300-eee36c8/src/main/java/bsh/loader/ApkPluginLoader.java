package bsh.loader;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;

public class ApkPluginLoader {

    private static final ConcurrentMap<String, PluginContext> sLoaderCache = new ConcurrentHashMap<>();
    private static final String DIR_NAME = "dynamic";

    static {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentApplicationMethod = activityThreadClass.getDeclaredMethod("currentApplication");
            Object context = currentApplicationMethod.invoke(null);
            if (context instanceof Context) {
                clearAllCache((Context) context);
            }
        } catch (Exception ignored) {
        }
    }

    public static class PluginContext extends ContextWrapper {
        private final Resources resources;
        private final ClassLoader classLoader;

        public PluginContext(Context base, Resources res, ClassLoader cl) {
            super(base);
            this.resources = res;
            this.classLoader = cl;
        }

        @Override
        public Resources getResources() {
            return resources;
        }

        @Override
        public ClassLoader getClassLoader() {
            return classLoader;
        }

        @Override
        public AssetManager getAssets() {
            return resources.getAssets();
        }
    }

    public static PluginContext loadApk(String apkPath, Context ctx) {
        File sourceFile = new File(apkPath);
        if (!sourceFile.exists()) return null;

        String md5 = DataUtil.getMd5ByFilePath(apkPath);
        if (md5 == null || md5.isEmpty()) return null;

        if (sLoaderCache.containsKey(md5)) {
            return sLoaderCache.get(md5);
        }

        File pluginsRoot = new File(ctx.getCacheDir(), DIR_NAME);
        File workDir = new File(pluginsRoot, md5);
        File targetApk = new File(workDir, "base.apk");
        File optDir = new File(workDir, "opt");
        File libDir = new File(workDir, "lib");

        try {
            if (!targetApk.exists()) {
                if (!workDir.exists()) workDir.mkdirs();
                if (!optDir.exists()) optDir.mkdirs();
                if (!libDir.exists()) libDir.mkdirs();

                copyFile(sourceFile, targetApk);
                targetApk.setWritable(false);
                extractSoLibraries(targetApk, libDir);
            }

            DexClassLoader classLoader = new DexClassLoader(
            targetApk.getAbsolutePath(),
            optDir.getAbsolutePath(),
            libDir.getAbsolutePath(),
            ctx.getClassLoader()
            );

            AssetManager assetManager = createAssetManager(targetApk.getAbsolutePath());
            Resources hostRes = ctx.getResources();
            Resources pluginRes = new Resources(assetManager, hostRes.getDisplayMetrics(), hostRes.getConfiguration());

            PluginContext pluginContext = new PluginContext(ctx, pluginRes, classLoader);

            sLoaderCache.put(md5, pluginContext);

            return pluginContext;

        } catch (Exception e) {
            System.err.println("[BeanShell] Failed to load plugin " + md5 + ": " + e.getMessage());
            return null;
        }
    }

    private static void copyFile(File src, File dest) throws IOException {
        try (InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        }
    }

    private static AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.getDeclaredConstructor().newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            return assetManager;
        } catch (Exception e) {
            return null;
        }
    }

    private static void extractSoLibraries(File apkFile, File libDir) {
        try (ZipFile zipFile = new ZipFile(apkFile)) {
            String[] supportedAbis = Build.SUPPORTED_ABIS;
            String targetAbi = null;
            for (String abi : supportedAbis) {
                if (hasAbiInApk(zipFile, abi)) {
                    targetAbi = abi;
                    break;
                }
            }
            if (targetAbi == null) return;
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String prefix = "lib/" + targetAbi + "/";
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith(prefix) && entry.getName().endsWith(".so")) {
                    File outFile = new File(libDir, new File(entry.getName()).getName());
                    try (InputStream is = zipFile.getInputStream(entry);
                            FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = is.read(buffer)) != -1) fos.write(buffer, 0, len);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static boolean hasAbiInApk(ZipFile zipFile, String abi) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        String prefix = "lib/" + abi + "/";
        while (entries.hasMoreElements()) {
            if (entries.nextElement().getName().startsWith(prefix)) return true;
        }
        return false;
    }

    public static void clearAllCache(Context context) {
        sLoaderCache.clear();
        File pluginsRoot = new File(context.getCacheDir(), DIR_NAME);
        deleteRecursive(pluginsRoot);
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) deleteRecursive(child);
            }
        }
        fileOrDirectory.setWritable(true);
        fileOrDirectory.delete();
    }
}