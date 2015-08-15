package authpe.listener;

import authpe.API;
import redstonelamp.event.Listener;
import redstonelamp.event.player.PlayerChatEvent;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerKickEvent;
import redstonelamp.event.player.PlayerMoveEvent;
import redstonelamp.event.player.PlayerQuitEvent;
import redstonelamp.resources.annotations.EventHandler;

public class EventListener implements Listener {
	private API api;
	
	public EventListener(API api) {
		this.api = api;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!this.api.isAuthenticated(event.getPlayer())) {
			event.getPlayer().sendMessage("You must authenticate to play on this server.");
			if(this.api.isRegistered(event.getPlayer()))
				event.getPlayer().sendMessage("Use /login <password> to login to your account");
			else
				event.getPlayer().sendMessage("Use /register <password> to create an account");
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(!this.api.isAuthenticated(event.getPlayer()))
			event.setCanceled(true);
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		if(!this.api.isAuthenticated(event.getPlayer())) {
			event.getPlayer().sendMessage("You must authenticate to play on this server.");
			if(this.api.isRegistered(event.getPlayer()))
				event.getPlayer().sendMessage("Use /login <password> to login to your account");
			else
				event.getPlayer().sendMessage("Use /register <password> to create an account");
			event.setCanceled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(!this.api.isAuthenticated(event.getPlayer()))
			this.api.deAuth(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if(!this.api.isAuthenticated(event.getPlayer()))
			this.api.deAuth(event.getPlayer());
	}
}
