package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.util.ResourceBundle;

import com.continuent.bristlecone.benchmark.db.Column;
import com.continuent.bristlecone.benchmark.db.SqlDialectForOracle;
import com.continuent.bristlecone.benchmark.db.Table;
import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.VisitScenario;
import java.io.InputStream;
import java.util.Properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Oracle spatial DBMS dialect information for 3D queries.
 */
public class SpatialSqlDialectForOracle3DNew implements SpatialSqlDialect3D {
    private String SRID;

    // NEW: Constructor that accepts Properties (recommended)
    public SpatialSqlDialectForOracle3DNew(Properties props) {
        this.SRID = props.getProperty("ORACLE_SRID", "").trim();
        
        if (this.SRID.isEmpty()) {
            throw new RuntimeException("ORACLE_SRID property is missing or empty");
        }
    }

    // EXISTING: Default constructor for backward compatibility
    public SpatialSqlDialectForOracle3DNew() {
        try (InputStream input = SpatialSqlDialectForOracle3DNew.class.getClassLoader()
                .getResourceAsStream("connection_general.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find connection_general.properties");
            }
            Properties props = new Properties();
            props.load(input);
            SRID = props.getProperty("ORACLE_SRID", "").trim();
            
            if (SRID.isEmpty()) {
                throw new RuntimeException("ORACLE_SRID property is missing or empty");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading properties", e);
        }
    }

    @Override
    public SupportedSqlDialect getSqlDialectType() {
        return SupportedSqlDialect.Oracle;
    }

    @Override
    public String getSelectBuffer3D() {
        return "SELECT SDO_GEOM.SDO_BUFFER(a.geom, 5280, 0.005) FROM arealm_merge a";
    }

    @Override
    public String getSelectClosestPoint3D() {
        return "SELECT SDO_GEOM.SDO_CLOSEST_POINTS(geom1, geom2, 0.005).closest_point FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectContains3D() {
        return "SELECT SDO_GEOM.RELATE(geom1, 'CONTAINS', geom2, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectConvexHull3D() {
        return "SELECT SDO_GEOM.SDO_CONVEXHULL(a.geom, 0.005) FROM arealm_merge a";
    }

    @Override
    public String getSelectCrosses3D() {
        return "SELECT SDO_GEOM.RELATE(geom1, 'CROSSES', geom2, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectDistance3D() {
        return "SELECT SDO_GEOM.SDO_DISTANCE(geom1, geom2, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectDistanceWithin3D() {
        return "SELECT SDO_GEOM.WITHIN_DISTANCE(geom1, geom2, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectDWithin3D() {
        return "SELECT SDO_GEOM.WITHIN_DISTANCE(geom1, geom2, ?, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectFullyWithin3D() {
        return "SELECT SDO_GEOM.RELATE(geom1, 'INSIDE+COVEREDBY', geom2, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectIntersects3D() {
        return "SELECT SDO_GEOM.RELATE(geom1, 'ANYINTERACT', geom2, 0.005) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectIs3D() {
        return "SELECT CASE WHEN geom.Get_Dims() = 3 THEN 1 ELSE 0 END FROM your_table WHERE id = ?";
    }

    @Override
    public String getSelectLength3D() {
        return "SELECT SDO_GEOM.SDO_LENGTH(geom, 0.005) FROM your_table WHERE id = ?";
    }

    @Override
    public String getSelectLongestLine3D() {
        return "SELECT SDO_GEOM.SDO_MAX_MBR_ORDINATE(SDO_GEOM.SDO_MBR(SDO_GEOM.SDO_XOR(geom1, geom2)), 'DISTANCE') FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectMaxDistance3D() {
        return "SELECT SDO_GEOM.SDO_MAX_MBR_ORDINATE(SDO_GEOM.SDO_MBR(SDO_GEOM.SDO_XOR(geom1, geom2)), 'DISTANCE') FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectShortestLine3D() {
        return "SELECT SDO_GEOM.SDO_CLOSEST_POINTS(geom1, geom2, 0.005).distance FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectReadSpatialBufferPolygon3D() {
        return "SELECT SDO_GEOM.SDO_BUFFER(geom, 100, 0.005) FROM your_table WHERE id = ?";
    }

