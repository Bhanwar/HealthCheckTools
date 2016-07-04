package com.snapdeal.healthcheck.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSHUtil {

	/** The Constant LOG. */
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(getClass());
	
    /** The connection. */
    private Connection connection = null;
    
    /** The key location. */
    private String user, host, keyLocation;
    
    /** The key. */
    private File key = null;
    
    /** The port. */
    private Integer port;

    /**
     * Inits the.
     *
     * @param user the user
     * @param host the host
     * @param port the port
     * @param keyLocation the key location
     */
    public SSHUtil(String user, String host, Integer port, String keyLocation) {
        this.user = user;
        this.host = host;
        this.port = port;
        this.keyLocation = keyLocation;
        LOG.debug("user : " + user + " host : " + host + " port : " + port + " key : " + keyLocation);
        ssh();
    }

    /**
     * Inits the.
     *
     * @param user the user
     * @param host the host
     * @param port the port
     */
    public SSHUtil(String user, String host, Integer port) {
        this.user = user;
        this.host = host;
        this.port = port;
        LOG.debug("user : " + user + " host : " + host + " port : " + port);
        ssh();
    }

    /**
     * Gets the default key location.
     *
     * @return the default key location
     */
    private String getDefaultKeyLocation() {
        String keyLocation = new StringBuilder(System.getProperty("user.home"))
                .append(File.separator).append(".ssh").append(File.separator).append("id_rsa").toString();
        return keyLocation;
    }

    /**
     * Ssh.
     */
    private void ssh() {

        LOG.debug("Establishing SSH connection");
        try {
            connection = new Connection(host, port);
            connection.connect();

            key = new File((keyLocation != null) ? keyLocation : getDefaultKeyLocation());

            boolean isAuthenticated = connection.authenticateWithPublicKey(
                    user, key, null);

            LOG.debug("Authentication successful : " + isAuthenticated);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");

        } catch (IOException e) {
        	LOG.error("Exception occured while performing SSH: " + e.getMessage(), e);
        }

    }

    /**
     * Open session.
     *
     * @return the session
     */
    private Session openSession() {
        Session session = null;
        try {
            session = connection.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return session;
    }

    /**
     * Execute command.
     *
     * @param command the command
     * @return the string buffer
     */
    public StringBuffer executeCommand(String command) {
        LOG.debug("Executing command : " + command);
        StringBuffer buffer = new StringBuffer();
        Session session = openSession();

        try {
            session.execCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream stdout = new StreamGobbler(session.getStdout());
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
        try {
            while (true) {
                String line = br.readLine();

                if (line == null)
                    break;
                if(!line.contains(user))
                	buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.debug("Received: " + buffer.toString());
        }
        closeSession(session);
        return buffer;
    }

    

    /**
     * Close session.
     *
     * @param session the session
     */
    private void closeSession(Session session) {
        session.close();
    }

    /**
     * Close connection.
     */
    public void closeConnection() {
    	if(connection != null) {
    		LOG.debug("Closing SSH connection");
    		connection.close();
    	}
    }
}