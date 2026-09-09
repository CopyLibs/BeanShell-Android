package bsh.loader.apk;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class BshApkContext extends ContextWrapper {
    private final ClassLoader classLoader;
    private final AssetManager assetManager;
    private final Resources apkRes;

    @SuppressWarnings("deprecation")
    public BshApkContext(Context base, ClassLoader loader, AssetManager manager) {
        super(base);
        classLoader = loader;
        assetManager = manager;
        Resources hostRes = base.getResources();
        apkRes = new Resources(manager, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public AssetManager getAssets() {
        return assetManager;
    }

    @Override
    public Resources getResources() {
        return apkRes;
    }
}
