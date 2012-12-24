package com.naughtyzombie.calypso.maven.source;

import com.naughtyzombie.calypso.maven.ui.CalypsoDataSourceException;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessCatalog;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.ReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 07/12/12
 * Time: 12:01

 */
@Component( role=CalypsoProcessDataSource.class, hint = "all")
public class AllCatalogCalypsoProcessDataSource extends CatalogCalypsoProcessDataSource {
    @Override
    public CalypsoProcessCatalog getCalypsoProcessCatalog(Properties properties) throws CalypsoDataSourceException {

        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(CALYPSO_CATALOG_FILENAME);
            Reader reader = ReaderFactory.newXmlReader(in);

            return readCatalog(reader);

        } catch (IOException e) {
            throw new CalypsoDataSourceException("Error reading catalog", e);
        }


    }

}
