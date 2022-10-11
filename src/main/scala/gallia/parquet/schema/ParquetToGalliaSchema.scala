package gallia
package parquet
package schema

import scala.collection.JavaConverters._
import org.apache.parquet.schema.Type

// ===========================================================================
object ParquetToGalliaSchema {
  
  @deprecated("t220222112205 - not ready yet")
  def directly(schema: ParquetSchema): gallia.Cls =
    ???
    //    schema
    //      .pipe(intermediate.IntermediateParser.apply)
    //      .pipe(GalliaParquetSchema.convertSchemaRecursively)

  // ---------------------------------------------------------------------------
  def directly(path: ParquetFilePath): gallia.Cls =
    path
      .pipe(readSchemaFromParquetFile)
      .pipe(directly)

  // ---------------------------------------------------------------------------
  def directly(path: HadoopPath): gallia.Cls =
    path
      .pipe(readSchemaFromParquetFile)
      .pipe(directly)
      
  // ===========================================================================
  def viaAvro(schema: ParquetSchema): gallia.Cls =
    schema
      .pipe(parquetToAvro)
      .pipe(gallia.avro.schema.AvroToGalliaSchema.convertRecursively)

  // ---------------------------------------------------------------------------
  def viaAvro(path: ParquetFilePath): gallia.Cls =
    path
      .pipe(readSchemaFromParquetFile)
      .pipe(viaAvro)
      
  // ---------------------------------------------------------------------------
  def viaAvro(path: HadoopPath): gallia.Cls =
    path
      .pipe(readSchemaFromParquetFile)
      .pipe(viaAvro)      

}

// ===========================================================================
