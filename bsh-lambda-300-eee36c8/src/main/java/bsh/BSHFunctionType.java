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

import java.util.ArrayList;
import java.util.List;

/**
 * AST node for handling High-Order Function type signatures.
 * Syntax: (ReceiverType.(ParamTypes) -> ReturnType)
 */
public class BSHFunctionType extends SimpleNode {
    public BSHFunctionType(int id) {
        super(id);
    }

    /**
     * Resolves this signature to a unique synthetic Functional Interface Class.
     */
    public Class<?> getType(CallStack callstack, Interpreter interpreter) throws EvalError {
        SignatureInfo info = collectSignatureInfo(callstack, interpreter);
        BshClassManager bcm = interpreter.getClassManager();
        
        // Retrieve the generated interface from the factory
        return SyntheticInterfaceFactory.getInstance().getInterface(bcm, info);
    }

    /**
     * Helper to extract signature components from child nodes.
     */
    public SignatureInfo collectSignatureInfo(CallStack callstack, Interpreter interpreter) throws EvalError {
        Class<?> receiver = null;
        List<Class<?>> params = new ArrayList<>();
        Class<?> returnType = Void.TYPE;

        int numChildren = jjtGetNumChildren();
        for (int i = 0; i < numChildren; i++) {
            Node child = jjtGetChild(i);
            if (child instanceof BSHReceiverType) {
                receiver = ((BSHReceiverType) child).getType(callstack, interpreter);
            } else if (child instanceof BSHType) {
                params.add(((BSHType) child).getType(callstack, interpreter));
            } else if (child instanceof BSHReturnType) {
                returnType = ((BSHReturnType) child).evalReturnType(callstack, interpreter);
            }
        }
        return new SignatureInfo(receiver, params, returnType);
    }

    /**
     * Structured data for the function signature.
     */
    public static class SignatureInfo {
        public final Class<?> receiver;
        public final List<Class<?>> params;
        public final Class<?> returnType;

        public SignatureInfo(Class<?> r, List<Class<?>> p, Class<?> rt) {
            this.receiver = r;
            this.params = p;
            this.returnType = rt;
        }
    }
}