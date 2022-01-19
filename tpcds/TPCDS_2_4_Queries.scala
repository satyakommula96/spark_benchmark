import com.databricks.spark.sql.perf.tpcds.TPCDS
 
// Note: Declare "sqlContext" for Spark 2.x version
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
 
val tpcds = new TPCDS (sqlContext = sqlContext)

// Set:
val databaseName = "tpcds" // name of database with TPCDS data.
sql(s"use $databaseName")
val resultLocation = "/tmp/tpcds_results" // place to write results
val iterations = 1 // how many iterations of queries to run.
val queries = tpcds.tpcds2_4Queries // queries to run.
val timeout = 24*60*60 // timeout, in seconds.

// Run:
val experiment = tpcds.runExperiment(
  queries, 
  iterations = iterations,
  resultLocation = resultLocation,
  forkThread = true)

experiment.waitForFinish(timeout)
experiment.getCurrentResults.createOrReplaceTempView("result") 
spark.sql("select substring(name,1,100) as Name, bround((parsingTime+analysisTime+optimizationTime+planningTime+executionTime)/1000.0,1) as Runtime_sec  from result").show(105,false)

