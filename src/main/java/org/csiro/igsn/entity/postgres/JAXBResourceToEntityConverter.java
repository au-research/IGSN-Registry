package org.csiro.igsn.entity.postgres;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.csiro.igsn.entity.service.ControlledValueEntityService;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;
import org.csiro.igsn.utilities.IGSNDateUtil;
import org.csiro.igsn.utilities.SpatialUtilities;

import com.vividsolutions.jts.io.ParseException;


public class JAXBResourceToEntityConverter {
	
	ControlledValueEntityService controlledValueEntityService;
	private List<CvResourceType> cvResourceTypes;
	private List<CvMaterialTypes> cvMaterialTypes;
	private List<CvIdentifierType> cvIdentifierType;
	
	
	
	public JAXBResourceToEntityConverter(ControlledValueEntityService controlledValueEntityService){
		this.controlledValueEntityService = controlledValueEntityService;
		cvResourceTypes = this.controlledValueEntityService.listResourceType();
		cvMaterialTypes = this.controlledValueEntityService.listMaterialTypes();
		cvIdentifierType = this.controlledValueEntityService.listIdentifierType();
		
	}
	
	
	private CvResourceType searchResourceType(String resourceType){
		for(CvResourceType type : cvResourceTypes){
			if(type.getResourceType().equals(resourceType)){
				return type;
			}
		}
		return null;
	}
	
	private CvMaterialTypes searchMaterialType(String materialType){
		for(CvMaterialTypes type : cvMaterialTypes){
			if(type.getMaterialType().equals(materialType)){
				return type;
			}
		}
		return null;
	}
	
