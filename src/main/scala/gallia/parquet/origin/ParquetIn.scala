package gallia
package parquet
package origin

import org.apache.parquet.hadoop.ParquetReader
import aptus.CloseabledIterator

// ===========================================================================
object ParquetIn {

  def readParquetPairViaAvro(path: ParquetFilePath): (AvroSchema, CloseabledIterator[AvroRecord]) = {
      val parquetAvroSchema = schema.readSchemaFromParquetFile(path).pipe(parquetToAvro)
      val parquetAvroData   = avroRecords(new HadoopPath(path))
  
      (parquetAvroSchema, parquetAvroData)
    }  

  def readParquetSchemaViaAvro(path: ParquetFilePath): AvroSchema = {
      val parquetAvroSchema = schema.readSchemaFromParquetFile(path).pipe(parquetToAvro)


      parquetAvroSchema
    }

  def readParquetDataViaAvro(path: ParquetFilePath): CloseabledIterator[AvroRecord] = {
      //val parquetAvroSchema = schema.readSchemaFromParquetFile(path).pipe(parquetToAvro)
      val parquetAvroData   = avroRecords(new HadoopPath(path))

      parquetAvroData
    }

    // ---------------------------------------------------------------------------
    private def avroRecords(path: HadoopPath): CloseabledIterator[AvroRecord] = {
        val reader = this.avroReader(path)

        val iter: Iterator[AvroRecord ] =
          Iterator
            .continually(reader.read)
            .takeWhile(_ != null)
  
        CloseabledIterator.fromPair(iter, reader)
      }
      
      // ---------------------------------------------------------------------------
      private def avroReader(path: HadoopPath): ParquetReader[AvroRecord ] =
        org.apache.parquet.avro
          .AvroParquetReader
          .builder[AvroRecord ](path)
        //.withDataModel(...) -- looks like default handles logical type conversions already
          .build()
          .asInstanceOf[ParquetReader[AvroRecord]]

  // ===========================================================================
  def readParquetPairDirectly(path: ParquetFilePath): (ParquetSchema, CloseabledIterator[ParquetRecord]) = {
    val parquetSchema = schema.readSchemaFromParquetFile(path)      
    val parquetData   = parquet.data.ParquetToGalliaData.parquetRecords(new HadoopPath(path))

    (parquetSchema, parquetData)
  }  
  
}

// ===========================================================================
