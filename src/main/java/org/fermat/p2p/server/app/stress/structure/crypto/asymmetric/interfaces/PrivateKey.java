/*
 * @#PrivateKey.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.interfaces;


import java.security.interfaces.ECPrivateKey;


public interface PrivateKey extends ECPrivateKey{
	
	String toHexString();

}
