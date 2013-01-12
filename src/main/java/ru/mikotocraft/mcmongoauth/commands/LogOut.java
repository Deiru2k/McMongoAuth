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

import ru.mikotocraft.mcmongoauth.McMongoAuth;
import ru.mikotocraft.mcmongoauth.SessionsManager;

public class LogOut implements CommandExecutor {
	private McMongoAuth plugin;
	private SessionsManager sm;
	
	public LogOut(McMongoAuth instance, SessionsManager _sm) {
		plugin = instance;
		sm = _sm;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can do this");
			return false;
		}
		Player player = (Player) sender;
		String playername = player.getPlayerListName().toLowerCase();
		if (sm.checkSessions(playername)) {
			sm.removeSession(playername);
			plugin.getLogger().info("Player " + playername + " logger out.");
			player.sendMessage(ChatColor.GREEN + "Logged you out.");
			return true;
		} else {
			player.sendMessage(ChatColor.GREEN + "You are not logged in.");
			return false;
		}
	}

}
