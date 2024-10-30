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
 * Initial developer(s): Robert Hodges and Ralph Hannus.
 * Contributor(s): 
 */

package  edu.toronto.cs.jackpine.benchmark.db;

import com.continuent.bristlecone.benchmark.db.SqlDialect;
import com.continuent.bristlecone.benchmark.db.Table;

import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.VisitScenario;

/**
 * 
 * @author sray
 */
public interface SpatialSqlDialect extends SqlDialect
{
  public static  enum SupportedSqlDialect {Informix, Ingres, Mysql, PostgreSQL, SQLServer, Oracle };
	
  public SupportedSqlDialect getSqlDialectType();
  /**
   * Returns the name of the JDBC driver class.
   */
    
 
  public abstract String getSelectAllFeaturesWithinADistanceFromPoint();
  //public abstract String getSelectAllFeaturesWithinADistanceFromPoint(Table t);
  public abstract String getSelectTotalLength();
  public abstract String getSelectTotalArea();
  public abstract String getSelectLongestLine();
  public abstract String getSelectLargestArea();
  public abstract String getSelectDimensionPolygon();
  public abstract String getSelectBufferPolygon(); 
  public abstract String getSelectConvexHullPolygon();
  public abstract String getSelectEnvelopeLine();
  public abstract String getSelectBoundingBoxSearch();
  
  public abstract String[] getSelectLongestLineIntersectsArea();
  public abstract String[] getSelectLineIntersectsLargestArea();
  public abstract String[] getSelectAreaOverlapsLargestArea();
  public abstract String[] getSelectLargestAreaContainsPoint();
  
  public abstract String getSelectAreaOverlapsArea();
  public abstract String getSelectAreaContainsArea();
  public abstract String getSelectAreaWithinArea();
  public abstract String getSelectAreaTouchesArea();
  public abstract String getSelectAreaEqualsArea();
  public abstract String getSelectAreaDisjointArea();
  
  //public String getSelectLongestLine3D();

  public abstract String getSelectLineIntersectsArea();
  public abstract String getSelectLineCrossesArea();
  public abstract String getSelectLineWithinArea();
  public abstract String getSelectLineTouchesArea();
  public abstract String getSelectLineOverlapsArea();
  
  public abstract String getSelectLineOverlapsLine();
  public abstract String getSelectLineCrossesLine();
  
  public abstract String getSelectPointEqualsPoint();
  public abstract String getSelectPointWithinArea();
  public abstract String getSelectPointIntersectsArea();
  public abstract String getSelectPointIntersectsLine();
  
  public abstract String getMaxRowidFromSpatialTableEdgesMerge();
  public abstract String getMaxRowidFromSpatialTableArealmMerge();
  public abstract String getInsertIntoEdgesMerge();
  public abstract String getInsertIntoArealmMerge();
  public abstract String getSpatialWriteCleanupEdgesMerge();
  public abstract String getSpatialWriteCleanupArealmMerge();
  
  public abstract String getCityStateForReverseGeocoding();
  public abstract String getStreetAddressForReverseGeocoding();
  
  public abstract String getGeocodingQuery();
  
  public abstract String getMapSearchSiteSearchQuery(VisitScenario visitScenario);
  public abstract String[] getMapSearchScenarioQueries(VisitScenario visitScenario);
  public abstract String[] getMapBrowseBoundingBoxQueries();
  
  public abstract String[] getLandUseQueries();
  public abstract String[] getEnvHazardQueries();
  
String getSelectBuffer3D();
String getSelectClosestPoint3D();
String getSelectContains3D();
String getSelectConvexHull3D();
String getSelectCrosses3D();
String getSelectDistance3D();
String getSelectReadSpatialLongestLine3D();
String getSelectReadSpatialBufferPolygon3D();
String getSelectShortestLine3D();
String getSelectMaxDistance3D();
String getSelectLength3D();
String getSelectDistanceWithin3D();
String getSelectDWithin3D();
String getSelectFullyWithin3D();
String getSelectIntersects3D();
String getSelectIs3D();
public String getSelectVolumeContainsLine3D();
public String getSelectVolumeContainsRegion3D();
public String getSelectRegionInsideVolume3D();
public String getSelectRegionCrossesVolume3D();
public String getSelectRegionIntersectsVolume3D();
public String getSelectLineIntersectsVolume3D();
public String getSelectLineWithinVolume3D();
public String getSelectLineContainsVolume3D();
public String getSelect3DRegionContainsRegion();
public String getSelect3DRegionEqualsRegion();
public String getSelect3DRegionContainsLine();
public String getSelect3DLineCrossesRegion();
public String getSelect3DLineWithinRegion();
public String getSelect3DPointWithinRegion();
public String getSelect3DLineEqualsLine();
public String getSelect3DLineCrossesLine();
public String getSelect3DPointEqualsPoint();
public String getSelect3DPointIntersectsLine();
public String getSelectLineOverlapsLine3D();
public String getSelectRegionContainsPoint3D();
public String getSelectRegionCoversPoint3D();
public String getSelectRegionCoversLine3D();
public String getSelectRegionCoversRegion3D();
public String getSelectVolumeCoversLine3D();
public String getSelectVolumeCoversRegion3D();
public String getSelectPointCoveredByRegion3D();
public String getSelectRegionCoveredByRegion3D();
public String getSelectLineCoveredByVolume3D();
public String getSelectRegionCoveredByVolume3D();
public String getPoint3DString(double x, double y, double z);
public String getBox3DString(double d, double e, double f, double g, double h, double i);
public String getCityStateForReverseGeocoding3D();
public String getStreetAddressForReverseGeocoding3D();
//String getSelectBuffer3D();
String getSelectLongestLine3D();
}