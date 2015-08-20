package authpe.listener;

import authpe.API;
import redstonelamp.Player;
import redstonelamp.event.Listener;
import redstonelamp.event.block.BlockBreakEvent;
import redstonelamp.event.block.BlockPlaceEvent;
import redstonelamp.event.cmd.CommandExecuteEvent;
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
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getCause() instanceof Player) {
			Player player = (Player) event.getCause();
			if(!this.api.isAuthenticated(player))
				event.setCanceled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getCause() instanceof Player) {
			Player player = (Player) event.getCause();
			if(!this.api.isAuthenticated(player))
				event.setCanceled(true);
		}
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
	public void onCommandExecuted(CommandExecuteEvent event) {
		if(event.getSender() instanceof Player) {
			Player player = (Player) event.getSender();
			if(!this.api.isAuthenticated(player)) {
				player.sendMessage("You must authenticate to play on this server.");
				if(this.api.isRegistered(player))
					player.sendMessage("Use /login <password> to login to your account");
				else
					player.sendMessage("Use /register <password> to create an account");
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(this.api.isAuthenticated(event.getPlayer()))
			this.api.deAuth(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if(this.api.isAuthenticated(event.getPlayer()))
			this.api.deAuth(event.getPlayer());
	}
}
