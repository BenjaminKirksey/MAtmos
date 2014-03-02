package eu.ha3.matmos.expansions.agents;

import java.io.IOException;
import java.util.Scanner;

import eu.ha3.matmos.engine.core.implem.Knowledge;
import eu.ha3.matmos.expansions.ExpansionIdentity;
import eu.ha3.matmos.tools.JasonExpansions_Engine1Deserializer2000;

/*
--filenotes-placeholder
*/

public class JasonLoadingAgent implements LoadingAgent
{
	@Override
	public boolean load(ExpansionIdentity identity, Knowledge knowledge)
	{
		try
		{
			String jasonString =
				new Scanner(identity.getPack().getInputStream(identity.getLocation())).useDelimiter("\\Z").next();
			return new JasonExpansions_Engine1Deserializer2000().loadJson(jasonString, identity, knowledge);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
}
