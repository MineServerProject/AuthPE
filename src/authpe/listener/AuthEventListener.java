package authpe.listener;

import java.io.File;

import authpe.API;
import redstonelamp.Player;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;
import redstonelamp.plugin.PluginBase;

public class AuthEventListener implements Listener {
	private PluginBase plugin;
	private API api;
	
	public AuthEventListener(PluginBase plugin, API api) {
		this.plugin = plugin;
		this.api = api;
	}

	public void onEvent(Event event) {
		Player player = event.getPlayer();
		File auth = null;
		switch(event.getEventName()) {
			case "PlayerJoinEvent":
				player.sendMessage("This server requires authentication to play.");
				if(api.getAuthenticator().isRegistered(player)) {
					player.sendMessage("An account exists with this player name!");
					player.sendMessage("Use /login <password> to login");
				} else {
					player.sendMessage("Use /register <password> to create an account");
				}
			break;
			
			case "PlayerMoveEvent":
				if(this.api.getAuthenticator().isAuthenticated(player))
					event.cancel();
			break;
			
			case "PlayerQuitEvent":
			case "PlayerKickEvent":
				auth = new File(api.getAuthenticator().getTempDir() + player.getIdentifier() + ".auth");
				if(auth.isFile())
					auth.delete();
			break;
			
			case "ServerStopEvent":
				for(File file : api.getAuthenticator().getTempDir().listFiles()) {
					if(file.isFile())
						file.delete();
				}
			break;
		}
	}
}
