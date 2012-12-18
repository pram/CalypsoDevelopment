package com.naughtyzombie.calypso.maven.source;

import com.naughtyzombie.calypso.maven.io.xpp3.CalypsoProcessCatalogXpp3Reader;
import com.naughtyzombie.calypso.maven.ui.CalypsoDataSourceException;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessCatalog;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 07/12/12
 * Time: 11:56
 */

@Component( role=CalypsoProcessDataSource.class, hint = "catalog")
public class CatalogCalypsoProcessDataSource
        extends AbstractLogEnabled
        implements CalypsoProcessDataSource {

    public static final String CALYPSO_CATALOG_FILENAME = "calypso-catalog-all.xml";


    @Override
    public CalypsoProcessCatalog getCalypsoProcessCatalog(Properties properties) throws CalypsoDataSourceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	protected CalypsoProcessCatalog readCatalog(Reader reader) throws CalypsoDataSourceException{
		try {
			CalypsoProcessCatalogXpp3Reader catalogReader = new CalypsoProcessCatalogXpp3Reader();

			return catalogReader.read(reader);
		} catch (IOException e) {
			throw new CalypsoDataSourceException("Error reading Calypso Process catalog.", e);
		} catch (XmlPullParserException e) {
			throw new CalypsoDataSourceException("Error parsing Calypso Process catalog.", e);
		} finally {
			IOUtil.close(reader);
		}
	}
}
