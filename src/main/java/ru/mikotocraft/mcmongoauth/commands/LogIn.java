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
import ru.mikotocraft.mcmongoauth.SessionsManager;
import ru.mikotocraft.mcmongoauth.McMongoAuth;

public class LogIn implements CommandExecutor {

	private McMongoAuth plugin;
	private DBHandler db;
	private SessionsManager sm;
	
	public LogIn(McMongoAuth instance, DBHandler _db, SessionsManager _sm) {
		plugin = instance;
		db = _db;
		sm = _sm;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can do this");
			return false;
		}
		try {
			String password = args[0];
			String playername = player.getPlayerListName().toLowerCase();
			if (!sm.checkSessions(playername)) {
				if (db.checkAuth(playername, password)) {
					sm.addSession(playername);
					player.sendMessage(ChatColor.GREEN + "You are now logged in!");
					plugin.getLogger().info("Player " + playername + " logged in.");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "Wrong password, or no such player.");
					return false;
				}
			} else {
					player.sendMessage(ChatColor.GREEN + "You are allready logged in.");
					return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
}