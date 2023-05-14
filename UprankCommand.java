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

public class UprankCommand extends ListenerAdapter{

	private Role r;
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
		if(!e.getFullCommandName().equals("uprank")) {
			return;
		}
		if(e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			url = e.getGuild().getIconUrl();
			m = e.getOption("user").getAsMember();
			r = e.getOption("role").getAsRole();
			teamupdates = e.getGuild().getRoleById("1107358257812611093");
			if(e.getName().equals("uprank")) {
				EmbedBuilder eb_CONFIRM = new EmbedBuilder();
				eb_CONFIRM.setTitle("Bist du dir sicher?", null);
				eb_CONFIRM.setColor(Color.YELLOW);
				eb_CONFIRM.setDescription("Bist du dir sicher, dass du "+m.getAsMention()+" auf "+r.getAsMention()+" upranken willst?");
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

			EmbedBuilder eb_UPRANK = new EmbedBuilder();
			eb_UPRANK.setTitle("TEAMUPDATE - UPRANK", null);
			eb_UPRANK.setColor(Color.GREEN);
			eb_UPRANK.setDescription("Name: "+m.getAsMention()+"\nNeuer Rang: "+r.getAsMention()+"\n\n"+teamupdates.getAsMention());
			ebAuthor(eb_UPRANK);

			Role azubi = e.getGuild().getRoleById("707001290785292349");
			Role sup = e.getGuild().getRoleById("707001163047895091");
			Role tmod = e.getGuild().getRoleById("707001018910507099");
			Role mod = e.getGuild().getRoleById("707000879391440948");
			Role srmod = e.getGuild().getRoleById("707000686927282177");
			Role leitung = e.getGuild().getRoleById("1107355501903229038");
			Role admin = e.getGuild().getRoleById("707000485659541525");
			Role coowner = e.getGuild().getRoleById("707000159514656778");
			TextChannel teamupdates_channel = e.getGuild().getTextChannelById("1107357240299618334");
			String name = m.getUser().getName();

			if(r.getName().equalsIgnoreCase("azubi")) {
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Azubi | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("sup")) {
				e.getGuild().removeRoleFromMember(m, azubi).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Supporter | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("t-mod")) {
				e.getGuild().removeRoleFromMember(m, sup).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Test-Mod | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("mod")) {
				e.getGuild().removeRoleFromMember(m, tmod).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Mod | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("srmod")) {
				e.getGuild().removeRoleFromMember(m, mod).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "SrMod | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("content")) {
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Content | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("admin")) {
				e.getGuild().removeRoleFromMember(m, srmod).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Admin | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("leitung")) {
				e.getGuild().removeRoleFromMember(m, admin).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Leitung | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("co-owner")) {
				e.getGuild().removeRoleFromMember(m, leitung).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Co-Owner | "+name).queue();
			}else if(r.getName().equalsIgnoreCase("owner")) {
				e.getGuild().removeRoleFromMember(m, coowner).queue();
				e.getGuild().addRoleToMember(m, r).queue();
				e.getGuild().modifyNickname(m, "Owner | "+name).queue();
			}


			EmbedBuilder was_upranked = new EmbedBuilder();
			was_upranked.setTitle("Erfolgreich geupranked", null);
			was_upranked.setColor(Color.GREEN);
			was_upranked.setDescription(m.getAsMention()+" wurde erfolgreich von "+e.getMember().getAsMention()+" auf "+r.getAsMention()+" geupranked.");
			ebAuthor(was_upranked);

			e.getMessageChannel().sendMessageEmbeds(was_upranked.build()).queue();
			teamupdates_channel.sendMessageEmbeds(eb_UPRANK.build()).queue();
		}
	}
}
