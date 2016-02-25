/*
 * @#NetworkServices.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums;

import org.fermat.p2p.server.app.stress.structure.exceptions.InvalidParameterException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatEnum;

public enum NetworkServices implements FermatEnum{
	
	 /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    BANK_NOTES               ("BNOTES"),
    CRYPTO_ADDRESSES         ("CRYPTADD"),
    INTRA_USER               ("IUS"),
    MONEY                    ("MONEY"),
    TEMPLATE                 ("TEMPLATE"),
    TRANSACTION_TRANSMISSION ("TRANSTX"),
    NEGOTIATION_TRANSMISSION ("NEGOTRS"),
    UNDEFINED                ("UNDEF"),
    WALLET_COMMUNITY         ("WALLCOMM"),
    WALLET_RESOURCES         ("WALLRES"),
    WALLET_STORE             ("WALLSTO")

    ;

    private final String code;

    NetworkServices(String code) {
        this.code = code;
    }

    public static NetworkServices getByCode(String code) throws InvalidParameterException {

        switch (code) {
            case "BNOTES":   return NetworkServices.BANK_NOTES;
            case "CRYPTADD": return NetworkServices.CRYPTO_ADDRESSES;
            case "IUS":      return NetworkServices.INTRA_USER;
            case "MONEY":    return NetworkServices.MONEY;
            case "TEMPLATE": return NetworkServices.TEMPLATE;
            case "UNDEF":    return NetworkServices.UNDEFINED;
            case "TRANSTX":  return NetworkServices.TRANSACTION_TRANSMISSION;
            case "NEGOTRS":  return NetworkServices.NEGOTIATION_TRANSMISSION;
            case "WALLCOMM": return NetworkServices.WALLET_COMMUNITY;
            case "WALLRES":  return NetworkServices.WALLET_RESOURCES;
            case "WALLSTO":  return NetworkServices.WALLET_STORE;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "The recieved code is not valid for the NetworkServices enum"
                );
        }
    }

    @Override
    public String getCode() { return this.code; }


}
