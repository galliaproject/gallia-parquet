package gallia
package parquet
package data

import scala.collection.JavaConverters._

import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.hadoop.ParquetWriter
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import org.apache.parquet.hadoop.api.WriteSupport

// ===========================================================================
object GalliaToParquetData {

  def parquetWriter(c: Cls, schema: ParquetSchema, path: ParquetFilePath): ParquetWriter[ParquetRecord] = {
    val extraMetaData = Map[String, String]().asJava
    
    // ---------------------------------------------------------------------------
    val writeSupport: WriteSupport[ParquetRecord] =
      new WriteSupport[ParquetRecord] {   
        private var consumer: RecordConsumer = null

        def init(conf: HadoopConfiguration): WriteSupport.WriteContext =
          new WriteSupport.WriteContext(schema, extraMetaData)        

        // ---------------------------------------------------------------------------
        def prepareForWrite(consumer: RecordConsumer): Unit = {
          this.consumer = consumer }

        // ---------------------------------------------------------------------------
        def write(o: ParquetRecord): Unit = 
          ???//CustomGalliaWriter.writeMessageObject(c, consumer)(o) -- TODO: t220222112205 - not ready yet 
      }

    // ---------------------------------------------------------------------------
    new ParquetWriter(
      new HadoopPath(path),
      writeSupport,
      CompressionCodecName.SNAPPY,
      ParquetWriter.DEFAULT_BLOCK_SIZE,
      ParquetWriter.DEFAULT_PAGE_SIZE)
  }

}

// ===========================================================================
