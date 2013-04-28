/*	
 	This file is part of McMongoAuth
 	
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package ru.mikotocraft.mcmongoauth;

import ru.mikotocraft.mcmongoauth.commands.Admin;
import ru.mikotocraft.mcmongoauth.commands.ChangePassword;
import ru.mikotocraft.mcmongoauth.commands.LogIn;
import ru.mikotocraft.mcmongoauth.commands.LogOut;
import ru.mikotocraft.mcmongoauth.commands.Register;
import ru.mikotocraft.mcmongoauth.commands.Unregister;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class McMongoAuth extends JavaPlugin {

    private DBHandler db;


    @Override
	public void onEnable(){
		/* if (!getServer().getOnlineMode()) {
			this.getLogger().warning("Server is in offline-mode. Disabling plugin");
			this.setEnabled(false);
			return;
		} */
		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();
        String dbname = config.getString("db-name");
        String collectionname = config.getString("collection-name");
		try {
			db = new DBHandler(dbname, collectionname);
		} catch (Exception e) {
			this.getLogger().warning("Can't connect to MongoDB instance. Disabling plugin.");
			this.setEnabled(false);
			return;
		}
        SessionsManager sm = new SessionsManager();
		PluginManager pm = getServer().getPluginManager();
		getCommand("login").setExecutor(new LogIn(this, db, sm));
		getCommand("logout").setExecutor(new LogOut(this, sm));
		getCommand("register").setExecutor(new Register(this, db, sm));
		getCommand("unregister").setExecutor(new Unregister(this, db, sm));
		getCommand("changepass").setExecutor(new ChangePassword(this, db));
		getCommand("authadmin").setExecutor(new Admin(this, db, sm));
		pm.registerEvents(new AuthListener(this, db, sm), this);
	}
	@Override
	public void onDisable() {
		if (this.isEnabled()) db.closeConnection();
	}
}