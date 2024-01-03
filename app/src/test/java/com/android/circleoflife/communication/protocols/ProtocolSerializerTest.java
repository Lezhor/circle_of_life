package com.android.circleoflife.communication.protocols;

import static org.junit.Assert.*;

import com.android.circleoflife.communication.pdus.sync.SendInstructionsPDU;
import com.android.circleoflife.communication.socket_communication.SocketCommunication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtocolSerializerTest {

    ProtocolSerializer serializer;
    Protocol protocol;

    SocketCommunication socketCommunication;

    @Before
    public void setUp() {
        protocol = Mockito.mock(Protocol.class);
        Mockito.when(protocol.getProtocolName()).thenReturn("MockedProtocol");
        Mockito.when(protocol.getVersion()).thenReturn("v1.0");
    }

    private void mockSocketCommunication(InputStream is, OutputStream os) {
        socketCommunication = Mockito.mock(SocketCommunication.class);
        Mockito.when(socketCommunication.connected()).thenReturn(true);
        Mockito.when(socketCommunication.getInputStream()).thenReturn(is);
        Mockito.when(socketCommunication.getOutputStream()).thenReturn(os);
    }

    @Test
    public void testDeserializeSendInstructionsPDUWithGeneric() {

        String[] array = new String[] {
                "SELECT * FROM LOL",
                "DELETE * FROM LOL",
        };

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(protocol.getProtocolName());
            dos.writeUTF(protocol.getVersion());
            dos.writeInt(SendInstructionsPDU.ID);
            dos.writeInt(array.length);
            for (String instruction : array) {
                dos.writeUTF(instruction);
            }

            mockSocketCommunication(new ByteArrayInputStream(os.toByteArray()), new ByteArrayOutputStream());
            serializer = new ProtocolSerializer(protocol, socketCommunication);

            SendInstructionsPDU pdu = serializer.deserialize(SendInstructionsPDU.class);
            assertArrayEquals(array, pdu.getInstructions());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeserializingWrongProtocol() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF("Wrong protocol");
            dos.writeUTF("Wrong version");
            dos.writeInt(2);

            mockSocketCommunication(new ByteArrayInputStream(os.toByteArray()), new ByteArrayOutputStream());
            serializer = new ProtocolSerializer(protocol, socketCommunication);

            assertThrows(IOException.class, () -> serializer.deserialize());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}