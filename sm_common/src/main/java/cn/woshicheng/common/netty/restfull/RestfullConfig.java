package cn.woshicheng.common.netty.restfull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;

import org.springframework.beans.factory.annotation.Value;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class RestfullConfig {
	private Properties properties;

	// http
	public static String iomode;
	public static String restfull_socket_port;
	public static String restfull_socket_rootPath;

	// https
	public String keyStoreType;
	public String sslKeyFileUrl;
	public String keyPwd;
	public String keyStorePwd;
	public String algorithm;

	public final static RestfullConfig initfig(String configResource) throws IOException {
		RestfullConfig config = new RestfullConfig();
		try (InputStream stream = getConfigStream(configResource)) {
			config.properties = new Properties();
			config.properties.load(stream);
			config.iomode = config.properties.getProperty("iomode");
			config.restfull_socket_port = config.properties.getProperty("restfull.socket.port");
			config.restfull_socket_rootPath = config.properties.getProperty("restfull.socket.rootPath");
			// ssl/https
			config.keyStoreType = config.properties.getProperty("ssl_keyStoreType");
			config.keyStoreType = config.properties.getProperty("ssl_sslKeyFileUrl");
			config.keyStoreType = config.properties.getProperty("ssl_keyPwd");
			config.keyStoreType = config.properties.getProperty("ssl_keyStorePwd");
			config.keyStoreType = config.properties.getProperty("ssl_algorithm");
		}
		;
		return config;
	}

	/**
	 * get properties stream
	 * 
	 * @return
	 */
	private static InputStream getConfigStream(String resource) {
		InputStream configStream = RestfullConfig.class.getResourceAsStream(resource);
		if (configStream == null)
			configStream = RestfullConfig.class.getClassLoader().getParent().getResourceAsStream(resource);
		if (configStream == null)
			throw new RestException("Cannot find " + resource + " !!!");
		return configStream;
	}

	public SslContext getSslContext() throws Exception {
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(new FileInputStream(sslKeyFileUrl), keyPwd.toCharArray());
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
		keyManagerFactory.init(keyStore, keyStorePwd.toCharArray());
		return SslContextBuilder.forServer(keyManagerFactory).build();
	}
}
