package com.Acrobot.ChestShop.Listeners.PreTransaction;

import static com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType.BUY;
import static com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType.SELL;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent.TransactionOutcome;

import net.musicmaniak.ChestShop.limiter.TransactionLimiter;

public class LimitChecker implements Listener {

    @EventHandler
    public static void onBuyCheck(PreTransactionEvent event) {
    	if (event.isCancelled() || event.getTransactionType() != SELL) {
            return;
        }
    	
    	if(event.getClient() == null) {
    		return;
    	}
    	
    	if(event.getLimit() > 0) {
        	double history = TransactionLimiter.getHistory(event.getSign(), event.getClient(), event.getLimitPeriod());
        	if(history > event.getLimit()) {
        		event.getClient().sendMessage("You have reached the maximum buy / sell amount (L) per period (P in hours) that is set by the shop owner.");
        		event.setCancelled(TransactionOutcome.OTHER);
        		return;
        	} 
        	
        	//event.getClient().sendMessage("You have bought " + history + " items from this shop so far in the limited period " + event.getLimitPeriod() + ", the limit is " + event.getLimit());
    	}
    	
    }
    
    @EventHandler
    public static void onSellCheck(PreTransactionEvent event) {
    	if (event.isCancelled() || event.getTransactionType() != BUY) {
            return;
        }
    	
    	if(event.getClient() == null) {
    		return;
    	}
    	
    	if(event.getLimit() > 0) {
        	double history = TransactionLimiter.getHistory(event.getSign(), event.getClient(), event.getLimitPeriod());
        	if(history > event.getLimit()) {
        		event.getClient().sendMessage("You have reached the maximum buy / sell amount (L) per period (P in hours) that is set by the shop owner.");
        		event.setCancelled(TransactionOutcome.OTHER);
        		return;
        	} 
        	
        	//event.getClient().sendMessage("You have sold " + history + " items from this shop so far in the limited period " + event.getLimitPeriod() + ", the limit is " + event.getLimit());
    	}
    }
}
