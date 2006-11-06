/**
 *    '$RCSfile: DataManager.java,v $'
 *
 *     '$Author: tao $'
 *       '$Date: 2006-11-06 19:57:54 $'
 *   '$Revision: 1.20 $'
 *
 *  For Details: http://kepler.ecoinformatics.org
 *
 * Copyright (c) 2003 The Regents of the University of California.
 * All rights reserved.
 * 
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, provided that the
 * above copyright notice and the following two paragraphs appear in
 * all copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 * FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN
 * IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 * PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY
 * OF CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
 * UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

package org.ecoinformatics.datamanager;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;



import org.ecoinformatics.datamanager.database.ConnectionNotAvailableException;
import org.ecoinformatics.datamanager.database.DatabaseAdapter;
import org.ecoinformatics.datamanager.database.DatabaseConnectionPoolInterface;
import org.ecoinformatics.datamanager.database.DatabaseHandler;
import org.ecoinformatics.datamanager.database.HSQLAdapter;
import org.ecoinformatics.datamanager.database.OracleAdapter;
import org.ecoinformatics.datamanager.database.PostgresAdapter;
import org.ecoinformatics.datamanager.database.TableMonitor;
import org.ecoinformatics.datamanager.download.DownloadHandler;
import org.ecoinformatics.datamanager.download.DataStorageInterface;
import org.ecoinformatics.datamanager.download.EcogridEndPointInterface;
import org.ecoinformatics.datamanager.parser.DataPackage;
import org.ecoinformatics.datamanager.parser.Entity;
import org.ecoinformatics.datamanager.parser.eml.Eml200Parser;


/**
 * 
 * The DataManager class is the controlling class for the library. It exposes
 * the high-level API to the calling application.
 * 
 * There are six use-cases that this library supports:
 * 
 * 1. Parse the metadata to get at its entities and attributes.
 * 2. Download a data table from a remote site, using the URL in the metadata.
 * 3. Load a data table into the database table cache.
 * 4. Query a data table with a SQL select statement.
 * 5. Set an upper limit on the size of the database table cache.
 * 6. Set an expiration policy on a table in the database table cache.
 *
 */
public class DataManager {
  
  
  /*
   * Class fields
   */

  /* Holds the singleton object for this class */
  private static DataManager dataManager = null;
  private static String databaseAdapterName = null;
  private static DatabaseConnectionPoolInterface connectionPool = null;
  
  // Constants
  private static final int MAXIMUM_NUMBER_TO_ACCESS_CONNECTIONPOOL = 10;
  private static final int SLEEP_TIME = 2000;
  
 
  /*
   * Constructors
   */
  
  /*
   * This is singleton class, so constructor is private
   */
  
  private DataManager(DatabaseConnectionPoolInterface connectionPool, String databaseAdapterName)
  {
    DataManager.connectionPool = connectionPool;
    DataManager.databaseAdapterName = databaseAdapterName;
  }
  
  
  /*
   * Class methods
   */

  /**
   * Gets the singleton instance of this class.
   * 
   * @return  the single instance of the DataManager class.
   */
  static public DataManager getInstance(DatabaseConnectionPoolInterface connectionPool, String databaseAdapterName) {
	if (dataManager == null)
	{
		dataManager = new DataManager(connectionPool, databaseAdapterName);
	}
	else if (DataManager.databaseAdapterName != null && !DataManager.databaseAdapterName.equals(databaseAdapterName))
	{
		dataManager = new DataManager(connectionPool, databaseAdapterName);
	}
	
    return dataManager;
  }
  

  /*
   * Gets DBConnection from connection pool. If no connection available, it will
   * sleep and try again. If ceiling times reachs, null will return.
   * 
   */
  private static Connection getConnection()
  {
      Connection connection = null;
      int index = 0;
      while (index <MAXIMUM_NUMBER_TO_ACCESS_CONNECTIONPOOL)
      {
          try
          {
              connection = connectionPool.getConnection();
              break;
          }
          catch (ConnectionNotAvailableException cna)
          {
              try
              {
                 Thread.sleep(SLEEP_TIME);
              }
              catch(Exception e)
              {
                 System.err.println("Error in DataManager.getConnection "+e.getMessage());
              }
          }
          catch (SQLException sql)
          {
              break;
          }
          index++;
      }
      return connection;
  }
  
  
  /**
   * Gets the database connection object. If the dbConnection field hasn't
   * already been initialized, creates a new connection and initializes the
   * field.
   * 
   * @return the Connection object which connects database
   */
  /*public static Connection getConnection() 
        throws ClassNotFoundException, SQLException {
    
     
  }*/
  

  /*
   * Returns checked out connection to connection pool
   */
  private static void returnConnection(Connection connection)
  {
      connectionPool.returnConnection(connection);
  }
  

