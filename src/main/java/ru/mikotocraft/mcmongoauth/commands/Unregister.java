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

package ru.mikotocraft.mcmongoauth.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.mikotocraft.mcmongoauth.DBHandler;
import ru.mikotocraft.mcmongoauth.McMongoAuth;
import ru.mikotocraft.mcmongoauth.SessionsManager;

public class Unregister implements CommandExecutor {
	private McMongoAuth plugin;
	private DBHandler db;
	private SessionsManager sm;
	
	public Unregister (McMongoAuth instance, DBHandler _db, SessionsManager _sm) {
		plugin = instance;
		db = _db;
		sm = _sm;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can do this");
			return false;
		}
		if (sender.hasPermission("mongoauth.user")) {
			Player player = (Player) sender;
			String playername = player.getPlayerListName().toLowerCase();
			try {
				String password = args[0];
				if (db.unregisterPlayer(playername, password)) {
					sm.removeSession(playername);
					player.sendMessage(ChatColor.GREEN + "Successfully unregistered.");
					plugin.getLogger().info("Player " + playername + " unregistered.");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "Wrong password.");
					return false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				player.sendMessage(ChatColor.RED + "Missing password.");
				return false;
			}
		} else {
			return false;
		}
	}
}
