package com.example.examplemod.magic.spell.special.communication_magic;
public final class CommunicationRules {
	public interface Peer {boolean present();boolean self();boolean receiverDisconnected();boolean senderDisconnected();boolean senderChat();boolean receiverChat();boolean muted();boolean senderBound();boolean receiverBound();}
	public static boolean ready(Peer p){return p.present()&&!p.self()&&!p.receiverDisconnected()&&!p.senderDisconnected()&&p.senderChat()&&p.receiverChat()&&!p.muted()&&p.senderBound()&&p.receiverBound();}
	public static boolean validLength(String content){return content.codePointCount(0,content.length())<=120;}
	private CommunicationRules() { }
}
