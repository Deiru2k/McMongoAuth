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

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import ru.mikotocraft.mcmongoauth.DBHandler;
import ru.mikotocraft.mcmongoauth.SessionsManager;

public class AuthListener implements Listener {
	private DBHandler db;
	private McMongoAuth plugin;
	private FileConfiguration config;
	private SessionsManager sm;
	
	public AuthListener(McMongoAuth instance, DBHandler _db, SessionsManager _sm) {
		sm = _sm;
		plugin = instance;
		config = plugin.getConfig();
		db = _db;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		boolean access = db.checkAccess(player.getPlayerListName());
		if (access) {
			player.sendMessage(ChatColor.RED + "Please, log in");
			return;
		} else {
			if (config.getString("enable-registration").equals("true")) {
				player.sendMessage(ChatColor.RED + "Please, register");
				return;
			}
			player.kickPlayer("You don't have the access to this server");
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) return;
		Player player =  event.getPlayer();
		String playername = player.getPlayerListName().toLowerCase();
		String command = event.getMessage().substring(1);
		String rootcommand = command.split(" ")[0];
		
		if (plugin.getCommand(rootcommand) != null && !plugin.getCommand(rootcommand).equals(plugin.getCommand("mcmongoauth"))){
			 return;
		}
		
		if (!sm.checkSessions(playername)) {
			 player.sendMessage("You have to register or login to use command.");
			 event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if( event.isCancelled() ) return;
		String playername = event.getPlayer().getPlayerListName().toLowerCase();
		if (!sm.checkSessions(playername)) {
			event.getPlayer().sendMessage("You have to register or login to chat.");
			event.setCancelled(true);
		}
	
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (event.isCancelled()) return;
		String playername = event.getPlayer().getPlayerListName().toLowerCase();
		if (!sm.checkSessions(playername)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		String playername = event.getPlayer().getPlayerListName().toLowerCase();
		if (!sm.checkSessions(playername)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if (event.isCancelled()) return;
		String playername = event.getPlayer().getPlayerListName().toLowerCase();
		if (!sm.checkSessions(playername)) event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled()) return;
		String playername = event.getPlayer().getPlayerListName().toLowerCase();
		if (!sm.checkSessions(playername)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickUp(PlayerPickupItemEvent event) {
		if (event.isCancelled()) return;
		String playername = event.getPlayer().getPlayerListName().toLowerCase();
		if (!sm.checkSessions(playername)) event.setCancelled(true);
	}
	
	
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playername = player.getPlayerListName().toLowerCase();
		if (sm.checkSessions(playername)) {
			sm.removeSession(playername);
			plugin.getLogger().info("Player " + playername + " logged out.");
			return;
		}
	}
}