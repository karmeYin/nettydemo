package com.yin.study.chapter14.Test;

import com.yin.study.chapter14.MemcachedRequest;
import com.yin.study.chapter14.MemcachedRequestEncoder;
import com.yin.study.chapter14.Opcode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

public class MemcachedRequestEncoderTest {
    @Test
    public void testMemcachedRequestEncoder() {
        MemcachedRequest request =
                new MemcachedRequest(Opcode.SET, "key1", "value1");
        EmbeddedChannel channel = new EmbeddedChannel(
                new MemcachedRequestEncoder());
        Assert.assertTrue(channel.writeOutbound(request));
        ByteBuf encoded = (ByteBuf) channel.readOutbound();
        Assert.assertNotNull(encoded);

        Assert.assertEquals(request.magic(), encoded.readByte());
        Assert.assertEquals(request.opCode(), encoded.readByte());
        Assert.assertEquals(4, encoded.readShort());
        Assert.assertEquals((byte) 0x08, encoded.readByte());
        Assert.assertEquals((byte) 0, encoded.readByte());
        Assert.assertEquals(0, encoded.readShort());
        Assert.assertEquals(4 + 6 + 8, encoded.readInt());
        Assert.assertEquals(request.id(), encoded.readInt());
        Assert.assertEquals(request.cas(), encoded.readLong());
        Assert.assertEquals(request.flags(), encoded.readInt());
        Assert.assertEquals(request.expires(), encoded.readInt());
        byte[] data = new byte[encoded.readableBytes()];
        encoded.readBytes(data);
        try {
            Assert.assertArrayEquals((request.key() + request.body()).getBytes("UTF-8"), data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(encoded.isReadable());
        Assert.assertFalse(channel.finish());
        Assert.assertNull(channel.readInbound());
    }
}
