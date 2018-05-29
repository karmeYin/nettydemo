package com.yin.study.chapter14.Test;

import io.netty.buffer.ByteBuf;
import org.junit.platform.commons.util.StringUtils;

public class Assert {
    public static void assertTrue(boolean b) {
        assert(b);
    }

    public static void assertNotNull(ByteBuf encoded) {
        assert null!=encoded;
    }

    public static void assertEquals(Object a, Object b) {
        System.out.println(a.toString()+"="+b.toString());
        assert a.toString().equals(b.toString());
    }

    public static void assertArrayEquals(byte[] bytes, byte[] data) {
        assert bytes.length==data.length;
    }

    public static void assertFalse(boolean readable) {
        assert !readable;
    }

    public static void assertNull(Object o) {
        assert null==o;
    }
}