	private CvIdentifierType searchIdentifierType(String identifierType){
		for(CvIdentifierType type : cvIdentifierType){
			if(type.getIdentifierType().equals(identifierType)){
				return type;
			}
		}
		return null;
	}
	
	
	
	
	public void convert(Resource resourceXML,Registrant registrant, Resources resourceEntity) throws ParseException, java.text.ParseException{
		
		resourceEntity.setRegisteredObjectType(resourceXML.getRegisteredObjectType());
		resourceEntity.setResourceIdentifier(resourceXML.getResourceIdentifier().getValue().toUpperCase());
		
		//VT: set alternateIdentifiers;
		if(resourceXML.getAlternateIdentifiers()!=null){
			Set<AlternateIdentifiers> alternateIdentifiers=new HashSet<AlternateIdentifiers>();
			for(String alternateIdentifier:resourceXML.getAlternateIdentifiers().getAlternateIdentifier()){
				alternateIdentifiers.add(new AlternateIdentifiers(resourceEntity,alternateIdentifier));
			}
			resourceEntity.setAlternateIdentifierses(alternateIdentifiers);
		}
		
		//VT: landing page, public, resource title
		resourceEntity.setLandingPage(resourceXML.getLandingPage());
		resourceEntity.setIsPublic(Boolean.valueOf(resourceXML.getIsPublic().isValue()));
		resourceEntity.setResourceTitle(resourceXML.getResourceTitle());
				
		if(resourceXML.getIsPublic().getEmbargoEnd()!=null && !resourceXML.getIsPublic().getEmbargoEnd().isEmpty()){
			if(resourceXML.getIsPublic().getEmbargoEnd().length() == 10){
				resourceEntity.setEmbargoEnd(IGSNDateUtil.getISODateFormatterShort().parse(resourceXML.getIsPublic().getEmbargoEnd().substring(0,10)));
			}else{
				throw new ParseException("The embargo end date is expected to be in the yyyy-mm-dd format.");
			}
		}
		
		
		//VT: resourceType
		Set<ResourceTypes> resourceTypes = new HashSet<ResourceTypes>();
		for(String resourceType:resourceXML.getResourceTypes().getResourceType()){
			resourceTypes.add(new ResourceTypes(resourceEntity,searchResourceType(resourceType)));
		}
		resourceEntity.setResourceTypeses(resourceTypes);
		
		//VT: MaterialType
		Set<MaterialTypes> materialTypes = new HashSet<MaterialTypes>();
		for(String materialType:resourceXML.getMaterialTypes().getMaterialType()){
			materialTypes.add(new MaterialTypes(resourceEntity,searchMaterialType(materialType)));
		}
		resourceEntity.setMaterialTypeses(materialTypes);
		
		//VT:Classification
		if(resourceXML.getClassifications()!=null){
			Set<Classifications> classificationses = new HashSet<Classifications>();
			for(Resource.Classifications.Classification classificationsXML:resourceXML.getClassifications().getClassification()){
				classificationses.add(new Classifications(resourceEntity,classificationsXML.getClassificationURI(),classificationsXML.getValue()));
			}
			resourceEntity.setClassificationses(classificationses);
		}
		
		//VT:Purpose
		if(resourceXML.getPurpose()!=null){
			resourceEntity.setPurpose(resourceXML.getPurpose());
		}
		
		//VT:sampledFeature
		if(resourceXML.getSampledFeatures()!=null){
			Set<SampledFeatures> sampledFeatureses = new HashSet<SampledFeatures>();
			for(Resource.SampledFeatures.SampledFeature sampleFeatureXML:resourceXML.getSampledFeatures().getValue().getSampledFeature()){
				sampledFeatureses.add(new SampledFeatures(resourceEntity,sampleFeatureXML.getSampledFeatureURI(),sampleFeatureXML.getValue()));
			}
			resourceEntity.setSampledFeatureses(sampledFeatureses);
		}
				
		//VT:location		
		if(resourceXML.getLocation()!=null){
			Location location = new Location();
			if(resourceXML.getLocation().getValue().getLocality()!=null){
				location.setLocality(resourceXML.getLocation().getValue().getLocality().getValue());
				location.setLocalityUri(resourceXML.getLocation().getValue().getLocality().getLocalityURI());
			}
			if(resourceXML.getLocation().getValue().getGeometry()!=null){
				location.setSrid("https://epsg.io/4326");
				location.setGeometryUri(resourceXML.getLocation().getValue().getGeometry().getGeometryURI());
				location.setVerticalDatum(resourceXML.getLocation().getValue().getGeometry().getVerticalDatum());
				location.setGeometry(SpatialUtilities.wktToGeometry(resourceXML.getLocation().getValue().getGeometry().getValue(), 
						resourceXML.getLocation().getValue().getGeometry().getSrid()));
			}	
			resourceEntity.setLocation(location);
		}
		
		
		
		//VT: Date
		if(resourceXML.getDate()!=null){
			ResourceDate resourceDate = new ResourceDate();
			if(resourceXML.getDate() != null && resourceXML.getDate().getValue().getTimeInstant()!=null){
				resourceDate.setTimeInstant(resourceXML.getDate().getValue().getTimeInstant());
			}else if(resourceXML.getDate() != null && resourceXML.getDate().getValue().getTimePeriod()!=null){
				resourceDate.setTimePeriodStart(resourceXML.getDate().getValue().getTimePeriod().getStart());
				resourceDate.setTimePeriodEnd(resourceXML.getDate().getValue().getTimePeriod().getEnd());
			}
			resourceEntity.setResourceDate(resourceDate);
		}
		
		
		//VT:Methods
		if(resourceXML.getMethod()!=null){
			Method method = new Method();
			method.setMethod(resourceXML.getMethod().getValue());			
			method.setMethodUri(resourceXML.getMethod().getMethodURI());
			resourceEntity.setMethod(method);
		}
		
		//VT: campaign
		if(resourceXML.getCampaign()!=null){
			resourceEntity.setCampaign(resourceXML.getCampaign());
		}
		
		//VT: CurationDetails		
		Set<CurationDetails> curationDetailses = new HashSet<CurationDetails>();
		for(Resource.CurationDetails.Curation curationXML:resourceXML.getCurationDetails().getCuration()){
			CurationDetails curationDetails = new CurationDetails();
			curationDetails.setResources(resourceEntity);
			if(curationXML.getCurator()!=null){
				curationDetails.setCurator(curationXML.getCurator());
			}
			if(curationXML.getCurationDate()!=null){
				curationDetails.setCurationDate(curationXML.getCurationDate());
			}
			if(curationXML.getCurationLocation()!=null){
				curationDetails.setCurationLocation(curationXML.getCurationLocation());
			}
			if(curationXML.getCuratingInstitution().getInstitutionURI()!=null){
				curationDetails.setInstitutionUri(curationXML.getCuratingInstitution().getInstitutionURI());				
			}
			curationDetails.setCuratingInstitution(curationXML.getCuratingInstitution().getValue());
			curationDetailses.add(curationDetails);
		}
		resourceEntity.setCurationDetailses(curationDetailses);
		
		//VT: Contributor	
		if(resourceXML.getContributors()!=null){
			Set<Contributors> contributorses = new HashSet<Contributors>();
			for(Resource.Contributors.Contributor contributorXML:resourceXML.getContributors().getContributor()){
				contributorses.add(new Contributors(resourceEntity,
						contributorXML.getContributorIdentifier()==null?null:searchIdentifierType(contributorXML.getContributorIdentifier().getContributorIdentifierType()),
						contributorXML.getContributorType(),
						contributorXML.getContributorName(),
						contributorXML.getContributorIdentifier()==null?null:contributorXML.getContributorIdentifier().getValue()));
				
			}
			resourceEntity.setContributorses(contributorses);
		}
		
		//VT: relatedResources	
		if(resourceXML.getRelatedResources()!=null){
			Set<RelatedResources> relatedResourceses = new HashSet<RelatedResources>();
			for(Resource.RelatedResources.RelatedResource relatedResourceXMl:resourceXML.getRelatedResources().getRelatedResource()){
				relatedResourceses.add(new RelatedResources(resourceEntity,
						searchIdentifierType(relatedResourceXMl.getRelatedResourceIdentifierType()),
						relatedResourceXMl.getValue(),
						relatedResourceXMl.getRelationType()));
			}
			resourceEntity.setRelatedResourceses(relatedResourceses);
		}
		
		//VT: comments		
		resourceEntity.setComments(resourceXML.getComments());
		
		//VT: logDate				
		resourceEntity.setLogDate(new LogDate(resourceXML.getLogDate().getEventType().value(),resourceXML.getLogDate().getValue()));
		
		resourceEntity.setModified(new Date());
		
		resourceEntity.setRegistrant(registrant);
	}
}
