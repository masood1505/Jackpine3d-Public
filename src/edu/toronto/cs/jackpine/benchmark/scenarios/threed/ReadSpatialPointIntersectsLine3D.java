/**
 * Jackpine Spatial Database Benchmark 
 *  Copyright (C) 2010 University of Toronto
 * 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 *
 * Developer: S. Ray
 * Contributor(s): 
 */

package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D; // Import the 3D interface
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

/**
 * A scenario for testing 3D spatial point intersects line queries.
 * 
 * @version 1.0
 */
public class ReadSpatialPointIntersectsLine3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialPointIntersectsLine3D.class);

    protected PreparedStatement[] pstmtArray;
    
    protected SpatialTableHelper3D helper3D;

    public ReadSpatialPointIntersectsLine3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }
    
    

    /** Create a prepared statement array. */
    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect3D dialect = (SpatialSqlDialect3D) helper.getSpatialSqlDialect();

        pstmtArray = new PreparedStatement[1];
        String sql = dialect.getSelect3DPointIntersectsLine();
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    /** Execute an iteration. */
    @Override
    public void iterate(long iterationCount) throws Exception {
        // Pick a table at random on which to operate.
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        logger.warn(pstmt.toString());
        // Do the query.
        pstmt.executeQuery();
    }

    /** Clean up resources used by scenario. */
    @Override
    public void cleanup() throws Exception {
        // Clean up connections.
        for (PreparedStatement pstmt : pstmtArray) {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        if (conn != null) {
            conn.close();
        }
    }
}
