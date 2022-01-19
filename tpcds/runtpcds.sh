../spark-3.2.0-bin-hadoop3.2/bin/spark-shell \
       	    --jars /home/datapelago/tpcds/spark-sql-perf/target/scala-2.12/spark-sql-perf_2.12-0.5.1-SNAPSHOT.jar \
	    --packages io.delta:delta-core_2.12:1.1.0 \
	    --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
	    --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog" \
            --master spark://127.0.1.1:7077 \
            --deploy-mode client \
            -i ./TPCDS_2_4_Queries.scala
