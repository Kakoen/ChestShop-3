package net.musicmaniak.ChestShop.limiter;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;

public class Transaction {

	private String playerId;
	private String signId;
	private double amount;
	private long timeStamp;
	
	public Transaction(OfflinePlayer player, Sign sign, double amount) {
		this.playerId = getPlayerId(player);
		this.signId = getSignId(sign);
		this.amount = amount;
		this.timeStamp = Calendar.getInstance().getTimeInMillis();
	}
	
	public Transaction(String playerId2, String signId2, double amount2, long timestamp2) {
		this.playerId = playerId2;
		this.signId = signId2;
		this.amount = amount2;
		this.timeStamp = timestamp2;
	}

	private String getSignId(Sign sign) {
		if(sign == null) {
			return "";
		}
		
		return "sign" + sign.getWorld().getName() + "-" + sign.getX() + "-" + sign.getY() + "-" + sign.getZ();
	}

	public static String getPlayerId(OfflinePlayer player) {
		if(player == null) {
			return "";
		}
		
		return player.getUniqueId().toString();
	}
	
	public double getAmount() {
		return amount;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

	public boolean isPlayer(OfflinePlayer player) {
		return StringUtils.equals(getPlayerId(player), playerId);
	}

	public boolean isSign(Sign sign) {
		return StringUtils.equals(getSignId(sign), signId);
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public String getSignId() {
		return signId;
	}
	
}
