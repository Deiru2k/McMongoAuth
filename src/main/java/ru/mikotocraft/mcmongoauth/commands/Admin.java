package ru.mikotocraft.mcmongoauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.mikotocraft.mcmongoauth.DBHandler;
import ru.mikotocraft.mcmongoauth.McMongoAuth;
import ru.mikotocraft.mcmongoauth.SessionsManager;

public class Admin
  implements CommandExecutor
{
  private McMongoAuth plugin;
  private DBHandler db;
  private SessionsManager sm;

  public Admin(McMongoAuth instance, DBHandler _db, SessionsManager _sm)
  {
    this.plugin = instance;
    this.db = _db;
    this.sm = _sm;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (sender.hasPermission("mongoauth.admin")) {
      if (args.length != 0) {
        if (args[0].equals("unregister"))
          try {
            String playername = args[1].toLowerCase();
            this.db.deletePlayer(playername);
            this.sm.removeSession(playername);
            this.plugin.getLogger().info(sender.getName() + " deleted player " + playername + " from database.");
            sender.sendMessage("Successfully deleted player.");
            return true;
          } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage("Missing playername");
            return false;
          }
        if (args[0].equals("changepass")) {
          try {
            String playername = args[1].toLowerCase();
            String password = args[2];
            this.db.modPassword(playername, password);
            this.plugin.getLogger().info(sender.getName() + " changed password for player " + playername + ".");
            sender.sendMessage("Successfully changed password for player");
            return true;
          } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage("Missing playername or new password");
            return false;
          }
        }
        return false;
      }

      return false;
    }

    return false;
  }
}