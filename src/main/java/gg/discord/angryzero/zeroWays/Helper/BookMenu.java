package gg.discord.angryzero.zeroWays.Helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * A reusable class to create interactive book menus.
 */
public class BookMenu {

    private final ItemStack book;
    private final BookMeta meta;
    private final List<BaseComponent[]> pages = new ArrayList<>();

    public BookMenu() {
        this.book = new ItemStack(Material.WRITTEN_BOOK);
        this.meta = (BookMeta) book.getItemMeta();
    }

    /**
     * Sets the title of the book.
     * @param title The title to set.
     * @return The current BookMenu instance.
     */
    public BookMenu setTitle(String title) {
        meta.setTitle(title);
        return this;
    }

    /**
     * Sets the author of the book.
     * @param author The author to set.
     * @return The current BookMenu instance.
     */
    public BookMenu setAuthor(String author) {
        meta.setAuthor(author);
        return this;
    }

    /**
     * Adds a page to the book from a ComponentBuilder.
     * @param pageBuilder The ComponentBuilder containing the page content.
     * @return The current BookMenu instance.
     */
    public BookMenu addPage(ComponentBuilder pageBuilder) {
        this.pages.add(pageBuilder.create());
        return this;
    }

    /**
     * Opens the finished book for a specific player.
     * @param player The player to open the book for.
     */
    public void open(Player player) {
        meta.spigot().setPages(pages);
        book.setItemMeta(meta);
        player.openBook(book);
    }

    /**
     * A helper method to easily create a clickable text component.
     * @param text The visible text.
     * @param command The command to run on click (without the '/').
     * @param hoverText The text to show on hover.
     * @return A configured TextComponent.
     */
    public static TextComponent createClickableText(String text, String command, String hoverText) {
        TextComponent component = new TextComponent(text);
        component.setColor(ChatColor.DARK_GREEN);

        // Add click event
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));

        // Add hover event
        if (hoverText != null && !hoverText.isEmpty()) {
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(hoverText).create()));
        }

        return component;
    }
}