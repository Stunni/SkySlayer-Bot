package de.stunni.skyslayer.listener;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import de.stunni.skyslayer.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UprankCommand extends ListenerAdapter{

	private Role r;
	private Member m;
	private Role teamupdates;
	private String url;


	public static boolean hasRole(Member member, Role role) {
		List<Role> memberRoles = member.getRoles();
		return memberRoles.contains(role);
	}

	public void ebAuthor(EmbedBuilder eb) {
		eb.setAuthor("SkySlayer", null, url);
	}


	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
		if(e.getFullCommandName().equals("uprank")) {
			Main.isUprank = true;
			Main.isKick = false;
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
				e.replyEmbeds(eb_CONFIRM.build()).setActionRow(Main.sendButtons()).queue(message -> {
					message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
				});

			}
		}
	}

	public void onButtonInteraction(ButtonInteractionEvent e) {
		EmbedBuilder was_upranked = new EmbedBuilder();
		was_upranked.setTitle("UPRANK", null);
		was_upranked.setColor(Color.GREEN);
		was_upranked.setDescription(m.getAsMention()+" wurde erfolgreich von "+e.getMember().getAsMention()+" auf "+r.getAsMention()+" geupranked.");
		ebAuthor(was_upranked);
		EmbedBuilder eb_UPRANK = new EmbedBuilder();
		eb_UPRANK.setTitle("TEAMUPDATE - UPRANK", null);
		eb_UPRANK.setColor(Color.GREEN);
		eb_UPRANK.setDescription("Name: "+m.getAsMention()+"\nNeuer Rang: "+r.getAsMention()+"\n\n"+teamupdates.getAsMention());
		ebAuthor(eb_UPRANK);
		TextChannel teamupdates_channel = e.getGuild().getTextChannelById("1107357240299618334");
		
		
		if(Main.isUprank && !Main.isKick) {
			if(e.getComponentId().equals("ja")) {
				Main.isCancelled = false;
				e.getMessageChannel().sendMessage("```Die Aktion wird ausgeführt...```").queue(message -> {
		            message.delete().queueAfter(3, TimeUnit.SECONDS);
		        });
				e.getMessageChannel().sendMessageEmbeds(was_upranked.build()).queue();
				teamupdates_channel.sendMessageEmbeds(eb_UPRANK.build()).queue();
			}else if(e.getComponentId().equals("nein")) {
				Main.isCancelled = true;
				e.getMessageChannel().sendMessage("```Die Aktion wird abgebrochen...```").queue(message -> {
		            message.delete().queueAfter(1, TimeUnit.SECONDS);
		        });
				e.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
				return;
			}
		}


		if(!Main.isCancelled) {

			
			Role azubi = e.getGuild().getRoleById("707001290785292349");
			Role sup = e.getGuild().getRoleById("707001163047895091");
			Role tmod = e.getGuild().getRoleById("707001018910507099");
			Role mod = e.getGuild().getRoleById("707000879391440948");
			Role srmod = e.getGuild().getRoleById("707000686927282177");
			Role leitung = e.getGuild().getRoleById("1107355501903229038");
			Role admin = e.getGuild().getRoleById("707000485659541525");
			Role coowner = e.getGuild().getRoleById("707000159514656778");
			
			String name = m.getUser().getName();

			if(r == azubi) {
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Azubi | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == sup) {
				e.getGuild().removeRoleFromMember(m, azubi).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Supporter | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == tmod) {
				e.getGuild().removeRoleFromMember(m, sup).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Test-Mod | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == mod) {
				e.getGuild().removeRoleFromMember(m, tmod).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Mod | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == srmod) {
				e.getGuild().removeRoleFromMember(m, mod).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "SrMod | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r.getName().equalsIgnoreCase("content")) {
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Content | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == admin) {
				e.getGuild().removeRoleFromMember(m, srmod).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Admin | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == leitung) {
				e.getGuild().removeRoleFromMember(m, admin).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Leitung | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r == coowner) {
				e.getGuild().removeRoleFromMember(m, leitung).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Co-Owner | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}else if(r.getName().equalsIgnoreCase("owner")) {
				e.getGuild().removeRoleFromMember(m, coowner).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().addRoleToMember(m, r).queueAfter(500, TimeUnit.MILLISECONDS);
				e.getGuild().modifyNickname(m, "Owner | "+name).queueAfter(500, TimeUnit.MILLISECONDS);
				Main.isSuccessUprank = true;
			}

			Main.isCancelled = false;
		}
	}
}
