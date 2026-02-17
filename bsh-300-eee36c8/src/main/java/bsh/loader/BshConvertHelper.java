package bsh.loader;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.command.dexer.DxContext;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.DexFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import dalvik.system.InMemoryDexClassLoader;

public class BshConvertHelper {
    private final DexOptions DEX_OPTIONS = new DexOptions();
    private final DxContext DX_CONTEXT = new DxContext();
    private final CfOptions CF_OPTIONS = new CfOptions();

    private byte[] convertClassToDex(String className, byte[] classBytes) throws IOException {
        String classFilePath = String.format("%s.class", className.replace('.', '/'));
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DirectClassFile classFile = new DirectClassFile(classBytes, classFilePath, true);
            classFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
            DexFile dexFile = new DexFile(DEX_OPTIONS);
            dexFile.add(CfTranslator.translate(DX_CONTEXT, classFile, classBytes, CF_OPTIONS, DEX_OPTIONS, dexFile));
            dexFile.writeTo(outputStream, null, true);
            return outputStream.toByteArray();
        }
    }

    public ClassLoader createCustomLoader(byte[] bytes, ClassLoader parentLoader) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new InMemoryDexClassLoader(buffer, parentLoader);
    }

    public ClassLoader convertClassToLoader(String className, byte[] classCode, ClassLoader parentLoader) throws IOException {
        byte[] dexBytes = convertClassToDex(className, classCode);
        return createCustomLoader(dexBytes, parentLoader);
    }

    public ClassLoader convertDexToLoader(String dexPath, ClassLoader parentLoader) throws IOException {
        File dexFile = new File(dexPath);
        byte[] dexBytes = Files.readAllBytes(dexFile.toPath());
        return createCustomLoader(dexBytes, parentLoader);
    }
}
