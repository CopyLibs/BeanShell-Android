package bsh;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Constructor;
import java.lang.invoke.MethodType;

@SuppressWarnings("unchecked")
public abstract class BshLambda {

    protected final Node expressionNode;
    protected final Interpreter interpreter;

    protected BshLambda(Node expressionNode, Interpreter interpreter) {
        this.expressionNode = expressionNode;
        this.interpreter = interpreter;
    }

    protected static BshLambda fromLambdaExpression(Node expressionNode, NameSpace declaringNameSpace,
            Modifiers[] paramsModifiers, Class<?>[] paramsTypes,
            String[] paramsNames, Node bodyNode, Interpreter interpreter) {
        return new BshLambdaFromLambdaExpression(expressionNode, declaringNameSpace, paramsModifiers,
        paramsTypes, paramsNames, bodyNode, interpreter);
    }

    protected static BshLambda fromMethodReference(Node expressionNode, Object thisArg,
            String methodName, Interpreter interpreter) {
        return new BshLambdaFromMethodReference(expressionNode, thisArg, methodName, interpreter);
    }

    protected <T> T convertTo(Class<T> functionalInterface) throws UtilEvalError {
        InvocationHandler handler = (proxy, method, args) -> {
            // Handling Object methods
            if (method.getDeclaringClass() == Object.class) {
                switch (method.getName()) {
                    case "toString":
                        return "BshLambdaProxy[" + this + "]";
                    case "equals":
                        return proxy == args[0];
                    case "hashCode":
                        return System.identityHashCode(proxy);
                }
            }

            // Handling default methods
            if (method.isDefault()) {
                return invokeDefaultMethod(proxy, method, args);
            }

            // Execute script lambda
            try {
                Object result = invokeImpl(args != null ? args : Reflect.ZERO_ARGS);
                return castResult(result, method.getReturnType());
            } catch (TargetError te) {
                throw te.getTarget();
            } catch (Throwable e) {
                throw new RuntimeEvalError("Lambda execution error: " + e.getMessage(), expressionNode, null, e);
            }
        };

        return (T) Proxy.newProxyInstance(
                        functionalInterface.getClassLoader(),
                        new Class[]{functionalInterface},
                        handler
                );
    }

    private Object castResult(Object res, Class<?> returnType) throws UtilEvalError {
        if (returnType == Void.TYPE || returnType == void.class) return null;

        Object unwrapped = Primitive.unwrap(res);
        if (unwrapped == null) {
            if (returnType.isPrimitive()) {
                return Primitive.unwrap(Primitive.getDefaultValue(returnType));
            }
            return null;
        }

        return Primitive.unwrap(Types.castObject(unwrapped, returnType, Types.ASSIGNMENT));
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            return lookup.findSpecial(declaringClass, method.getName(),
                    MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                    declaringClass)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } catch (IllegalAccessException e) {
            Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class, int.class);
            constructor.setAccessible(true);
            return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                    .unreflectSpecial(method, declaringClass)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }
    }

    protected abstract Object invokeImpl(Object[] args)
            throws UtilEvalError, EvalError, TargetError;

    public static boolean isAssignable(Class<?> from, Class<?> to, int round) {
        return Types.isFunctionalInterface(to);
    }

    private static class BshLambdaFromLambdaExpression extends BshLambda {
        private final NameSpace declaringNameSpace;
        private final String[] paramsNames;
        private final Node bodyNode;

        public BshLambdaFromLambdaExpression(Node expressionNode, NameSpace declaringNameSpace,
                Modifiers[] paramsModifiers, Class<?>[] paramsTypes,
                String[] paramsNames, Node bodyNode, Interpreter interpreter) {
            super(expressionNode, interpreter);
            this.declaringNameSpace = declaringNameSpace;
            this.paramsNames = paramsNames;
            this.bodyNode = bodyNode;
        }

        @Override
        protected Object invokeImpl(Object[] args) throws UtilEvalError, EvalError, TargetError {
            NameSpace localNS = new NameSpace(declaringNameSpace, "LambdaContext");
            for (int i = 0; i < paramsNames.length; i++) {
                if (i < args.length) localNS.setVariable(paramsNames[i], args[i], false);
            }

            Object res = bodyNode.eval(new CallStack(localNS), interpreter);
            return (res instanceof ReturnControl) ? ((ReturnControl) res).value : res;
        }
    }

    private static class BshLambdaFromMethodReference extends BshLambda {
        private final Object thisArg;
        private final String methodName;

        public BshLambdaFromMethodReference(Node expressionNode, Object thisArg, String methodName, Interpreter interpreter) {
            super(expressionNode, interpreter);
            this.thisArg = thisArg;
            this.methodName = methodName;
        }

        @Override
        protected Object invokeImpl(Object[] args) throws UtilEvalError, EvalError, TargetError {
            return Reflect.invokeObjectMethod(thisArg, methodName, args, interpreter, new CallStack(), expressionNode);
        }
    }
}