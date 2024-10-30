package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.util.Properties;
import org.apache.log4j.Logger;

public class SpatialSqlDialectFactory {
    private static final Logger logger = Logger.getLogger(SpatialSqlDialectFactory.class);
    private static volatile SpatialSqlDialectFactory instance;

    private SpatialSqlDialectFactory() {}

    public static SpatialSqlDialectFactory getInstance() {
        if (instance == null) {
            synchronized (SpatialSqlDialectFactory.class) {
                if (instance == null) {
                    instance = new SpatialSqlDialectFactory();
                }
            }
        }
        return instance;
    }

    public SpatialSqlDialect3D getDialect(String url, Properties props) throws UnsupportedOperationException {
        if (url.startsWith("jdbc:postgresql:")) {
            logger.info("Creating PostgreSQL 3D spatial dialect");
            return new SpatialSqlDialectForPostgreSQL3DNew(props);
        }
        // Add other dialect creations here

        logger.warn("No suitable 3D spatial dialect found for URL: " + url);
        throw new UnsupportedOperationException("No suitable 3D spatial dialect found for URL: " + url);
    }
}