  /**
   * Get the value of the databaseAdapterName field.
   * 
   * @return  the value of the databaseAdapterName field
   */
  public static String getDatabaseAdapterName() {
    return databaseAdapterName;
  }
  
  
  /**
   * Gets the object of the database connection pool
   *  @retrun the object of dababase connection pool
   */
   public static DatabaseConnectionPoolInterface getDatabaseConnectionPool()
   {
       return connectionPool;
   }


  /*
   * Instance methods
   */
  
  /**
   * Create a database view from one or more entities in an entity list.
   * 
   * @param  ANSISQL    ANSI SQL string to create the view.
   * @param  entityList Array of entities whose table names and attribute
   *         names are used in creating the view.
   * @return a boolean value indicating the success of the create view 
   *         operation. True will be returned if successful, else false
   *         will be returned.
   */
  public boolean createDataView(String ANSISQL, Entity[] entityList) {
    boolean success = true;
    
    return success;
  }
 
  
  /**
   * Downloads all entities in a data package using the calling application's 
   * data storage interface. This allows the calling application to manage its 
   * data store in its own way. This version of the method downloads all 
   * entities in the entity list of the data package. This method implements
   * Use Case #2.
   * 
   * @param  dataPackage the data package containing a list of entities
   * @param  endPointInfo which provides ecogrid endpoint information
   * @param  dataStorageList the destination (data storage) of the downloading
   * @return a boolean value indicating the success of the download operation.
   *         true if successful, else false.
   */
  public boolean downloadData(DataPackage dataPackage, EcogridEndPointInterface endPointInfo,
                              DataStorageInterface[] dataStorageList) {
    boolean success = true;
    Entity[] entities = dataPackage.getEntityList();
    
    for (int i = 0; i < entities.length; i++) {
      Entity entity = entities[i];
      success = success && downloadData(entity, endPointInfo, dataStorageList);
    }
    
    return success;
  }

  
  /**
   * Downloads a single entity using the calling application's data storage
   * interface. This allows the calling application to manage its data store
   * in its own way. This method implements Use Case #2.
   * 
   * @param  the entity whose data is to be downloaded
   * @param  endPointInfo which provides ecogrid endpoint information
   * @param  dataStorageList the destination (data storage) of the downloading
   * @return a boolean value indicating the success of the download operation.
   *         true if successful, else false.
   */
  public boolean downloadData(Entity entity, EcogridEndPointInterface endPointInfo,
                              DataStorageInterface[] dataStorageList) {
    DownloadHandler downloadHandler = entity.getDownloadHandler(endPointInfo);
    boolean success = false;
    
    if (downloadHandler != null) {
      

      try {
        
    	/*downloadHandler.setDataStorageCladdList(dataStorageList);
        Thread loadData = new Thread(downloadHandler);
        loadData.start();
        
        while (!downloadHandler.isCompleted()) {
        }
        
        success = downloadHandler.isSuccess();*/
    	success = downloadHandler.download(dataStorageList);
    	
      } 
      catch (Exception e) {
        System.err.println("Error downloading entity name '" + 
                           entity.getName() + "': " + e.getMessage());
        success = false;
      }
    }
    
    return success;
  }
 
  
  /**
   * Downloads data from an input stream using the calling application's data 
   * storage interface. This allows the calling application to manage its 
   * data store in its own way. The metadata input stream first needs to be
   * parsed and a data package created from it. Then, all entities in the data
   * package are downloaded. This method implements Use Case #2.
   * 
   * @param  metadataInputStream an input stream to the metadata. 
   * @param  endPointInfo which provides ecogrid endpoint information
   * @param  dataStorageList the destination (data storage) of the downloading
   * @return a boolean value indicating the success of the download operation.
   *         true if successful, else false.
   */
  public boolean downloadData(InputStream metadataInputStream, EcogridEndPointInterface endPointInfo,
                              DataStorageInterface[] dataStorageList) 
        throws Exception {
    boolean success = false;
    DataPackage dataPackage = parseMetadata(metadataInputStream);
    
    if (dataPackage != null) {
      success = downloadData(dataPackage, endPointInfo, dataStorageList);
    }
    
    return success;
  }
  
 
  /**
   * Drops all tables in a data package. This is simply a pass-through to the
   * DatabaseHandler class. It's useful in the DataManager class for cleaning
   * up tables after unit testing, but not sure that we'd want to expose this
   * as part of the API.
   * 
   * @param dataPackage  the DataPackage object whose tables are to be dropped
   * @return true if successful, else false
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws Exception
   */
  boolean dropTables(DataPackage dataPackage)
          throws ClassNotFoundException, SQLException, Exception {
    boolean success;
    Connection conn = getConnection();
    if (conn == null)
    {
    	throw new Exception("DBConnection is not available");
    }
    try
    {
	    DatabaseHandler databaseHandler = 
	                                 new DatabaseHandler(conn, databaseAdapterName);
	    
	    success = databaseHandler.dropTables(dataPackage);
    }
    finally
    {
    	returnConnection(conn);
    }
    
    return success;
  }
  

