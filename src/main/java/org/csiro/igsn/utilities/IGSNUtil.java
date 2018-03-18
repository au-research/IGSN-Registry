package org.csiro.igsn.utilities;

import java.util.Set;

import org.csiro.igsn.entity.postgres.Prefix;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;

public class IGSNUtil {
	
	public static boolean resourceStartsWithAllowedPrefix(Set<Prefix> allowedPrefix,Resource r){
		boolean result = false;
		for(Prefix prefix:allowedPrefix){
			if(r.getResourceIdentifier().getValue().startsWith(prefix.getPrefix())){
				return true;
			};
		}
		return result;
	}
	
	public static boolean stringStartsWithAllowedPrefix(Set<Prefix> allowedPrefix,String p){		
		for(Prefix prefix:allowedPrefix){
			if(p.toUpperCase().startsWith(prefix.getPrefix().toUpperCase())){
				return true;
			};
		}
		return false;
	}


}
