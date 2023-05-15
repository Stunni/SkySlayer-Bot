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
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UprankCommand extends ListenerAdapter{

	public Role r;
	public Member m;
	public String url;
	public Role r2;

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
		if(e.getFullCommandName().equals("uprank")) {
			Main.isUprank = true;
			Main.isKick = false;
			if(e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				url = e.getGuild().getIconUrl();
				m = e.getOption("user").getAsMember();
				r = e.getOption("role").getAsRole();
				r2 = e.getGuild().getRoleById("1107737275162427523");
				MessageEmbed confirmEmbed = Main.confirmEmbedUprank(r, m, url).build();
				e.replyEmbeds(confirmEmbed).setActionRow(Main.sendButtonsUprank()).queue();
			}
		}
	}



	public void onButtonInteraction(ButtonInteractionEvent e) {
		if(Main.isUprank) {
			TextChannel teamupdates_channel = e.getGuild().getTextChannelById("1107357240299618334");

			EmbedBuilder eb_UPRANK = new EmbedBuilder();
			eb_UPRANK.setColor(Color.GREEN);
			eb_UPRANK.setTitle("SkySlayer - Teamupdate", null);
			eb_UPRANK.addField("**UPRANK**", " ", false);
			eb_UPRANK.addField("Name", m.getAsMention(), false);
			eb_UPRANK.addField("Neuer Rang", r.getAsMention(), false);
			eb_UPRANK.addField(" ", "**Glückwunsch** :tada:", false);
			Main.ebAuthor(eb_UPRANK, url);
			if(e.getComponentId().equals("ja")) {
				Main.isCancelledUprank = false;
				e.getMessageChannel().sendMessage("```Die Aktion wird ausgeführt...```").queue(message -> {
					message.delete().queueAfter(1250, TimeUnit.MILLISECONDS);
				});
				e.getMessage().delete().queueAfter(1250, TimeUnit.MILLISECONDS);
				e.getMessageChannel().sendMessageEmbeds(eb_UPRANK.build()).queue(message -> {
					String emojiUnicode = "\uD83C\uDF89";
					Emoji emoji = Emoji.fromUnicode(emojiUnicode);
					message.addReaction(emoji).queue();
				});
				
				teamupdates_channel.sendMessageEmbeds(eb_UPRANK.build()).queue();
				
				String name = m.getUser().getName();
				e.getGuild().modifyNickname(m, e.getGuild().getRoleById(r.getId()).getName() + " | " + name)
				.flatMap(nickResult -> e.getGuild().addRoleToMember(m, r))
				.flatMap(roleResult -> e.getGuild().addRoleToMember(m, r2))
				.queue();
				Main.removeRolesExcept(e.getGuild(), m, r, r2);
				Main.isUprank = false;
			}else if(e.getComponentId().equals("nein")) {
				Main.isCancelledUprank = true;
				e.getMessageChannel().sendMessage("```Die Aktion wird abgebrochen...```").queue(message -> {
					message.delete().queueAfter(750, TimeUnit.MILLISECONDS);
				});
				e.getMessage().delete().queueAfter(750, TimeUnit.MILLISECONDS);
				return;
			}
		}


//		if(!Main.isCancelledUprank && m != null && Main.isUprank) {
//			String name = m.getUser().getName();
//			e.getGuild().modifyNickname(m, e.getGuild().getRoleById(r.getId()).getName() + " | " + name)
//			.flatMap(nickResult -> e.getGuild().addRoleToMember(m, r))
//			.flatMap(roleResult -> e.getGuild().addRoleToMember(m, r2))
//			.queue();
//			Main.removeRolesExcept(e.getGuild(), m, r, r2);
//			Main.isUprank = false;
//		}else if(m == null) {
//			System.out.println(m+" ist null    2");
//		}
	}
}
