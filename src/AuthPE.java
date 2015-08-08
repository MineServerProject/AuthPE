import authpe.API;
import authpe.listener.AuthCommandListener;
import authpe.listener.AuthEventListener;
import redstonelamp.plugin.PluginBase;
import redstonelamp.resources.annotations.RedstonePlugin;

@RedstonePlugin(				//The @RedstonePlugin annotation does nothing at the moment but will be used later
	name = "AuthPE",
	version = "1.1.2",
	api = 1.4,
	author = "Philip Shilling"	
)
public class AuthPE extends PluginBase {
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
