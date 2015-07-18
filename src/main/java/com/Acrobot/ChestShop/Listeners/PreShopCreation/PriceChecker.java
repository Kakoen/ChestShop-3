package com.Acrobot.ChestShop.Listeners.PreShopCreation;

import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;

import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static com.Acrobot.Breeze.Utils.PriceUtil.isPrice;
import static com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome.INVALID_PRICE;
import static com.Acrobot.ChestShop.Signs.ChestShopSign.PRICE_LINE;

/**
 * @author Acrobot
 */
public class PriceChecker implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public static void onPreShopCreation(PreShopCreationEvent event) {
        String line = event.getSignLine(PRICE_LINE).toUpperCase();
        String[] part = line.split(":");
        
        int buy = 0;
        int sell = 0;
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
        		int result = Integer.parseInt(p.substring(1));
        		
        		if(p.charAt(0) == 'B') {
            		buy = result;
            	} else if(p.charAt(0) == 'S') {
            		sell = result;
            	} else if(p.charAt(0) == 'L') {
            		limit = result;
            	} else if (p.charAt(0) == 'P') {
            		limitPer = result;
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
        if(buy > 0) {
        	newLine = "B " + buy;
        }
        
        if(sell > 0) {
        	if(!StringUtils.isEmpty(newLine)) {
        		newLine = newLine + ":";
        	}
        	newLine = newLine + "S " + sell;
        }
        
        if(limit > 0) {
       		newLine = newLine + ":";
        	newLine = newLine + "L " + limit;
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
        	event.getPlayer().sendMessage("No buy / sell price given!");
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
