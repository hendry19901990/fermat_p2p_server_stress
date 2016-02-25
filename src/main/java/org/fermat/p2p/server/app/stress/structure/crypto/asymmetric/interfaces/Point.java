/*
 * @#Point.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.interfaces;

import java.math.BigInteger;

public interface Point {

	BigInteger getX();
	BigInteger getY();
	String toUncompressedString();
	String toCompressedString();
	boolean equals(Object obj);
	int hashCode();
	
}
