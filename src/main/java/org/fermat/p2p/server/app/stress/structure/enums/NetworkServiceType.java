/*
 * @#NetworkServiceType.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums;

import org.fermat.p2p.server.app.stress.structure.exceptions.InvalidParameterException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatEnum;

public enum NetworkServiceType implements FermatEnum{

	
	/*
     * For doing the code more readable, please keep the elements in the Enum sorted alphabetically.
     * Network Service prefix or Type sufix are not necessary having in count that the name of the plugin is NETWORK SERVICE TYPE.,
     */

    ASSET_USER_ACTOR                 ("AUANS"),
    ASSET_ISSUER_ACTOR               ("AIANS"),
    ASSET_REDEEM_POINT_ACTOR         ("ARPANS"),
    ASSET_TRANSMISSION               ("ASS_TRANS"),
    CHAT                             ("CHAT"),
    CRYPTO_ADDRESSES                 ("CADD"),
    CRYPTO_BROKER                    ("CRBR"),
    CRYPTO_CUSTOMER                  ("CRCU"),
    CRYPTO_PAYMENT_REQUEST           ("CPR"),
    CRYPTO_TRANSMISSION              ("CRY_TRANS"),
    INTRA_USER                       ("INT_USR"),
    TEMPLATE                         ("TEMP"),
    TRANSACTION_TRANSMISSION         ("TRTX"),
    NEGOTIATION_TRANSMISSION         ("NGTR"),
    UNDEFINED                        ("UNDEF"),

    ;

    private String code;

    NetworkServiceType(String code){
        this.code = code;
    }

    public static NetworkServiceType getByCode(final String code) throws InvalidParameterException {

        switch (code){

            case "AUANS"     : return ASSET_USER_ACTOR;
            case "AIANS"     : return ASSET_ISSUER_ACTOR;
            case "ARPANS"    : return ASSET_REDEEM_POINT_ACTOR;
            case "ASS_TRANS" : return ASSET_TRANSMISSION;
            case "CADD"      : return CRYPTO_ADDRESSES;
            case "CHAT"      : return CHAT;
            case "CRBR"      : return CRYPTO_BROKER;
            case "CRCU"      : return CRYPTO_CUSTOMER;
            case "CPR"       : return CRYPTO_PAYMENT_REQUEST;
            case "CRY_TRANS" : return CRYPTO_TRANSMISSION;
            case "INT_USR"   : return INTRA_USER;
            case "TEMP"      : return TEMPLATE;
            case "TRTX"      : return TRANSACTION_TRANSMISSION;
            case "NGTR"      : return NEGOTIATION_TRANSMISSION;
            case "UNDEF"     : return UNDEFINED;

            default: throw new InvalidParameterException(
                    "Code received: "+code,
                    "The code received is not valid for NetworkServiceType enum."
            );
        }

    }

    @Override
    public String getCode() {
        return this.code;
    }
	
}