    @Override
    public String getSelectReadSpatialLongestLine3D() {
        return "SELECT SDO_GEOM.SDO_MAX_MBR_ORDINATE(SDO_GEOM.SDO_MBR(SDO_GEOM.SDO_XOR(geom1, geom2)), 'DISTANCE') FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectAreaContainsArea() {
        return "SELECT SDO_GEOM.RELATE(a.geom, 'CONTAINS', b.geom, 0.005) FROM your_table a, your_table b WHERE a.id = ? AND b.id = ?";
    }

    @Override
    public String getSelectAllFeaturesWithinADistanceFromPoint() {
        return "SELECT * FROM your_table WHERE SDO_WITHIN_DISTANCE(geom, SDO_GEOMETRY(3001, " + SRID + ", SDO_POINT_TYPE(?, ?, ?), NULL, NULL), ?) = 'TRUE'";
    }

    @Override
    public String getSelectBoundingBoxSearch() {
        return "SELECT * FROM your_table WHERE SDO_FILTER(geom, SDO_GEOMETRY(3003, " + SRID + ", NULL, SDO_ELEM_INFO_ARRAY(1,1003,3), SDO_ORDINATE_ARRAY(?, ?, ?, ?, ?, ?))) = 'TRUE'";
    }

    @Override
    public String getSelect3DPointIntersectsLine() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(point_geom, 'ANYINTERACT', line_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DPointEqualsPoint() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(point_geom1, 'EQUAL', point_geom2, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DLineCrossesLine() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(line_geom1, 'CROSSES', line_geom2, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DLineEqualsLine() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(line_geom1, 'EQUAL', line_geom2, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DPointWithinRegion() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(point_geom, 'INSIDE', region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DLineWithinRegion() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(line_geom, 'INSIDE', region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DLineCrossesRegion() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(line_geom, 'CROSSES', region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DRegionContainsLine() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(region_geom, 'CONTAINS', line_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DRegionEqualsRegion() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(region_geom, 'EQUAL', another_region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelect3DRegionContainsRegion() {
        return "SELECT id FROM geometries WHERE SDO_GEOM.RELATE(region_geom, 'CONTAINS', another_region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectLineContainsVolume3D() {
        return "SELECT * FROM your_table WHERE SDO_GEOM.RELATE(line_geom, 'CONTAINS', volume_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectLineWithinVolume3D() {
        return "SELECT * FROM your_table WHERE SDO_GEOM.RELATE(line_geom, 'INSIDE', volume_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectVolumeContainsLine3D() {
        return "SELECT * FROM your_table WHERE SDO_GEOM.RELATE(volume_column, 'CONTAINS', line_column, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectLineIntersectsVolume3D() {
        return "SELECT * FROM your_table WHERE SDO_GEOM.RELATE(line_column, 'ANYINTERACT', volume_column, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectVolumeContainsRegion3D() {
        return "SELECT volume_id FROM volume_table WHERE SDO_GEOM.RELATE(volume_geom, 'CONTAINS', region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionInsideVolume3D() {
        return "SELECT region_id FROM region_table WHERE SDO_GEOM.RELATE(region_geom, 'INSIDE', volume_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionCrossesVolume3D() {
        return "SELECT region_id FROM region_table, volume_table WHERE SDO_GEOM.RELATE(region_table.region_geom, 'OVERLAPBDYINTERSECT', volume_table.volume_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionIntersectsVolume3D() {
        return "SELECT region_id FROM region_table, volume_table WHERE SDO_GEOM.RELATE(region_table.region_geom, 'ANYINTERACT', volume_table.volume_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionContainsPoint3D() {
        return "SELECT region_id FROM region_table, point_table WHERE SDO_GEOM.RELATE(region_table.region_geom, 'CONTAINS', point_table.point_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionCoversPoint3D() {
        return "SELECT region_id FROM region_table, point_table WHERE SDO_GEOM.RELATE(region_table.region_geom, 'COVERS', point_table.point_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionCoversLine3D() {
        return "SELECT region_id FROM region_table, line_table WHERE SDO_GEOM.RELATE(region_table.region_geom, 'COVERS', line_table.line_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionCoversRegion3D() {
        return "SELECT region1_id FROM region_table r1, region_table r2 WHERE SDO_GEOM.RELATE(r1.region_geom, 'COVERS', r2.region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectVolumeCoversLine3D() {
        return "SELECT volume_id FROM volume_table v, line_table l WHERE SDO_GEOM.RELATE(v.volume_geom, 'COVERS', l.line_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectVolumeCoversRegion3D() {
        return "SELECT volume_id FROM volume_table v, region_table r WHERE SDO_GEOM.RELATE(v.volume_geom, 'COVERS', r.region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectPointCoveredByRegion3D() {
        return "SELECT point_id FROM point_table p, region_table r WHERE SDO_GEOM.RELATE(p.point_geom, 'COVEREDBY', r.region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectRegionCoveredByRegion3D() {
        return "SELECT region_id FROM region_table1 r1, region_table2 r2 WHERE SDO_GEOM.RELATE(r1.region_geom, 'COVEREDBY', r2.region_geom, 0.005) = 'TRUE'";
    }

    @Override
    public String getSelectLineCoveredByVolume3D() {
        return "SELECT line_id FROM line_table l, volume_table v WHERE SDO_GEOM.RELATE(l.line_geom, 'COVEREDBY', v.volume_geom, 0.005) = 'TRUE'";
    }
    
    
			//////////////////////////////////////////MACRO BENCHMARK /////////////////////////////////

    
    public String getGeocodingQuery3D() {
        String sql = "SELECT t.tlid, t.fraddr, t.fraddl, t.toaddr, t.toaddl," +
            " t.zipL, t.zipR, t.tolat, t.tolong, t.frlong, t.frlat," +
            " t.long1, t.lat1, t.long2, t.lat2, t.long3, t.lat3, t.long4, t.lat4," +
            " t.long5, t.lat5, t.long6, t.lat6, t.long7, t.lat7, t.long8, t.lat8," +
            " t.long9, t.lat9, t.long10, t.lat10, t.fedirp, t.fetype, t.fedirs," +
            " t.elevation, t.building_height, t.num_floors, t.building_type" +
            " FROM geocoder_address t" +
            " WHERE t.fename = :fename AND " +
            "(" +
            " (t.fraddL <= :addr AND t.toaddL >= :addr) OR (t.fraddL >= :addr AND t.toaddL <= :addr) " +
            " OR (t.fraddR <= :addr AND t.toaddR >= :addr) OR (t.fraddR >= :addr AND t.toaddR <= :addr) " +
            ")" +
            " AND (t.zipL = :zip OR t.zipR = :zip)";

        return sql;
    }
    
    public String getCityStateForReverseGeocoding3D() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT name, st, SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY(?, " + SRID + "), 0.005) as dist " +
                  "FROM cityinfo_3d " +
                  "ORDER BY dist FETCH FIRST 1 ROW ONLY");
        return sb.toString();
    }

    public String getStreetAddressForReverseGeocoding3D() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT fullname, lfromadd, ltoadd, rfromadd, rtoadd, zipl, zipr, " +
                  "SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY(?, " + SRID + "), 0.005) as d " +
                  "FROM edges_merge_3d " +
                  "WHERE SDO_ANYINTERACT(geom, SDO_GEOMETRY(?, " + SRID + ")) = 'TRUE' " +
                  "AND SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY(?, " + SRID + "), 0.005) < 0.1 " +
                  "AND roadflg = 'Y' " +
                  "ORDER BY d FETCH FIRST 1 ROW ONLY");
        return sb.toString();
    }
    
    public void performSpatialJoin(Connection conn) throws Exception {
        String sql = "SELECT g.id AS green_id, r.id AS red_id " +
                     "FROM green_table g, red_table r " +
                     "WHERE SDO_RELATE(g.geometry_column, r.geometry_column, 'mask=ANYINTERACT') = 'TRUE'";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long startTime = System.currentTimeMillis();
            ResultSet rs = pstmt.executeQuery();
            long endTime = System.currentTimeMillis();
            System.out.println("Oracle Spatial Join executed in " + (endTime - startTime) + " ms");
            
            while (rs.next()) {
                System.out.println("Green ID: " + rs.getInt("green_id") + ", Red ID: " + rs.getInt("red_id"));
            }
        }
    }
    
    public void findClosestBloodVessels(Connection conn) throws Exception {
        String sql = "SELECT p.id AS purple_id, g.id AS green_id " +
                     "FROM purple_table p, green_table g " +
                     "WHERE SDO_NN(g.geometry_column, p.geometry_column, 'sdo_num_res=1') = 'TRUE' " +
                     "ORDER BY p.id";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long startTime = System.currentTimeMillis();
            ResultSet rs = pstmt.executeQuery();
            long endTime = System.currentTimeMillis();
            System.out.println("Oracle Nearest Neighbor Query executed in " + (endTime - startTime) + " ms");
            
            while (rs.next()) {
                System.out.println("Purple ID: " + rs.getInt("purple_id") + ", Closest Green ID: " + rs.getInt("green_id"));
            }
        }
    }


	@Override
	public String getLandUseQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSelectLineOverlapsLine3D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSelectRegionCoveredByVolume3D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsJdbcUrl(String url) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSelectBodyEqualsBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSelectBodyOverlapsBody() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectArealmIntersectsBuildings3Dvs3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelect3DLineEqualsLinevs3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectArea3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectPerimeter3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelect3DDistance3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getClosestPointAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getShortestLineAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getLongestLineAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getLineInterpolatePointAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectBuildingsDifferenceArea3Dvs3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String get3DConvexHullAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String get3DUnionSAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectBuildingIntersectionArea3Dvs3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectBuildingFullyWithinArea3d() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectLineOverlapsLine3Dvs3DLine() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectBuildingFullyWithinArea3DLine() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectBuildingIntersectionArea3Dvs3DLine() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectBuildingsDifferenceArea3Dvs3DLine() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getClosestPointAreaQueryLine() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getLongestLineAreaQueryLine() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getLineInterpolatePointAreaQueryLine() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectAreaContainsBuildings3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectLineIsContainedBuilding3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectLineOverlapsBuildings3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSelectArealmOverlapsBuildings3D() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getBuilding3DIntersectsAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBuilding3DIntersectsLineQuery() {
	    return "SELECT /*+ INDEX(a ROADS_GEOMETRY_IDX) INDEX(b BUILDINGS_GEOMETRY_IDX) */ " +
	           "a.FID AS road_id, " +
	           "b.ID AS building_id, " +
	           "1 AS touches " +
	           "FROM sys.roads_3d_new a, sys.buildings3d b " +
	           "WHERE SDO_TOUCH(a.geometry, b.geometry) = 'TRUE'";
	}


	@Override
	public String getBuilding3DDistanceLineQuery() {
	    return "SELECT /*+ INDEX(a ROADS_GEOMETRY_IDX) INDEX(b BUILDINGS_GEOMETRY_IDX) */ " +
	           "a.FID AS road_id, " +
	           "b.ID AS building_id, " +
	           "SDO_GEOM.SDO_DISTANCE(a.geometry, b.geometry, 0.005) AS distance " +
	           "FROM sys.roads_3d_new a, sys.buildings3d b " +
	           "WHERE SDO_WITHIN_DISTANCE(a.geometry, b.geometry, 'distance=10 unit=meter') = 'TRUE'";
	}



	@Override
	public String getBuilding3DDistanceAreaQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getBuilding3DDistanceWithinBuildingQuery() {
	    return "SELECT a.id as id_a, " +
	           "b.id as id_b, " +
	           "a.gml_id as gml_id_a, " +
	           "b.gml_id as gml_id_b " +
	           "FROM sys.buildings3d a " +
	           "JOIN sys.buildings3d b ON a.id < b.id " +
	           "WHERE SDO_WITHIN_DISTANCE(a.geometry, b.geometry, 'distance=10 unit=METER') = 'TRUE'";
	}

	@Override
	public String getBoundingBox3DQuery() {
	    return "SELECT id, SDO_GEOM.SDO_MBR(geometry) AS bounding_box_3d FROM sys.arealm3d";
	}


	@Override
	public String getConvexHullQuery() {
	    return "SELECT id, " +
	           "SDO_GEOM.SDO_CONVEXHULL(geometry, 0.005) AS convex_hull " +
	           "FROM sys.arealm3d";
	}

	@Override
	public String getDimensionsQuery() {
	    return "SELECT DISTINCT t.geometry.SDO_GTYPE/1000 AS dimensions " +
	           "FROM sys.arealm3d t";
	}



	@Override
	public String getLength3DQuery() {
	    return "SET TIMING ON;\n" +
	           "SELECT SDO_GEOM.SDO_LENGTH(geometry, 0.005) as length_3d FROM sys.arealm3d;\n" +
	           "SET TIMING OFF;";
	}


	@Override
	public String getPerimeter3DQuery() {
	    return "SET TIMING ON;\n" +
	           "SELECT SDO_GEOM.SDO_LENGTH(geometry, 0.005) as perimeter_3d FROM sys.arealm3d;\n" +
	           "SET TIMING OFF;";
	}



	@Override
	public String getBridgeAnalysisQuery() {
	    return "BEGIN DBMS_RANDOM.SEED(12345); END; " +
	           "WITH selected_points AS ( " +
	           "SELECT /*+ SAMPLE(1) */ id, geometry " +
	           "FROM buildings3d " +
	           "WHERE ROWNUM <= 100 " +
	           "), " +
	           "bridge_analysis AS ( " +
	           "SELECT s.id, " +
	           "COUNT(*) as connection_points, " +
	           "MAX(SDO_GEOM.SDO_DISTANCE( " +
	           "SDO_CS.MAKE_3D(b.geometry, 75), " +
	           "SDO_CS.MAKE_3D(s.geometry, 75), " +
	           "0.005, " +
	           "'unit=meter' " +
	           ")) as bridge_span " +
	           "FROM buildings3d b " +
	           "JOIN selected_points s ON SDO_WITHIN_DISTANCE( " +
	           "b.geometry, " +
	           "s.geometry, " +
	           "'distance=100 unit=meter' " +
	           ") = 'TRUE' " +
	           "WHERE b.id != s.id " +
	           "GROUP BY s.id " +
	           ") " +
	           "SELECT id, connection_points, bridge_span " +
	           "FROM bridge_analysis " +
	           "WHERE connection_points > 5 " +
	           "ORDER BY bridge_span DESC " +
	           "FETCH FIRST 5 ROWS ONLY";
	}
	


	@Override
	public String getCancerousAnalysisIntersectionQuery() {
	    return "SET TIMING ON;\n" +
	           "SELECT a.ROWID as cell1_id,\n" +
	           "       b.ROWID as cell2_id,\n" +
	           "       CASE WHEN SDO_GEOM.RELATE(a.geometry, 'ANYINTERACT', b.geometry, 0.005) = 'TRUE'\n" +
	           "            THEN 1\n" +
	           "            ELSE 0\n" +
	           "       END AS intersection_3d\n" +
	           "FROM synthetic_cells a, synthetic_cells b\n" +
	           "WHERE a.ROWID < b.ROWID\n" +
	           "  AND SDO_WITHIN_DISTANCE(a.geometry, b.geometry, 'distance=10') = 'TRUE'\n" +
	           "  AND ROWNUM <= 10000;\n" +
	           "SET TIMING OFF;";
	}



	@Override
	public String getEmergencyRoutesQuery() {
	    return "EXPLAIN ANALYZE\n" +
	           "SET AUTOTRACE ON EXPLAIN STAT\n" +
	           "SET TIMING ON\n" +
	           "BEGIN DBMS_RANDOM.SEED(12345); END;\n" +
	           "/\n" +
	           "WITH selected_points AS (\n" +
	           "    SELECT /*+ SAMPLE(1) */ id, geometry\n" +
	           "    FROM buildings3d\n" +
	           "    WHERE ROWNUM <= 50\n" +
	           "),\n" +
	           "route_analysis AS (\n" +
	           "    SELECT s.id,\n" +
	           "           COUNT(*) as building_density,\n" +
	           "           MAX(SDO_GEOM.SDO_DISTANCE(\n" +
	           "               r.geometry,\n" +
	           "               s.geometry,\n" +
	           "               0.005,\n" +
	           "               'unit=meter'\n" +
	           "           )) as clearance_radius\n" +
	           "    FROM buildings3d r\n" +
	           "    JOIN selected_points s ON SDO_WITHIN_DISTANCE(\n" +
	           "        r.geometry,\n" +
	           "        s.geometry,\n" +
	           "        'distance=100 unit=meter'\n" +
	           "    ) = 'TRUE'\n" +
	           "    WHERE r.id != s.id\n" +
	           "    GROUP BY s.id\n" +
	           ")\n" +
	           "SELECT id, building_density, clearance_radius\n" +
	           "FROM route_analysis\n" +
	           "ORDER BY building_density ASC, clearance_radius DESC\n" +
	           "FETCH FIRST 5 ROWS ONLY;";
	}



	@Override
	public String getFutureExpansionQuery() {
	    return "EXPLAIN ANALYZE\n" +
	           "SET AUTOTRACE ON EXPLAIN STAT\n" +
	           "SET TIMING ON\n" +
	           "BEGIN DBMS_RANDOM.SEED(12345); END;\n" +
	           "/\n" +
	           "WITH selected_points AS (\n" +
	           "    SELECT /*+ SAMPLE(1) */ id, geometry\n" +
	           "    FROM buildings3d\n" +
	           "    WHERE ROWNUM <= 100\n" +
	           "),\n" +
	           "expansion_analysis AS (\n" +
	           "    SELECT s.id,\n" +
	           "           COUNT(*) as building_density,\n" +
	           "           MIN(SDO_GEOM.SDO_DISTANCE(\n" +
	           "               r.geometry,\n" +
	           "               s.geometry,\n" +
	           "               0.005,\n" +
	           "               'unit=meter'\n" +
	           "           )) as distance_to_nearest,\n" +
	           "           AVG(SDO_GEOM.SDO_DISTANCE(\n" +
	           "               r.geometry,\n" +
	           "               s.geometry,\n" +
	           "               0.005,\n" +
	           "               'unit=meter'\n" +
	           "           )) as avg_distance\n" +
	           "    FROM buildings3d r\n" +
	           "    JOIN selected_points s ON SDO_WITHIN_DISTANCE(\n" +
	           "        r.geometry,\n" +
	           "        s.geometry,\n" +
	           "        'distance=500 unit=meter'\n" +
	           "    ) = 'TRUE'\n" +
	           "    WHERE r.id != s.id\n" +
	           "    GROUP BY s.id\n" +
	           ")\n" +
	           "SELECT id, building_density, distance_to_nearest, avg_distance\n" +
	           "FROM expansion_analysis\n" +
	           "WHERE building_density < 50\n" +
	           "ORDER BY building_density ASC, avg_distance DESC\n" +
	           "FETCH FIRST 5 ROWS ONLY;";
	}


	@Override
	public String getGardenAnalysisQuery() {
	    return "SET TIMING ON\n" +
	           "BEGIN DBMS_RANDOM.SEED(12345); END;\n" +
	           "/\n" +
	           "WITH selected_points AS (\n" +
	           "    SELECT /*+ SAMPLE(1) */ id, geometry\n" +
	           "    FROM buildings3d\n" +
	           "    WHERE ROWNUM <= 100\n" +
	           "),\n" +
	           "garden_analysis AS (\n" +
	           "    SELECT s.id,\n" +
	           "           COUNT(*) as space_score,\n" +
	           "           MIN(SDO_GEOM.SDO_DISTANCE(\n" +
	           "               SDO_CS.MAKE_3D(b.geometry, 100),\n" +
	           "               SDO_CS.MAKE_3D(s.geometry, 100),\n" +
	           "               0.005,\n" +
	           "               'unit=meter'\n" +
	           "           )) as clear_space\n" +
	           "    FROM buildings3d b\n" +
	           "    JOIN selected_points s ON SDO_WITHIN_DISTANCE(\n" +
	           "        b.geometry,\n" +
	           "        s.geometry,\n" +
	           "        'distance=150 unit=meter'\n" +
	           "    ) = 'TRUE'\n" +
	           "    WHERE b.id != s.id\n" +
	           "    GROUP BY s.id\n" +
	           ")\n" +
	           "SELECT id, space_score, clear_space\n" +
	           "FROM garden_analysis\n" +
	           "ORDER BY clear_space DESC\n" +
	           "FETCH FIRST 5 ROWS ONLY;\n" +
	           "SET TIMING OFF;";
	}



	@Override
	public String getLength3DMedicalAnalysisQuery() {
	    return "SET TIMING ON;\n" +
	           "SELECT ROWID, SDO_GEOM.SDO_LENGTH(geometry, 0.005) AS perimeter_3d FROM synthetic_cells;\n" +
	           "SET TIMING OFF;";
	}


	@Override
	public String getPerimeter3DMedicalAnalysisQuery() {
	    return "EXPLAIN ANALYZE SELECT \n" +
	           " ogc_fid,\n" +
	           " ST_3DLength(wkb_geometry) AS perimeter_3d\n" +
	           "FROM \n" +
	           " synthetic_cells_3d;";
	}


	@Override
	public String getSubwayStationLocationQuery() {
	    return "BEGIN DBMS_RANDOM.SEED(12345); END;\n" +
	           "/\n" +
	           "SET TIMING ON\n" +
	           "WITH selected_points AS (\n" +
	           "    SELECT /*+ SAMPLE(1) */ id, geometry\n" +
	           "    FROM buildings3d\n" +
	           "    WHERE ROWNUM <= 100\n" +
	           "),\n" +
	           "subway_analysis AS (\n" +
	           "    SELECT s.id,\n" +
	           "           COUNT(*) as population_density,\n" +
	           "           MIN(SDO_GEOM.SDO_DISTANCE(\n" +
	           "               b.geometry,\n" +
	           "               s.geometry,\n" +
	           "               0.005,\n" +
	           "               'unit=meter'\n" +
	           "           )) as min_distance_meters\n" +
	           "    FROM buildings3d b\n" +
	           "    JOIN selected_points s ON SDO_WITHIN_DISTANCE(\n" +
	           "        b.geometry,\n" +
	           "        s.geometry,\n" +
	           "        'distance=25 unit=meter'\n" +
	           "    ) = 'TRUE'\n" +
	           "    WHERE b.id != s.id\n" +
	           "    GROUP BY s.id\n" +
	           ")\n" +
	           "SELECT id, population_density, min_distance_meters\n" +
	           "FROM subway_analysis\n" +
	           "ORDER BY population_density DESC, min_distance_meters\n" +
	           "FETCH FIRST 5 ROWS ONLY;\n" +
	           "SET TIMING OFF;";
	}



}


