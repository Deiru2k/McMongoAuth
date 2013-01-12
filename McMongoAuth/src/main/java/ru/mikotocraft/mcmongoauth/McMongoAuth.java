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


import ru.mikotocraft.mcmongoauth.AuthListener;
import ru.mikotocraft.mcmongoauth.DBHandler;
import ru.mikotocraft.mcmongoauth.SessionsManager;
import ru.mikotocraft.mcmongoauth.commands.ChangePassword;
import ru.mikotocraft.mcmongoauth.commands.LogIn;
import ru.mikotocraft.mcmongoauth.commands.LogOut;
import ru.mikotocraft.mcmongoauth.commands.Register;
import ru.mikotocraft.mcmongoauth.commands.Unregister;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public final class McMongoAuth extends JavaPlugin {
	
	private String dbname;
	private String collectionname;
	private DBHandler db;
	private SessionsManager sm;

	
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		sm = new SessionsManager();
		FileConfiguration config = this.getConfig();
		dbname = config.getString("db-name");
		collectionname = config.getString("collection-name");
		db = new DBHandler(dbname, collectionname);
		PluginManager pm = getServer().getPluginManager();
		getCommand("login").setExecutor(new LogIn(this, db, sm));
		getCommand("logout").setExecutor(new LogOut(this, sm));
		getCommand("register").setExecutor(new Register(this, db, sm));
		getCommand("unregister").setExecutor(new Unregister(this, db, sm));
		getCommand("changepass").setExecutor(new ChangePassword(this, db));
		pm.registerEvents(new AuthListener(this, db, sm), this);
	}
	@Override
	public void onDisable() {
		db.closeConnection();
	}
}