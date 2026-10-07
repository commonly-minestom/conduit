package net.minestom.conduit.economy;

import java.util.UUID;

/**
 * Contract for an economy.
 */
public interface EconomyProvider {
    /**
     * Gets the balance of a player.
     * @param player the player to check
     * @return the balance, or 0 if the player has no account
     */
    double balance(UUID player);

    /**
     * Checks if a player has at least the given amount.
     * @param player the player to check
     * @param amount the amount to check
     * @return true if the balance covers the amount, false otherwise
     */
    default boolean has(UUID player, double amount) {
        return balance(player) >= amount;
    }

    /**
     * Deposits an amount to a player.
     * @param player the player to deposit to
     * @param amount the amount to deposit, must be valid
     * @return true if the deposit succeeded, false otherwise
     */
    boolean deposit(UUID player, double amount);

    /**
     * Withdraws an amount from a player.
     * @param player the player to withdraw from
     * @param amount the amount to withdraw, must be valid
     * @return true if the withdraw succeeded, false on insufficient funds or invalid amount
     */
    boolean withdraw(UUID player, double amount);

    /**
     * Transfers an amount between players.
     * @param from the player to withdraw from
     * @param to the player to deposit to
     * @param amount the amount to transfer, must be valid
     * @return true if the transfer succeeded, false otherwise
     */
    default boolean transfer(UUID from, UUID to, double amount) {
        if (!withdraw(from, amount)) {
            return false;
        }
        return deposit(to, amount);
    }

    /**
     * Sets the balance of a player.
     * @param player the player to set the balance for
     * @param amount the new balance, must be valid
     * @return true if the balance was set, false otherwise
     */
    boolean set(UUID player, double amount);
}
