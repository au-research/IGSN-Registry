package org.csiro.igsn.jaxb.registration.bindings;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.csiro.igsn.api.controllers.IGSNMintCtrl;
import org.csiro.igsn.entity.service.ResourceEntityService;
import org.csiro.igsn.jaxb.registration.bindings.Resources.*;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.IsPublic;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.Location;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.ResourceIdentifier;
import org.csiro.igsn.utilities.IGSNDateUtil;
import org.csiro.igsn.utilities.SpatialUtilities;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


@Service
public class JsonToSchemaConverterCSIRO {

	ObjectFactory objectFactory;
	ResourceEntityService resourceEntityService;
	final Logger log = Logger.getLogger(JsonToSchemaConverterCSIRO.class);


	@Autowired
	public JsonToSchemaConverterCSIRO(ResourceEntityService resourceEntityService){
		this.objectFactory = new ObjectFactory();
		this.resourceEntityService = resourceEntityService;
	}


	private boolean isNull(JsonElement test){
		if(test!=null && !test.isJsonNull()){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Parse in JsonElement to be parsed into jaxb binding
	 * @param resourceElement
	 * @return
	 * @throws TransformException
	 * @throws FactoryException
	 * @throws NoSuchAuthorityCodeException
	 * @throws MismatchedDimensionException
	 */
	public org.csiro.igsn.jaxb.registration.bindings.Resources.Resource convert(JsonElement resourceElement,String baseUrl) throws MismatchedDimensionException, NoSuchAuthorityCodeException, FactoryException, TransformException {

		JsonObject resourceJO = resourceElement.getAsJsonObject();

		Resource resourceXML = this.objectFactory.createResourcesResource();

		ResourceIdentifier resourceIdentifier = this.objectFactory.createResourcesResourceResourceIdentifier();
		if(!isNull(resourceJO.get("randomIds")) && resourceJO.get("randomIds").getAsBoolean()){
			String randomId = "";
			do{
				randomId = resourceJO.get("resourceIdentifier").getAsString() + RandomStringUtils.randomAlphanumeric(9).toUpperCase();
			}while((this.resourceEntityService.searchResourceByIdentifier(randomId)!=null));
			resourceIdentifier.setValue(randomId);
		}else{
			resourceIdentifier.setValue(resourceJO.get("resourceIdentifier").getAsString());
		}
		resourceXML.setResourceIdentifier(resourceIdentifier);

		resourceXML.setRegisteredObjectType(resourceJO.get("registeredObjectType").getAsString());
		log.debug("defaultLandingPage is:" + resourceJO.get("defaultLandingPage"));
		if(!isNull(resourceJO.get("defaultLandingPage")) && resourceJO.get("defaultLandingPage").getAsBoolean()){
			resourceXML.setLandingPage(baseUrl + "/#/meta/" + resourceIdentifier.getValue());
		}else{
			resourceXML.setLandingPage(resourceJO.get("landingPage").getAsString());
		}

		IsPublic isPublic = this.objectFactory.createResourcesResourceIsPublic();
		isPublic.setValue(resourceJO.get("isPublic").getAsBoolean());
		resourceXML.setIsPublic(isPublic);
		if(!isNull(resourceJO.get("embargoEnd"))){
			//VT: Extremely poor way to format the date however only the web form would be using this converter therefore we
			//are guaranteed that the date format length will be longer than 10.
			resourceXML.getIsPublic().setEmbargoEnd(resourceJO.get("embargoEnd").getAsString().substring(0, 10));
		}
		resourceXML.setResourceTitle(resourceJO.get("resourceTitle").getAsString());


		resourceXML.setResourceTypes(this.objectFactory.createResourcesResourceResourceTypes());
		resourceXML.getResourceTypes().resourceType = new ArrayList<String>();
		JsonArray resourceTypeses = resourceJO.get("resourceTypeses").getAsJsonArray();
		for(JsonElement resourceType : resourceTypeses) {
			resourceXML.getResourceTypes().resourceType.add((resourceType.getAsJsonObject()).getAsJsonObject("cvResourceType").get("resourceType").getAsString());
		}


		resourceXML.setMaterialTypes(this.objectFactory.createResourcesResourceMaterialTypes());
		resourceXML.getMaterialTypes().materialType = new ArrayList<String>();
		JsonArray materialTypeses = resourceJO.get("materialTypeses").getAsJsonArray();
		for(JsonElement materialType : materialTypeses) {
			resourceXML.getMaterialTypes().materialType.add(materialType.getAsJsonObject().getAsJsonObject("cvMaterialTypes").get("materialType").getAsString());
		}


		resourceXML.setCurationDetails(this.objectFactory.createResourcesResourceCurationDetails());
		resourceXML.getCurationDetails().curation = new ArrayList<Resource.CurationDetails.Curation>();
		Resource.CurationDetails.Curation curationXML = new Resource.CurationDetails.Curation();

		JsonArray curationDetailses = resourceJO.get("curationDetailses").getAsJsonArray();
		for(JsonElement curationDetail : curationDetailses) {
			JsonObject curationDetailObject = curationDetail.getAsJsonObject();
			if(!isNull(curationDetailObject.get("curator"))){
				curationXML.setCurator(curationDetailObject.get("curator").getAsString());
			}
			if(!isNull(curationDetailObject.get("curationDate"))){
				curationXML.setCurationDate(curationDetailObject.get("curationDate").getAsString().substring(0, 10));
			}
			if(!isNull(curationDetailObject.get("curationLocation"))){
				curationXML.setCurationLocation(curationDetailObject.get("curationLocation").getAsString());
			}


			curationXML.setCuratingInstitution(this.objectFactory.createResourcesResourceCurationDetailsCurationCuratingInstitution());
			if(!isNull(curationDetailObject.get("curatingInstitution"))){
				curationXML.getCuratingInstitution().setValue(curationDetailObject.get("curatingInstitution").getAsString());
			}
			if(!isNull(curationDetailObject.get("institutionUri"))){
				curationXML.getCuratingInstitution().setInstitutionURI(curationDetailObject.get("institutionUri").getAsString());
			}
			resourceXML.getCurationDetails().curation.add(curationXML);
		}

		if(!isNull(resourceJO.get("location"))){
			Location locationXML = this.objectFactory.createResourcesResourceLocation();
			locationXML.setLocality(this.objectFactory.createResourcesResourceLocationLocality());
			JsonObject locationObject = resourceJO.get("location").getAsJsonObject();
			if(!isNull(locationObject.get("localityUri"))){
				locationXML.getLocality().setLocalityURI(locationObject.get("localityUri").getAsString());
			}
			if(!isNull(locationObject.get("locality"))){
				locationXML.getLocality().setValue(locationObject.get("locality").getAsString());
			}
			Resource.Location.Geometry geometry = this.objectFactory.createResourcesResourceLocationGeometry();
			boolean hasGeometry = false;
			if(!isNull(locationObject.get("srid"))){
				geometry.setSrid(locationObject.get("srid").getAsString());
				hasGeometry = true;
			}
			if(!isNull(locationObject.get("verticalDatum"))){
				geometry.setVerticalDatum(locationObject.get("verticalDatum").getAsString());
				hasGeometry = true;
			}
			if(!isNull(locationObject.get("geometryUri"))){
				geometry.setGeometryURI(locationObject.get("geometryUri").getAsString());
				hasGeometry = true;
			}

			if(!isNull(locationObject.get("wkt")) && !resourceJO.get("locationInputType").getAsString().equalsIgnoreCase("utm")){
				geometry.setValue(locationObject.get("wkt").getAsString());
				hasGeometry = true;
			}else if(resourceJO.get("locationInputType").getAsString().equalsIgnoreCase("utm") &&
					!isNull(locationObject.get("easting")) && !isNull(locationObject.get("northing")) ){
				geometry.setValue(SpatialUtilities.convertUTM_MGA942Geographic_EPSG4326(locationObject.get("easting").getAsDouble(),
						locationObject.get("northing").getAsDouble(), locationObject.get("zone").getAsString()).toText());
				geometry.setSrid("https://epsg.io/4326");
				hasGeometry = true;
			}
			if(hasGeometry){
				locationXML.setGeometry(geometry);
			}
			resourceXML.setLocation(this.objectFactory.createResourcesResourceLocation(locationXML));
		}



		ArrayList<String> alternateIdentifiers = new ArrayList<String>();

		JsonArray alternateIdentifierses = resourceJO.get("alternateIdentifierses").getAsJsonArray();
		for(JsonElement alternateIdentifier : alternateIdentifierses) {
			if(alternateIdentifier.getAsJsonObject().entrySet().size()==0){
				continue;
			}
			alternateIdentifiers.add(alternateIdentifier.getAsJsonObject().get("alternateIdentifier").getAsString());
		}
		if(!alternateIdentifiers.isEmpty()){
			resourceXML.setAlternateIdentifiers(this.objectFactory.createResourcesResourceAlternateIdentifiers());
			resourceXML.getAlternateIdentifiers().alternateIdentifier = alternateIdentifiers;
		}



		resourceXML.setClassifications(this.objectFactory.createResourcesResourceClassifications());
		resourceXML.getClassifications().classification = new ArrayList<Resource.Classifications.Classification>();
		JsonArray classificationses = resourceJO.get("classificationses").getAsJsonArray();
		for(JsonElement classification : classificationses) {
			if(classification.getAsJsonObject().entrySet().size()==0){
				continue;
			}
			Resource.Classifications.Classification classificationXML = new Resource.Classifications.Classification();
			classificationXML.setValue(classification.getAsJsonObject().get("classification").getAsString());
			classificationXML.setClassificationURI(classification.getAsJsonObject().get("classificationUri").getAsString());
			resourceXML.getClassifications().classification.add(classificationXML);
		}
		if(resourceXML.getClassifications().classification.isEmpty()){
			resourceXML.setClassifications(null);
		}

		if(!isNull(resourceJO.get("purpose"))){
			resourceXML.setPurpose(resourceJO.get("purpose").getAsString());
		}


		JsonArray sampledFeatureses = resourceJO.get("sampledFeatureses").getAsJsonArray();
		Resource.SampledFeatures sampledFeatures = this.objectFactory.createResourcesResourceSampledFeatures();
		sampledFeatures.sampledFeature = new ArrayList<Resource.SampledFeatures.SampledFeature>();
		for(JsonElement sampledFeature : sampledFeatureses) {
			if(sampledFeature.getAsJsonObject().entrySet().size()==0){
				continue;
			}
			JsonObject sampledFeatureObject = sampledFeature.getAsJsonObject();
			Resource.SampledFeatures.SampledFeature sampledFeatureXML = new Resource.SampledFeatures.SampledFeature();
			if(!isNull(sampledFeatureObject.get("sampledFeature"))){
				sampledFeatureXML.setValue(sampledFeatureObject.get("sampledFeature").getAsString());
			}
			if(!isNull(sampledFeatureObject.get("sampledFeatureUri"))){
				sampledFeatureXML.setSampledFeatureURI(sampledFeatureObject.get("sampledFeatureUri").getAsString());
			}
			sampledFeatures.sampledFeature.add(sampledFeatureXML);
		}
		if(!sampledFeatures.sampledFeature.isEmpty()){
			resourceXML.setSampledFeatures(this.objectFactory.createResourcesResourceSampledFeatures(sampledFeatures));
		}


		if(!isNull(resourceJO.get("resourceDate")) && !isNull(resourceJO.get("resourceDate").getAsJsonObject().get("timeInstant"))){
			Resource.Date date = new Resource.Date();
			date.setTimeInstant(resourceJO.get("resourceDate").getAsJsonObject().get("timeInstant").getAsString().substring(0,10));
			resourceXML.setDate(this.objectFactory.createResourcesResourceDate(date));
		}


		if(!isNull(resourceJO.get("method"))){
			resourceXML.setMethod(this.objectFactory.createResourcesResourceMethod());
			if(resourceJO.get("method").getAsJsonObject().get("method")!=null){
				resourceXML.getMethod().setValue(resourceJO.get("method").getAsJsonObject().get("method").getAsString());
			}
			if(resourceJO.get("method").getAsJsonObject().get("methodUri")!=null){
				resourceXML.getMethod().setMethodURI(resourceJO.get("method").getAsJsonObject().get("methodUri").getAsString());
			}
		}

		if(!isNull(resourceJO.get("campaign"))){
			resourceXML.setCampaign(resourceJO.get("campaign").getAsString());
		}
		if(!isNull(resourceJO.get("comments"))){
			resourceXML.setComments(resourceJO.get("comments").getAsString());
		}



		resourceXML.setContributors(this.objectFactory.createResourcesResourceContributors());
		resourceXML.getContributors().contributor = new ArrayList<Resource.Contributors.Contributor>();
		JsonArray contributorses = resourceJO.get("contributorses").getAsJsonArray();
		for(JsonElement contributor : contributorses) {
			JsonObject contributorObject = contributor.getAsJsonObject();
			if(contributorObject.entrySet().size()==0){
				continue;
			}
			Resource.Contributors.Contributor contributorXML = new Resource.Contributors.Contributor();
			if(!isNull(contributorObject.get("contributorName"))){
				contributorXML.setContributorName(contributorObject.get("contributorName").getAsString());
			}

			if(!isNull(contributorObject.get("contributorIdentifier")) || !isNull(contributorObject.get("cvIdentifierType"))){
				contributorXML.setContributorIdentifier(this.objectFactory.createResourcesResourceContributorsContributorContributorIdentifier());
				if(!isNull(contributorObject.get("contributorIdentifier"))){
					contributorXML.getContributorIdentifier().setValue(contributorObject.get("contributorIdentifier").getAsString());
				}
				if(!isNull(contributorObject.get("cvIdentifierType"))){
					contributorXML.getContributorIdentifier().setContributorIdentifierType(contributorObject.get("cvIdentifierType").getAsJsonObject().get("identifierType").getAsString());
				}

			}
			if(!isNull(contributorObject.get("contributorType"))){
				contributorXML.setContributorType(contributorObject.get("contributorType").getAsString());
			}
			resourceXML.getContributors().contributor.add(contributorXML);
		}
		if(resourceXML.getContributors().contributor.isEmpty()){
			resourceXML.setContributors(null);
		}



		resourceXML.setRelatedResources(this.objectFactory.createResourcesResourceRelatedResources());
		resourceXML.getRelatedResources().relatedResource = new ArrayList<Resource.RelatedResources.RelatedResource>();
		JsonArray relatedResourceses = resourceJO.get("relatedResourceses").getAsJsonArray();
		for(JsonElement relatedResource : relatedResourceses) {
			JsonObject relatedResourceObject = relatedResource.getAsJsonObject();
			if(relatedResourceObject.entrySet().size()==0){
				continue;
			}
			Resource.RelatedResources.RelatedResource relatedResourceXML = new Resource.RelatedResources.RelatedResource();
			if(!isNull(relatedResourceObject.get("relatedResource"))){
				relatedResourceXML.setValue(relatedResourceObject.get("relatedResource").getAsString());
			}
			if(!isNull(relatedResourceObject.get("relationType"))){
				relatedResourceXML.setRelationType(relatedResourceObject.get("relationType").getAsString());
			}
			if(!isNull(relatedResourceObject.get("cvIdentifierType"))){
				relatedResourceXML.setRelatedResourceIdentifierType(relatedResourceObject.get("cvIdentifierType").getAsJsonObject().get("identifierType").getAsString());
			}
			resourceXML.getRelatedResources().relatedResource.add(relatedResourceXML);
		}
		if(resourceXML.getRelatedResources().relatedResource.isEmpty()){
			resourceXML.setRelatedResources(null);
		}


		resourceXML.setLogDate(this.objectFactory.createResourcesResourceLogDate());
		resourceXML.getLogDate().setEventType(EventType.fromValue(resourceJO.get("logDate").getAsJsonObject().get("eventType").getAsString()));
		resourceXML.getLogDate().setValue(IGSNDateUtil.getISODateFormatter().format(new Date()));

		return resourceXML;
	}





}
