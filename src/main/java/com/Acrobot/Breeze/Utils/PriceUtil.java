package com.Acrobot.Breeze.Utils;

/**
 * @author Acrobot
 */
public class PriceUtil {
    public static final double NO_PRICE = -1;
    public static final double FREE = 0;

    public static final String FREE_TEXT = "free";

    public static final char BUY_INDICATOR = 'b';
    public static final char SELL_INDICATOR = 's';
    public static final char LIMIT_INDICATOR = 'l';
    public static final char LIMIT_PERIOD_INDICATOR = 'p';

    /**
     * Gets the price from the text
     *
     * @param text      Text to check
     * @param indicator Price indicator (for example, B for buy)
     * @return price
     */
    public static double get(String text, char indicator) {
        String[] split = text.replace(" ", "").toLowerCase().split(":");
        String character = String.valueOf(indicator).toLowerCase();

        for (String part : split) {
            if (!part.startsWith(character) && !part.endsWith(character)) {
                continue;
            }

            part = part.replace(character, "");

            if (part.equals(FREE_TEXT)) {
                return FREE;
            }

            if (NumberUtil.isDouble(part)) {
                double price = Double.valueOf(part);

                if (Double.isInfinite(price) || price <= 0) {
                    return NO_PRICE;
                } else {
                    return price;
                }
            }
        }

        return NO_PRICE;
    }

    /**
     * Gets the buy price from the text
     *
     * @param text Text to check
     * @return Buy price
     */
    public static double getBuyPrice(String text) {
        return get(text, BUY_INDICATOR);
    }

    /**
     * Gets the sell price from the text
     *
     * @param text Text to check
     * @return Sell price
     */
    public static double getLimit(String text) {
        return get(text, LIMIT_INDICATOR);
    }
    
    /**
     * Gets the buy price from te text
     *
     * @param text Text to check
     * @return Buy price
     */
    public static long getLimitPeriod(String text) {
    	long result = (long)get(text, LIMIT_PERIOD_INDICATOR);
    	if(result > 0) {
    		result = result * 60 * 60 * 1000;
    	}
        return result;
    }

    /**
     * Gets the sell price from te text
     *
     * @param text Text to check
     * @return Sell price
     */
    public static double getSellPrice(String text) {
        return get(text, SELL_INDICATOR);
    }

    /**
     * Tells if there is a buy price
     *
     * @param text Price text
     * @return If there is a buy price
     */
    public static boolean hasBuyPrice(String text) {
        return hasPrice(text, BUY_INDICATOR);
    }

    /**
     * Tells if there is a sell price
     *
     * @param text Price text
     * @return If there is a sell price
     */
    public static boolean hasSellPrice(String text) {
        return hasPrice(text, SELL_INDICATOR);
    }
    
    /**
     * Tells if there is a limit
     *
     * @param text Price text
     * @return If there is a buy limit
     */
    public static boolean hasBuyLimit(String text) {
        return hasPrice(text, LIMIT_INDICATOR);
    }

    /**
     * Tells if there is a limit period
     *
     * @param text Price text
     * @return If there is a sell price
     */
    public static boolean hasLimitPeriod(String text) {
        return hasPrice(text, LIMIT_PERIOD_INDICATOR);
    }

    /**
     * Tells if there is a price with the specified indicator
     *
     * @param text      Price text
     * @param indicator Price indicator
     * @return If the text contains indicated price
     */
    public static boolean hasPrice(String text, char indicator) {
        return get(text, indicator) != NO_PRICE;
    }

    /**
     * Checks if the string is a valid price
     *
     * @param text Text to check
     * @return Is the string a valid price
     */
    public static boolean isPrice(String text) {
        if (NumberUtil.isDouble(text)) {
            return true;
        }

        return text.trim().equalsIgnoreCase(FREE_TEXT);
    }
}

