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

public class ChangePassword implements CommandExecutor {
	private McMongoAuth plugin;
	private DBHandler db;
	
	public ChangePassword(McMongoAuth instance, DBHandler _db) {
		plugin = instance;
		db = _db;
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
				String new_password = args[1];
				if (db.changePassword(playername, password, new_password)) {
					plugin.getLogger().info("Player " + playername + " has changed password.");
					player.sendMessage(ChatColor.GREEN + "Successfully changed your password");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "Wrong password.");
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}	
}