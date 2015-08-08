package authpe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import redstonelamp.Player;
import redstonelamp.plugin.PluginBase;

public class Authenticator {
	private PluginBase plugin;
	private API api;
	private File authDir;
	private File tempDir;
	
	public Authenticator(PluginBase plugin, API api) {
		this.plugin = plugin;
		this.api = api;
		this.authDir = new File(plugin.getDataFolder() + "/players/");
		if(!this.authDir.isDirectory())
			this.authDir.mkdirs();
		this.tempDir = new File(this.authDir + "cache/");
	}
	
	public File getAuthDir() {
		return this.authDir;
	}
	
	public File getTempDir() {
		return this.tempDir;
	}
	
	public void createAccount(Player player, String pass) {
		if(!(new File(this.authDir + "/" + player.getIdentifier() + ".acnt").isFile())) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(this.authDir + "/" + player.getIdentifier() + ".acnt", "UTF-8");
				writer.println(api.hash(pass));
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isAuthenticated(Player player) {
		if(new File(this.tempDir + player.getIdentifier() + ".auth").isFile())
			return true;
		return false;
	}

	public boolean isRegistered(Player player) {
		if(new File(this.authDir + player.getIdentifier() + ".acnt").isFile())
			return true;
		return false;
	}
	
	public String getPassword(Player player) {
		String password = null;
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(this.authDir + "/" + player.getIdentifier() + ".acnt"));
			while((line = br.readLine()) != null) {
				password = line;
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null)br.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return password;
	}

	public boolean tryLogin(Player player, String pass) {
		if(isRegistered(player))
			if(getPassword(player).equals(api.hash(pass))) {
				authenticate(player);
			}
		return false;
	}
	
	public void authenticate(Player player) {
		if(!(new File(this.tempDir + "/" + player.getIdentifier() + ".temp").isFile())) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(this.getAuthDir() + "/" + player.getIdentifier() + ".acnt", "UTF-8");
				writer.println("Currently Authenticated");
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}
