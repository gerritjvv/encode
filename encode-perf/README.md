## Overview

Runs encryption decryption benchmarks for:

  * FST
  * Kryo
  * Clojure Nippy


To use run:

```bash
./build.sh build
# then
./build.sh run
```


## Results


### 2018 MacbookPro AES-NI 
These were run on my mac 2018 mac.
Please run your own on the servers you expect to deploy on for accurate numbers.

```bash
Benchmark                Mode  Cnt       Score       Error  Units    Size Bytes
KryoBench.decodeRaw      thrpt  200  940994.821 ± 17113.635  ops/s    
KryoBench.encodeRaw      thrpt  200  667163.954 ± 11866.944  ops/s   274

FSTBench.decodeFastRaw   thrpt  200  669369.172 ± 12568.461  ops/s
FSTBench.encodeFastRaw   thrpt  200  775964.368 ± 21509.038  ops/s   621

FSTBench.decodeRaw       thrpt  200  484232.435 ±  9395.248  ops/s
FSTBench.encodeRaw       thrpt  200  716760.613 ± 17428.384  ops/s   318

NippyBench.decodeRaw     thrpt  200   38264.631 ±   868.061  ops/s
NippyBench.encodeRaw     thrpt  200  190582.664 ±  4243.056  ops/s   734
```

Data Size:

```bash
Benchmark                Size Bytes  Cnt       Score       Error  Units
KryoBench.encodeRaw      thrpt  200  667163.954 ± 11866.944  ops/s

FSTBench.encodeFastRaw   thrpt  200  775964.368 ± 21509.038  ops/s

FSTBench.encodeRaw       thrpt  200  716760.613 ± 17428.384  ops/s

NippyBench.encodeRaw     thrpt  200  190582.664 ±  4243.056  ops/s
```