package org.csiro.igsn.jaxb.oai.bindings.csiro;

import java.util.ArrayList;
import java.util.List;
import org.csiro.igsn.entity.postgres.AlternateIdentifiers;
import org.csiro.igsn.entity.postgres.Classifications;
import org.csiro.igsn.entity.postgres.Contributors;
import org.csiro.igsn.entity.postgres.CurationDetails;
import org.csiro.igsn.entity.postgres.MaterialTypes;
import org.csiro.igsn.entity.postgres.RelatedResources;
import org.csiro.igsn.entity.postgres.ResourceTypes;
import org.csiro.igsn.entity.postgres.Resources;
import org.csiro.igsn.entity.postgres.SampledFeatures;
import org.csiro.igsn.jaxb.oai.bindings.JAXBConverterInterface;
import org.csiro.igsn.jaxb.oai.bindings.csiro.Resources.Resource;
import org.csiro.igsn.jaxb.oai.bindings.csiro.Resources.Resource.Location;
import org.csiro.igsn.utilities.IGSNDateUtil;
import org.csiro.igsn.jaxb.oai.bindings.csiro.Resources.Resource.Classifications.Classification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EntityToSchemaConverterCSIRO implements JAXBConverterInterface{
	
	ObjectFactory objectFactory;
	final String METAPREFIX="cs_igsn";
	final String NAMESPACE_FOR_BINDING="https://igsn.csiro.au/schemas/3.0";
	final Class XML_ROOT_CLASS = org.csiro.igsn.jaxb.oai.bindings.csiro.Resources.class;
	
	@Value("#{configProperties['IGSN_CSIRO_XSD_URL']}")
	private String SCHEMA_LOCATION_FOR_BINDING;		
		
	
	
	public EntityToSchemaConverterCSIRO(){
		this.objectFactory = new ObjectFactory();		 
	}
	

	@Override
	public boolean supports(String metadataPrefix){
	
		if(metadataPrefix==null || !metadataPrefix.toLowerCase().equals(this.METAPREFIX)){
			return false;
		}else{
			return true;
		}
			
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

	@Override
	public org.csiro.igsn.jaxb.oai.bindings.csiro.Resources convert(Resources resource) {		
		Resource resourceXML = this.objectFactory.createResourcesResource();
		
		resourceXML.setRegisteredObjectType(resource.getRegisteredObjectType());
		
		resourceXML.setResourceIdentifier(this.objectFactory.createResourcesResourceResourceIdentifier());
		resourceXML.getResourceIdentifier().setValue(resource.getResourceIdentifier());
		
		resourceXML.setLandingPage(resource.getLandingPage());
		
		resourceXML.setIsPublic(this.objectFactory.createResourcesResourceIsPublic());
		resourceXML.getIsPublic().setValue(resource.getIsPublic());		
		if(resource.getEmbargoEnd()!=null){
			resourceXML.getIsPublic().setEmbargoEnd(IGSNDateUtil.getISODateFormatterShort().format(resource.getEmbargoEnd()));
		}
		
		resourceXML.setResourceTitle(resource.getResourceTitle());
		
		if(resource.getAlternateIdentifierses()!=null && !resource.getAlternateIdentifierses().isEmpty()){
			resourceXML.setAlternateIdentifiers(this.objectFactory.createResourcesResourceAlternateIdentifiers());
			resourceXML.getAlternateIdentifiers().alternateIdentifier = new ArrayList<String>(); 
			for(AlternateIdentifiers alternateIdentifiers:resource.getAlternateIdentifierses()){
				resourceXML.getAlternateIdentifiers().alternateIdentifier.add(alternateIdentifiers.getAlternateIdentifier());
			}
		}
		
		resourceXML.setResourceTypes(this.objectFactory.createResourcesResourceResourceTypes());
		resourceXML.getResourceTypes().resourceType = new ArrayList<String>();
		for(ResourceTypes resourceTypes:resource.getResourceTypeses()){
			resourceXML.getResourceTypes().resourceType.add(resourceTypes.getCvResourceType().getResourceType());
		}
		
		resourceXML.setMaterialTypes(this.objectFactory.createResourcesResourceMaterialTypes());
		resourceXML.getMaterialTypes().materialType = new ArrayList<String>();
		for(MaterialTypes materialType:resource.getMaterialTypeses()){
			resourceXML.getMaterialTypes().materialType.add(materialType.getCvMaterialTypes().getMaterialType());
		}
		
		if(resource.getClassificationses()!=null && !resource.getClassificationses().isEmpty()){
			resourceXML.setClassifications(this.objectFactory.createResourcesResourceClassifications());
			resourceXML.getClassifications().classification = new ArrayList<Classification>();
			for(Classifications classifications:resource.getClassificationses()){
				Classification classificationsXML = this.objectFactory.createResourcesResourceClassificationsClassification();
				classificationsXML.setClassificationURI(classifications.getClassificationUri());
				classificationsXML.setValue(classifications.getClassification());
				resourceXML.getClassifications().classification.add(classificationsXML);
			}
		}
		
		if(resource.getPurpose()!=null){
			resourceXML.setPurpose(resource.getPurpose());
		}
		
		if(resource.getSampledFeatureses()!=null && !resource.getSampledFeatureses().isEmpty()){			
			Resource.SampledFeatures sampledFeatureXMLs = this.objectFactory.createResourcesResourceSampledFeatures();	
			sampledFeatureXMLs.sampledFeature = new ArrayList<Resource.SampledFeatures.SampledFeature>();
			for(SampledFeatures sampledFeatures:resource.getSampledFeatureses()){
				Resource.SampledFeatures.SampledFeature sampledFeatureXML = this.objectFactory.createResourcesResourceSampledFeaturesSampledFeature();
				sampledFeatureXML.setSampledFeatureURI(sampledFeatures.getSampledFeatureUri());
				sampledFeatureXML.setValue(sampledFeatures.getSampledFeature());
				sampledFeatureXMLs.getSampledFeature().add(sampledFeatureXML);
			}
			resourceXML.setSampledFeatures(this.objectFactory.createResourcesResourceSampledFeatures(sampledFeatureXMLs));
		}
		
		if(resource.getLocation()!=null){
			Location locationXML = this.objectFactory.createResourcesResourceLocation();
			locationXML.setLocality(this.objectFactory.createResourcesResourceLocationLocality());
			locationXML.getLocality().setLocalityURI(resource.getLocation().getLocalityUri());
			locationXML.getLocality().setValue(resource.getLocation().getLocality());
			locationXML.setGeometry(this.objectFactory.createResourcesResourceLocationGeometry());
			locationXML.getGeometry().setSrid(resource.getLocation().getSrid());
			locationXML.getGeometry().setVerticalDatum(resource.getLocation().getVerticalDatum());
			locationXML.getGeometry().setGeometryURI(resource.getLocation().getGeometryUri());
			locationXML.getGeometry().setValue(resource.getLocation().getGeometry().toText());	
			resourceXML.setLocation(this.objectFactory.createResourcesResourceLocation(locationXML));
		}
				
		if(resource.getResourceDate()!=null && !(resource.getResourceDate().getTimeInstant()==null && resource.getResourceDate().getTimePeriodStart()==null)){
			Resource.Date date = new Resource.Date();
			if(resource.getResourceDate().getTimeInstant()!=null && !resource.getResourceDate().getTimeInstant().isEmpty()){
				date.setTimeInstant(resource.getResourceDate().getTimeInstant());
			}else{
				Resource.Date.TimePeriod timeperiod = new Resource.Date.TimePeriod();
				timeperiod.setStart(resource.getResourceDate().getTimePeriodStart());
				timeperiod.setEnd(resource.getResourceDate().getTimePeriodEnd());
				date.setTimePeriod(timeperiod);
			}			
			resourceXML.setDate(this.objectFactory.createResourcesResourceDate(date));
		}
		
		
		if(resource.getMethod()!=null){
			resourceXML.setMethod(this.objectFactory.createResourcesResourceMethod());
			resourceXML.getMethod().setMethodURI(resource.getMethod().getMethodUri());
			resourceXML.getMethod().setValue(resource.getMethod().getMethod());
		}
		
		if(resource.getCampaign()!=null){
			resourceXML.setCampaign(resource.getCampaign());
		}
		
		
		resourceXML.setCurationDetails(this.objectFactory.createResourcesResourceCurationDetails());
		resourceXML.getCurationDetails().curation = new ArrayList<Resource.CurationDetails.Curation>();
		for(CurationDetails curationDetails:resource.getCurationDetailses()){
			Resource.CurationDetails.Curation curationDetailXML = new Resource.CurationDetails.Curation();
			curationDetailXML.setCurator(curationDetails.getCurator());
			curationDetailXML.setCurationDate(curationDetails.getCurationDate());
			curationDetailXML.setCurationLocation(curationDetails.getCurationLocation());
			curationDetailXML.setCuratingInstitution(this.objectFactory.createResourcesResourceCurationDetailsCurationCuratingInstitution());
			curationDetailXML.getCuratingInstitution().setInstitutionURI(curationDetails.getInstitutionUri());
			curationDetailXML.getCuratingInstitution().setValue(curationDetails.getCuratingInstitution());
			resourceXML.getCurationDetails().curation.add(curationDetailXML);
		}
		
		if(resource.getContributorses()!=null && !resource.getContributorses().isEmpty()){
			resourceXML.setContributors(this.objectFactory.createResourcesResourceContributors());
			resourceXML.getContributors().contributor = new ArrayList<Resource.Contributors.Contributor>();
			for(Contributors contributor:resource.getContributorses()){
				Resource.Contributors.Contributor contributorXML = new Resource.Contributors.Contributor();
				contributorXML.setContributorType(contributor.getContributorType());
				contributorXML.setContributorName(contributor.getContributorName());
				if(contributor.getCvIdentifierType()!=null){
					contributorXML.setContributorIdentifier(this.objectFactory.createResourcesResourceContributorsContributorContributorIdentifier());
					contributorXML.getContributorIdentifier().setContributorIdentifierType(contributor.getCvIdentifierType().getIdentifierType());
				}
				if(contributor.getContributorIdentifier()!=null){
					if(contributorXML.getContributorIdentifier()==null){
						contributorXML.setContributorIdentifier(this.objectFactory.createResourcesResourceContributorsContributorContributorIdentifier());
					}
					contributorXML.getContributorIdentifier().setValue(contributor.getContributorIdentifier());
				}
				resourceXML.getContributors().contributor.add(contributorXML);
			}
		}
		
		if(resource.getRelatedResourceses()!=null && !resource.getRelatedResourceses().isEmpty()){
			resourceXML.setRelatedResources(this.objectFactory.createResourcesResourceRelatedResources());
			resourceXML.getRelatedResources().relatedResource = new ArrayList<Resource.RelatedResources.RelatedResource>();
			for(RelatedResources relatedResources:resource.getRelatedResourceses()){
				Resource.RelatedResources.RelatedResource relatedResourcesXML = new Resource.RelatedResources.RelatedResource();
				if(relatedResources.getCvIdentifierType()!=null){
					relatedResourcesXML.setRelatedResourceIdentifierType(relatedResources.getCvIdentifierType().getIdentifierType());
				}
				if(relatedResources.getRelationType()!=null){
					relatedResourcesXML.setRelationType(relatedResources.getRelationType());
				}
				if(relatedResources.getRelatedResource()!=null){
					relatedResourcesXML.setValue(relatedResources.getRelatedResource());
				}
				resourceXML.getRelatedResources().relatedResource.add(relatedResourcesXML);
			}
		}
		
		if(resource.getComments()!=null && !resource.getComments().isEmpty()){
			resourceXML.setComments(resource.getComments());
		}
		
		resourceXML.setLogDate(this.objectFactory.createResourcesResourceLogDate());
		resourceXML.getLogDate().setEventType(EventType.fromValue(resource.getLogDate().getEventType()));
		resourceXML.getLogDate().setValue(resource.getLogDate().getLogDate());
		
		org.csiro.igsn.jaxb.oai.bindings.csiro.Resources resourcesXMLRoot = objectFactory.createResources();	
		resourcesXMLRoot.getResource().add(resourceXML);
		return resourcesXMLRoot;
	}



	

}