  /**
   * Loads all entities in a data package to the database table cache. This
   * method implements Use Case #3.
   * 
   * @param  dataPackage the data package containing a list of entities whose
   *         data is to be loaded into the database table cache.
   * @param  endPointInfo which provides ecogrid endpoint information
   * @return a boolean value indicating the success of the load-data operation.
   *         true if successful, else false.
   */
  public boolean loadDataToDB(DataPackage dataPackage, EcogridEndPointInterface endPointInfo)
        throws ClassNotFoundException, SQLException, Exception {
    boolean success = true;
    Entity[] entities = dataPackage.getEntityList();
    
    for (int i = 0; i < entities.length; i++) {
      success = success && loadDataToDB(entities[i],endPointInfo);
    }
    
    return success;
  }
  
  
  /**
   * Loads data from a single entity into the database table cache.
   * This method implements Use Case #3.
   * 
   * @param  entity  the entity whose data is to be loaded into the database 
   *                 table cache.
   * @param  endPointInfo which provides ecogrid endpoint information
   * @return a boolean value indicating the success of the load-data operation.
   *         true if successful, else false.
   */
  public boolean loadDataToDB(Entity entity, EcogridEndPointInterface endPointInfo) 
        throws ClassNotFoundException, SQLException, Exception {
    Connection conn = getConnection();
    boolean success = false;
    if (conn == null)
    {
    	throw new Exception("DBConnection is not available");
    }
    try
    {
	    DatabaseHandler databaseHandler = new DatabaseHandler(conn, 
	                                                          databaseAdapterName);
	    
	
	    // First, generate a table for the entity
	    success = databaseHandler.generateTable(entity);
	    
	    // If we have a table, then load the data for the entity.
	    if (success) {
	      success = databaseHandler.loadDataToDB(entity, endPointInfo);
	    }
    }
    finally
    {
    	returnConnection(conn);
    }
    return success;
  }
  
  
  /**
   * Loads all entities in a data package to the database table cache. This
   * version of the method is passed a metadata input stream that needs
   * to be parsed. Then, all entities in the data package are loaded to the
   * database table cache. This method implements Use Case #3.
   * 
   * @param  metadataInputStream the metadata input stream to be parsed into
   *         a DataPackage object.
   * @param  endPointInfo which provides ecogrid endpoint information
   * @return a boolean value indicating the success of the load-data operation.
   *         true if successful, else false.
   */
  public boolean loadDataToDB(InputStream metadataInputStream, EcogridEndPointInterface endPointInfo) 
        throws Exception {
    boolean success = false;
    
    DataPackage dataPackage = parseMetadata(metadataInputStream);
    
    if (dataPackage != null) {
      success = loadDataToDB(dataPackage, endPointInfo);
    }
    
    return success;
  }
  
  
  /**
   * Parses metadata using the appropriate parser object. The return value is
   * a DataPackage object containing the parsed metadata. This method
   * implements Use Case #1.
   * 
   * @param metadataInputStream  an input stream to the metadata to be parsed.
   * @return a DataPackage object containing the parsed metadata
   */
  public DataPackage parseMetadata(InputStream metadataInputStream) 
                                  throws Exception {
    DataPackage dataPackage = null;
    Eml200Parser eml200Parser = new Eml200Parser();
    
    eml200Parser.parse(metadataInputStream);
    dataPackage = eml200Parser.getDataPackage();
    
    return dataPackage;
  }
  
  
  /**
   * Runs a database query on one or more data packages. This method
   * implements Use Case #4.
   * 
   * @param ANSISQL  A string holding the ANSI SQL selection syntax.
   * @param packages The data packages holding the entities to be queried. 
   *                 Metadata about the data types of the attributes being
   *                 queried is contained in these data packages.
   * @return A ResultSet object holding the query results.
   */
  public ResultSet selectData(String ANSISQL, DataPackage[] packages) 
        throws ClassNotFoundException, SQLException, Exception {
    Connection conn = getConnection();
    if (conn == null)
    {
    	throw new Exception("DBConnection is not available");
    }
    DatabaseHandler databaseHandler;
    ResultSet resultSet = null;
    try
    {
      databaseHandler = new DatabaseHandler(conn, databaseAdapterName);
      resultSet = databaseHandler.selectData(ANSISQL, packages);
    }
    finally
    {
    	returnConnection(conn);
    }
    
    return resultSet;
  }
  
 
  /**
  * Runs a database query on one or more metadata input streams. Each of the
  * metadata input streams needs to first be parsed, creating a list of data 
  * packages. The data packages contain entities, and the entities hold metadata
  * about the data types of the attributes being queried. This method 
  * implements Use Case #4.
  * 
  * @param ANSISQL  A string holding the ANSI SQL selection syntax.
  * @param packages An array of input streams that need to be parsed into a 
  *                 list of data packages. The data packages hold the lists of
  *                 entities to be queried. Metadata about the data types of the
  *                 attributes in the select statement is contained in these 
  *                 data packages.
  * @return A ResultSet object holding the query results.
  */
  public ResultSet selectData(String ANSISQL, InputStream[] emlInputStreams) 
        throws Exception {
    DataPackage[] packages = new DataPackage[emlInputStreams.length];
    ResultSet resultSet = null;
    
    for (int i = 0; i < emlInputStreams.length; i++) {
      DataPackage dataPackage = parseMetadata(emlInputStreams[i]);
      packages[i] = dataPackage;
    }
    
    resultSet = selectData(ANSISQL, packages);
    
    return resultSet;
  }
  

