package org.csiro.oai;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.apache.http.conn.UnsupportedSchemeException;
import org.csiro.igsn.entity.postgres.Resources;
import org.csiro.igsn.entity.service.ResourceEntityService;
import org.csiro.igsn.jaxb.oai.bindings.JAXBConverterInterface;
import org.csiro.oai.binding.DeletedRecordType;
import org.csiro.oai.binding.GetRecordType;
import org.csiro.oai.binding.GranularityType;
import org.csiro.oai.binding.HeaderType;
import org.csiro.oai.binding.IdentifyType;
import org.csiro.oai.binding.ListIdentifiersType;
import org.csiro.oai.binding.ListMetadataFormatsType;
import org.csiro.oai.binding.ListRecordsType;
import org.csiro.oai.binding.MetadataFormatType;
import org.csiro.oai.binding.MetadataType;
import org.csiro.oai.binding.OAIPMHerrorType;
import org.csiro.oai.binding.OAIPMHerrorcodeType;
import org.csiro.oai.binding.OAIPMHtype;
import org.csiro.oai.binding.ObjectFactory;
import org.csiro.oai.binding.RecordType;
import org.csiro.oai.binding.RequestType;
import org.csiro.oai.binding.ResumptionTokenType;
import org.csiro.oai.binding.StatusType;
import org.csiro.oai.binding.VerbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class OAIService {
	
	ObjectFactory oaiObjectFactory;
	TokenResumptionService tokenResumptionService;
	
	SimpleDateFormat dateFormatterLong = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	SimpleDateFormat dateFormatterShort = new SimpleDateFormat("yyyy-MM-dd");
	
	List<JAXBConverterInterface> jaxbConverterInterface;
	
	@Value("#{configProperties['OAI_IDENTIFIER_PREFIX']}")
	private String OAI_IDENTIFIER_PREFIX;
	
	@Value("#{configProperties['OAI_BASEURL_VALUE']}")
	private String OAI_BASEURL_VALUE;
	
	@Value("#{configProperties['OAI_REPO_NAME']}")
	private String OAI_REPO_NAME;
	
	@Value("#{configProperties['OAI_ADMIN_EMAIL']}")
	private String OAI_ADMIN_EMAIL;
	

	
	
	
	@Autowired
	public OAIService(List<JAXBConverterInterface> jaxbConverterInterface){
		oaiObjectFactory = new ObjectFactory();
		this.jaxbConverterInterface = jaxbConverterInterface;
		this.tokenResumptionService = new TokenResumptionService();
	}
	 
	
	public JAXBElement<OAIPMHtype> getBadResumptionToken(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.BAD_VERB);
		errorType.setValue("Expired or corrupted resumption token");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getBadVerb() throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.BAD_VERB);
		errorType.setValue("Illegal OAI Verb");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	 
	
	public JAXBElement<OAIPMHtype> getNoSetHierarchy(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.NO_SET_HIERARCHY);
		errorType.setValue("This repository does not support sets");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getBadArgument(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
		errorType.setValue("Missing require arguement");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getNoRecordMatch(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.NO_RECORDS_MATCH);
		errorType.setValue("Unable to find a matching record");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getCannotDisseminateFormat(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
		errorType.setValue("metadataPrefix unrecognized");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getIdDoesNotExist(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST);
		errorType.setValue("Unable to find ID");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getIdentify() throws DatatypeConfigurationException {
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date	
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.IDENTIFY);	
		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		IdentifyType identifyType = new IdentifyType();
		identifyType.setRepositoryName(this.OAI_REPO_NAME);
		identifyType.setBaseURL(this.OAI_BASEURL_VALUE);
		identifyType.setProtocolVersion("2.0");		
		identifyType.getAdminEmail().add(this.OAI_ADMIN_EMAIL);
		identifyType.setEarliestDatestamp("2015-11-01T00:00:00Z");		
		identifyType.setGranularity(GranularityType.YYYY_MM_DD_THH_MM_SS_Z);
		identifyType.setDeletedRecord(DeletedRecordType.PERSISTENT);
		identifyType.getCompression().add("deflate");
		
		
//		DescriptionType descriptionType  = new DescriptionType();
//		
//		identifyType.getDescription().add(descriptionType);
		
		oaiType.setIdentify(identifyType);
		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
		
	}
	
	public JAXBElement<OAIPMHtype> getListMetadataFormat(Resources resources,String identifier) throws DatatypeConfigurationException {
		
		if(identifier!=null && resources==null){
			return this.getIdDoesNotExist(VerbType.GET_RECORD);
		}
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.LIST_METADATA_FORMATS);	
		requestType.setIdentifier(identifier);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		ListMetadataFormatsType listMetadataFormatsType = new ListMetadataFormatsType();
		
		for(JAXBConverterInterface converter:this.jaxbConverterInterface){
			MetadataFormatType metadataFormatType = new MetadataFormatType();
			metadataFormatType.setMetadataPrefix(converter.getMetadataPrefix());
			metadataFormatType.setSchema(converter.getSchemaLocation());
			metadataFormatType.setMetadataNamespace(converter.getNamespace());
			
			listMetadataFormatsType.getMetadataFormat().add(metadataFormatType);
		}
		oaiType.setListMetadataFormats(listMetadataFormatsType);
		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
		
	}
	
	private boolean isDeleted(String eventType){
		if(eventType.toLowerCase().equals("deprecated") || eventType.toLowerCase().equals("destroyed")){
			return true;
		}else{
			return false;
		}
	}
	
	public JAXBElement<OAIPMHtype> getRecordOAI(Resources resources, String metadataPrefix) throws Exception{
		
		if(resources==null){
			return this.getIdDoesNotExist(VerbType.GET_RECORD);
		}
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);	
		requestType.setIdentifier(OAI_IDENTIFIER_PREFIX + resources.getResourceIdentifier());
		requestType.setMetadataPrefix(metadataPrefix);
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT:GetRecord
		GetRecordType getRecordType = new GetRecordType();
		RecordType recordType = new RecordType();
		HeaderType headerType = new HeaderType();		
		
		//GetRecord header
		headerType.setIdentifier(OAI_IDENTIFIER_PREFIX + resources.getResourceIdentifier());
		headerType.setDatestamp(dateFormatterShort.format(resources.getModified()));
		if(isDeleted(resources.getLogDate().getEventType())){
			headerType.setStatus(StatusType.DELETED);
		}				
		recordType.setHeader(headerType);	
		
		//VT: Set Metadata
		JAXBConverterInterface converter = this.getSuitableConverter(metadataPrefix);
		if(converter==null){
			return this.getCannotDisseminateFormat(VerbType.GET_RECORD);
		}
		
		if(!isDeleted(resources.getLogDate().getEventType())){
			recordType.setMetadata(getMetaData(converter,resources));
		}
		
		
		getRecordType.setRecord(recordType);						
		oaiType.setGetRecord(getRecordType);		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	
	
	public JAXBElement<OAIPMHtype> getListIdentifier(List<Resources> resources, String metadataPrefix, String from, String until, Long totalCount, TokenResumption tokenResumption) throws DatatypeConfigurationException, JAXBException, UnsupportedSchemeException{
		
		if(resources.isEmpty()){
			return this.getNoRecordMatch(VerbType.LIST_RECORDS);
		}
		
		//VT: Find suitable converter
		JAXBConverterInterface converter = this.getSuitableConverter(metadataPrefix);
		if(converter==null){
			return this.getCannotDisseminateFormat(VerbType.LIST_IDENTIFIERS);
		}
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.LIST_IDENTIFIERS);
		if(tokenResumption == null){
			if(from != null && !from.isEmpty()){
				requestType.setFrom(from);
			}		
			if(until !=null && !until.isEmpty()){
				requestType.setUntil(until);
			}
			requestType.setMetadataPrefix(metadataPrefix);
		}else{
			requestType.setResumptionToken(tokenResumption.getKey());
		}
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT:GetRecord
		ListIdentifiersType listIdentifiersType = new ListIdentifiersType();
		
		for(Resources resource : resources){			
			HeaderType headerType = new HeaderType();		
			
			//GetRecord header
			headerType.setIdentifier(OAI_IDENTIFIER_PREFIX + resource.getResourceIdentifier());
			headerType.setDatestamp(dateFormatterShort.format(resource.getModified()));
			if(resource.getLogDate()!=null && isDeleted(resource.getLogDate().getEventType())){
				headerType.setStatus(StatusType.DELETED);
			}				
															
			listIdentifiersType.getHeader().add(headerType);
		}
		
		
		listIdentifiersType.setResumptionToken(manageResumptionToken( metadataPrefix,  from,  until,  totalCount,  tokenResumption));
		oaiType.setListIdentifiers(listIdentifiersType);	
		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	
	public JAXBElement<OAIPMHtype> getListRecords(List<Resources> resources, String metadataPrefix, String from, String until, Long totalCount, TokenResumption tokenResumption) throws Exception{
		
		if(resources.isEmpty()){
			return this.getNoRecordMatch(VerbType.LIST_RECORDS);
		}
		
		//VT: Find suitable converter
		JAXBConverterInterface converter = this.getSuitableConverter(metadataPrefix);
		if(converter==null){
			return this.getCannotDisseminateFormat(VerbType.LIST_RECORDS);
		}
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormatterLong.format(new Date())));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.LIST_RECORDS);
		if(tokenResumption == null){
			if(from != null && !from.isEmpty()){
				requestType.setFrom(from);
			}		
			if(until !=null && !until.isEmpty()){
				requestType.setUntil(until);
			}
			requestType.setMetadataPrefix(metadataPrefix);
		}else{
			requestType.setResumptionToken(tokenResumption.getKey());
		}
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT:GetRecord
		ListRecordsType listRecordsType = new ListRecordsType();
		
		for(Resources resource : resources){
			RecordType recordType = new RecordType();
			HeaderType headerType = new HeaderType();		
			
			//GetRecord header
			headerType.setIdentifier(OAI_IDENTIFIER_PREFIX + resource.getResourceIdentifier());
			headerType.setDatestamp(dateFormatterShort.format(resource.getModified()));
			if(isDeleted(resource.getLogDate().getEventType())){
				headerType.setStatus(StatusType.DELETED);
			}else{
				try {
					MetadataType metadata = getMetaData(converter, resource);
					recordType.setMetadata(metadata);
				} catch (Exception e) {
					// TODO handle failed converting metadata for this record
				}
			}
			recordType.setHeader(headerType);	
			listRecordsType.getRecord().add(recordType);	
		}
		
		
		listRecordsType.setResumptionToken(manageResumptionToken( metadataPrefix,  from,  until,  totalCount,  tokenResumption));
		oaiType.setListRecords(listRecordsType);	
		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	
	
	private ResumptionTokenType manageResumptionToken(
			String metadataPrefix, String from, String until, Long totalCount,
			TokenResumption tokenResumption) throws DatatypeConfigurationException {

		//VT: Less record than the paging size therefore no token needed.
		if(totalCount <= ResourceEntityService.PAGING_SIZE){
			return null;
		}
		
		//VT: First entry without toekn and totalCount > than paging therefore generate token
		if(tokenResumption == null){
			TokenResumption token = new TokenResumption();
			token.setCompleteListSize(totalCount);
			token.setCursor(0);
			token.setPage(0);
			token.setFrom(from);
			token.setUntil(until);
			token.setMetadataprefix(metadataPrefix);
			String key = tokenResumptionService.put(token);
			
			ResumptionTokenType rtt = new ResumptionTokenType();
			rtt.setCompleteListSize(BigInteger.valueOf(token.getCompleteListSize().intValue()));
			rtt.setCursor(BigInteger.valueOf(token.getCursor()));
			rtt.setExpirationDate(tokenResumptionService.getExpiration(key));
			rtt.setValue(key);
			
			return rtt;
		}else{
			//VT: sequent entry has token					
			tokenResumption.setCursor(tokenResumption.getPage() * ResourceEntityService.PAGING_SIZE);
			
			if(tokenResumption.getPage() * ResourceEntityService.PAGING_SIZE >= totalCount - 1){
				//VT this is the end of the page
				ResumptionTokenType rtt = new ResumptionTokenType();
				rtt.setCompleteListSize(BigInteger.valueOf(tokenResumption.getCompleteListSize().intValue()));
				rtt.setCursor(BigInteger.valueOf(tokenResumption.getCursor()));								
				return rtt;
			}else{
				
				tokenResumptionService.update(tokenResumption.getKey(),tokenResumption);
				
				ResumptionTokenType rtt = new ResumptionTokenType();
				rtt.setCompleteListSize(BigInteger.valueOf(tokenResumption.getCompleteListSize().intValue()));
				rtt.setCursor(BigInteger.valueOf(tokenResumption.getCursor()));
				rtt.setExpirationDate(tokenResumptionService.getExpiration(tokenResumption.getKey()));
				rtt.setValue(tokenResumption.getKey());
				return rtt;
			}
		}
		
	}


	public MetadataType getMetaData(JAXBConverterInterface converter,Resources resource) throws Exception{

		MetadataType metaDataType = new MetadataType();
		metaDataType.setAny(converter.convert(resource));
				
		return metaDataType;
	}
	
	
	
	public JAXBConverterInterface getSuitableConverter(String metadataPrefix) throws UnsupportedSchemeException{
		for(JAXBConverterInterface converter:this.jaxbConverterInterface){
			if(converter.supports(metadataPrefix)){
				return converter;
			}
		}
				
		throw new UnsupportedSchemeException("Unable to find a converter that supports this schema");
	}


	


	

	
}
