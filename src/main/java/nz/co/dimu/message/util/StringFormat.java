package nz.co.dimu.message.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huanghao on 2016-8-7.
 */
public class StringFormat {
    public static String format(String messagePattern, Object... argArray) {
        if(messagePattern == null) {
            return messagePattern;
        } else if(argArray == null) {
            return messagePattern;
        } else {
            int i = 0;
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

            int L;
            for(L = 0; L < argArray.length; ++L) {
                int j = messagePattern.indexOf("{}", i);
                if(j == -1) {
                    if (i == 0) {
                        return messagePattern;
                    }
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return sbuf.toString();
                }

                if(isEscapedDelimeter(messagePattern, j)) {
                    if(!isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append('{');
                        i = j + 1;
                    } else {
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                        i = j + 2;
                    }
                } else {
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                    i = j + 2;
                }
            }

            sbuf.append(messagePattern, i, messagePattern.length());
            return sbuf.toString();
        }
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if(delimeterStartIndex == 0) {
            return false;
        } else {
            char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
            return potentialEscape == 92;
        }
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == 92;
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if(o == null) {
            sbuf.append("null");
        } else {
            if(!o.getClass().isArray()) {
                safeObjectAppend(sbuf, o);
            } else if(o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[])((boolean[])o));
            } else if(o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[])((byte[])o));
            } else if(o instanceof char[]) {
                charArrayAppend(sbuf, (char[])((char[])o));
            } else if(o instanceof short[]) {
                shortArrayAppend(sbuf, (short[])((short[])o));
            } else if(o instanceof int[]) {
                intArrayAppend(sbuf, (int[])((int[])o));
            } else if(o instanceof long[]) {
                longArrayAppend(sbuf, (long[])((long[])o));
            } else if(o instanceof float[]) {
                floatArrayAppend(sbuf, (float[])((float[])o));
            } else if(o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[])((double[])o));
            } else {
                objectArrayAppend(sbuf, (Object[])((Object[])o), seenMap);
            }

        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String t = o.toString();
            sbuf.append(t);
        } catch (Throwable var3) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
            var3.printStackTrace();
            sbuf.append("[FAILED toString()]");
        }

    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if(!seenMap.containsKey(a)) {
            seenMap.put(a, (Object)null);
            int len = a.length;

            for(int i = 0; i < len; ++i) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if(i != len - 1) {
                    sbuf.append(", ");
                }
            }

            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }

        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;

        for(int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if(i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }
}
