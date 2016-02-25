/*
 * @#Signature.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.interfaces;


import java.math.BigInteger;

public interface Signature {
	String SIGNATURE_SEPARATOR = " ";
	int SIGNATURE_SEPARATOR_PARTS = 2;
	BigInteger getR();
	BigInteger getS();
	boolean verifyMessageSignature(final BigInteger messageHash, final PublicKey publicKey);
} 