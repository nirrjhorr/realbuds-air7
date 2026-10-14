package com.realbuds.app.proto

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProtocolTest {

    @Test
    fun testAncZeroAliasAndWireMapping() {
        // Wire 0 and 1 must both resolve deterministically to OFF
        assertEquals(AncMode.OFF, AncMode.fromWire(0))
        assertEquals(AncMode.OFF, AncMode.fromWire(1))
        assertEquals(AncMode.OFF, AncMode.byWire(0))
        assertEquals(AncMode.OFF, AncMode.byWire(1))

        // Genuine wire values
        assertEquals(AncMode.TRANSPARENCY, AncMode.fromWire(2))
        assertEquals(AncMode.ANC_MAX, AncMode.fromWire(8))
        assertEquals(AncMode.ANC_MODERATE, AncMode.fromWire(16))
        assertEquals(AncMode.ANC_SMART, AncMode.fromWire(32))

        // Buds Air 7 levels
        assertEquals(
            listOf(AncMode.ANC_SMART, AncMode.ANC_MAX, AncMode.ANC_MODERATE),
            AncMode.ancLevels
        )
    }

    @Test
    fun testLinkFrameWrapAndParseSingle() {
        val payload = byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
        val wrapped = LinkFrame.wrapSingle(payload)

        assertTrue(wrapped.isNotEmpty())
        assertEquals(Protocol.SOF, wrapped[0])

        val parsed = LinkFrame.parseOne(wrapped)
        assertNotNull(parsed)
        assertEquals(LinkFrame.FSN_SINGLE, parsed!!.fsn)
        assertArrayEquals(payload, parsed.payload)
    }

    @Test
    fun testLinkFrameMalformedSafeguard() {
        // Short buffer
        assertNull(LinkFrame.parseOne(byteArrayOf(0xAA.toByte(), 0x01)))

        // Non-SOF leading byte
        assertNull(LinkFrame.parseOne(byteArrayOf(0xBB.toByte(), 0x01, 0x02, 0x03, 0x04)))

        // Malformed varint (all continuation bits set without end)
        val truncatedVarint = byteArrayOf(0xAA.toByte(), 0x80.toByte(), 0x80.toByte())
        assertNull(LinkFrame.parseOne(truncatedVarint))

        // Truncated body where declared length exceeds buffer
        val truncatedBody = byteArrayOf(0xAA.toByte(), 0x20.toByte(), 0x00, 0x00, 0x01)
        assertNull(LinkFrame.parseOne(truncatedBody))
    }

    @Test
    fun testPacketEncodingAndDecoding() {
        val payload = byteArrayOf(0x10, 0x20, 0x30)
        val packet = Packet(commandId = 0x0106, transferId = 42, payload = payload)

        val bytes = packet.toBytes()
        assertEquals(5 + payload.size, bytes.size)

        val parsed = Packet.parse(bytes)
        assertNotNull(parsed)
        assertEquals(0x0106, parsed!!.commandId)
        assertEquals(42, parsed.transferId)
        assertArrayEquals(payload, parsed.payload)
    }

    @Test
    fun testFeatureAirSevenBoundaries() {
        val expected = listOf(6, 4, 5, 11, 8, 17, 24, 9, 26, 12, 29, 27, 38)
        assertEquals(expected, Feature.airSeven)

        // Ensure unsupported IDs are filtered
        assertTrue(Feature.airSeven.contains(Feature.SPATIAL_AUDIO.id))
        assertTrue(Feature.airSeven.contains(Feature.GAME_MODE.id))
        assertTrue(Feature.airSeven.contains(Feature.AUTO_PLAY_PAUSE.id))
        assertTrue(Feature.airSeven.contains(Feature.HIGH_VOLUME_MODE.id))
    }
}
