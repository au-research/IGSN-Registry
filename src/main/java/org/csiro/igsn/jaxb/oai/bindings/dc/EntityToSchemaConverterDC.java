package org.csiro.igsn.jaxb.oai.bindings.dc;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.csiro.igsn.entity.postgres.Classifications;
import org.csiro.igsn.entity.postgres.Contributors;
import org.csiro.igsn.entity.postgres.CurationDetails;
import org.csiro.igsn.entity.postgres.MaterialTypes;
import org.csiro.igsn.entity.postgres.ResourceTypes;
import org.csiro.igsn.entity.postgres.Resources;
import org.csiro.igsn.jaxb.oai.bindings.JAXBConverterInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EntityToSchemaConverterDC implements JAXBConverterInterface{
	
	final String METAPREFIX="oai_dc";
	final String NAMESPACE_FOR_BINDING="http://www.openarchives.org/OAI/2.0/oai_dc/";
	final String SCHEMA_LOCATION_FOR_BINDING="http://www.openarchives.org/OAI/2.0/oai_dc.xsd";
	final Class XML_ROOT_CLASS = OaiDcType.class;
	
	@Value("#{configProperties['IGSN_HANDLE_PREFIX']}")
	private String IGSN_HANDLE_PREFIX;
	
	
	SimpleDateFormat dateFormatterLong = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");
	
	
	@Override
	public boolean supports(String metadataPrefix){
	
		if(metadataPrefix==null || !metadataPrefix.toLowerCase().equals(this.METAPREFIX)){
			return false;
		}else{
			return true;
		}
			
	}
		
	
	@Override
	public JAXBElement<OaiDcType> convert(Resources resources){
		ObjectFactory dcObjectfactory = new ObjectFactory();
		OaiDcType oaiDcType = new OaiDcType();
		//Title
		ElementType title = new ElementType();
		title.setValue(resources.getResourceTitle());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createTitle(title));
		
		for(Contributors contributors:resources.getContributorses()){
			if(contributors.getContributorType().equals("http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator")){
				ElementType creator = new ElementType();
				creator.setValue(contributors.getContributorName());
				oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createCreator(creator));
			}
		}

		for(CurationDetails curationDetails:resources.getCurationDetailses()){
			ElementType publisher = new ElementType();
			publisher.setValue(curationDetails.getCurator());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createPublisher(publisher));
		}


		for(Classifications classifications:resources.getClassificationses()){
			ElementType subject = new ElementType();
			subject.setValue(classifications.getClassification());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createSubject(subject));
		}


		if(resources.getComments()!=null){
			ElementType description = new ElementType();
			description.setValue(resources.getComments());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDescription(description));
		}
		
		
		for(MaterialTypes materialType:resources.getMaterialTypeses()){
			ElementType type = new ElementType();
			type.setValue(materialType.getCvMaterialTypes().getMaterialType());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createFormat(type));
		}
				
		
		if(resources.getResourceDate()!=null){
			ElementType date = new ElementType();
			if(resources.getResourceDate().getTimeInstant()!=null){
				date.setValue((resources.getResourceDate().getTimeInstant()));
			}else{
				date.setValue((resources.getResourceDate().getTimePeriodStart()));
			}
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDate(date));
		}
		
		if(resources.getLocation()!=null && resources.getLocation().getGeometry() != null){
			ElementType coverage = new ElementType();
			coverage.setValue("Coordinates(lon/Lat):" + resources.getLocation().getGeometry().toText());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createCoverage(coverage));
		}
		
		for(ResourceTypes resourceTypes:resources.getResourceTypeses()){
			ElementType type = new ElementType();
			type.setValue(resourceTypes.getCvResourceType().getResourceType());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createType(type));
		}
		
		
		ElementType identifier = new ElementType();
		identifier.setValue(IGSN_HANDLE_PREFIX + resources.getResourceIdentifier());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createIdentifier(identifier));
		
		return dcObjectfactory.createDc(oaiDcType);	
	}



	@Override
	public String getMetadataPrefix() {		
		return METAPREFIX;
	}



	@Override
	public String getNamespace() {
		return NAMESPACE_FOR_BINDING;
	}



	@Override
	public String getSchemaLocation() {
		
		return SCHEMA_LOCATION_FOR_BINDING;
	}



	@Override
	public Class getXMLRootClass() {
		
		return XML_ROOT_CLASS;
	}








}
