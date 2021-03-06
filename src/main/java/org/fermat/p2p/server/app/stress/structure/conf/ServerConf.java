/*
 * @#ServerConf.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.conf;

public class ServerConf {
	
	/**
     * Represent the SERVER_IP in the production environment
     */
    public static final String SERVER_IP_PRODUCTION = "52.35.64.221";

    /**
     * Represent the SERVER_IP in the developer environment
     */
    public static final String SERVER_IP_DEVELOPER_AWS = "52.37.225.161";

    /**
     * Represent the SERVER_IP in the local environment
     */
    public static final String SERVER_IP_DEVELOPER_LOCAL = "190.198.33.125";

    /**
     * Represents the value of DISABLE_CLIENT
     */
    public static final Boolean DISABLE_CLIENT = Boolean.TRUE;

    /**
     * Represents the value of ENABLE_CLIENT
     */
    public static final Boolean ENABLE_CLIENT = Boolean.FALSE;

    /**
     * Represent the WS_PROTOCOL
     */
    public static final String WS_PROTOCOL = "ws://";

    /**
     * Represent the WEB_SOCKET_CONTEXT_PATH
     */
    public static final String WEB_SOCKET_CONTEXT_PATH = "/fermat/ws/";

    /**
     * Represent the HTTP_PROTOCOL
     */
    public static final String HTTP_PROTOCOL = "http://";
    
    /**
     * Represent the DEFAULT_PORT
     */
    public static final int DEFAULT_PORT = 9090;

    /**
     * Represent the WEB_SERVICE_PORT
     */
    public static final int WEB_SERVICE_PORT = 8080;

}
