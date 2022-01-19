rm -rf metastore_ddb
rm derby.log

../spark-3.2.0-bin-hadoop3.2/bin/spark-shell \
	--jars ../spark-sql-perf/target/scala-2.12/spark-sql-perf_2.12-0.5.1-SNAPSHOT.jar \
      	--master spark://127.0.1.1:7077 \
       	--deploy-mode client \
	--executor-memory 8G \
	--num-executors 4 \
	--executor-cores 2 \
	-i ./GenTPCHData.scala
