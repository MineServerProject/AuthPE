package authpe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import net.redstonelamp.Player;
import authpe.AuthPE;

public class API {
	private ArrayList<Player> authenticated = new ArrayList<Player>();
	private AuthPE plugin;
	private File playersDir;
	
	public API(AuthPE plugin) {
		this.plugin = plugin;
		this.playersDir = new File(this.plugin.getDataFolder() + "/players/");
		if(!this.playersDir.isDirectory())
			this.playersDir.mkdirs();
	}

	public boolean isAuthenticated(Player player) {
		if(this.authenticated.contains(player))
			return true;
		return false;
	}

	public void deAuth(Player player) {
		if(this.isAuthenticated(player))
			this.authenticated.remove(player);
	}
	
	public boolean isRegistered(Player player) {
		if(new File(this.playersDir + "/" + player.getName().toLowerCase() + ".acnt").isFile())
			return true;
		return false;
	}

	public boolean registerPlayer(Player player, String pass) {
		if(!this.isRegistered(player)) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(new File(this.playersDir + "/" + player.getName().toLowerCase() + ".acnt")));
				writer.write(this.hash(pass));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(writer != null)
						writer.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public boolean tryLogin(Player player, String pass) {
		pass = this.hash(pass);
		String realPass = this.getPassword(player);
		if(pass.equals(realPass)) {
			this.authenticated.add(player);
			return true;
		}
		return false;
	}
	
	public String getPassword(Player player) {
		String pass = null;
		if(this.isRegistered(player)) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(this.playersDir + "/" + player.getName().toLowerCase() + ".acnt")));
				pass = br.readLine();
				br.close();
	    	} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pass;
	}
	
	private String hash(String string) {
		String hash = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(string.getBytes());
			hash = new String(messageDigest.digest());
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
}