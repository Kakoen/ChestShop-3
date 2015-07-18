package com.Acrobot.ChestShop.Listeners.PreShopCreation;

import static com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome.INVALID_PRICE;
import static com.Acrobot.ChestShop.Signs.ChestShopSign.PRICE_LINE;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;

/**
 * @author Acrobot
 */
public class PriceChecker implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public static void onPreShopCreation(PreShopCreationEvent event) {
        String line = event.getSignLine(PRICE_LINE).toUpperCase();
        //line = line.replaceAll("(\\.\\d*?[1-9])0+", "$1"); //remove trailing zeroes

        String[] part = line.split(":");
        
        double buy = 0;
        double sell = 0;
        int limit = 0;
        int limitPer = 0;
        
        for(String p : part) {
        	p = p.replace(" ", "");
        	p = StringUtils.upperCase(p);
        	if(p.length() == 1) {
        		event.getPlayer().sendMessage("Price should be something like 'B 10:S 5' or 'B10:L10:P5' if you want to limit each player to buy a max of 10 items per 5 hours.");
        		event.setOutcome(INVALID_PRICE);
        		return;
        	}
        	
        	try {
        		double result = Double.parseDouble(p.substring(1));
        		
        		if(p.charAt(0) == 'B') {
            		buy = result;
            	} else if(p.charAt(0) == 'S') {
            		sell = result;
            	} else if(p.charAt(0) == 'L') {
            		limit = (int)result;
            	} else if (p.charAt(0) == 'P') {
            		limitPer = (int)result;
            	} else {
            		throw new Exception("Invalid part " + p.substring(0, 1));
            	}
        	} catch(Exception e) {
        		if(event.getPlayer() != null) {
        			event.getPlayer().sendMessage("Invalid part: " + p + ", " + e.getMessage());
        			event.getPlayer().sendMessage("Price should be something like 'B 10:S 5' or 'B10:L10:P5' if you want to limit each player to buy a max of 10 items per 5 hours.");
        		}
        		event.setOutcome(INVALID_PRICE);
        		return;
        	}
        	
        }
        
        if(buy <= 0 && sell <= 0) {
        	event.getPlayer().sendMessage("No buy / sell price given");
        	event.setOutcome(INVALID_PRICE);
        	return;
        }
        
        String newLine = "";
        NumberFormat nf = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
        if(buy > 0) {
        	newLine = "B " + nf.format(buy);
        }
        
        if(sell > 0) {
        	if(!StringUtils.isEmpty(newLine)) {
        		newLine = newLine + ":";
        	}
        	newLine = newLine + "S " + nf.format(sell);
        }
        
        if(limit > 0) {
       		newLine = newLine + ":";
        	newLine = newLine + "L " +  limit;
        }
        
        if(limitPer > 0) {
       		newLine = newLine + ":";
        	newLine = newLine + "P " + limitPer;
        }
        
        line = newLine;

        if (line.length() > 15) {
            line = line.replace(" ", "");
        }

        if (line.length() > 15) {
            event.setOutcome(INVALID_PRICE);
            return;
        }

        event.setSignLine(PRICE_LINE, line);

        if (!PriceUtil.hasBuyPrice(line) && !PriceUtil.hasSellPrice(line)) {
        	if(event.getPlayer() != null) {
        		event.getPlayer().sendMessage("No buy / sell price given!");
        	}
            event.setOutcome(INVALID_PRICE);
        }
    }

    private static boolean isInvalid(String part) {
        char characters[] = {'B', 'S', 'L', 'P'};

        for (char character : characters) {
            if (part.contains(Character.toString(character))) {
                return !PriceUtil.hasPrice(part, character);
            }
        }

        return false;
    }
}
