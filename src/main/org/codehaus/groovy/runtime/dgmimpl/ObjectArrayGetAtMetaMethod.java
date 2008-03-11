package org.codehaus.groovy.runtime.dgmimpl;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;

/**
 * Support the subscript operator for an Array.
 *
 */
public class ObjectArrayGetAtMetaMethod extends ArrayGetAtMetaMethod {
    private static final CachedClass OBJECT_ARR_CLASS = ReflectionCache.OBJECT_ARRAY_CLASS;

    public Class getReturnType() {
        return Object.class;
    }

    public final CachedClass getDeclaringClass() {
        return OBJECT_ARR_CLASS;
    }

    public Object invoke(Object object, Object[] arguments) {
        final Object[] objects = (Object[]) object;
        return objects[normaliseIndex(((Integer) arguments[0]).intValue(), objects.length)];
    }

    public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
        if (!(args [0] instanceof Integer))
          return PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args);
        else
            return new PojoMetaMethodSite(site, metaClass, metaMethod, params) {
                public Object invoke(Object receiver, Object[] args) {
                    final Object[] objects = (Object[]) receiver;
                    return objects[normaliseIndex(((Integer) args[0]).intValue(), objects.length)];
                }

                public Object invokeBinop(Object receiver, Object arg) {
                    final Object[] objects = (Object[]) receiver;
                    return objects[normaliseIndex(((Integer) arg).intValue(), objects.length)];
                }
            };
    }
}
