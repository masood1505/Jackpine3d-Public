package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.InputStream;


import java.util.Properties;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.InputStream;
import java.util.Properties;

public class SpatialSqlDialectForSQLServer3D implements SpatialSqlDialect3D {
	  static String SRID = "";

	    static {
	        try (InputStream input = SpatialSqlDialectForSQLServer3D.class.getClassLoader()
	                .getResourceAsStream("connection_general.properties")) {
	            if (input == null) {
	                throw new RuntimeException("Unable to find connection_general.properties");
	            }
	            Properties props = new Properties();
	            props.load(input);
	            SRID = props.getProperty("SQLSERVER_SRID", "").trim();
	        } catch (Exception e) {
	            throw new RuntimeException("Error loading properties", e);
	        }
	    }
	
    @Override
    public SupportedSqlDialect getSqlDialectType() {
        return SupportedSqlDialect.SQLServer;
    }

    public String getSelectBuffer3D() {
        return "SELECT a.geom.STBuffer(5280) FROM arealm_merge a";
    }

    @Override
    public String getSelectClosestPoint3D() {
        return "SELECT geom1.STClosestPoint(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectContains3D() {
        return "SELECT geom1.STContains(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectConvexHull3D() {
        return "SELECT a.geom.STConvexHull() FROM arealm_merge a";
    }

    @Override
    public String getSelectCrosses3D() {
        return "SELECT geom1.STCrosses(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectDistance3D() {
        return "SELECT geom1.STDistance(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectDistanceWithin3D() {
        return "SELECT geom1.STDistance(geom2) <= ? FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectDWithin3D() {
        return "SELECT geom1.STDistance(geom2) <= ? FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectFullyWithin3D() {
        return "SELECT geom1.STWithin(geom2) = 1 FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectIntersects3D() {
        return "SELECT geom1.STIntersects(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectIs3D() {
        return "SELECT geom.STDimension() = 3 FROM your_table WHERE id = ?";
    }

    @Override
    public String getSelectLength3D() {
        return "SELECT geom.STLength() FROM your_table WHERE id = ?";
    }

    @Override
    public String getSelectLongestLine3D() {
        return "SELECT geom1.STMaxDistance(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectMaxDistance3D() {
        return "SELECT geom1.STMaxDistance(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectShortestLine3D() {
        return "SELECT geom1.STShortestLineTo(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectReadSpatialBufferPolygon3D() {
        return "SELECT geom.STBuffer(100) FROM your_table WHERE id = ?";
    }

    @Override
    public String getSelectReadSpatialLongestLine3D() {
        return "SELECT geom1.STMaxDistance(geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
    }

    @Override
    public String getSelectAreaContainsArea() {
        return "SELECT a.geom.STContains(b.geom) FROM your_table a, your_table b WHERE a.id = ? AND b.id = ?";
    }

    public String getSelectAllFeaturesWithinADistanceFromPoint() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelectBoundingBoxSearch() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelect3DPointIntersectsLine() {
        return "SELECT id FROM geometries WHERE point_geom.STIntersects(line_geom) = 1";
    }

    @Override
    public String getSelect3DPointEqualsPoint() {
        return "SELECT id FROM geometries WHERE point_geom1.STEquals(point_geom2) = 1";
    }

    @Override
    public String getSelect3DLineCrossesLine() {
        return "SELECT id FROM geometries WHERE line_geom1.STCrosses(line_geom2) = 1";
    }

    public String getSelect3DLineEqualsLine() {
        return "SELECT id FROM geometries WHERE line_geom1.STEquals(line_geom2) = 1";
    }

    @Override
    public String getSelect3DPointWithinRegion() {
        return "SELECT id FROM geometries WHERE point_geom.STWithin(region_geom) = 1";
    }

    public String getSelect3DLineWithinRegion() {
        return "SELECT id FROM geometries WHERE line_geom.STWithin(region_geom) = 1";
    }

    @Override
    public String getSelect3DLineCrossesRegion() {
        return "SELECT id FROM geometries WHERE line_geom.STCrosses(region_geom) = 1";
    }

    @Override
    public String getSelect3DRegionContainsLine() {
        return "SELECT id FROM geometries WHERE region_geom.STContains(line_geom) = 1";
    }

    public String getSelect3DRegionEqualsRegion() {
        return "SELECT id FROM geometries WHERE region_geom.STEquals(another_region_geom) = 1";
    }

    @Override
    public String getSelect3DRegionContainsRegion() {
        return "SELECT id FROM geometries WHERE region_geom.STContains(another_region_geom) = 1";
    }

    @Override
    public String getSelectLineContainsVolume3D() {
        return "SELECT * FROM your_table WHERE line_geom.STContains(volume_geom) = 1";
    }

    @Override
    public String getSelectLineWithinVolume3D() {
        return "SELECT * FROM your_table WHERE line_geom.STWithin(volume_geom) = 1";
    }

    @Override
    public String getSelectVolumeContainsLine3D() {
        return "SELECT * FROM your_table WHERE volume_column.STContains(line_column) = 1";
    }

    @Override
    public String getSelectLineIntersectsVolume3D() {
        return "SELECT * FROM your_table WHERE line_column.STIntersects(volume_column) = 1";
    }

    public String getSelectVolumeContainsRegion3D() {
        return "SELECT volume_id FROM volume_table WHERE volume_geom.STContains(region_geom) = 1";
    }

