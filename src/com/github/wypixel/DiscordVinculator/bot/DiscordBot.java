package com.github.wypixel.DiscordVinculator.bot;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DiscordBot {

    public JDA bot;
    public Guild guild;
    public String prefix;
    public List<Command> commands = new ArrayList<>();

    public DiscordBot(String prefix, String token, String guild) {
        try {
            this.prefix = prefix;
            bot = JDABuilder.createDefault(token)
                    .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                    .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new BotListener())
                    .build().awaitReady();
            this.guild = bot.getGuildById(guild);
            if (this.guild == null) {
                DiscordVinculator.getPlugin().getLogger().warning("Servidor '" + guild + "' nao encontrado.");
                return;
            }
            loadCommands();
        } catch (Exception e) {
            //e.printStackTrace();
            DiscordVinculator.getPlugin().getLogger().warning("Nao foi possivel iniciar conexao com o Bot!");
        }
    }
    public void loadCommands() {
        if(commands.size() >= 1) return;
        //registerCommand(new SynchronizeCommand());
    }

    public DiscordBot registerCommand(Command command) {
        if(commands.contains(command)) return this;
        commands.add(command);
        return this;
    }

    public void sendMessage(Member member, String message) {
        member.getUser().openPrivateChannel().submit()
                .thenCompose(channel -> channel.sendMessage(message).submit())
                .whenComplete((msg, error) -> {
                    if (error != null) DiscordVinculator.getPlugin().getLogger().warning("Houve uma tentativa de enviar mensagem direta a " + member.getUser().getAsTag());
                });
    }

    public void sendMessage(User user, String message) {
        user.openPrivateChannel().submit()
                .thenCompose(channel -> channel.sendMessage(message).submit())
                .whenComplete((msg, error) -> {
                    if (error != null) DiscordVinculator.getPlugin().getLogger().warning("Houve uma tentativa de enviar mensagem direta a " + user.getAsTag());
                });
    }

    public void sendMessage(String channelID, String message) {
        TextChannel channel = bot.getTextChannelById(channelID);
        channel.sendMessage(message).queue();
    }

    public void sendMessageByEmbed(String channelID, String embed, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription(embed);
        sendMessage(channelID, eb, message);
    }

    public void sendMessageByEmbed(String channelID, String embed) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription(embed);
        sendMessage(channelID, eb);
    }

    public Message sendMessage(String channelID, EmbedBuilder eb, String message) {
        sendMessage(channelID, message);
        return sendMessage(channelID, eb);
    }

    public Message sendMessage(String channelID, EmbedBuilder eb) {
        TextChannel channel = bot.getTextChannelById(channelID);
        return channel.sendMessage(eb.build()).complete();
    }

    public JDA getBot() {
        return bot;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getPrefix() {
        return prefix;
    }

    public java.util.List<Command> getCommands() {
        return commands;
    }
}
