package com.github.wypixel.DiscordVinculator.bot;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Command {


    public String name;
    public List<String> aliases;
    public String[] args;
    public String[] channels;
    public boolean removeMessage;
    public Member author;
    public MessageChannel channel;
    public TextChannel textChannel;
    public String description;
    public Message message;
    public DiscordBot bot = DiscordVinculator.getAPI().getBot();

    public Command(String name) {
        this.name = name;
    }

    public Command(String name, String description, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    public Command(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
        this.description = "Sem descrição.";
    }

    public Command(String name, String description) {
        this.name = name;
        this.aliases = new ArrayList<>();
        this.description = description;
    }
    
    public void setArgs(String[] args) {
        this.args = args;
    }
    
    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public boolean isRemoveMessage() {
        return removeMessage;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public void sendMessage(EmbedBuilder embed) {
        bot.sendMessage(channel.getId(), embed);
    }

    public void sendMessage(EmbedBuilder embed, String message) {
        bot.sendMessage(channel.getId(), embed, message);
    }

    public void sendMessage(String message) {
        bot.sendMessage(channel.getId(), message);
    }

    public boolean detect(String arg) {
        if(arg.toLowerCase().startsWith(bot.getPrefix() + name.toLowerCase())) return true;
        for(String a : this.aliases) {
            if(arg.toLowerCase().startsWith(bot.getPrefix() + a.toLowerCase())) return true;
        }
        return false;
    }

    public String[] getChannels() {
        return channels;
    }

    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public Integer getInteger(String string) {
        return Integer.valueOf(string);
    }

    public String getArgs(String[] args, int starting) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = starting; i < args.length; i++) {
            stringBuilder.append(args[i] + " ");
        }
        return stringBuilder.toString();
    }

    public String getDescription() {
        return description;
    }

    public void cmdPreset(String message, String... obs) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setAuthor("[TUTORIAL]", null, bot.getGuild().getIconUrl());
        eb.addField("Como usar:", "`" + bot.getPrefix() + name + " " + message + "`",true);;
        for(String ob : obs) {
            eb.addField("Observação:", ob, true);
        }
        eb.setThumbnail(bot.getGuild().getIconUrl());
        eb.setFooter(bot.getGuild().getName(), bot.getGuild().getIconUrl());
        sendMessage(eb, author.getAsMention());
    }

    public void executor() {
        try {
            execute();
        } catch (Exception e) {
            channel.sendMessage(author.getAsMention() + " **|** Houve um problema ao executar este comando!");
        }
    }
    public void execute() {

    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public DiscordBot getBot() {
        return bot;
    }

    public Member getMember(String id) {
        return bot.getGuild().getMemberById(id);
    }

    public void setMessage(Message msg) {
        this.message = msg;
    }

    public Message getMessage() {
        return message;
    }

}
