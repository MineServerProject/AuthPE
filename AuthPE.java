import redstonelamp.Server;
import redstonelamp.plugin.PluginBase;
import redstonelamp.event.PlayerJoinEvent;
import redstonelamp.event.PlayerMoveEvent;

class AuthPE extends PluginBase {
    public void onLoad() {
        this.getServer.getLogger().info("AuthPE has been enabled!");
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
    
    public boolean onPlayerMove(PlayerMoveEvent event) { //This is not the final method type
        String player = event.getPlayer();
        if(isAuthenticated(player))
            return false;
    }
    
    public void onDisable() {
        this.getServer().getLogger().warn("AuthPE is no longer enabled! Did the server shut down?");
    }
    
    
    // ===== Authentication Methods ===== \\
    public boolean isAuthenticated(String player) {
        return false;
    }
    
    public boolean accountExistsForPlayer(String player) {
        return false;
    }
}
