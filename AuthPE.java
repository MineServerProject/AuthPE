import redstonelamp.plugin.PluginBase;
import authpe.API;
import authpe.listener.AuthCommandListener;
import authpe.listener.AuthEventListener;

class AuthPE extends PluginBase {
	private API api = new API(this);
	
	public void onEnable() {
		this.getDataFolder();
		this.getServer().getEventManager().registerEventListener(new AuthEventListener(this, api));
		this.getServer().getCommandManager().registerCommand("login", "Login to your AuthPE account", new AuthCommandListener(this, api));
		this.getServer().getCommandManager().registerCommand("register", "Register a new AuthPE account", new AuthCommandListener(this, api));
		this.getLogger().info("AuthPE has been enabled!");
	}
	
	public void onDisable() {
		this.getLogger().warning("AuthPE is no longer enabled! Did the server stop?");
	}
}