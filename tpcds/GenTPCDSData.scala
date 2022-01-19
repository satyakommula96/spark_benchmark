import com.databricks.spark.sql.perf.tpcds.TPCDSTables
 
// Note: Declare "sqlContext" for Spark 2.x version
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
 
// Set:
// Note: Here my env is using MapRFS, so I changed it to "hdfs:///tpcds".
// Note: If you are using HDFS, the format should be like "hdfs://namenode:9000/tpcds"
val rootDir = "./parquet" // root directory of location to create data in.
 
val databaseName = "tpcds" // name of database to create.
val scaleFactor = "100" // scaleFactor defines the size of the dataset to generate (in GB).
val format = "parquet" // valid spark format like parquet "parquet".
val dsdgen = "../tpcds-kit/tools", // location of dsdgen
// Run:
val tables = new TPCDSTables(sqlContext,
    dsdgenDir = dsdgen,
    scaleFactor = scaleFactor,
    useDoubleForDecimal = false, // true to replace DecimalType with DoubleType
    useStringForDate = false) // true to replace DateType with StringType
 
 
tables.genData(
    location = rootDir,
    format = format,
    overwrite = true, // overwrite the data that is already there
    partitionTables = true, // create the partitioned fact tables 
    clusterByPartitionColumns = true, // shuffle to get partitions coalesced into single files. 
    filterOutNullPartitionValues = false, // true to filter out the partition with NULL key value
    tableFilter = "", // "" means generate all tables
    numPartitions = 20) // how many dsdgen partitions to run - number of input tasks.
 
// Create the specified database
sql(s"create database if not exists $databaseName")
// Create metastore tables in a specified database for your data.
// Once tables are created, the current database will be switched to the specified database.
tables.createExternalTables(rootDir, "parquet", databaseName, overwrite = true, discoverPartitions = true)
// Or, if you want to create temporary tables
// tables.createTemporaryTables(location, format)
 
// For CBO only, gather statistics on all columns:
tables.analyzeTables(databaseName, analyzeColumns = true) 

//exit
sys.exit(0)
