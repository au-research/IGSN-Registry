package org.csiro.igsn.utilities;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtilities {
	
	public static Geometry wktToGeometry(String wkt,String sridXML) throws ParseException {
		
		int srid = Integer.parseInt(sridXML.replace("https://epsg.io/", ""));
				
				
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),srid));
        Geometry geom = null;
        try {
            geom = fromText.read(wkt);
            if( srid != 4326){
            	CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + srid);
        		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");        		
        		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        		geom = JTS.transform( geom, transform);
        		geom.setSRID(4326);
        		
        		//VT: For some reason this transformation swapped X and Y around for EPGS:4326 vs the others
//        		Coordinate[] original = geom.getCoordinates();
//                for(int i =0; i<original.length; i++){
//                    Double swapValue = original[i].x;
//                    original[i].x = original[i].y;
//                    original[i].y = swapValue;
//                }
            }
            
        } catch (Exception e) {
            throw new ParseException("Unable to parse and convert WKT");
        }
        return geom;
    }
	
	public static Geometry convertUTM_MGA942Geographic_EPSG4326(Double easting, Double northing, String zone) throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException, TransformException{
		CoordinateReferenceSystem geographic = CRS.decode("EPSG:4326", false);
		CoordinateReferenceSystem geographic2 = ReferencingFactoryFinder.getCRSAuthorityFactory(
				"EPSG", null).createCoordinateReferenceSystem("4326");
		

		
		CoordinateReferenceSystem utm = ReferencingFactoryFinder.getCRSAuthorityFactory(
				"EPSG", null).createCoordinateReferenceSystem("283"+zone);
		
		CoordinateOperationFactory coFactory = ReferencingFactoryFinder
				.getCoordinateOperationFactory(null);
		
		CoordinateReferenceSystem sourceCRS = utm;		
		CoordinateReferenceSystem targetCRS = geographic;

		GeometryFactory gf = new GeometryFactory();
        Coordinate coord = new Coordinate( easting, northing );
        Point point = gf.createPoint( coord );
        
		
		MathTransform mathTransform =CRS.findMathTransform( sourceCRS, targetCRS,true );
		Geometry g2 = JTS.transform(point, mathTransform);
		
		//VT: Flip around to correct the lat lon from the utm to geographic transformation
		Coordinate[] original = g2.getCoordinates();
		for(int i = 0; i < original.length; i++){
		    Double swapValue = original[i].x;
		    original[i].x = original[i].y;
		    original[i].y = swapValue;
		}
	    
		return g2;
	}
	

	public static Geometry convertUTM_MGA942Geographic_EPSG4326(String easting, String northing, String zone) throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException, TransformException{
		return convertUTM_MGA942Geographic_EPSG4326(Double.parseDouble(easting),Double.parseDouble(northing),zone);
	}
}
