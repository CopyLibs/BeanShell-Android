package bsh.loader.apk;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import bsh.loader.DataUtil;
import dalvik.system.DexClassLoader;

public class BshApkLoaderHelper {
    private static final ConcurrentMap<String, BshApkContext> apkCtxMap = new ConcurrentHashMap<>();

    private static boolean deleteRecursive(File target) {
        if (!target.exists()) return true;
        if (target.isDirectory()) {
            File[] children = target.listFiles();
            if (children == null) return false;
            for (File child : children) {
                if (!deleteRecursive(child)) return false;
            }
        }
        return target.delete() || !target.exists();
    }

    private static boolean ensureDirectory(File directory) {
        if (directory.isDirectory()) return true;
        if (directory.exists()) return false;
        return directory.mkdirs() || directory.isDirectory();
    }

    private static void copyReadOnlyApk(File source, File target) throws IOException {
        try (FileInputStream input = new FileInputStream(source);
             FileOutputStream output = new FileOutputStream(target)) {
            if (!target.setReadOnly()) throw new IOException("SetReadOnly Failed");
            byte[] buf = new byte[8192];
            int len;
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            output.getFD().sync();
        }
    }

    private static String findTargetAbi(ZipFile zipFile) {
        for (String abi : Build.SUPPORTED_ABIS) {
            String prefix = "lib/" + abi + "/";
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (!entry.isDirectory() && entryName.startsWith(prefix) && entryName.endsWith(".so")) {
                    return abi;
                }
            }
        }
        return null;
    }

    private static void extractLib(File target, File libDir) throws IOException {
        try (ZipFile zipFile = new ZipFile(target)) {
            String targetAbi = findTargetAbi(zipFile);
            if (targetAbi == null) return;
            String prefix = "lib/" + targetAbi + "/";
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (!entry.isDirectory() && entryName.startsWith(prefix) && entryName.endsWith(".so")) {
                    String libName = entryName.substring(prefix.length());
                    if (libName.isEmpty() || libName.contains("/") || libName.contains("\\")) continue;
                    File outFile = new File(libDir, libName);
                    try (InputStream input = zipFile.getInputStream(entry);
                         FileOutputStream output = new FileOutputStream(outFile)
                    ) {
                        byte[] buf = new byte[8192];
                        int len;
                        while ((len = input.read(buf)) != -1) {
                            output.write(buf, 0, len);
                        }
                    }
                }
            }
        }
    }

    private static AssetManager createAssetManager(String path) {
        try {
            Class<AssetManager> clazz = AssetManager.class;
            AssetManager manager = clazz.getDeclaredConstructor().newInstance();
            Method method = clazz.getMethod("addAssetPath", String.class);
            method.invoke(manager, path);
            return manager;
        } catch (Exception e) {
            return null;
        }
    }

    private static BshApkContext convertApkContext(Context ctx, String md5, String apkPath) throws IOException {
        File apkFile = new File(apkPath);
        File rootDir = new File(ctx.getCacheDir(), "dynamic");
        File workDir = new File(rootDir, md5);

        File targetFile = new File(workDir, "base.apk");
        File tmpFile = new File(workDir, "base.apk.tmp");
        File libDir = new File(workDir, "lib");
        File optDir = new File(workDir, "opt");

        boolean cacheReady = targetFile.isFile()
                && !targetFile.canWrite()
                && libDir.isDirectory()
                && optDir.isDirectory();
        if (!cacheReady) {
            if (!deleteRecursive(workDir)) {
                throw new IOException("Delete Cache Failed: " + workDir);
            }
            if (!ensureDirectory(libDir) || !ensureDirectory(optDir)) {
                throw new IOException("Create Cache Directory Failed: " + workDir);
            }
            try {
                copyReadOnlyApk(apkFile, tmpFile);
                extractLib(tmpFile, libDir);
                if (!tmpFile.renameTo(targetFile)) {
                    throw new IOException("RenameTo Failed");
                }
            } catch (Exception e) {
                deleteRecursive(workDir);
                throw e;
            }
        }

        DexClassLoader loader = new DexClassLoader(targetFile.getAbsolutePath(), optDir.getAbsolutePath(), libDir.getAbsolutePath(), ctx.getClassLoader());
        AssetManager manager = createAssetManager(targetFile.getAbsolutePath());
        return new BshApkContext(ctx, loader, manager);
    }

    public static BshApkContext getContextByApk(Context ctx, String apkPath) {
        final String md5 = DataUtil.getMd5ByFilePath(apkPath);
        if (md5 == null) return null;
        return apkCtxMap.computeIfAbsent(md5, k -> {
            try {
                return convertApkContext(ctx, md5, apkPath);
            } catch (Exception e) {
                System.err.println("[BeanShell] GetContextByApk: " + e);
                return null;
            }
        });
    }
}
