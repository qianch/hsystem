package org.hibernate.transform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qianchen
 * @date 2022/01/21
 */

public class AliasToEntityMapResultTransformer extends AliasedTupleSubsetResultTransformer {
    public static final AliasToEntityMapResultTransformer INSTANCE = new AliasToEntityMapResultTransformer();

    private AliasToEntityMapResultTransformer() {
    }

    /**
     * 重写Hibernate中返回key-value格式，key统一返回大写
     *
     * @param tuple
     * @param aliases
     * @return
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map result = new HashMap(tuple.length);
        for (int i = 0; i < tuple.length; ++i) {
            String alias = aliases[i];
            if (alias != null) {
                result.put(alias.toUpperCase(), tuple[i]);
            }
        }
        return result;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
