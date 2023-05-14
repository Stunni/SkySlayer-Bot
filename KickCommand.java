package de.stunni.skyslayer.listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class KickCommand extends ListenerAdapter{

	private String grund;
	private Member m;
	private Role teamupdates;
	private String url;
	static boolean isCancelled;

	public static boolean hasRole(Member member, Role role) {
		List<Role> memberRoles = member.getRoles();
		return memberRoles.contains(role);
	}

	public void ebAuthor(EmbedBuilder eb) {
		eb.setAuthor("SkySlayer", null, url);
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
		if(!e.getFullCommandName().equals("kickteam")) {
			return;
		}
		if(e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			url = e.getGuild().getIconUrl();
			m = e.getOption("user").getAsMember();
			if(e.getOption("grund") != null) grund = e.getOption("grund").getAsString();
			teamupdates = e.getGuild().getRoleById("1107358257812611093");
			if(e.getName().equals("kickteam")) {
				EmbedBuilder eb_CONFIRM = new EmbedBuilder();
				eb_CONFIRM.setTitle("Bist du dir sicher?", null);
				eb_CONFIRM.setColor(Color.YELLOW);
				eb_CONFIRM.setDescription("Bist du dir sicher, dass du "+m.getAsMention()+" aus dem Team kicken willst?");
				ebAuthor(eb_CONFIRM);
				e.replyEmbeds(eb_CONFIRM.build()).setActionRow(sendButtons()).queue();

			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static List<Button> sendButtons() {
		List<Button> buttons = new ArrayList();
		buttons.add(Button.success("ja", "Ja").withEmoji(Emoji.fromUnicode("✅")));
		buttons.add(Button.danger("nein", "Abbrechen").withEmoji(Emoji.fromUnicode("❌")));
		return buttons;
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent e) {
		if(e.getComponentId().equals("ja")) {
			isCancelled = false;
			e.editMessage("Die Aktion wurde durchgeführt.").queue();
			e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
		}else if(e.getComponentId().equals("nein")) {
			isCancelled = true;
			e.editMessage("Die Aktion wurde abgebrochen.").queue();
			e.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
			return;
		}


		if(isCancelled == false) {

			EmbedBuilder eb_KICK = new EmbedBuilder();
			eb_KICK.setTitle("TEAMUPDATE - KICK", null);
			eb_KICK.setColor(Color.RED);
			if(grund != null) {
				eb_KICK.setDescription("Name: "+m.getAsMention()+"\nGrund: "+grund+"\n\n"+teamupdates.getAsMention());
			}else if(grund == null){
				eb_KICK.setDescription("Name: "+m.getAsMention()+"\nGrund: /"+"\n\n"+teamupdates.getAsMention());
			}
			ebAuthor(eb_KICK);

			Role azubi = e.getGuild().getRoleById("707001290785292349");
			Role sup = e.getGuild().getRoleById("707001163047895091");
			Role tmod = e.getGuild().getRoleById("707001018910507099");
			Role mod = e.getGuild().getRoleById("707000879391440948");
			Role srmod = e.getGuild().getRoleById("707000686927282177");
			Role leitung = e.getGuild().getRoleById("1107355501903229038");
			Role admin = e.getGuild().getRoleById("707000485659541525");
			Role coowner = e.getGuild().getRoleById("707000159514656778");
			TextChannel teamupdates_channel = e.getGuild().getTextChannelById("1107357240299618334");

			if(m.getRoles().contains(azubi)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, azubi).queue();
			}
			if(m.getRoles().contains(sup)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, sup).queue();
			}
			if(m.getRoles().contains(tmod)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, tmod).queue();
			}
			if(m.getRoles().contains(mod)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, mod).queue();
			}
			if(m.getRoles().contains(srmod)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, srmod).queue();
			}
			if(m.getRoles().contains(leitung)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, leitung).queue();
			}
			if(m.getRoles().contains(admin)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, admin).queue();
			}
			if(m.getRoles().contains(coowner)) {
				e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
				e.getGuild().removeRoleFromMember(m, coowner).queue();
			}


			EmbedBuilder was_kicked = new EmbedBuilder();
			was_kicked.setTitle("Erfolgreich gekicked", null);
			was_kicked.setColor(Color.GREEN);
			was_kicked.setDescription(m.getAsMention()+" wurde erfolgreich von "+e.getMember().getAsMention()+" gekicked.");
			ebAuthor(was_kicked);

			e.getMessageChannel().sendMessageEmbeds(was_kicked.build()).queue();
			teamupdates_channel.sendMessageEmbeds(eb_KICK.build()).queue();
		}
	}
}
