import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import redstonelamp.Player;
import redstonelamp.plugin.PluginBase;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandSender;
import redstonelamp.event.player.PlayerInteractEvent;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerMoveEvent;
import redstonelamp.event.player.PlayerDisconnectEvent;

class AuthPE extends PluginBase {
	public void onLoad() {
		if(!(new File(this.getDataFolder()).isDirectory())) {
			new File(this.getDataFolder()).mkdirs();
			new File(this.getDataFolder() + "players/").mkdirs();
		}
		new File(this.getDataFolder() + "cache/").mkdirs();
		this.getCommandRegistrationManager().registerCommand("login", "Login to the server!", false);
		this.getCommandRegistrationManager().registerCommand("register", "Register an account on the server!", false);
		this.getServer().getLogger().info("AuthPE has been enabled!");
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!isAuthenticated(player.getName())) {
			player.sendMessage("You must authenticate your account to play!");
			if(accountExistsForPlayer(player.getName())) {
				player.sendMessage("An account already exists for the username \"" + player + "\"");
				player.sendMessage("Please run /login <password> to login and play");
			} else {
				player.sendMessage("An account does not exists for the username \"" + player + "\"");
				player.sendMessage("Please run /register <password> to register and play");
				player.sendMessage("(Remember, you will need to know your password to play in the future)");
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, List<String> args) {
		switch(cmd.getName()) {
			case "login":
				if(args.size() > 0)
					if(this.accountExistsForPlayer(sender.getName()) && this.hash(args.get(1)).equals(this.getPassword(sender.getName()))) {
						this.authenticate(sender.getName());
						sender.sendMessage("You have been authenticated!");
					} else
						sender.sendMessage("Error during authentication!");
				else
					sender.sendMessage("Usage: /login <password>");
			break;
			
			case "register":
				if(args.size() > 0)
					if(!this.accountExistsForPlayer(sender.getName())) {
						this.createAccount(sender.getName(), this.hash(args.get(1)));
						if(this.accountExistsForPlayer(sender.getName())) {
							this.authenticate(sender.getName());
						} else
							sender.sendMessage("Error during authentication");
					} else
						sender.sendMessage("An account already exists with the name " + sender + "!");
				else
					sender.sendMessage("Usage: /register <password>");
			break;
		}
		return true;
	}
	
	public boolean onPlayerMove(PlayerMoveEvent event) {
		String player = event.getPlayer().getName();
		if(this.isAuthenticated(player))
			return false;
		return true;
	}
	
	public boolean onPlayerInteract(PlayerInteractEvent event) {
		String player = event.getPlayer().getName();
		if(this.isAuthenticated(player))
			return false;
		return true;
	}
	
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		String player = event.getPlayer().getName();
		if(this.isAuthenticated(player))
			this.deAuthenticate(player);
	}

	public void onDisable() {
		new File(this.getDataFolder() + "cache/").delete();
		this.getServer().getLogger().warn("AuthPE is no longer enabled! Did the server shut down?");
	}
	
	
	// ===== Authentication Methods ===== \\
	public boolean isAuthenticated(String player) {
		if(!(new File(this.getDataFolder() + "cache/" + player + ".temp").isFile()))
			return false;
		return true;
	}
	
	public void authenticate(String player) {
		if(!(new File(this.getDataFolder() + "cache/" + player + ".temp").isFile())) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(this.getDataFolder() + "players/" + player + ".acnt", "UTF-8");
				writer.println("Currently Authenticated");
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deAuthenticate(String player) {
		if(!(new File(this.getDataFolder() + "cache/" + player + ".temp").isFile()))
			new File(this.getDataFolder() + "cache/" + player + ".temp").delete();
	}
	
	public boolean accountExistsForPlayer(String player) {
		if(!(new File(this.getDataFolder() + "players/" + player + ".acnt").isFile()))
			return false;
		return true;
	}
	
	public void createAccount(String player, String password) {
		if(!(new File(this.getDataFolder() + "players/" + player + ".acnt").isFile())) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(this.getDataFolder() + "players/" + player + ".acnt", "UTF-8");
				writer.println(password);
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getPassword(String player) {
		String password = null;
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(this.getDataFolder() + "players/" + player + ".acnt"));
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
	
	private String hash(String string) {
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
