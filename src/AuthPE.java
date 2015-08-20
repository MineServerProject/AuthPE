import authpe.API;
import authpe.cmd.*;
import authpe.listener.EventListener;
import redstonelamp.plugin.PluginBase;
import redstonelamp.resources.annotations.RedstonePlugin;

@RedstonePlugin(
	name = "AuthPE",
	version = "1.1.4",
	api = 1.4,
	author = "Philip Shilling",
	description = "Block impersonators from joining your server!",
	website = "http://RedstoneLamp.net"
)
public class AuthPE extends PluginBase {
	private API api = new API(this);
	
	public void onEnable() {
		this.getDataFolder();
		this.getServer().getEventManager().registerEvents(new EventListener(api));
		this.getServer().getCommandManager().registerCommand("login", "Login to your AuthPE account", new LoginCommand(api), "authpe.cmd.login");
		this.getServer().getCommandManager().registerCommand("register", "Register a new AuthPE account", new RegisterCommand(api), "authpe.cmd.register");
		this.getLogger().info("AuthPE has been enabled!");
	}
	
	public void onDisable() {
		this.getLogger().warning("AuthPE is no longer enabled! Did the server stop?");
	}
}
