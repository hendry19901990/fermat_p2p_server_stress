/*
 * @#Curve.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.interfaces;

import java.math.BigInteger;
import java.security.spec.ECParameterSpec;


public interface Curve {

	BigInteger getA();
	BigInteger getB();
	Point getG();
	int getH();
	BigInteger getN();
	BigInteger getP();
	ECParameterSpec getParams();
	
}
