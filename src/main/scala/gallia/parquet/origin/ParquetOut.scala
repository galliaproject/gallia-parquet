package gallia
package parquet
package origin

import org.apache.parquet.hadoop._

// ===========================================================================
object ParquetOut {
  import metadata.CompressionCodecName.SNAPPY
  import ParquetFileWriter.Mode.OVERWRITE

  // ---------------------------------------------------------------------------
  def writeParquetViaAvro(path: ParquetFilePath)(avroSchema: AvroSchema)(f: ParquetWriter[AvroRecord] => Unit): Unit = {    
    val writer =
      org.apache.parquet.avro
        .AvroParquetWriter
        .builder[AvroRecord](new HadoopPath(path))
          .withSchema          (avroSchema) // for WriteSupport
        //.withDataModel(...) -- looks like default handles logical type conversions already except "local-timestamp-{millis,micros}" (missing in TimeConversions) - t220408111030          
          .withConf            (new HadoopConfiguration)
          .withCompressionCodec(SNAPPY)
          .withWriteMode       (OVERWRITE)
        .build()

      f(writer)

      writer.close()    
  }
  
  // ===========================================================================
  def writeParquetDirectly(path: ParquetFilePath)(parquetSchema: ParquetSchema)(f: ParquetWriter[ParquetRecord] => Unit): Unit = {
    val galliaSchema: Cls = schema.ParquetToGalliaSchema.directly(parquetSchema)
    val writer = data.GalliaToParquetData.parquetWriter(galliaSchema, parquetSchema, path)
    f(writer)
    writer.close()
  }
  
}

// ===========================================================================
