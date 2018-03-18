import org.csiro.igsn.utilities.SpatialUtilities;
import org.geotools.factory.FactoryRegistryException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

public class TEST {
	public static void main(String [] args) throws NoSuchAuthorityCodeException, FactoryRegistryException, FactoryException, MismatchedDimensionException, TransformException{

		
		String zone = "52";
		double easting =596450.153;
		double northing = 6680793.777;
		System.out.print(SpatialUtilities.convertUTM_MGA942Geographic_EPSG4326(easting, northing, zone));
		
//		String zone = "53";
//		double easting =616450.153;
//		double northing = 6680793.777;
//		System.out.print(SpatialUtilities.convertUTM_MGA942Geographic_EPSG4326(easting, northing, zone));
		
	}

}
