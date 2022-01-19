import com.databricks.spark.sql.perf.tpch.TPCH
import com.databricks.spark.sql.perf.Query
import com.databricks.spark.sql.perf.ExecutionMode.CollectResults
import com.databricks.spark.sql.perf.Benchmark
import org.apache.commons.io.IOUtils



// Note: Declare "sqlContext" for Spark 2.x version
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
 
val tpch = new TPCH (sqlContext = sqlContext)
// Set:
val databaseName = "tpch" // name of database with TPCDS data.
sql(s"use $databaseName")
val resultLocation = "/tmp/tpch_results" // place to write results
val iterations = 1 // how many iterations of queries to run.

  
val queries = (1 to 22).map { q =>
  val queryContent: String = IOUtils.toString(
    getClass().getClassLoader().getResourceAsStream(s"tpch/queries/$q.sql"))
  new Query(s"Q$q", spark.sqlContext.sql(queryContent), description = s"TPCH Query $q",
    executionMode = CollectResults)
}

val timeout = 24*60*60 // timeout, in seconds.
// Run:
val experiment = tpch.runExperiment(  
  queries, 
  iterations = iterations,
  resultLocation = resultLocation,
  forkThread = true)

experiment.waitForFinish(timeout)
experiment.getCurrentResults.createOrReplaceTempView("result")
spark.sql("select substring(name,1,100) as Name, bround((parsingTime+analysisTime+optimizationTime+planningTime+executionTime)/1000.0,1) as Runtime_sec  from result").show(105,false)

//exit
sys.exit(0)
