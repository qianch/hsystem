package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;
import com.bluebirdme.mes.core.valid.annotations.In;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class InValidator implements Validator {
    public InValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{Integer.class, Boolean.class, String.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        In in = (In) annotation;
        boolean inside = false;
        if (t != null) {
            int i;
            if (t instanceof Integer) {
                Integer[] x = new Integer[in.i().length];
                if (in.i().length != 0) {
                    for (i = 0; i < in.i().length; ++i) {
                        x[i] = in.i()[i];
                        if (in.i()[i] == (Integer) t) {
                            inside = true;
                        }
                    }
                }

                if (!inside) {
                    throw new ValidException(desc + "只能为[" + toString(x) + "]");
                }
            } else if (t instanceof Boolean) {
                if (in.b().length != 0) {
                    Boolean[] booleans = new Boolean[in.i().length];

                    for (i = 0; i < in.b().length; ++i) {
                        booleans[i] = in.b()[i];
                        if (in.b()[i] == (Boolean) t) {
                            inside = true;
                        }
                    }

                    if (!inside) {
                        throw new ValidException(desc + "只能为[" + toString(booleans) + "]");
                    }
                }
            } else if (in.s().length != 0) {
                String[] insides = new String[in.i().length];

                for (i = 0; i < in.s().length; ++i) {
                    insides[i] = in.s()[i];
                    if (in.s()[i].equals(t)) {
                        inside = true;
                    }
                }

                if (!inside) {
                    throw new ValidException(desc + "只能为[" + toString(insides) + "]");
                }
            }
        }

    }

    public static <T> String toString(T[] array) {
        StringBuffer sb = new StringBuffer("");
        if (array != null && array.length != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int i = 0;
            Object[] arr$ = array;
            int len$ = array.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                T t = (T) arr$[i$];
                if (!(t instanceof Integer) && !(t instanceof Long) && !(t instanceof Short) && !(t instanceof Boolean) && !(t instanceof Byte) && !(t instanceof String) && !(t instanceof Character) && !(t instanceof Float) && !(t instanceof Double) && !(t instanceof Date)) {
                    try {
                        throw new Exception("Array.toString()方法仅支持Integer,Short,Long,Boolean,Byte,String,Character,Float,Double,Date");
                    } catch (Exception var9) {
                        var9.printStackTrace();
                        return sb.toString();
                    }
                }

                if (t instanceof Date) {
                    sb.append(i++ == 0 ? sdf.format(t) : "," + sdf.format(t));
                } else {
                    sb.append(i++ == 0 ? t.toString() : "," + t.toString());
                }
            }

            return sb.toString();
        } else {
            return sb.toString();
        }
    }
}
