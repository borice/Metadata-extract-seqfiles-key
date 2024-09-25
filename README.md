[![Scala CI](https://github.com/htrc/Metadata-extract-seqfiles-key/actions/workflows/ci.yml/badge.svg)](https://github.com/htrc/Metadata-extract-seqfiles-key/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/htrc/Metadata-extract-seqfiles-key/graph/badge.svg?token=0UyR6cfzcW)](https://codecov.io/gh/htrc/Metadata-extract-seqfiles-key)
[![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/htrc/Metadata-extract-seqfiles-key?include_prereleases&sort=semver)](https://github.com/htrc/Metadata-extract-seqfiles-key/releases/latest)

# Metadata-extract-seqfiles-key
This tool can be used to extract specific keys out of sequence files

# Build
`sbt clean stage` - generates the unpacked, runnable application in the `target/universal/stage/` folder.  
`sbt clean universal:packageBin` - generates an application ZIP file

# Usage
*Note:* Must use one of the supported JVMs for Apache Spark (at this time Java 8 through Java 11 are supported)
```text
extract-seqfiles-key <version>
HathiTrust Research Center
  -l, --log-level  <LEVEL>    (Optional) The application log level; one of INFO,
                              DEBUG, OFF (default = INFO)
  -n, --num-partitions  <N>   (Optional) The number of partitions to split the
                              input set of HT IDs into, for increased
                              parallelism
  -o, --output  <DIR>         Write the output to DIR
      --spark-log  <FILE>     (Optional) Where to write logging output from
                              Spark to
  -h, --help                  Show help message
  -v, --version               Show version of this program

 trailing arguments:
  input (required)   The path to the folder containing the input data
```
