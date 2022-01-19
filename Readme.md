# TPC-H and TPC-DS performance benchmark for Spark with DeltaLake

###  Clone submodules

```bash
git submodule init
git submodule update
```

## Prerequisites

### Install Dependencies

```bash
sudo apt-get install unzip zip gcc make flex bison byacc git build-essential -y
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 8.0.302-open
sdk install sbt 0.13.18
```

### Download spark

```bash
wget https://archive.apache.org/dist/spark/spark-3.2.0/spark-3.2.0-bin-hadoop3.2.tgz
tar -xzf spark-3.2.0-bin-hadoop3.2.tgz
```
**NOTE:** Deploy spark in standalone cluster mode. 

### Download sbt launcher

```bash
wget -P /tmp https://github.com/sbt/sbt/releases/download/v0.13.18/sbt-0.13.18.tgz
tar -xf /tmp/sbt-0.13.18.tgz  -C /tmp
```


## Build

### spark-sql-perf

```bash
cd spark-sql-perf
cp /tmp/sbt/bin/sbt-launch.jar build/sbt-launch-0.13.18.jar
bin/run
sbt +package
```

### tpcds-kit

```bash
cd ../tpcds-kit/tools
make OS=LINUX
```
### tpch-dbgen

```bash
cd ../../tpch-dbgen
git checkout 0469309147b42abac8857fa61b4cf69a6d3128a8 -- bm_utils.c
make
```
**NOTE:** This should be installed on all cluster nodes with the same location and build `tpcds-kit`, `tpch-dbgen`

## Run Benchmark

**NOTE:** Change `executor-memory` , `num-executors`,`executor-cores` a/c to your machine specifications in `.sh` files.

### TPCH
```bash
cd ../tpch
#For generating ~100GB parquet data
./gendata.sh
# For runing all 22 TPC-H Queries
./runtpch.sh
```

### TPCDS
```bash
cd ../tpcds
#For generating ~100GB parquet data
./gendata.sh
# For runing all 99 TPC-DS Queries
./runtpch.sh
```

