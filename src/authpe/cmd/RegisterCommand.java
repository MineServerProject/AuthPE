package authpe.cmd;

import authpe.API;
import redstonelamp.Player;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandListener;
import redstonelamp.cmd.CommandSender;

public class RegisterCommand implements CommandListener {
	private API api;
	
	public RegisterCommand(API api) {
		this.api = api;
	}
	
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.getSender() instanceof Player) {
			if(!api.isRegistered(((Player) sender.getSender()))) {
				if(args.length > 1) {
					this.api.registerPlayer(((Player) sender.getSender()), args[1]);
					if(this.api.tryLogin(((Player) sender.getSender()), args[1]))
						sender.sendMessage("You have been authenticated!");
					else
						sender.sendMessage("Error during authentication");
				} else
					sender.sendMessage("Usage: /register <password>");
			} else {
				sender.sendMessage("An account exists with your player name.");
				sender.sendMessage("Use /login <password> to login to your account");
			}
		}
	}
}
