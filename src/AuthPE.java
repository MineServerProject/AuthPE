import authpe.API;
import authpe.cmd.*;
import net.redstonelamp.plugin.java.JavaPlugin;

public class AuthPE extends JavaPlugin {
	private API api = new API(this);
	
	@Override
	public void onEnable() {	this.getServer().getCommandManager().registerCommand("login", "Login to your AuthPE account", new LoginCommand(api));
		this.getServer().getCommandManager().registerCommand("register", "Register a new AuthPE account", new RegisterCommand(api));
		this.getLogger().info("AuthPE has been enabled!");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().warning("AuthPE is no longer enabled! Did the server stop?");
	}
}