import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;

import redstonelamp.Server;
import redstonelamp.plugin.PluginBase;
import redstonelamp.command.Command;
import redstonelamp.command.CommandSender;
import redstonelamp.command.CommandExecutor;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerMoveEvent;
import redstonelamp.event.player.PlayerDisconnectEvent;

class AuthPE extends PluginBase {
    public void onLoad() {
        if(!(new File(this.getDataFolder()).isDirectory())) {
        	new File(this.getDataFolder()).mkdirs();
            new File(this.getDataFolder() + "players/").mkdirs();
            new File(this.getDataFolder() + "cache/").mkdirs();
        }
        this.getCommandRegistrationManager().registerCommand("login", "Login to the server!", false);
        this.getCommandRegistrationManager().registerCommand("register", "Register an account on the server!", false);
        this.getServer().getLogger().info("AuthPE has been enabled!");
    }
    
    public void onPlayerJoin(PlayerJoinEvent event) { //This is not the final method type
        String player = event.getPlayer();
        if(!isAuthenticated(player)) {
            player.sendMessage("You must authenticate your account to play!");
            if(accountExistsForPlayer(player)) {
                player.sendMessage("An account already exists for the username \"" + player + "\"");
                player.sendMessage("Please run /login <password> to login and play");
            } else {
                player.sendMessage("An account does not exists for the username \"" + player + "\"");
                player.sendMessage("Please run /register <password> to register and play");
                player.sendMessage("(Remember, you will need to know your password to play in the future)");
            }
        }
    }
    
    public void onCommand(String sender, String cmd, String[] args) {
    	switch(cmd) {
    	    case "login":
    	        //TODO: Process login
    	    break;
    	    
    	    case "register":
    	        //TODO: Process registration
    	    break;
    	}
    }
    
    public boolean onPlayerMove(PlayerMoveEvent event) { //This is not the final method type
        String player = event.getPlayer();
        if(this.isAuthenticated(player))
            return false;
    }
    
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    	String player = event.getPlayer();
        if(this.isAuthenticated(player))
        	this.deAuthenticate(player);
    }

	public void onDisable() {
        this.getServer().getLogger().warn("AuthPE is no longer enabled! Did the server shut down?");
    }
    
    
    // ===== Authentication Methods ===== \\
    public boolean isAuthenticated(String player) {
        if(!(new File(this.getDataFolder() + "cache/" + player + ".temp").isFile()))
        	return false;
        return true;
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
            PrintWriter writer = new PrintWriter(this.getDataFolder() + "players/" + player + ".acnt", "UTF-8");
            writer.println(password);
            writer.close();
        }
    }
    
    public String getPassword(String player) {
    	String password;
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
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(string.getBytes());
        String hash = new String(md.digest());
        return hash;
    }
}
