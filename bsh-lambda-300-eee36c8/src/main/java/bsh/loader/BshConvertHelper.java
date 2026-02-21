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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dalvik.system.InMemoryDexClassLoader;

public class BshConvertHelper {
    private byte[] convertClassToDex(String className, byte[] classBytes) throws IOException {
        DexOptions dexOptions = new DexOptions();
        CfOptions cfOptions = new CfOptions();
        DxContext dxContext = new DxContext();
        String classFilePath = String.format("%s.class", className.replace('.', '/'));
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DirectClassFile classFile = new DirectClassFile(classBytes, classFilePath, true);
            classFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
            DexFile dexFile = new DexFile(dexOptions);
            dexFile.add(CfTranslator.translate(dxContext, classFile, classBytes, cfOptions, dexOptions, dexFile));
            dexFile.writeTo(outputStream, null, true);
            return outputStream.toByteArray();
        }
    }

    private byte[] convertJarToDex(String jarPath) throws IOException {
        DexOptions dexOptions = new DexOptions();
        CfOptions cfOptions = new CfOptions();
        DxContext dxContext = new DxContext();
        DexFile dexFile = new DexFile(dexOptions);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(jarPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.endsWith(".class") && !entryName.startsWith("META-INF/")) {
                    byte[] classBytes = DataUtil.readAllBytes(zis);
                    DirectClassFile classFile = new DirectClassFile(classBytes, entryName, true);
                    classFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
                    dexFile.add(CfTranslator.translate(dxContext, classFile, classBytes, cfOptions, dexOptions, dexFile));
                }
            }
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
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

    public ClassLoader convertJarToLoader(String jarPath, ClassLoader parentLoader) throws IOException {
        byte[] dexBytes = convertJarToDex(jarPath);
        return createCustomLoader(dexBytes, parentLoader);
    }
}