  /**
   * Runs a database query on a view. The view must already exist in the
   * database (see createDataView() method).
   * 
   * @param  ANSISQL  A string holding the ANSI SQL selection syntax.
   * @return A ResultSet object holding the query results.
   */
  public ResultSet selectDataFromView(String ANSISQL) {
    ResultSet resultSet = null;
    
    return resultSet;
  }
  
  
  /**
   * Set the String value of the databaseAdapterName field.
   * 
   * This method should probably throw an exception if the value does not
   * match any members of the recognized set of database adapter names.
   * 
   * @param databaseAdapterName
   */
  public void setDatabaseAdapterName(String databaseAdapterName) {
    DataManager.databaseAdapterName = databaseAdapterName;
  }

  
  /**
   * Sets an upper limit on the size of the database table cache. If the limit
   * is about to be exceeded, the TableMonitor will attempt to free up space
   * by deleting old tables from the table cache. This method implements
   * Use Case #5.
   * 
   * @param size The upper limit, in MB, on the size of the database table
   *        cache.
   */
  public void setDatabaseSize(int size) throws SQLException, ClassNotFoundException {
	Connection connection = getConnection();
	if (connection == null)
    {
    	throw new SQLException("DBConnection is not available");
    }
	try
	{
		DatabaseAdapter dbAdapter = getDatabaseAdapterObject(databaseAdapterName);
	    TableMonitor tableMonitor = 
	                            new TableMonitor(connection, dbAdapter);
	    
	    tableMonitor.setDBSize(size);
	}
	finally
	{
		returnConnection(connection);
	}
  }
  
  
  /**
   * Sets the expiration policy on a table in the database table cache. The
   * policy is an enumerated integer value indicating whether this table can
   * be expired from the cache. (The precise meaning of these values is yet to
   * be determined.) This method implements Use Case #6.
   * 
   * @param tableName the name of the table whose expiration policy is being
   *                  set
   * @param policy    an enumerated integer value indicating whether the table
   *                  should be expired from the datbase table cache. (The
   *                  precise meaning of this value is yet to be determined.)
   */
  public void setTableExpirationPolicy(String tableName, int policy) 
        throws SQLException, ClassNotFoundException {
	Connection connection = getConnection();
	if (connection == null)
    {
    	throw new SQLException("DBConnection is not available");
    }
	try
	{
	   DatabaseAdapter dbAdapter = getDatabaseAdapterObject(databaseAdapterName);
       TableMonitor tableMonitor = new TableMonitor(connection, dbAdapter);
    
       tableMonitor.setTableExpirationPolicy(tableName, policy);
	}
	finally
	{
		returnConnection(connection);
	}
  }
  
  
  /** 
   * Constructs and returns a DatabaseAdapter object based on a given database 
   * adapter name.
   * 
   * @param dbAdapterName   Database adapter name, a string. It should match
   *                        one of the constants in the DatabaseAdapter class,
   *                        e.g. DatabaseAdapter.POSTGRES_ADAPTER. If no match
   *                        is made, returns null.
   */
  private DatabaseAdapter getDatabaseAdapterObject(String dbAdapterName)
  {
    if (dbAdapterName.equals(DatabaseAdapter.POSTGRES_ADAPTER)) {
      PostgresAdapter databaseAdapter = new PostgresAdapter();
      return databaseAdapter;
    } 
    else if (dbAdapterName.equals(DatabaseAdapter.HSQL_ADAPTER)) {
      HSQLAdapter databaseAdapter = new HSQLAdapter();
      return databaseAdapter;
    } 
    else if (dbAdapterName.equals(DatabaseAdapter.ORACLE_ADAPTER)) {
      OracleAdapter databaseAdapter = new OracleAdapter();
      return databaseAdapter;
    }
      
    return null;
  }

}
