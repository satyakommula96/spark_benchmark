# TPC-H and TPC-DS performance benchmark for Spark with DeltaLake

Based on: [Delta Lake: High-Performance ACID Table Storage over Cloud Object Stores](http://www.vldb.org/pvldb/vol13/p3411-armbrust.pdf)

<div align="center">
	<img width="500" height="300" src="logo/spark_benchmark.png" alt="spark_benchmark">
</div>

## Table of Contents

1. [Clone submodules](#1-clone-submodules)
2. [Prerequisites](#2-prerequisites)
    - [Install Dependencies](#21-install-dependencies)
    - [Download spark](#22-download-spark)
    - [Download sbt launcher](#23-download-sbt-launcher)
3. [Build](#3-build)
    - [spark-sql-perf](#31-spark-sql-perf)
    - [tpcds-kit](#32-tpcds-kit)
    - [tpch-dbgen](#33-tpch-dbgen)
4. [Performance Benchmarking](#4-performance-benchmarking)
    - [TPCH](#41-tpch)
        - [Parquet](#411-parquet)
        - [ORC](#412-orc)
        - [CSV](#413-csv)
        - [JSON](#414-json)
    - [TPCDS](#42-tpcds)
        - [Parquet](#421-parquet)
        - [ORC](#422-orc)
        - [CSV](#423-csv)
        - [JSON](#424-json)
5. [Reports](#5-reports)
    - [TPCH Report](#51-tpch-reports)
    - [TPCDS Report](#52-tpcds-reports)

## 1. Clone submodules

```bash
git submodule init
git submodule update
```

## 2. Prerequisites

### 2.1 Install Dependencies

```bash
sudo apt-get install unzip zip gcc make flex bison byacc git build-essential -y
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 8.0.302-open
sdk install sbt 0.13.18
```

### 2.2 Download spark

```bash
wget https://archive.apache.org/dist/spark/spark-3.2.0/spark-3.2.0-bin-hadoop3.2.tgz
mkdir spark3
tar -xzf spark-3.2.0-bin-hadoop3.2.tgz --strip 1 -C spark3
```

**NOTE:** Deploy spark in standalone cluster mode.

### 2.3 Download sbt launcher

```bash
wget -P /tmp https://github.com/sbt/sbt/releases/download/v0.13.18/sbt-0.13.18.tgz
tar -xf /tmp/sbt-0.13.18.tgz  -C /tmp
```

## 3. Build

### 3.1 spark-sql-perf

```bash
cd spark-sql-perf
cp /tmp/sbt/bin/sbt-launch.jar build/sbt-launch-0.13.18.jar
bin/run
sbt +package
```

### 3.2 tpcds-kit

```bash
cd ../tpcds-kit/tools
make OS=LINUX
```

### 3.3 tpch-dbgen

```bash
cd ../../tpch-dbgen
git checkout 0469309147b42abac8857fa61b4cf69a6d3128a8 -- bm_utils.c
make
```

**NOTE:** This should be installed on all cluster nodes with the same location and build `tpcds-kit`, `tpch-dbgen`

## 4. Performance Benchmarking

**NOTE:** Change `master-ip` `executor-memory` , `num-executors`,`executor-cores` a/c to your machine specifications in `.sh` files.

### 4.1 TPCH

#### 4.1.1 Parquet

```bash
cd ../tpch
#For generating ~100GB parquet data
./gendata_parquet.sh
# For runing all 22 TPC-H Queries
./runtpch_parquet.sh
```

#### 4.1.2 ORC

```bash
cd ../tpch
#For generating ~100GB orc data
./gendata_orc.sh
# For runing all 22 TPC-H Queries
./runtpch_orc.sh
```

#### 4.1.3 CSV

```bash
cd ../tpch
#For generating ~100GB csv data
./gendata_csv.sh
# For runing all 22 TPC-H Queries
./runtpch_csv.sh
```

#### 4.1.4 JSON

```bash
cd ../tpch
#For generating ~100GB csv data
./gendata_json.sh
# For runing all 22 TPC-H Queries
./runtpch_son.sh
```

### 4.2 TPCDS

#### 4.2.1 Parquet

```bash
cd ../tpcds
#For generating ~100GB parquet data
./gendata_parquet.sh
# For runing all 99 TPC-DS Queries
./runtpch_parquet.sh
```

#### 4.2.2 ORC

```bash
cd ../tpcds
#For generating ~100GB orc data
./gendata_orc.sh
# For runing all 99 TPC-DS Queries
./runtpch_orc.sh
```

#### 4.2.3 CSV

```bash
cd ../tpcds
#For generating ~100GB csv data
./gendata_csv.sh
# For runing all 99 TPC-DS Queries
./runtpch_csv.sh
```

#### 4.2.4 JSON

```bash
cd ../tpcds
#For generating ~100GB csv data
./gendata_json.sh
# For runing all 99 TPC-DS Queries
./runtpch_json.sh

## 5. Reports

### 5.1 TPCH reports

```bash
cd tpch/tpch_<parquet,orc,csv>_reports
# result will be present in part*.csv file
```

### 5.2 TPCDS reports

```bash
cd tpcds/tpcds_<parquet,orc,csv,json>_reports
# result will be present in part*.csv file
```
