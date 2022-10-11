package gallia.parquet

import gallia._
import aptus.{Path => _, _}

// ===========================================================================
object ParquetGalliaOut {  
  
  def writeHeadViaAvro (head: HeadS)(path: ParquetFilePath): Unit = new run.GenericRunnerZ(writeParquetViaAvro(_)(path)).run(head)
  
  @deprecated("t220222112205 - not ready yet")
  def writeHeadDirectly(head: HeadS)(path: ParquetFilePath): Unit = new run.GenericRunnerZ(writeParquetDirectly(_)(path)).run(head)

  // ===========================================================================
  def writeParquetViaAvro(value: AObj)(path: ParquetFilePath): Unit = writeParquetViaAvro(value.inAObjs)(path)

  // ---------------------------------------------------------------------------
  def writeParquetViaAvro(value: AObjs)(path: ParquetFilePath): Unit = {
    val avroSchema = avro.schema.GalliaToAvroSchema(value.c)
//avroSchema.getField("value").schema() // {"type":"bytes","logicalType":"decimal","precision":38,"scale":18}
//avroSchema.getField("value").schema().getLogicalType.p__ // FIXME?
////avroSchema.getField("value").schema().setLogicalType(null)

    origin.ParquetOut.writeParquetViaAvro(path)(avroSchema) { writer => import avro._
      value.z
        .consumeSelfClosing
        .map(gallia.avro.data.GalliaToAvroData.convertRecursively(value.c, avroSchema))
        .foreach(writer.write) } }      
  
  // ===========================================================================
  @deprecated("t220222112205 - not ready yet")
  def writeParquetDirectly(value: AObj)(path: ParquetFilePath): Unit = writeParquetDirectly(value.inAObjs)(path)

  // ---------------------------------------------------------------------------
  @deprecated("t220222112205 - not ready yet")
  def writeParquetDirectly(value: AObjs)(path: ParquetFilePath): Unit =
    value.c
      .pipe(schema.GalliaToParquetSchema.convertRecursively)
      .pipe { parquetSchema =>         
        origin.ParquetOut.writeParquetDirectly(path)(parquetSchema) { writer =>              
          value.z
            .consumeSelfClosing
            .foreach(writer.write) } }  
  
}

// ===========================================================================
