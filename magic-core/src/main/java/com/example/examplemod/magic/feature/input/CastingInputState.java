package com.example.examplemod.magic.feature.input;
import java.util.List;
import java.util.ArrayList;
/** Local cast input policy. Packets and physical keys belong to the client boundary. */
public final class CastingInputState {
	public record Action(String action,String spell){}
	private boolean mustRelease=true,armed;
	private int localDelay;
	public boolean mustRelease(){return mustRelease;}
	public boolean armed(){return armed;}
	public boolean interrupt(){boolean stop=armed;armed=false;mustRelease=true;localDelay=0;return stop;}
	public void disconnect(){armed=false;mustRelease=true;}
	public List<Action> sample(boolean down,boolean modeRequested,String selected){
		var actions=new ArrayList<Action>(2);
		if(!down){if(armed)actions.add(new Action("release",""));armed=false;mustRelease=false;localDelay=0;return List.copyOf(actions);}
		if(mustRelease)return List.of();
		if(!armed&&modeRequested){mustRelease=true;return List.of(new Action("mode",selected));}
		if(!armed){actions.add(new Action("arm",""));armed=true;}
		if(localDelay>0)localDelay--;
		if(localDelay==0&&!selected.isEmpty()){actions.add(new Action("cast",selected));localDelay=2;}
		return List.copyOf(actions);
	}
}
