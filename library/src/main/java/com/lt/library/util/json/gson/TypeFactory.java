package com.lt.library.util.json.gson;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeFactory {

    public static Type $parameterized(Type ownerType, Type rawType, Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);
    }

    public static Type $set(Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Set.class, typeArguments);
    }

    public static Type $list(Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, List.class, typeArguments);
    }

    public static Type $map(Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, typeArguments);
    }

    public static Type $array(Type type) {
        return $Gson$Types.arrayOf(type);
    }

    public static Type $subTypeOf(Type type) {
        return $Gson$Types.subtypeOf(type);
    }

    public static Type $superTypeOf(Type type) {
        return $Gson$Types.supertypeOf(type);
    }
}
