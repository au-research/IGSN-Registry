

app.service('selectListService', ['$q','$http',function($q,$http) {

	this.getResourceType = function(){
    	  return [
				"http://vocabulary.odm2.org/specimentype/automated",
				"http://vocabulary.odm2.org/specimentype/core",
				"http://vocabulary.odm2.org/specimentype/coreHalfRound",
				"http://vocabulary.odm2.org/specimentype/corePiece",
				"http://vocabulary.odm2.org/specimentype/coreQuarterRound",
				"http://vocabulary.odm2.org/specimentype/coreSection",
				"http://vocabulary.odm2.org/specimentype/coreSectionHalf",
				"http://vocabulary.odm2.org/specimentype/coreSub-Piece",
				"http://vocabulary.odm2.org/specimentype/coreWholeRound",
				"http://vocabulary.odm2.org/specimentype/cuttings",
				"http://vocabulary.odm2.org/specimentype/dredge",
				"http://vocabulary.odm2.org/specimentype/foliageDigestion",
				"http://vocabulary.odm2.org/specimentype/foliageLeaching",
				"http://vocabulary.odm2.org/specimentype/forestFloorDigestion",
				"http://vocabulary.odm2.org/specimentype/grab",
				"http://vocabulary.odm2.org/specimentype/individualSample",
				"http://vocabulary.odm2.org/specimentype/litterFallDigestion",
				"http://vocabulary.odm2.org/specimentype/orientedCore",
				"http://vocabulary.odm2.org/specimentype/other",
				"http://vocabulary.odm2.org/specimentype/petriDishDryDeposition",
				"http://vocabulary.odm2.org/specimentype/precipitationBulk",
				"http://vocabulary.odm2.org/specimentype/rockPowder",
				"http://vocabulary.odm2.org/specimentype/standardReferenceSpecimen",
				"http://vocabulary.odm2.org/specimentype/terrestrialSection",
				"http://vocabulary.odm2.org/specimentype/thinSection",
				"http://www.opengis.net/def/nil/OGC/0/inapplicable",
				"http://www.opengis.net/def/nil/OGC/0/missing",
				"http://www.opengis.net/def/nil/OGC/0/template",
				"http://www.opengis.net/def/nil/OGC/0/unknown",
				"http://www.opengis.net/def/nil/OGC/0/withheld"
    	          ];
      };
      
      this.getMaterialType = function(){
    	  return [
				"http://vocabulary.odm2.org/medium/air",
				"http://vocabulary.odm2.org/medium/gas",
				"http://vocabulary.odm2.org/medium/habitat",
				"http://vocabulary.odm2.org/medium/ice",
				"http://vocabulary.odm2.org/medium/liquidAqueous",
				"http://vocabulary.odm2.org/medium/liquidOrganic",
				"http://vocabulary.odm2.org/medium/mineral",
				"http://vocabulary.odm2.org/medium/notApplicable",
				"http://vocabulary.odm2.org/medium/organism",
				"http://vocabulary.odm2.org/medium/other",
				"http://vocabulary.odm2.org/medium/particulate",
				"http://vocabulary.odm2.org/medium/regolith",
				"http://vocabulary.odm2.org/medium/rock",
				"http://vocabulary.odm2.org/medium/sediment",
				"http://vocabulary.odm2.org/medium/snow",
				"http://vocabulary.odm2.org/medium/soil",
				"http://vocabulary.odm2.org/medium/tissue",
				"http://www.opengis.net/def/nil/OGC/0/inapplicable",
				"http://www.opengis.net/def/nil/OGC/0/missing",
				"http://www.opengis.net/def/nil/OGC/0/template",
				"http://www.opengis.net/def/nil/OGC/0/unknown",
				"http://www.opengis.net/def/nil/OGC/0/withheld"
    	          ];
      };
      
      this.getEpsg = function(){
    	  return [
				"https://epsg.io/3112",
				"https://epsg.io/4283",
				"https://epsg.io/4326",
				"https://epsg.io/4939",
				"https://epsg.io/5711",
				"https://epsg.io/5712",
				"https://epsg.io/8311"
    	          ];
      };
      
      this.getIdentifierType = function(){
    	  return [
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ARK",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/arXiv",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/bibcode",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/DOI",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/EAN13",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/EISSN",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/Handle",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IGSN",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISBN",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISNI",				
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISSN",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISTC",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/LISSN",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/LSID",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ORCID",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PMID",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PURL",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/UPC",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/URL",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/URN",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/VIAF"
    	          ];
      };
      
      
      this.getContributorType = function(){
    	  return [
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/collaborator",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/contributor",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/custodian",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/funder",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/owner",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/pointOfContact",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/principalInvestigator",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/processor",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/rightsHolder",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/sponsor",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/stakeholder",
				"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/user"
      	          ];
      };
      
      this.getRelationType = function(){
    	  return [
  				
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/HasDigitalRepresentation",  			
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/HasReferenceResource",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/HasSamplingFeature",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsAggregateOf",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsDerivedFrom",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsDigitalRepresentationOf",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsDocumentedBy",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsIdenticalTo",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsMemberOf",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsSamplingFeatureOf",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsSourceOf",
  				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/Participates"
        	          ];
      }
      
      
      this.registeredObjectType = function(){
    	  return [
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PhysicalSample",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/SampleCollection",
				"http://pid.geoscience.gov.au/def/voc/igsn-codelists/SamplingFeature"
      	          ];
      }
      
      this.getTrueFalse = function(){
    	  return [
				"true",
				"false"
    	          ];
     }
      
      this.getMGAZone = function(){
    	  return [
				"49",
				"50",
				"51",
				"52",
				"53",
				"54",
				"55",
				"56"
  	          ];
      }
      
}]);