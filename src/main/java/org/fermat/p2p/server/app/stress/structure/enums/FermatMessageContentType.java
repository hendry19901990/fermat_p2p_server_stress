/*
 * @#FermatMessageContentType.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums;

import org.fermat.p2p.server.app.stress.structure.exceptions.InvalidParameterException;


public enum FermatMessageContentType {

	/**
     * The enum values
     */
    TEXT  ("TXT"),
    BYTE  ("BYTE"),
    IMAGE ("IMG"),
    VIDEO ("VIDEO");

    /**
     * Represent the code of the message status
     */
    private final String code;

    /**
     * Constructor whit parameter
     *
     * @param code the valid code
     */
    FermatMessageContentType(String code) {
        this.code = code;
    }

    /**
     * Return a string code
     *
     * @return String that represent of the message status
     */
    public String getCode()   { return this.code ; }

    /**
     * Return the enum by the code
     *
     * @param code the valid code
     * @return MessagesTypes enum
     * @throws InvalidParameterException error with is no a valid code
     */
    public static FermatMessageContentType getByCode(String code) throws InvalidParameterException {

        switch (code) {
            case "TXT":
                return FermatMessageContentType.TEXT;
            case "BYTE":
                return FermatMessageContentType.BYTE;
            case "IMG":
                return FermatMessageContentType.IMAGE;
            case "VIDEO":
                return FermatMessageContentType.VIDEO;
        }

        /**
         * If we try to convert am invalid string.
         */
        throw new InvalidParameterException(code);
    }

    /**
     * (non-Javadoc)
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return getCode();
    }
	
}
