import com.databricks.spark.sql.perf.tpch.TPCHTables

val sqlContext = new org.apache.spark.sql.SQLContext(sc)
 
val rootDir = "./json" // root directory of location to create data in.
val absolutePath = new java.io.File("../").getCanonicalPath //get absolute path
val dbgen = absolutePath+"/tpch-dbgen"  // absolute location of dbgen
val databaseName = "tpch_json" // name of database to create.
val scaleFactor = "100" // scaleFactor defines the size of the dataset to generate (in GB).
val format = "json" // valid spark format like parquet "parquet".

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

tpch_tables.createExternalTables(rootDir, format, databaseName, overwrite = true, discoverPartitions = true)

tpch_tables.analyzeTables(databaseName, analyzeColumns = true)  

//exit
sys.exit(0)
