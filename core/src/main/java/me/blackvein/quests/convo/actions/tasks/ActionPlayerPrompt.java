/*
 * Copyright (c) 2014 PikaMug and contributors. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package me.blackvein.quests.convo.actions.tasks;

import me.blackvein.quests.Quests;
import me.blackvein.quests.convo.actions.ActionsEditorNumericPrompt;
import me.blackvein.quests.convo.actions.ActionsEditorStringPrompt;
import me.blackvein.quests.convo.actions.main.ActionMainPrompt;
import me.blackvein.quests.convo.generic.ItemStackPrompt;
import me.blackvein.quests.events.editor.actions.ActionsEditorPostOpenNumericPromptEvent;
import me.blackvein.quests.events.editor.actions.ActionsEditorPostOpenStringPromptEvent;
import me.blackvein.quests.util.CK;
import me.blackvein.quests.util.ConfigUtil;
import me.blackvein.quests.util.ItemUtil;
import me.blackvein.quests.util.Lang;
import me.blackvein.quests.util.MiscUtil;
import me.blackvein.quests.util.RomanNumeral;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ActionPlayerPrompt extends ActionsEditorNumericPrompt {
    
    private final Quests plugin;
    
    public ActionPlayerPrompt(final ConversationContext context) {
        super(context);
        this.plugin = (Quests)context.getPlugin();
    }
    
    private final int size = 10;
    
    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public String getTitle(final ConversationContext context) {
        return Lang.get("eventEditorPlayer");
    }
    
    @Override
    public ChatColor getNumberColor(final ConversationContext context, final int number) {
        switch (number) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
            return ChatColor.BLUE;
        case 10:
            return ChatColor.GREEN;
        default:
            return null;
        }
    }
    
    @Override
    public String getSelectionText(final ConversationContext context, final int number) {
        switch (number) {
        case 1:
            return ChatColor.YELLOW + Lang.get("eventEditorSetMessage");
        case 2:
            return ChatColor.YELLOW + Lang.get("eventEditorSetItems");
        case 3:
            return ChatColor.YELLOW + Lang.get("eventEditorSetPotionEffects");
        case 4:
            return ChatColor.YELLOW + Lang.get("eventEditorSetHunger");
        case 5:
            return ChatColor.YELLOW + Lang.get("eventEditorSetSaturation");
        case 6:
            return ChatColor.YELLOW + Lang.get("eventEditorSetHealth");
        case 7:
            return ChatColor.YELLOW + Lang.get("eventEditorSetTeleport");
        case 8:
            return ChatColor.YELLOW + Lang.get("eventEditorSetCommands");
        case 9:
            return ChatColor.YELLOW + Lang.get("eventEditorClearInv");
        case 10:
            return ChatColor.GREEN + Lang.get("done");
        default:
            return null;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public String getAdditionalText(final ConversationContext context, final int number) {
        switch (number) {
        case 1:
            if (context.getSessionData(CK.E_MESSAGE) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                return ChatColor.GRAY + "(" + ChatColor.AQUA + context.getSessionData(CK.E_MESSAGE) + ChatColor.GRAY + ")";
            }
        case 2:
            if (context.getSessionData(CK.E_ITEMS) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                final StringBuilder text = new StringBuilder();
                final LinkedList<ItemStack> items = (LinkedList<ItemStack>) context.getSessionData(CK.E_ITEMS);
                if (items != null) {
                    for (final ItemStack is : items) {
                        if (is != null) {
                            text.append("\n").append(ChatColor.GRAY).append("     - ").append(ItemUtil.getString(is));
                        }
                    }
                    return text.toString();
                }
            }
        case 3:
            if (context.getSessionData(CK.E_POTION_TYPES) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                final StringBuilder text = new StringBuilder();
                final LinkedList<String> types = (LinkedList<String>) context.getSessionData(CK.E_POTION_TYPES);
                final LinkedList<Long> durations = (LinkedList<Long>) context.getSessionData(CK.E_POTION_DURATIONS);
                final LinkedList<Integer> mags = (LinkedList<Integer>) context.getSessionData(CK.E_POTION_STRENGTH);
                int index = -1;
                if (types != null && durations != null && mags != null) {
                    for (final String type : types) {
                        index++;
                        text.append("\n").append(ChatColor.GRAY).append("     - ").append(ChatColor.AQUA).append(type)
                                .append(ChatColor.DARK_PURPLE).append(" ").append(RomanNumeral.getNumeral(mags
                                .get(index))).append(ChatColor.GRAY).append(" -> ").append(ChatColor.DARK_AQUA)
                                .append(MiscUtil.getTime(durations.get(index) * 50L));
                    }
                }
                return text.toString();
            }
        case 4:
            if (context.getSessionData(CK.E_HUNGER) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                return ChatColor.GRAY + "(" + ChatColor.AQUA + context.getSessionData(CK.E_HUNGER) + ChatColor.GRAY 
                        + ")";
            }
        case 5:
            if (context.getSessionData(CK.E_SATURATION) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                return ChatColor.GRAY + "(" + ChatColor.AQUA + context.getSessionData(CK.E_SATURATION) + ChatColor.GRAY 
                        + ")";
            }
        case 6:
            if (context.getSessionData(CK.E_HEALTH) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                return ChatColor.GRAY + "(" + ChatColor.AQUA + context.getSessionData(CK.E_HEALTH) + ChatColor.GRAY 
                        + ")";
            }
        case 7:
            if (context.getSessionData(CK.E_TELEPORT) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                return ChatColor.GRAY + "(" + ChatColor.AQUA + context.getSessionData(CK.E_TELEPORT) + ChatColor.GRAY 
                        + ")";
            }
        case 8:
            if (context.getSessionData(CK.E_COMMANDS) == null) {
                return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
            } else {
                final StringBuilder text = new StringBuilder();
                for (final String s : (LinkedList<String>) Objects.requireNonNull(context
                        .getSessionData(CK.E_COMMANDS))) {
                    text.append("\n").append(ChatColor.GRAY).append("     - ").append(ChatColor.AQUA).append(s);
                }
                return text.toString();
            }
        case 9:
            if (context.getSessionData(CK.E_CLEAR_INVENTORY) == null) {
                return ChatColor.GRAY + "(" + ChatColor.RED + Lang.get("false") + ChatColor.GRAY + ")";
            } else {
                final Boolean clearOpt = (Boolean) context.getSessionData(CK.E_CLEAR_INVENTORY);
                return ChatColor.GRAY + "(" + (Boolean.TRUE.equals(clearOpt) ? ChatColor.GREEN + Lang.get("true")
                        : ChatColor.RED + Lang.get("false")) + ChatColor.GRAY + ")";
            }
        case 10:
            return "";
        default:
            return null;
        }
    }

    @Override
    public @NotNull String getBasicPromptText(final ConversationContext context) {
        if (context.getSessionData(CK.E_CLEAR_INVENTORY) == null) {
            context.setSessionData(CK.E_CLEAR_INVENTORY, false);
        }
        
        final ActionsEditorPostOpenNumericPromptEvent event
                = new ActionsEditorPostOpenNumericPromptEvent(context, this);
        plugin.getServer().getPluginManager().callEvent(event);

        final StringBuilder text = new StringBuilder(ChatColor.GOLD + "- " + getTitle(context) + " -");
        for (int i = 1; i <= size; i++) {
            text.append("\n").append(getNumberColor(context, i)).append(ChatColor.BOLD).append(i)
                    .append(ChatColor.RESET).append(" - ").append(getSelectionText(context, i)).append(" ")
                    .append(getAdditionalText(context, i));
        }
        return text.toString();
    }

    @Override
    protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final Number input) {
        switch (input.intValue()) {
        case 1:
            return new ActionPlayerMessagePrompt(context);
        case 2:
            return new ActionPlayerItemListPrompt(context);
        case 3:
            return new ActionPlayerPotionListPrompt(context);
        case 4:
            return new ActionPlayerHungerPrompt(context);
        case 5:
            return new ActionPlayerSaturationPrompt(context);
        case 6:
            return new ActionPlayerHealthPrompt(context);
        case 7:
            if (context.getForWhom() instanceof Player) {
                final Map<UUID, Block> selectedTeleportLocations = plugin.getActionFactory().getSelectedTeleportLocations();
                selectedTeleportLocations.put(((Player) context.getForWhom()).getUniqueId(), null);
                plugin.getActionFactory().setSelectedTeleportLocations(selectedTeleportLocations);
                return new ActionPlayerTeleportPrompt(context);
            } else {
                context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("consoleError"));
                return new ActionPlayerPrompt(context);
            }
        case 8:
            if (!plugin.hasLimitedAccess(context.getForWhom())) {
                return new ActionPlayerCommandsPrompt(context);
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("noPermission"));
                return new ActionPlayerPrompt(context);
            }
        case 9:
            final Boolean b = (Boolean) context.getSessionData(CK.E_CLEAR_INVENTORY);
            if (Boolean.TRUE.equals(b)) {
                context.setSessionData(CK.E_CLEAR_INVENTORY, false);
            } else {
                context.setSessionData(CK.E_CLEAR_INVENTORY, true);
            }
            return new ActionPlayerPrompt(context);
        case 10:
            return new ActionMainPrompt(context);
        default:
            return new ActionPlayerPrompt(context);
        }
    }
    
    public class ActionPlayerMessagePrompt extends ActionsEditorStringPrompt {

        public ActionPlayerMessagePrompt(final ConversationContext context) {
            super(context);
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetMessagePrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdCancel")) && !input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                context.setSessionData(CK.E_MESSAGE, input);
            } else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                context.setSessionData(CK.E_MESSAGE, null);
            }
            return new ActionMainPrompt(context);
        }
    }

    public class ActionPlayerItemListPrompt extends ActionsEditorNumericPrompt {

        public ActionPlayerItemListPrompt(final ConversationContext context) {
            super(context);
        }
        
        private final int size = 3;
        
        @Override
        public int getSize() {
            return size;
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return Lang.get("eventEditorGiveItemsTitle");
        }
        
        @Override
        public ChatColor getNumberColor(final ConversationContext context, final int number) {
            switch (number) {
            case 1:
            case 2:
                return ChatColor.BLUE;
            case 3:
                return ChatColor.GREEN;
            default:
                return null;
            }
        }
        
        @Override
        public String getSelectionText(final ConversationContext context, final int number) {
            switch (number) {
            case 1:
                return ChatColor.YELLOW + Lang.get("stageEditorDeliveryAddItem");
            case 2:
                return ChatColor.RED + Lang.get("clear");
            case 3:
                return ChatColor.GREEN + Lang.get("done");
            default:
                return null;
            }
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public String getAdditionalText(final ConversationContext context, final int number) {
            switch(number) {
            case 1:
                if (context.getSessionData(CK.E_ITEMS) == null) {
                    return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
                } else {
                    final StringBuilder text = new StringBuilder();
                    for (final ItemStack is : (List<ItemStack>) Objects.requireNonNull(context
                            .getSessionData(CK.E_ITEMS))) {
                        text.append("\n").append(ChatColor.GRAY).append("     - ")
                                .append(ItemUtil.getDisplayString(is));
                    }
                    return text.toString();
                }
            case 2:
            case 3:
                return "";
            default:
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull String getBasicPromptText(final ConversationContext context) {
            // Check/add newly made item
            if (context.getSessionData("tempStack") != null) {
                if (context.getSessionData(CK.E_ITEMS) != null) {
                    final List<ItemStack> items = (List<ItemStack>) context.getSessionData(CK.E_ITEMS);
                    if (items != null) {
                        items.add((ItemStack) context.getSessionData("tempStack"));
                        context.setSessionData(CK.E_ITEMS, items);
                    }
                } else {
                    final LinkedList<ItemStack> itemRewards = new LinkedList<>();
                    itemRewards.add((ItemStack) context.getSessionData("tempStack"));
                    context.setSessionData(CK.E_ITEMS, itemRewards);
                }
                ItemStackPrompt.clearSessionData(context);
            }
            
            final ActionsEditorPostOpenNumericPromptEvent event
                    = new ActionsEditorPostOpenNumericPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            
            final StringBuilder text = new StringBuilder(ChatColor.GOLD + getTitle(context));
            for (int i = 1; i <= size; i++) {
                text.append("\n").append(getNumberColor(context, i)).append(ChatColor.BOLD).append(i)
                        .append(ChatColor.RESET).append(" - ").append(getSelectionText(context, i)).append(" ")
                        .append(getAdditionalText(context, i));
            }
            return text.toString();
        }

        @Override
        protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final Number input) {
            switch(input.intValue()) {
            case 1:
                return new ItemStackPrompt(context, ActionPlayerItemListPrompt.this);
            case 2:
                context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("eventEditorItemsCleared"));
                context.setSessionData(CK.E_ITEMS, null);
                return new ActionPlayerItemListPrompt(context);
            case 3:
                return new ActionMainPrompt(context);
            default:
                return new ActionPlayerItemListPrompt(context);
            }
        }
    }
    
    public class ActionPlayerPotionListPrompt extends ActionsEditorNumericPrompt {

        public ActionPlayerPotionListPrompt(final ConversationContext context) {
            super(context);
        }
        
        private final int size = 5;
        
        @Override
        public int getSize() {
            return size;
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return Lang.get("eventEditorPotionEffectsTitle");
        }
        
        @Override
        public ChatColor getNumberColor(final ConversationContext context, final int number) {
            switch (number) {
            case 1:
            case 2:
            case 3:
                return ChatColor.BLUE;
            case 4:
                return ChatColor.RED;
            case 5:
                return ChatColor.GREEN;
            default:
                return null;
            }
        }
        
        @Override
        public String getSelectionText(final ConversationContext context, final int number) {
            switch (number) {
            case 1:
                return ChatColor.YELLOW + Lang.get("eventEditorSetPotionEffectTypes");
            case 2:
                return ChatColor.YELLOW + Lang.get("eventEditorSetPotionDurations");
            case 3:
                return ChatColor.YELLOW + Lang.get("eventEditorSetPotionMagnitudes");
            case 4:
                return ChatColor.RED + Lang.get("clear");
            case 5:
                return ChatColor.GREEN + Lang.get("done");
            default:
                return null;
            }
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public String getAdditionalText(final ConversationContext context, final int number) {
            switch (number) {
            case 1:
                if (context.getSessionData(CK.E_POTION_TYPES) == null) {
                    return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
                } else {
                    final StringBuilder text = new StringBuilder();
                    for (final String s : (LinkedList<String>) Objects.requireNonNull(context
                            .getSessionData(CK.E_POTION_TYPES))) {
                        text.append("\n").append(ChatColor.GRAY).append("     - ").append(ChatColor.AQUA).append(s);
                    }
                    return text.toString();
                }
            case 2:
                if (context.getSessionData(CK.E_POTION_DURATIONS) == null) {
                    return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
                } else {
                    final StringBuilder text = new StringBuilder();
                    for (final Long l : (LinkedList<Long>) Objects.requireNonNull(context
                            .getSessionData(CK.E_POTION_DURATIONS))) {
                        text.append("\n").append(ChatColor.GRAY).append("     - ").append(ChatColor.DARK_AQUA)
                                .append(MiscUtil.getTime(l * 50L));
                    }
                    return text.toString();
                }
            case 3:
                if (context.getSessionData(CK.E_POTION_STRENGTH) == null) {
                    return ChatColor.GRAY + "(" + Lang.get("noneSet") + ")";
                } else {
                    final StringBuilder text = new StringBuilder();
                    for (final int i : (LinkedList<Integer>) Objects.requireNonNull(context
                            .getSessionData(CK.E_POTION_STRENGTH))) {
                        text.append("\n").append(ChatColor.GRAY).append("     - ").append(ChatColor.DARK_PURPLE)
                                .append(i);
                    }
                    return text.toString();
                }
            case 4:
            case 5:
                return "";
            default:
                return null;
            }
        }

        @Override
        public @NotNull String getBasicPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenNumericPromptEvent event
                    = new ActionsEditorPostOpenNumericPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            final StringBuilder text = new StringBuilder(ChatColor.GOLD + getTitle(context));
            for (int i = 1; i <= size; i++) {
                text.append("\n").append(getNumberColor(context, i)).append(ChatColor.BOLD).append(i)
                        .append(ChatColor.RESET).append(" - ").append(getSelectionText(context, i)).append(" ")
                        .append(getAdditionalText(context, i));
            }
            return text.toString();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final Number input) {
            switch (input.intValue()) {
            case 1:
                return new ActionPlayerPotionTypesPrompt(context);
            case 2:
                if (context.getSessionData(CK.E_POTION_TYPES) == null) {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("eventEditorMustSetPotionTypesFirst"));
                    return new ActionPlayerPotionListPrompt(context);
                } else {
                    return new ActionPlayerPotionDurationsPrompt(context);
                }
            case 3:
                if (context.getSessionData(CK.E_POTION_TYPES) == null) {
                    context.getForWhom().sendRawMessage(ChatColor.RED
                            + Lang.get("eventEditorMustSetPotionTypesAndDurationsFirst"));
                    return new ActionPlayerPotionListPrompt(context);
                } else if (context.getSessionData(CK.E_POTION_DURATIONS) == null) {
                    context.getForWhom().sendRawMessage(ChatColor.RED
                            + Lang.get("eventEditorMustSetPotionDurationsFirst"));
                    return new ActionPlayerPotionListPrompt(context);
                } else {
                    return new ActionPlayerPotionMagnitudesPrompt(context);
                }
            case 4:
                context.getForWhom().sendRawMessage(ChatColor.YELLOW + Lang.get("eventEditorPotionsCleared"));
                context.setSessionData(CK.E_POTION_TYPES, null);
                context.setSessionData(CK.E_POTION_DURATIONS, null);
                context.setSessionData(CK.E_POTION_STRENGTH, null);
                return new ActionPlayerPotionListPrompt(context);
            case 5:
                final int one;
                final int two;
                final int three;
                final List<String> types = (List<String>) context.getSessionData(CK.E_POTION_TYPES);
                final List<Long> durations = (List<Long>) context.getSessionData(CK.E_POTION_DURATIONS);
                final List<Integer> strength = (List<Integer>) context.getSessionData(CK.E_POTION_STRENGTH);
                if (types != null) {
                    one = types.size();
                } else {
                    one = 0;
                }
                if (durations != null) {
                    two = durations.size();
                } else {
                    two = 0;
                }
                if (strength != null) {
                    three = strength.size();
                } else {
                    three = 0;
                }
                if (one == two && two == three) {
                    return new ActionMainPrompt(context);
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("eventEditorListSizeMismatch"));
                    return new ActionPlayerPotionListPrompt(context);
                }
            default:
                return new ActionPlayerPotionListPrompt(context);
            }
        }
    }

    public class ActionPlayerPotionTypesPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerPotionTypesPrompt(final ConversationContext context) {
            super(context);
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return Lang.get("eventEditorPotionTypesTitle");
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetPotionEffectsPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);

            final StringBuilder potions = new StringBuilder(ChatColor.LIGHT_PURPLE + getTitle(context) + "\n");
            final PotionEffectType[] potionArr = PotionEffectType.values();
            for (int i = 0; i < potionArr.length; i++) {
                potions.append(ChatColor.AQUA).append(potionArr[i].getName());
                if (i < (potionArr.length - 1)) {
                    potions.append(ChatColor.GRAY).append(", ");
                }
            }
            potions.append("\n").append(ChatColor.YELLOW).append(getQueryText(context));
            return potions.toString();
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdCancel"))) {
                final LinkedList<String> effTypes = new LinkedList<>();
                for (final String s : input.split(" ")) {
                    if (PotionEffectType.getByName(s.toUpperCase()) != null) {
                        effTypes.add(Objects.requireNonNull(PotionEffectType.getByName(s.toUpperCase())).getName());
                        context.setSessionData(CK.E_POTION_TYPES, effTypes);
                    } else {
                        context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("eventEditorInvalidPotionType")
                                .replace("<input>", s));
                        return new ActionPlayerPotionTypesPrompt(context);
                    }
                }
            }
            return new ActionPlayerPotionListPrompt(context);
        }
    }

    public class ActionPlayerPotionDurationsPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerPotionDurationsPrompt(final ConversationContext context) {
            super(context);
        }

        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetPotionDurationsPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdCancel"))) {
                final LinkedList<Long> effDurations = new LinkedList<>();
                for (final String s : input.split(" ")) {
                    try {
                        final int i = Integer.parseInt(s);
                        final long l = i * 1000L;
                        if (l < 1000) {
                            context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("invalidMinimum")
                                    .replace("<number>", "1"));
                            return new ActionPlayerPotionDurationsPrompt(context);
                        }
                        effDurations.add(l / 50L);
                    } catch (final NumberFormatException e) {
                        context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqNotANumber")
                                .replace("<input>", s));
                        return new ActionPlayerPotionDurationsPrompt(context);
                    }
                }
                context.setSessionData(CK.E_POTION_DURATIONS, effDurations);
            }
            return new ActionPlayerPotionListPrompt(context);
        }
    }

    public class ActionPlayerPotionMagnitudesPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerPotionMagnitudesPrompt(final ConversationContext context) {
            super(context);
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetPotionMagnitudesPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdCancel"))) {
                final LinkedList<Integer> magAmounts = new LinkedList<>();
                for (final String s : input.split(" ")) {
                    try {
                        final int i = Integer.parseInt(s);
                        if (i < 1) {
                            context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("invalidMinimum")
                                    .replace("<number>", "1"));
                            return new ActionPlayerPotionMagnitudesPrompt(context);
                        }
                        magAmounts.add(i);
                    } catch (final NumberFormatException e) {
                        context.getForWhom().sendRawMessage(ChatColor.RED + Lang.get("reqNotANumber")
                                .replace("<input>", s));
                        return new ActionPlayerPotionMagnitudesPrompt(context);
                    }
                }
                context.setSessionData(CK.E_POTION_STRENGTH, magAmounts);
            }
            return new ActionPlayerPotionListPrompt(context);
        }
    }
    
    public class ActionPlayerHungerPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerHungerPrompt(final ConversationContext context) {
            super(context);
        }

        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetHungerPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                try {
                    final int i = Integer.parseInt(input);
                    if (i < 0) {
                        context.getForWhom().sendRawMessage(ChatColor.RED
                                + Lang.get("invalidMinimum").replace("<number>", "0"));
                        return new ActionPlayerHungerPrompt(context);
                    } else {
                        context.setSessionData(CK.E_HUNGER, i);
                    }
                } catch (final NumberFormatException e) {
                    context.getForWhom().sendRawMessage(ChatColor.RED
                            + Lang.get("reqNotANumber").replace("<input>", input));
                    return new ActionPlayerHungerPrompt(context);
                }
            } else {
                context.setSessionData(CK.E_HUNGER, null);
            }
            return new ActionMainPrompt(context);
        }
    }

    public class ActionPlayerSaturationPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerSaturationPrompt(final ConversationContext context) {
            super(context);
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetSaturationPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                try {
                    final int i = Integer.parseInt(input);
                    if (i < 0) {
                        context.getForWhom().sendRawMessage(ChatColor.RED
                                + Lang.get("invalidMinimum").replace("<number>", "0"));
                        return new ActionPlayerSaturationPrompt(context);
                    } else {
                        context.setSessionData(CK.E_SATURATION, i);
                    }
                } catch (final NumberFormatException e) {
                    context.getForWhom().sendRawMessage(ChatColor.RED
                            + Lang.get("reqNotANumber").replace("<input>", input));
                    return new ActionPlayerSaturationPrompt(context);
                }
            } else {
                context.setSessionData(CK.E_SATURATION, null);
            }
            return new ActionMainPrompt(context);
        }
    }

    public class ActionPlayerHealthPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerHealthPrompt(final ConversationContext context) {
            super(context);
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetHealthPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                try {
                    final int i = Integer.parseInt(input);
                    if (i < 0) {
                        context.getForWhom().sendRawMessage(ChatColor.RED
                                + Lang.get("invalidMinimum").replace("<number>", "0"));
                        return new ActionPlayerHealthPrompt(context);
                    } else {
                        context.setSessionData(CK.E_HEALTH, i);
                    }
                } catch (final NumberFormatException e) {
                    context.getForWhom().sendRawMessage(ChatColor.RED
                            + Lang.get("reqNotANumber").replace("<input>", input));
                    return new ActionPlayerHealthPrompt(context);
                }
            } else {
                context.setSessionData(CK.E_HEALTH, null);
            }
            return new ActionMainPrompt(context);
        }
    }
    
    public class ActionPlayerTeleportPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerTeleportPrompt(final ConversationContext context) {
            super(context);
        }

        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetTeleportPrompt");
        }
        
        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            final Player player = (Player) context.getForWhom();
            if (input.equalsIgnoreCase(Lang.get("cmdDone"))) {
                final Map<UUID, Block> selectedTeleportLocations = plugin.getActionFactory()
                        .getSelectedTeleportLocations();
                final Block block = selectedTeleportLocations.get(player.getUniqueId());
                if (block != null) {
                    final Location loc = block.getLocation();
                    context.setSessionData(CK.E_TELEPORT, ConfigUtil.getLocationInfo(loc));
                    selectedTeleportLocations.remove(player.getUniqueId());
                    plugin.getActionFactory().setSelectedTeleportLocations(selectedTeleportLocations);
                } else {
                    player.sendMessage(ChatColor.RED + Lang.get("eventEditorSelectBlockFirst"));
                    return new ActionPlayerTeleportPrompt(context);
                }
                return new ActionMainPrompt(context);
            } else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                context.setSessionData(CK.E_TELEPORT, null);
                final Map<UUID, Block> selectedTeleportLocations = plugin.getActionFactory()
                        .getSelectedTeleportLocations();
                selectedTeleportLocations.remove(player.getUniqueId());
                plugin.getActionFactory().setSelectedTeleportLocations(selectedTeleportLocations);
                return new ActionMainPrompt(context);
            } else if (input.equalsIgnoreCase(Lang.get("cmdCancel"))) {
                final Map<UUID, Block> selectedTeleportLocations = plugin.getActionFactory()
                        .getSelectedTeleportLocations();
                selectedTeleportLocations.remove(player.getUniqueId());
                plugin.getActionFactory().setSelectedTeleportLocations(selectedTeleportLocations);
                return new ActionMainPrompt(context);
            } else {
                return new ActionPlayerTeleportPrompt(context);
            }
        }
    }

    public class ActionPlayerCommandsPrompt extends ActionsEditorStringPrompt {

        public ActionPlayerCommandsPrompt(final ConversationContext context) {
            super(context);
        }
        
        @Override
        public String getTitle(final ConversationContext context) {
            return null;
        }

        @Override
        public String getQueryText(final ConversationContext context) {
            return Lang.get("eventEditorSetCommandsPrompt");
        }

        @Override
        public @NotNull String getPromptText(final @NotNull ConversationContext context) {
            final ActionsEditorPostOpenStringPromptEvent event
                    = new ActionsEditorPostOpenStringPromptEvent(context, this);
            plugin.getServer().getPluginManager().callEvent(event);
            
            return ChatColor.YELLOW + getQueryText(context);
        }

        @Override
        public Prompt acceptInput(final @NotNull ConversationContext context, final String input) {
            if (input == null) {
                return null;
            }
            if (!input.equalsIgnoreCase(Lang.get("cmdCancel")) && !input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                final String[] commands = input.split(Lang.get("charSemi"));
                final LinkedList<String> cmdList = new LinkedList<>(Arrays.asList(commands));
                context.setSessionData(CK.E_COMMANDS, cmdList);
            } else if (input.equalsIgnoreCase(Lang.get("cmdClear"))) {
                context.setSessionData(CK.E_COMMANDS, null);
            }
            return new ActionMainPrompt(context);
        }
    }
}
