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

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DBHandler {
	private MongoClient mongoClient;
	private DB db;
	private DBCollection players;
	private MessageDigest md;
	
	public DBHandler(String dbname, String collectionname){
		try {
		this.openConnection(dbname, collectionname);
		} catch (UnknownHostException e) {}
	}
	
	public boolean changePassword(String playername, String old_password, String new_password) {
		if (this.checkAuth(playername, old_password)) {
			try {
				String password = this.encryptPassword(new_password);
				BasicDBObject query = new BasicDBObject().append("playername", playername);
				BasicDBObject update = new BasicDBObject().append("$set", new BasicDBObject().append("password", password));
				players.update(query, update);
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean unregisterPlayer(String playername, String password) {
		if (this.checkAuth(playername, password)) {
			BasicDBObject query = new BasicDBObject().append("playername", playername);
			players.remove(query);
			return true;
		} else {
			return false;
		}
	}
	
	private void openConnection(String dbname, String collectionname) throws UnknownHostException{
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {}
		
		try {
			mongoClient = new MongoClient("127.0.0.1", 27017);
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
		}
		db = mongoClient.getDB(dbname);
		players = db.getCollection(collectionname);
	}
	
	public void closeConnection(){
	try {
		mongoClient.close();
	} catch (Exception e) {}
	}
	
	private String encryptPassword(String password) throws UnsupportedEncodingException {
		byte[] hash = md.digest(password.getBytes("UTF-8"));
		StringBuffer pass = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) pass.append('0');
			pass.append(hex);
		}
		return pass.toString();
	}
	
	public boolean checkAccess(String playername) {
		BasicDBObject query = new BasicDBObject();
		query.put("playername", playername.toLowerCase());
		DBObject player = players.findOne(query);
		if (player == null) return false;
		boolean result = (Boolean) player.get("allowed"); 
		if(result){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkInventory(String playername) {
		BasicDBObject query = new BasicDBObject().append("playername", playername);
		DBObject player = players.findOne(query);
		if (player.get("inventory") == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void registerPlayer(String playername, String plain_password) {
		try {
			String password = this.encryptPassword(plain_password);
			BasicDBObject query = new BasicDBObject();
			query.put("playername", playername);
			
			if (players.findOne(query) == null) {
				BasicDBObject player = new BasicDBObject();
				player.put("playername", playername);
				player.put("password", password);
				player.put("allowed", true);
				players.insert(player);
				return;
			} else { 
				return;
			}
		} catch (Exception e) {
			return;
		}
	}
	
	public boolean checkAuth(String playername, String plain_password) {
		try {
			String password = this.encryptPassword(plain_password);
			BasicDBObject query = new BasicDBObject();
			query.put("playername", playername);	
			DBObject player = players.findOne(query);
			if (password.equals(player.get("password"))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
