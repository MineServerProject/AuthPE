package authpe.listener;

import authpe.API;
import redstonelamp.Player;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;
import redstonelamp.plugin.PluginBase;

public class AuthCommandListener implements CommandListener {
	private PluginBase plugin;
	private API api;
	
	public AuthCommandListener(PluginBase plugin, API api) {
		this.plugin = plugin;
		this.api = api;
	}

	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.getSender() instanceof Player) {
			Player player = (Player) sender.getSender();
			switch(cmd.getName()) {
				case "login":
					if(api.getAuthenticator().isRegistered(player)) {
						if(args.length <= 2) {
							if(api.getAuthenticator().tryLogin(player, args[1]))
								player.sendMessage("You have been authenticated!");
							else
								player.sendMessage("Error during authentication.");
						}
					} else {
						player.sendMessage("No account exists with this name.");
						player.sendMessage("Use /register <password> to create an account");
					}
					break;
				
				case "register":
					if(!api.getAuthenticator().isRegistered(player)) {
						if(args.length <= 2) {
							api.getAuthenticator().createAccount(player, args[1]);
							if(api.getAuthenticator().tryLogin(player, args[1]))
								player.sendMessage("You have been authenticated!");
							else
								player.sendMessage("Error during authentication.");
						}
					} else {
						player.sendMessage("An account exists with this name.");
						player.sendMessage("Use /login <password> to login");
					}
				break;
			}
		}
	}
}
