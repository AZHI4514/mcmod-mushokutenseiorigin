package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.special.communication_magic.*;
class CommunicationModuleTest {
	@Test void sameUuidOnDifferentServersCannotShareDraft(){var a=new CommunicationDrafts<String>();var b=new CommunicationDrafts<String>();var id=java.util.UUID.randomUUID();a.put(id,new CommunicationDrafts.Draft<>(id,"signed",1200));assertNull(b.get(id));assertEquals("signed",a.remove(id).message());assertNull(a.get(id));}
	@Test void textLimitCountsCodepoints(){assertTrue(CommunicationRules.validLength("😀".repeat(120)));assertFalse(CommunicationRules.validLength("😀".repeat(121)));}
	static class Peer implements CommunicationRules.Peer {boolean muted,bound=true;public boolean present(){return true;}public boolean self(){return false;}public boolean receiverDisconnected(){return false;}public boolean senderDisconnected(){return false;}public boolean senderChat(){return true;}public boolean receiverChat(){return true;}public boolean muted(){return muted;}public boolean senderBound(){return true;}public boolean receiverBound(){return bound;}}
	@Test void bothBindingAndUnmutedRecipientRequired(){var p=new Peer();assertTrue(CommunicationRules.ready(p));p.bound=false;assertFalse(CommunicationRules.ready(p));p.bound=true;p.muted=true;assertFalse(CommunicationRules.ready(p));}
	@Test void absentPeerSkipsPrivacyReads(){assertFalse(CommunicationRules.ready(new Peer(){public boolean present(){return false;}public boolean self(){fail();return false;}}));}
	static class World implements CommunicationRelease.World {StringBuilder log=new StringBuilder();boolean ready=true,throwRecipient;public boolean ready(){return ready;}public void takeDraft(){log.append('t');}public void sendRecipient(){log.append('r');if(throwRecipient)throw new IllegalStateException();}public void sendSender(){log.append('s');}public void visual(){log.append('v');}}
	@Test void signedDraftConsumedBeforeSendAndSenderEcho(){var w=new World();assertTrue(CommunicationRelease.cast(w));assertEquals("trsv",w.log.toString());}
	@Test void invalidDraftIsNotConsumed(){var w=new World();w.ready=false;assertFalse(CommunicationRelease.cast(w));assertEquals("",w.log.toString());}
	@Test void networkFailureDoesNotEchoOrDraw(){var w=new World();w.throwRecipient=true;assertThrows(IllegalStateException.class,()->CommunicationRelease.cast(w));assertEquals("tr",w.log.toString());}
}
