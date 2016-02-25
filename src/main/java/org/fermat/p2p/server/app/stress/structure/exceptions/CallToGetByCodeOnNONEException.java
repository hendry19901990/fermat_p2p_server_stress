/*
 * @#CallToGetByCodeOnNONEException.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;



public class CallToGetByCodeOnNONEException extends FermatException{

	private static final long serialVersionUID = 1L;

	/**
     * This is the constructor that every inherited FermatException must implement
     *
     * @param message        the short description of the why this exception happened, there is a public static constant called DEFAULT_MESSAGE that can be used here
     * @param cause          the exception that triggered the throwing of the current exception, if there are no other exceptions to be declared here, the cause should be null
     * @param context        a String that provides the values of the variables that could have affected the exception
     * @param possibleReason an explicative reason of why we believe this exception was most likely thrown
     */
    public CallToGetByCodeOnNONEException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
	
}
