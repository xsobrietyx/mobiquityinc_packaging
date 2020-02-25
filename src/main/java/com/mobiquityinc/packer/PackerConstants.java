package com.mobiquityinc.packer;

/**
 * Class that holds some necessary business related constants.
 * Previously was implemented as Interface with constants. Previous approach is anti pattern
 * because interface declares type, behaviour. It should have "is a" relationship with a class that implements it.
 */
public final class PackerConstants {
    private PackerConstants() {
    }

    public static int MAX_PACKAGE_WEIGHT = 100;
    public static int MAX_ITEM_WEIGHT = 100;
    public static int MAX_ITEM_COST = 100;
    public static int MAX_ITEMS_AMOUNT = 15;
}
