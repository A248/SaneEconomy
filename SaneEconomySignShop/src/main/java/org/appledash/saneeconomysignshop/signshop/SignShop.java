package org.appledash.saneeconomysignshop.signshop;

import org.appledash.saneeconomy.economy.Currency;
import org.appledash.saneeconomysignshop.signshop.ShopTransaction.TransactionDirection;
import org.appledash.saneeconomysignshop.util.ItemInfo;
import org.appledash.saneeconomysignshop.util.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by appledash on 10/2/16.
 * Blackjack is still best pony.
 */
public class SignShop implements Serializable {
    private final UUID ownerUuid;
    private final SerializableLocation location;
    private final ItemInfo item;
    private final int quantity;
    private final BigDecimal buyPrice;
    private final BigDecimal sellPrice;

    public SignShop(UUID ownerUuid, Location location, ItemStack item, int quantity, double buyPrice, double sellPrice) {
        if ((ownerUuid == null) || (location == null) || (item == null)) {
            throw new IllegalArgumentException("ownerUuid, location, and item must not be null.");
        }

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        this.ownerUuid = ownerUuid;
        this.location = new SerializableLocation(location);
        this.item = new ItemInfo(item);
        this.quantity = quantity;
        this.buyPrice = BigDecimal.valueOf(buyPrice);
        this.sellPrice = BigDecimal.valueOf(sellPrice);
    }

    /**
     * Get the in-world Location of this SignShop
     * @return Location
     */
    public Location getLocation() {
        return this.location.getBukkitLocation();
    }

    /**
     * Get the type of item this SignShop is selling
     * @return Material representing item/block type
     */
    public ItemStack getItemStack() {
        return this.item.toItemStack();
    }

    /**
     * Get the ItemInfo for the item this SignShop is selling
     * @return ItemInfo representing the type and quantity of item
     */
    public ItemInfo getItem() {
        return this.item;
    }

    /**
     * Get the price that the player can buy this item from the server for
     * @return Buy price for this.getQuantity() items
     */
    public BigDecimal getBuyPrice() {
        return this.buyPrice;
    }

    /**
     * Get the price that the player can sell this item to the server for
     * @return Buy price for this.getQuantity() items
     */
    public BigDecimal getSellPrice() {
        return this.sellPrice;
    }

    /**
     * Get the price that the player can buy a specific number of this item from the server for.
     * Scales based on the defined price for the defined quantity.
     * @param quantity Quantity of items to price
     * @return Price to buy that number of items at this shop
     */
    public BigDecimal getBuyPrice(int quantity) {
        return this.buyPrice.multiply(BigDecimal.valueOf((double) quantity / this.quantity)); // TODO: Is this okay?
    }

    /**
     * Get the price that the player can sell a specific number of this item to the server for.
     * Scales based on the defined price for the defined quantity.
     * @param quantity Quantity of items to price
     * @return Price to sell that number of items at this shop
     */
    public BigDecimal getSellPrice(int quantity) {
        return this.sellPrice.multiply(BigDecimal.valueOf((double) quantity / this.quantity)); // TODO: Is this okay?
    }

    /**
     * Check if anyone can buy items from this shop
     * @return True if they can, false if they can't
     */
    public boolean canBuy() {
        return this.buyPrice.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Check if anyone can sell items to this shop
     * @return True if they can, false if they can't
     */
    public boolean canSell() {
        return this.sellPrice.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Get the UUID of the player that created this SignShop
     * @return UUID
     */
    public UUID getOwnerUuid() {
        return this.ownerUuid;
    }

    /**
     * Get the number of items that this shop will sell
     * @return Number of items
     */
    public int getQuantity() {
        return this.quantity;
    }

    public ShopTransaction makeTransaction(Currency currency, Player player, TransactionDirection direction, int quantity) {
        return new ShopTransaction(currency, direction, player, this.item, quantity, (direction == TransactionDirection.BUY) ? this.getBuyPrice(quantity) : this.getSellPrice(quantity));
    }
}