    @Override
    public String getSelectRegionInsideVolume3D() {
        return "SELECT region_id FROM region_table WHERE region_geom.STWithin(volume_geom) = 1";
    }

    @Override
    public String getSelectRegionCrossesVolume3D() {
        return "SELECT region_id FROM region_table, volume_table WHERE region_table.region_geom.STCrosses(volume_table.volume_geom) = 1";
    }

    @Override
    public String getSelectRegionIntersectsVolume3D() {
        return "SELECT region_id FROM region_table, volume_table WHERE region_table.region_geom.STIntersects(volume_table.volume_geom) = 1";
    }

    @Override
    public String getSelectRegionContainsPoint3D() {
        return "SELECT region_id FROM region_table, point_table WHERE region_table.region_geom.STContains(point_table.point_geom) = 1";
    }

    public String getSelectRegionCoversPoint3D() {
        return "SELECT region_id FROM region_table, point_table WHERE region_table.region_geom.STContains(point_table.point_geom) = 1";
    }

    @Override
    public String getSelectRegionCoversLine3D() {
        return "SELECT region_id FROM region_table, line_table WHERE region_table.region_geom.STContains(line_table.line_geom) = 1";
    }

    public String getSelectRegionCoversRegion3D() {
        return "SELECT region1_id FROM region_table AS r1, region_table AS r2 WHERE r1.region_geom.STContains(r2.region_geom) = 1";
    }

    public String getSelectVolumeCoversLine3D() {
        return "SELECT volume_id FROM volume_table AS v, line_table AS l WHERE v.volume_geom.STContains(l.line_geom) = 1";
    }

    @Override
    public String getSelectVolumeCoversRegion3D() {
        return "SELECT volume_id FROM volume_table AS v, region_table AS r WHERE v.volume_geom.STContains(r.region_geom) = 1";
    }

    public String getSelectPointCoveredByRegion3D() {
        return "SELECT point_id FROM point_table AS p JOIN region_table AS r ON r.region_geom.STIntersects(p.point_geom) = 1";
    }

    public String getSelectRegionCoveredByRegion3D() {
        return "SELECT region_id FROM region_table1 AS r1 JOIN region_table2 AS r2 ON r1.region_geom.STIntersects(r2.region_geom) = 1";
    }

    public String getSelectLineCoveredByVolume3D() {
        return "SELECT line_id FROM line_table AS l JOIN volume_table AS v ON v.volume_geom.STIntersects(l.line_geom) = 1";
    }

    public String getSelectRegionCoveredByVolume3D() {
        return "SELECT region_id FROM region_table AS r JOIN volume_table AS v ON v.volume_geom.STIntersects(r.region_geom) = 1";
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
            " WHERE t.fename = @fename AND " +
            "(" +
            " (t.fraddL <= @addr AND t.toaddL >= @addr) OR (t.fraddL >= @addr AND t.toaddL <= @addr) " +
            " OR (t.fraddR <= @addr AND t.toaddR >= @addr) OR (t.fraddR >= @addr AND t.toaddR <= @addr) " +
            ")" +
            " AND (t.zipL = @zip OR t.zipR = @zip)";

        return sql;
    }
    
    public String getCityStateForReverseGeocoding3D() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT TOP 1 name, st, geom.STDistance(geometry::STGeomFromText(?, " + SRID + ")) as dist " +
                  "FROM cityinfo_3d " +
                  "ORDER BY dist");
        return sb.toString();
    }

    public String getStreetAddressForReverseGeocoding3D() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT TOP 1 fullname, lfromadd, ltoadd, rfromadd, rtoadd, zipl, zipr, " +
                  "geom.STDistance(geometry::STGeomFromText(?, " + SRID + ")) as d " +
                  "FROM edges_merge_3d " +
                  "WHERE geom.STIntersects(geometry::STGeomFromText(?, " + SRID + ")) = 1 " +
                  "AND geom.STDistance(geometry::STGeomFromText(?, " + SRID + ")) < 0.1 " +
                  "AND roadflg = 'Y' " +
                  "ORDER BY d");
        return sb.toString();
    }
    
    public void performSpatialJoin(Connection conn) throws Exception {
        String sql = "SELECT g.id AS green_id, r.id AS red_id " +
                     "FROM green_table g, red_table r " +
                     "WHERE g.geometry_column.STIntersects(r.geometry_column) = 1";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long startTime = System.currentTimeMillis();
            ResultSet rs = pstmt.executeQuery();
            long endTime = System.currentTimeMillis();
            System.out.println("SQL Server Spatial Join executed in " + (endTime - startTime) + " ms");
            
            while (rs.next()) {
                System.out.println("Green ID: " + rs.getInt("green_id") + ", Red ID: " + rs.getInt("red_id"));
            }
        }
    }
    
    public void findClosestBloodVessels(Connection conn) throws Exception {
        String sql = "SELECT p.id AS purple_id, g.id AS green_id " +
                     "FROM purple_table p, green_table g " +
                     "WHERE g.geometry_column.STDistance(p.geometry_column) = (" +
                     "SELECT MIN(g2.geometry_column.STDistance(p.geometry_column)) " +
                     "FROM green_table g2)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long startTime = System.currentTimeMillis();
            ResultSet rs = pstmt.executeQuery();
            long endTime = System.currentTimeMillis();
            System.out.println("SQL Server Nearest Neighbor Query executed in " + (endTime - startTime) + " ms");
            
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
	public String getSelectLineOverlapsLine3Dvs3D() {
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

}
