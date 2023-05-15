package de.stunni.skyslayer;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import de.stunni.skyslayer.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class KickCommand extends ListenerAdapter{

	public String grund;
	public Member m;
	public String icon_URL;
	public Role oldRole;

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
		if (e.getFullCommandName().equals("kickteam")) {
			Main.isKick = true;
			Main.isUprank = false;
			if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				icon_URL = e.getGuild().getIconUrl();
				m = e.getOption("teammitglied").getAsMember();
				if(e.getOption("grund") != null) {
					grund = e.getOption("grund").getAsString();	
				}
				oldRole = m.getRoles().get(0);
				MessageEmbed confirmEmbed = Main.confirmEmbedKick(m, icon_URL).build();
				e.replyEmbeds(confirmEmbed).setActionRow(Main.sendButtonsKick()).queue();
			}
		}
	}

	public void onButtonInteraction(ButtonInteractionEvent e) {
		if(Main.isKick) {
			TextChannel teamupdates_channel = e.getGuild().getTextChannelById("1107357240299618334");
			;

			EmbedBuilder eb_KICK = new EmbedBuilder();
			eb_KICK.setColor(Color.RED);
			eb_KICK.setTitle("SkySlayer - Teamupdate", null);
			eb_KICK.addField("**KICK**", " ", false);
			eb_KICK.addField("Name", m.getAsMention(), false);
			if(grund != null) {
				eb_KICK.addField("Grund", grund, false);
			}
			eb_KICK.addField("Alter Rang", m.getRoles().get(0).getAsMention(), false);
			eb_KICK.addField(" ", "**Viel Erfolg weiterhin!** :skyslayer:", false);
			Main.ebAuthor(eb_KICK, icon_URL);

			if(e.getComponentId().equals("ja1")) {
				Main.isCancelledKick = false;
				e.getMessageChannel().sendMessage("```Die Aktion wird ausgeführt...```").queue(message -> {
					message.delete().queueAfter(1250, TimeUnit.MILLISECONDS);
				});
				e.getMessage().delete().queueAfter(1250, TimeUnit.MILLISECONDS);
				e.getMessageChannel().sendMessageEmbeds(eb_KICK.build()).queue();
				teamupdates_channel.sendMessageEmbeds(eb_KICK.build()).queue();
				
				String newNick = m.getUser().getName();
				Main.removeAllRoles(e.getGuild(), m);
				e.getGuild().modifyNickname(m, newNick).queueAfter(1, TimeUnit.SECONDS);
				Main.isKick = false;
			}else if (e.getComponentId().equals("nein1")) {
				Main.isCancelledKick = true;
				e.getMessageChannel().sendMessage("```Die Aktion wird abgebrochen...```").queue(message -> {
					message.delete().queueAfter(750, TimeUnit.MILLISECONDS);
				});
				e.getMessage().delete().queueAfter(750, TimeUnit.MILLISECONDS);
				return;
			}
		}


//		if(!Main.isCancelledKick && m != null && Main.isKick) {
//			String newNick = m.getUser().getName();
//			Main.removeAllRoles(e.getGuild(), m);
//			e.getGuild().modifyNickname(m, newNick).queueAfter(500, TimeUnit.MILLISECONDS);
//			Main.isKick = false;
//		}
	}
}
