/*
 * @#FermatPacketEncoder.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.commons.contents;

import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;

public class FermatPacketEncoder {
	
    /**
     * Encode the fermat packet
     *
     * @param fermatPacket
     * @return string encode
     */
    public static String encode(FermatPacket fermatPacket){

        /*
         * Convert the fermatPacket to json string representation and encrypted whit the public key of the receiver
         */
        return AsymmetricCryptography.encryptMessagePublicKey(fermatPacket.toJson(), fermatPacket.getDestination());

    }

}
