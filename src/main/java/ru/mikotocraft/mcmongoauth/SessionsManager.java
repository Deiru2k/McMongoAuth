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

import java.util.ArrayList;

public class SessionsManager {
	private ArrayList<String> sessions;
	
	public SessionsManager(){
		sessions = new ArrayList<String>();
	}
	
	public void addSession(String playername) {
		if (!sessions.contains(playername)) {
			sessions.add(playername);
		}
	}
	
	public void removeSession(String playername) {
		if (sessions.contains(playername)) {
			sessions.remove(playername);
        }
	}
	public boolean checkSessions(String playername) {
        return sessions.contains(playername);
	}
}