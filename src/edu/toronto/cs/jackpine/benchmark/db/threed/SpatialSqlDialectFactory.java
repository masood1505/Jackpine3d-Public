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
        // First try to determine dialect from DBMS property
        String dbms = props.getProperty("DBMS", "").toLowerCase().trim();
        
        if (!dbms.isEmpty()) {
            logger.info("Using DBMS property: " + dbms);
            switch (dbms) {
                case "postgres":
                case "postgresql":
                    logger.info("Creating PostgreSQL 3D spatial dialect from DBMS property");
                    return new SpatialSqlDialectForPostgreSQL3DNew(props);
                case "oracle":
                    logger.info("Creating Oracle 3D spatial dialect from DBMS property");
                    return new SpatialSqlDialectForOracle3DNew();
                default:
                    logger.warn("Unknown DBMS property value: " + dbms + ". Falling back to URL detection.");
                    break;
            }
        } else {
            logger.info("DBMS property not set, falling back to URL detection");
        }
        
        // Fallback to URL-based detection if DBMS property is not set or unknown
        if (url.startsWith("jdbc:postgresql:")) {
            logger.info("Creating PostgreSQL 3D spatial dialect from URL detection");
            return new SpatialSqlDialectForPostgreSQL3DNew(props);
        } else if (url.startsWith("jdbc:oracle:")) {
            logger.info("Creating Oracle 3D spatial dialect from URL detection");
            return new SpatialSqlDialectForOracle3DNew();
        }

        logger.error("No suitable 3D spatial dialect found for URL: " + url + " and DBMS: " + dbms);
        throw new UnsupportedOperationException(
            "No suitable 3D spatial dialect found for URL: " + url + 
            " and DBMS: " + dbms + 
            ". Supported DBMS values are: 'postgres', 'oracle'"
        );
    }
}