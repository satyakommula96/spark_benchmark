import com.databricks.spark.sql.perf.tpch.TPCHTables

val sqlContext = new org.apache.spark.sql.SQLContext(sc)
 
val rootDir = "./parquet" // root directory of location to create data in.
val dbgen = "../tpch-dbgen"  // location of dbgen
val databaseName = "tpch" // name of database to create.
val scaleFactor = "100" // scaleFactor defines the size of the dataset to generate (in GB).
val format = "parquet" // valid spark format like parquet "parquet".

val tpch_tables = new TPCHTables(
    sqlContext,
    dbgenDir = dbgen, 
    scaleFactor=scaleFactor,
    useDoubleForDecimal=false, 
    useStringForDate=false,
    generatorParams=Nil)
 
 
tpch_tables.genData(
    location = rootDir,
    format = format,
    overwrite = true, 
    partitionTables = true, 
    clusterByPartitionColumns = true,  
    filterOutNullPartitionValues = false, 
    tableFilter = "", 
    numPartitions = 20) 

sql(s"create database if not exists $databaseName")

tpch_tables.createExternalTables(rootDir, "parquet", databaseName, overwrite = true, discoverPartitions = true)

tpch_tables.analyzeTables(databaseName, analyzeColumns = true)  
