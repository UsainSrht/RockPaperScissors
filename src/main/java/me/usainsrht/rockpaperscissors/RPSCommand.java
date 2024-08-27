package me.usainsrht.rockpaperscissors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Locale;
import java.util.Random;

public class RPSCommand {

    public static LiteralCommandNode<CommandSourceStack> crateCommand(RockPaperScissors plugin) {
        return Commands.literal("rockpaperscissors")
                .then(Commands.argument("rockpaperscissors", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            if (context.getSource().getSender().hasPermission("rockpaperscissors.reload")) builder.suggest("reload");
                            for (RPSElement rpsElement : RPSElement.values()) {
                                builder.suggest(rpsElement.name());
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            if (context.getSource().getSender() instanceof Player player) {
                                String arg = context.getArgument("rockpaperscissors", String.class);
                                if (arg.equalsIgnoreCase("reload") && player.hasPermission("rockpaperscissors.reload")) {
                                    plugin.reload();
                                    return Command.SINGLE_SUCCESS;
                                }
                                if (player.hasMetadata("rps-ingame") && !player.getMetadata("rps-ingame").isEmpty()) {
                                    RPSElement userElement = RPSElement.valueOf(arg.toUpperCase(Locale.ENGLISH));
                                    RPSElement[] elements = RPSElement.values();
                                    RPSElement computerElement = elements[new Random().nextInt(elements.length)];

                                    RPSResult result = userElement.over(computerElement);

                                    if (result == RPSResult.WIN) {
                                        player.sendRichMessage(plugin.getConfig().getString("messages.win"), Placeholder.unparsed("pick", computerElement.name()));
                                        player.playSound(player, plugin.getConfig().getString("sounds.win"), 1f, 1f);
                                        if (userElement == RPSElement.ROCK) {
                                            player.sendRichMessage(plugin.getConfig().getString("messages.rock_over_scissors"));
                                            player.playSound(player, plugin.getConfig().getString("sounds.rock_over_scissors"), 1f, 1f);
                                        } else if (userElement == RPSElement.PAPER) {
                                            player.sendRichMessage(plugin.getConfig().getString("messages.paper_over_rock"));
                                            player.playSound(player, plugin.getConfig().getString("sounds.paper_over_rock"), 1f, 1f);
                                        } else if (userElement == RPSElement.SCISSORS) {
                                            player.sendRichMessage(plugin.getConfig().getString("messages.scissors_over_paper"));
                                            player.playSound(player, plugin.getConfig().getString("sounds.scissors_over_paper"), 1f, 1f);
                                        }
                                    } else if (result == RPSResult.LOSE) {
                                        player.sendRichMessage(plugin.getConfig().getString("messages.lose"), Placeholder.unparsed("pick", computerElement.name()));
                                        player.playSound(player, plugin.getConfig().getString("sounds.lose"), 1f, 1f);
                                        if (computerElement == RPSElement.ROCK) {
                                            player.sendRichMessage(plugin.getConfig().getString("messages.rock_over_scissors"));
                                            player.playSound(player, plugin.getConfig().getString("sounds.rock_over_scissors"), 1f, 1f);
                                        } else if (computerElement == RPSElement.PAPER) {
                                            player.sendRichMessage(plugin.getConfig().getString("messages.paper_over_rock"));
                                            player.playSound(player, plugin.getConfig().getString("sounds.paper_over_rock"), 1f, 1f);
                                        } else if (computerElement == RPSElement.SCISSORS) {
                                            player.sendRichMessage(plugin.getConfig().getString("messages.scissors_over_paper"));
                                            player.playSound(player, plugin.getConfig().getString("sounds.scissors_over_paper"), 1f, 1f);
                                        }
                                    } else if (result == RPSResult.DRAW) {
                                        player.sendRichMessage(plugin.getConfig().getString("messages.draw"), Placeholder.unparsed("pick", computerElement.name()));
                                        player.playSound(player, plugin.getConfig().getString("sounds.draw"), 1f, 1f);
                                    } else throw new RuntimeException("Something went wrong");

                                    player.removeMetadata("rps-ingame", plugin);

                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                            return -1;
                        })
                )
                .executes(context -> {
                    if (context.getSource().getSender() instanceof Player player) {
                        player.sendRichMessage(plugin.getConfig().getString("messages.rock_paper_scissors"));
                        player.playSound(player, plugin.getConfig().getString("sounds.rock_paper_scissors"), 1f, 1f);
                        player.setMetadata("rps-ingame", new FixedMetadataValue(plugin, true));
                        return Command.SINGLE_SUCCESS;
                    }
                    context.getSource().getSender().sendMessage("player only");
                    return -1;
                })
                .build();
    }

}