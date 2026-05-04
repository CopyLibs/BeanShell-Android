/*****************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one                *
 * or more contributor license agreements.  See the NOTICE file              *
 * distributed with this work for additional information                     *
 * regarding copyright ownership.  The ASF licenses this file                *
 * to you under the Apache License, Version 2.0 (the                         *
 * "License"); you may not use this file except in compliance                *
 * with the License.  You may obtain a copy of the License at                *
 *                                                                           *
 *     http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing,                *
 * software distributed under the License is distributed on an               *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                    *
 * KIND, either express or implied.  See the License for the                 *
 * specific language governing permissions and limitations                   *
 * under the License.                                                        *
 *****************************************************************************/

package bsh;

import bsh.org.objectweb.asm.ClassWriter;
import bsh.org.objectweb.asm.Opcodes;

/**
 * Factory for generating synthetic functional interfaces.
 * Uses a stable hash-based naming convention to support Android Dex limits.
 */
public class SyntheticInterfaceFactory implements Opcodes {
    private static final SyntheticInterfaceFactory INSTANCE = new SyntheticInterfaceFactory();
    private static final String GENERATED_PACKAGE_FQ = "bsh.generated.";

    public static SyntheticInterfaceFactory getInstance() {
        return INSTANCE;
    }

    public Class<?> getInterface(BshClassManager bcm, BSHFunctionType.SignatureInfo info) {
        String hash = Integer.toHexString(generateSignatureHash(info));
        String shortName = (info.receiver != null ? "Func_R_" : "Func_") + hash;
        String fqName = GENERATED_PACKAGE_FQ + shortName;

        Class<?> existing = bcm.classForName(fqName);
        if (existing != null) return existing;

        synchronized (this) {
            
            byte[] jvmBytecode = generateInterfaceBytecode(shortName, info);
            try {
                
                Class<?> genClass = bcm.loadGeneratedClass(fqName, jvmBytecode);
                bcm.cacheClassInfo(fqName, genClass);
                return genClass;
            } catch (Exception e) {
                Interpreter.debug("Failed to inject synthetic interface: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Generates a unique hash for the function signature.
     */
    private int generateSignatureHash(BSHFunctionType.SignatureInfo info) {
        
        int result = info.returnType.getName().hashCode();
        result = 31 * result + (info.receiver != null ? info.receiver.getName().hashCode() : 0);
        for (Class<?> p : info.params) {
            result = 31 * result + (p != null ? p.getName().hashCode() : 0);
        }
        return result & 0x7FFFFFFF; // Ensure positive
    }

    private byte[] generateInterfaceBytecode(String shortName, BSHFunctionType.SignatureInfo info) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        String internalName = "bsh/generated/" + shortName;

        /**
         * Select the appropriate marker interface.
         * Using $ for inner class internal name.
         */
        String marker = info.receiver != null 
            ? "bsh/SyntheticInterfaceFactory$BshReceiverLambdaMarker" 
            : "bsh/SyntheticInterfaceFactory$BshLambdaMarker";

        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, 
                 internalName, null, "java/lang/Object", new String[] { marker });

        // Build method descriptor: (Params)Return
        StringBuilder desc = new StringBuilder("(");
        if (info.receiver != null) {
            desc.append(BSHType.getTypeDescriptor(info.receiver));
        }
        for (Class<?> p : info.params) {
            desc.append(BSHType.getTypeDescriptor(p));
        }
        desc.append(")").append(BSHType.getTypeDescriptor(info.returnType));

        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "invoke", desc.toString(), null, null).visitEnd();
        cw.visitEnd();

        return cw.toByteArray();
    }
    
    /**
     * Internal marker interface for all synthetic BeanShell lambda functions.
     */
    public interface BshLambdaMarker {}

    /**
     * Internal marker interface specifically for lambdas with receivers.
     */
    public interface BshReceiverLambdaMarker extends BshLambdaMarker {}
}