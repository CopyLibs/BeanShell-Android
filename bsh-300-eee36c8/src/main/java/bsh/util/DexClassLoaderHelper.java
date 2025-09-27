package bsh.util;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.command.dexer.DxContext;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.DexFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import dalvik.system.InMemoryDexClassLoader;

public class DexClassLoaderHelper {
    private final DexOptions DEX_OPTIONS = new DexOptions();
    private final DxContext DX_CONTEXT = new DxContext();
    private final CfOptions CF_OPTIONS = new CfOptions();
    private final HashMap<String, Class<?>> cachedClassMap = new HashMap<>();

    private byte[] convertClassToDex(String name, byte[] classCode) throws IOException {
        String classFilePath = String.format("%s.class", name.replace('.', '/'));
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            DirectClassFile directClassFile = new DirectClassFile(classCode, classFilePath, true);
            directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
            DexFile dexFile = new DexFile(DEX_OPTIONS);
            dexFile.add(CfTranslator.translate(DX_CONTEXT, directClassFile, classCode, CF_OPTIONS, DEX_OPTIONS, dexFile));
            dexFile.writeTo(byteStream, null, true);
            return byteStream.toByteArray();
        }
    }

    private ClassLoader createDexClassLoader(byte[] dexByteArray) {
        ByteBuffer dexBuffer = ByteBuffer.wrap(dexByteArray);
        ClassLoader parentClassLoader = new FixClassloader(DexClassLoaderHelper.class.getClassLoader());
        return new InMemoryDexClassLoader(dexBuffer, parentClassLoader);
    }

    public Class<?> loadClassInternal(String name, byte[] code) {
        if (cachedClassMap.containsKey(name)) return cachedClassMap.get(name);
        try {
            byte[] dexByteArray = convertClassToDex(name, code);
            ClassLoader classLoader = createDexClassLoader(dexByteArray);
            Class<?> clazz = classLoader.loadClass(name);
            cachedClassMap.put(name, clazz);
            return clazz;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static final class FixClassloader extends ClassLoader {
        public FixClassloader(ClassLoader parent) {
            super(parent);
        }

        @Override
        public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }
    }
}
