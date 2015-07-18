package com.Acrobot.ChestShop.Tests;

import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.Acrobot.ChestShop.Listeners.PreShopCreation.PriceChecker.onPreShopCreation;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Andrzej Pomirski (Acrobot)
 */
@RunWith(JUnit4.class)
public class PriceCheckerTest {

    String[] getPriceString(String prices) {
        return new String[]{null, null, prices, null};
    }

    @Test
    public void testPrice() {
        PreShopCreationEvent event = new PreShopCreationEvent(null, null, getPriceString("B 1"));
        onPreShopCreation(event);
        assertFalse(event.isCancelled());

        event = new PreShopCreationEvent(null, null, getPriceString("S 1"));
        onPreShopCreation(event);
        assertFalse(event.isCancelled());

        event = new PreShopCreationEvent(null, null, getPriceString("B 1:S 1"));
        onPreShopCreation(event);
        assertFalse(event.isCancelled());

        event = new PreShopCreationEvent(null, null, getPriceString("BS 1"));
        onPreShopCreation(event);
        assertTrue(event.isCancelled());

        event = new PreShopCreationEvent(null, null, getPriceString("B 1S0"));
        onPreShopCreation(event);
        assertTrue(event.isCancelled());
        
        event = new PreShopCreationEvent(null, null, getPriceString("B 5 : S 5 : L 64 : P 24"));
        onPreShopCreation(event);
        assertFalse(event.isCancelled());

        String priceString = "5 B 5";
        assertTrue(PriceUtil.getBuyPrice(priceString) == PriceUtil.NO_PRICE);

        priceString = "5 S 5";
        assertTrue(PriceUtil.getSellPrice(priceString) == PriceUtil.NO_PRICE);

        priceString = "5 B 5:5 S 5";
        assertTrue(PriceUtil.getBuyPrice(priceString) == PriceUtil.NO_PRICE);
        assertTrue(PriceUtil.getSellPrice(priceString) == PriceUtil.NO_PRICE);
        
        priceString = "B5:S5:L64:P24";
        assertTrue(PriceUtil.getBuyPrice(priceString) == 5);
        assertTrue(PriceUtil.getSellPrice(priceString) == 5);
        assertTrue(PriceUtil.getLimit(priceString) == 64);
        assertTrue(PriceUtil.getLimitPeriod(priceString) == 24 * 60 * 60 * 1000);
        
        String[] lines = new String[]{"Admin Shop", "1", "B20:L10:P1", "Wood"};
        assertTrue(ChestShopSign.isValidPreparedSign(lines));
        
        lines = new String[]{"Admin Shop", "1", "B 20:L 10:P 1", "Wood"};
        assertTrue(ChestShopSign.isValidPreparedSign(lines));
    }
}
