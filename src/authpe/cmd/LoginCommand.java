package authpe.cmd;

import authpe.API;
import net.redstonelamp.Player;
import net.redstonelamp.cmd.CommandListener;
import net.redstonelamp.cmd.CommandSender;

public class LoginCommand implements CommandListener {
	private API api;
	
	public LoginCommand(API api) {
		this.api = api;
	}
	
	public void onCommand(CommandSender sender, String cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(api.isRegistered(((Player) sender.getSender()))) {
				if(args.length > 1)
					if(this.api.tryLogin(((Player) sender.getSender()), args[1]))
						sender.sendMessage("You have been authenticated!");
					else
						sender.sendMessage("Error during authentication");
				else
					sender.sendMessage("Usage: /login <password>");
			} else {
				sender.sendMessage("No account exists with your player name.");
				sender.sendMessage("Use /register <password> to create an account");
			}
		}
	}
}