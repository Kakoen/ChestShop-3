package net.musicmaniak.ChestShop.limiter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TransactionLimiter {

	public static File logFile = new File("plugins/ChestShop/transactionlog.json");
	
	public static List<Transaction> history;
	
	public synchronized static double getHistory(Sign sign, Player player, long period) {
		if(history == null) {
			load();
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		long minimum = now - period;
		
		double total = 0;
		for(int i = 0; i < history.size(); i++) {
			Transaction t = history.get(i);
			if(t.isSign(sign)) {
				if(t.getTimeStamp() > minimum) {
					if(t.isPlayer(player)) {
						total += t.getAmount();
					}
				} else {
					//Remove old history
					history.remove(i);
					i--;
				}
			}
		}
		
		return total;
	}
	
	public synchronized static void addToHistory(Sign sign, OfflinePlayer player, double amount) {
		if(history == null) {
			load();
		}
		
		Transaction t = new Transaction(player, sign, amount);
		history.add(0, t);
		
		save();
	}

	private synchronized static void save() {
		JsonArray arr = new JsonArray();
		for(Transaction t : history) {
			JsonObject tobj = new JsonObject();
			tobj.addProperty("player", t.getPlayerId());
			tobj.addProperty("sign", t.getSignId());
			tobj.addProperty("amount", t.getAmount());
			tobj.addProperty("time", t.getTimeStamp());
			arr.add(tobj);
		}
		arr.toString();
		
		try(FileOutputStream os = new FileOutputStream(logFile)) {
			os.write(arr.toString().getBytes("UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized static void load() {
		history = new ArrayList<Transaction>();
		
		try(FileInputStream is = new FileInputStream(logFile)) {
			JsonParser parser = new JsonParser();
			JsonArray arr = parser.parse(new InputStreamReader(is, "UTF-8")).getAsJsonArray();
			for(int i = 0; i < arr.size(); i++) {
				JsonObject obj = arr.get(i).getAsJsonObject();
				String playerId = obj.get("player").getAsString();
				String signId = obj.get("sign").getAsString();
				double amount = obj.get("amount").getAsDouble();
				long timestamp = obj.get("time").getAsLong();
				history.add(new Transaction(playerId, signId, amount, timestamp));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
