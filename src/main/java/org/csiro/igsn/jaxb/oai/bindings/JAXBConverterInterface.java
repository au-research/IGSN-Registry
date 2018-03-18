package org.csiro.igsn.jaxb.oai.bindings;


import javax.xml.datatype.DatatypeConfigurationException;

import org.csiro.igsn.entity.postgres.Resources;



public interface JAXBConverterInterface {

	public boolean supports(String metadataPrefix);
	
	public String getMetadataPrefix();
	
	public String getNamespace();
	
	public String getSchemaLocation();
	
	public Object convert(Resources resource) throws Exception;
	
	public Class getXMLRootClass();

}
