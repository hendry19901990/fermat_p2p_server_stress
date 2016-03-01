/*
 * @#PlatformComponentType.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums;

import org.fermat.p2p.server.app.stress.structure.interfaces.FermatEnum;

public enum PlatformComponentType implements FermatEnum {
	

    // Definition types

    ACTOR_ASSET_ISSUER          ("ACT_ASI"),
    ACTOR_ASSET_REDEEM_POINT    ("ACT_ASR"),
    ACTOR_ASSET_USER            ("ACT_ASU"),
    ACTOR_CRYPTO_BROKER         ("ACT_CRB"),
    ACTOR_CRYPTO_CUSTOMER       ("ACT_CUS"),
    ACTOR_INTRA_USER            ("ACT_IU"),
    ACTOR_NETWORK_SERVICE       ("ANS"),
    COMMUNICATION_CLOUD_CLIENT  ("COM_CLD_CLI"),
    COMMUNICATION_CLOUD_SERVER  ("COM_CLD_SER"),
    NETWORK_SERVICE             ("NS"),;

    /**
     * Represent the code
     */
    private final String code;

    /**
     * Constructor whit parameter
     *
     * @param code
     */
    PlatformComponentType(final String code){
        this.code = code;
    }

    /**
     * Return the PlatformComponentType represented by the code pass as parameter
     *
     * @param code
     * @return PlatformComponentType
     */
    public static PlatformComponentType getByCode(final String code){

        switch (code){

            case "ACT_ASI"     : return ACTOR_ASSET_ISSUER;
            case "ACT_ASR"     : return ACTOR_ASSET_REDEEM_POINT;
            case "ACT_ASU"     : return ACTOR_ASSET_USER;
            case "ACT_CRB"     : return ACTOR_CRYPTO_BROKER;
            case "ACT_CUS"     : return ACTOR_CRYPTO_CUSTOMER;
            case "ACT_IU"      : return ACTOR_INTRA_USER;
            case "ANS"         : return ACTOR_NETWORK_SERVICE;
            case "COM_CLD_CLI" : return COMMUNICATION_CLOUD_CLIENT;
            case "COM_CLD_SER" : return COMMUNICATION_CLOUD_SERVER;
            case "NS"          : return NETWORK_SERVICE;

            default: throw new IllegalArgumentException();
        }
    }

    /**
     * Return the code representation
     *
     * @return String
     */
    @Override
    public String getCode() {
        return this.code;
    }

}
