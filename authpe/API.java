package authpe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import redstonelamp.plugin.PluginBase;

public class API {
	private PluginBase plugin;
	private Authenticator authenticator;
	
	public API(PluginBase plugin) {
		this.plugin = plugin;
		this.authenticator = new Authenticator(plugin, this);
	}
	
	public Authenticator getAuthenticator() {
		return authenticator;
	}
	
	public String hash(String string) {
		MessageDigest md;
		String hash = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(string.getBytes());
			hash = new String(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
}
