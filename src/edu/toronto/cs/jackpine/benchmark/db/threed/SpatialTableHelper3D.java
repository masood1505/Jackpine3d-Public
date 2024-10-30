package edu.toronto.cs.jackpine.benchmark.db.threed;

import org.apache.log4j.Logger;
import com.continuent.bristlecone.benchmark.db.TableHelper;
import java.util.Properties;

public class SpatialTableHelper3D extends TableHelper {
    private static Logger logger = Logger.getLogger(SpatialTableHelper3D.class);

    protected final SpatialSqlDialect3D spatialSqlDialect3D;

    public SpatialTableHelper3D(String url, String login, String password, Properties props) {
        super(url, login, password);
        this.spatialSqlDialect3D = SpatialSqlDialectFactory.getInstance().getDialect(url, props);
        logger.info("SpatialTableHelper3D initialized with URL: " + url);
    }

    /** 
     * Returns the 3D SQLDialect used by this helper. 
     */
    public SpatialSqlDialect3D getSpatialSqlDialect3D() {
        return spatialSqlDialect3D;
    }

    // You can add more 3D-specific methods here if needed
}
