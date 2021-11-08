package com.github.wypixel.DiscordVinculator.bot;

import com.github.wypixel.DiscordVinculator.bot.DiscordBot;
import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import com.github.wypixel.DiscordVinculator.spigot.VinculatorAPI;
import com.github.wypixel.DiscordVinculator.spigot.config.PlayerConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        long time = System.currentTimeMillis();
        DiscordBot bot = DiscordVinculator.getAPI().getBot();
        Member author = msg.getMember();
        VinculatorAPI api = DiscordVinculator.getAPI();
        if(author == null) return;
        if(msg.getGuild() != null && msg.getGuild() != bot.getGuild()) return;
        if (!author.getUser().isBot()) {
            if (msg.getContentRaw().startsWith(bot.getPrefix())) {
                if(msg.getContentRaw().toLowerCase().contains(bot.getPrefix() + "vincular")) {
                    String[] args = msg.getContentRaw().split(" ");
                    if(args.length >= 2) {
                        for(String key : api.getKeys().values()) {
                            if(args[1].equalsIgnoreCase(key)) {
                                UUID uuid = api.getUUIDbyKey(args[1]);
                                String name = Bukkit.getOfflinePlayer(api.getUUIDbyKey(args[1])).getName();
                                channel.sendMessage("Sua conta foi vinculada com sucesso para **" + api.getUUIDbyKey(args[1]) + "**").queue();
                                try {
                                    author.modifyNickname(name).queue();
                                    channel.sendMessage("Seu apelido foi alterado para: **" + name + "**!").queue();
                                } catch (Exception e) {
                                    channel.sendMessage(author.getAsMention() + " **|** Não foi possível definir seu nickname! :(").queue();
                                }
                                new PlayerConfig(uuid).setID(author.getId()).load();
                                Bukkit.getPlayer(uuid).sendMessage("§aSeu discord foi vinculado com sucesso!");
                                return;
                            }
                        }
                    }
                }
                for (Command command : bot.getCommands()) {
                    if (command.detect(msg.getContentRaw())) {
                        command.setAuthor(author);
                        command.setChannel(channel);
                        command.setMessage(msg);
                        command.setTextChannel(msg.getTextChannel());
                        String args = msg.getContentRaw();
                        command.setArgs(args.split(" "));
                        if (command.isRemoveMessage()) {
                            msg.delete();
                        }
                        command.executor();
                        return;
                    }
                }
                channel.sendMessage(author.getAsMention() + " **|** O comando `" + msg.getContentRaw().split(" ")[0] + "` não existe!").queue();
            }
        }
    }
}
