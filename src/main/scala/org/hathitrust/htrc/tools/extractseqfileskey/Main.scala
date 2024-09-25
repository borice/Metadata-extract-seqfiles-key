package org.hathitrust.htrc.tools.extractseqfileskey

import com.gilt.gfc.time.Timer
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.hathitrust.htrc.tools.extractseqfileskey.Helper.logger

import scala.io.Codec
import scala.language.reflectiveCalls

object Main {
  val appName: String = "extract-seqfiles-key"

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args.to(Seq))
    val inputPath = conf.inputPath().toString
    val outputPath = conf.outputPath().toString

    conf.outputPath().mkdirs()

    // set up logging destination
    conf.sparkLog.toOption match {
      case Some(logFile) => System.setProperty("spark.logFile", logFile)
      case None =>
    }
    System.setProperty("logLevel", conf.logLevel().toUpperCase)

    // set up Spark context
    val sparkConf = new SparkConf()
    sparkConf.setAppName(appName)
    sparkConf.setIfMissing("spark.master", "local[*]")

    val spark = SparkSession.builder()
      .config(sparkConf)
      .getOrCreate()

    //    import spark.implicits._

    implicit val sc: SparkContext = spark.sparkContext
    implicit val codec: Codec = Codec.UTF8

    val numPartitions = conf.numPartitions.getOrElse(sc.defaultMinPartitions)

    logger.info("Starting...")

    // record start time
    val t0 = System.nanoTime()

    val inputRDD = sc.sequenceFile[String, String](inputPath, minPartitions = numPartitions)

    inputRDD.keys.saveAsTextFile(outputPath + "/output")

    // record elapsed time and report it
    val t1 = System.nanoTime()
    val elapsed = t1 - t0

    logger.info(f"All done in ${Timer.pretty(elapsed)}")
  }

}
