

app.service('selectListService', ['$q','$http',function($q,$http) {

    this.getResourceType = function(){
        return [{key:"http://vocabulary.odm2.org/specimentype/automated",value:"Automated"},
            {key:"http://vocabulary.odm2.org/specimentype/core",value:"Core"},
            {key:"http://vocabulary.odm2.org/specimentype/coreHalfRound",value:"Core half round"},
            {key:"http://vocabulary.odm2.org/specimentype/corePiece",value:"Core piece"},
            {key:"http://vocabulary.odm2.org/specimentype/coreQuarterRound",value:"Core quarter round"},
            {key:"http://vocabulary.odm2.org/specimentype/coreSection",value:"Core section"},
            {key:"http://vocabulary.odm2.org/specimentype/coreSectionHalf",value:"Core section half"},
            {key:"http://vocabulary.odm2.org/specimentype/coreSub-Piece",value:"Core sub-piece"},
            {key:"http://vocabulary.odm2.org/specimentype/coreWholeRound",value:"Core whole round"},
            {key:"http://vocabulary.odm2.org/specimentype/cuttings",value:"Cuttings"},
            {key:"http://vocabulary.odm2.org/specimentype/dredge",value:"Dredge"},
            {key:"http://vocabulary.odm2.org/specimentype/foliageDigestion",value:"Foliage digestion"},
            {key:"http://vocabulary.odm2.org/specimentype/foliageLeaching",value:"Foliage leaching"},
            {key:"http://vocabulary.odm2.org/specimentype/forestFloorDigestion",value:"Forest floor digestion"},
            {key:"http://vocabulary.odm2.org/specimentype/grab",value:"Grab"},
            {key:"http://vocabulary.odm2.org/specimentype/individualSample",value:"Individual sample"},
            {key:"http://vocabulary.odm2.org/specimentype/litterFallDigestion",value:"Litter fall digestion"},
            {key:"http://vocabulary.odm2.org/specimentype/orientedCore",value:"Oriented core"},
            {key:"http://vocabulary.odm2.org/specimentype/other",value:"Other"},
            {key:"http://vocabulary.odm2.org/specimentype/petriDishDryDeposition",value:"Petri dish (dry deposition)"},
            {key:"http://vocabulary.odm2.org/specimentype/precipitationBulk",value:"Precipitation bulk"},
            {key:"http://vocabulary.odm2.org/specimentype/rockPowder",value:"Rock powder"},
            {key:"http://vocabulary.odm2.org/specimentype/standardReferenceSpecimen",value:"Standard reference specimen"},
            {key:"http://vocabulary.odm2.org/specimentype/terrestrialSection",value:"Terrestrial section"},
            {key:"http://vocabulary.odm2.org/specimentype/thinSection",value:"Thin section"},
            {key:"http://www.opengis.net/def/nil/OGC/0/inapplicable",value:"Inapplicable"},
            {key:"http://www.opengis.net/def/nil/OGC/0/missing",value:"Missing"},
            {key:"http://www.opengis.net/def/nil/OGC/0/template",value:"Template"},
            {key:"http://www.opengis.net/def/nil/OGC/0/unknown",value:"Unknown"},
            {key:"http://www.opengis.net/def/nil/OGC/0/withheld",value:"Withheld"}];
    };
      
      this.getMaterialType = function(){
    	  return [
              {key:"http://vocabulary.odm2.org/medium/air",value:"Air"},
              {key:"http://vocabulary.odm2.org/medium/gas",value:"Gas"},
              {key:"http://vocabulary.odm2.org/medium/habitat",value:"Habitat"},
              {key:"http://vocabulary.odm2.org/medium/ice",value:"Ice"},
              {key:"http://vocabulary.odm2.org/medium/liquidAqueous",value:"Liquid aqueous"},
              {key:"http://vocabulary.odm2.org/medium/liquidOrganic",value:"Liquid organic"},
              {key:"http://vocabulary.odm2.org/medium/mineral",value:"Mineral"},
              {key:"http://vocabulary.odm2.org/medium/notApplicable",value:"Not applicable"},
              {key:"http://vocabulary.odm2.org/medium/organism",value:"Organism"},
              {key:"http://vocabulary.odm2.org/medium/other",value:"Other"},
              {key:"http://vocabulary.odm2.org/medium/particulate",value:"Particulate"},
              {key:"http://vocabulary.odm2.org/medium/regolith",value:"Regolith"},
              {key:"http://vocabulary.odm2.org/medium/rock",value:"Rock"},
              {key:"http://vocabulary.odm2.org/medium/sediment",value:"Sediment"},
              {key:"http://vocabulary.odm2.org/medium/snow",value:"Snow"},
              {key:"http://vocabulary.odm2.org/medium/soil",value:"Soil"},
              {key:"http://vocabulary.odm2.org/medium/tissue",value:"Tissue"},
              {key:"http://www.opengis.net/def/nil/OGC/0/inapplicable",value:"Inaplicable"},
              {key:"http://www.opengis.net/def/nil/OGC/0/missing",value:"Missing"},
              {key:"http://www.opengis.net/def/nil/OGC/0/template",value:"Template"},
              {key:"http://www.opengis.net/def/nil/OGC/0/unknown",value:"Unknown"},
              {key:"http://www.opengis.net/def/nil/OGC/0/withheld",value:"Withheld"}
    	          ];
      };
      
      this.getEpsg = function(){
    	  return [
              {
                  key: "https://epsg.io/3112,",
                  value: "EPSG:3112 - Projected coordinate system"
              },
              {
                  key: "https://epsg.io/4283",
                  value: "EPSG:4283 - Geodetic coordinate system"
              },
              {
                  key: "https://epsg.io/4326",
                  value: "EPSG:4326 - Geodetic coordinate system"
              },
              {
                  key: "https://epsg.io/4939",
                  value: "EPSG:4939 - Geodetic 3D coordinate system"
              },
              {
                  key: "https://epsg.io/5711",
                  value: "EPSG:5711 - Vertical coordinate system"
              },
              {
                  key: "https://epsg.io/5712",
                  value: "EPSG:5712 - Vertical coordinate system"
              },
              {
                  key: "https://epsg.io/8311",
                  value: "EPSG:8311"
              }
          ];
      };
      
      this.getIdentifierType = function(){
    	  return [
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ARK",value:"ARK"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/arXiv",value:"arXiv"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/bibcode",value:"bibcode"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/DOI",value:"DOI"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/EAN13",value:"EAN13"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/EISSN",value:"EISSN"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/Handle",value:"Handle"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IGSN",value:"IGSN"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISBN",value:"ISBN"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISNI",value:"ISNI"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISSN",value:"ISSN"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ISTC",value:"ISTC"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/LISSN",value:"LISSN"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/LSID",value:"LSID"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/ORCID",value:"ORCID"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PMID",value:"PMID"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PURL",value:"PURL"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/UPC",value:"UPC"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/URL",value:"URL"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/URN",value:"URN"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/VIAF",value:"VIAF"}
    	          ];
      };
      
      
      this.getContributorType = function(){
    	  return [
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/collaborator",value:"Collaborator"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/contributor",value:"Contributor"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/custodian",value:"Custodian"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/funder",value:"Funder"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator",value:"Originator"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/owner",value:"Owner"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/pointOfContact",value:"Point of contact"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/principalInvestigator",value:"Principal investigator"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/processor",value:"Processor"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/rightsHolder",value:"Rights holder"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/sponsor",value:"Sponsor"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/stakeholder",value:"Stakeholder"},
              {key:"http://registry.it.csiro.au/def/isotc211/CI_RoleCode/user",value:"User"}
      	          ];
      };
      
      this.getRelationType = function(){
    	  return [

              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/HasDigitalRepresentation", value:"Has digital representation"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/HasReferenceResource", value:"Has reference resource"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/HasSamplingFeature", value:"Has sampling feature"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsAggregateOf", value:"Is aggregate of"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsDerivedFrom", value:"Is derived from"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsDigitalRepresentationOf", value:"Is digital representation of"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsDocumentedBy", value:"Is documented by"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsIdenticalTo", value:"Is identical to"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsMemberOf", value:"Is member of"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsSamplingFeatureOf", value:"Is sampling feature of"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/IsSourceOf", value:"Is source of"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/Participates", value:"Participates"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/hasAssociationWith", value:"Has association with"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/collectedAsPartOf", value:"Collected as part of"}
        	          ];
      }
      
      
      this.registeredObjectType = function(){
    	  return [
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PhysicalSample", value:"Physical sample"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/SampleCollection", value:"Sample collection"},
              {key:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/SamplingFeature", value:"Sampling feature"}
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

    this.getValue = function(the_key, kvArray) {
        var ret = the_key;
        angular.forEach(kvArray,function(item){
            if (item.key == the_key){
                ret = item.value;
            }
        });
        return ret;
    }
      
}]);

