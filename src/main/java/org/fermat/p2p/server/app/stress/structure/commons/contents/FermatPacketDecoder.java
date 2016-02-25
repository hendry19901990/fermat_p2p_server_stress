/*
 * @#FermatPacketDecoder.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.commons.contents;

import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;

public class FermatPacketDecoder {
	
    /**
     * Decode the fermat packet
     *
     * @param fermatPacketEncode
     * @param privateKey
     * @return FermatPacket
     */
    public static FermatPacket decode(String fermatPacketEncode, String privateKey)  {

        /*
        * Decode the string into a json string representation
        */
        String fermatPacketJsonDecode = AsymmetricCryptography.decryptMessagePrivateKey(fermatPacketEncode, privateKey);

        /**
         * Construct the fermat packet object with the decode json string
         */
        return  FermatPacketCommunicationFactory.constructFermatPacketFromJsonString(fermatPacketJsonDecode);

    }

}
