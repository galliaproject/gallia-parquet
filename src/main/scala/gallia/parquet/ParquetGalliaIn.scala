package gallia
package parquet

import aptus.CloseabledIterator

// ===========================================================================
object ParquetGalliaIn {
  @deprecated("t220222112205 - not ready yet")
  def readToHeadDirectly(path: ParquetFilePath): gallia.HeadO = streamToHeadDirectly(path).force.one
  def readToHeadViaAvro (path: ParquetFilePath): gallia.HeadO = streamToHeadViaAvro (path).force.one

  // ---------------------------------------------------------------------------
  def streamToHeadViaAvro(path: ParquetFilePath): gallia.HeadS = {
    val (parquetAvroSchema, galliaSchema) = readGalliaSchemasViaAvro(path)

    actions.in
      .GenericInputZ( // TODO: t220302091219 - move schema ingest in a proper validation test
          galliaSchema,
          readGalliaDataViaAvro(parquetAvroSchema, galliaSchema)(path))
      .pipe(heads.Head.inputZ) }

  // ---------------------------------------------------------------------------
  @deprecated("t220222112205 - not ready yet")
  def streamToHeadDirectly(path: ParquetFilePath): gallia.HeadS = ???
    //    actions.in
    //      .GenericInputZ( // TODO: t220302091219 - move schema ingest in a proper validation test
    //          ???,
    //          readGalliaPairDirectly(path))
    //      .pipe(heads.Head.inputZ)
      
  // ===========================================================================
  @deprecated("t220222112205 - not ready yet") def readAObjsDirectly(path: ParquetFilePath): AObjs = ???//readGalliaPairDirectly(path).pipe { case (schema, data) => AObjs(schema, Objs.from(data)) }
  @deprecated("t220222112205 - not ready yet") def readAObjDirectly (path: ParquetFilePath): AObj  = readAObjsDirectly     (path).forceAObj
  
  // ===========================================================================
  def readAObjParquetViaAvro (path: ParquetFilePath): AObj  = readAObjsParquetViaAvro(path).forceAObj
  def readAObjsParquetViaAvro(path: ParquetFilePath): AObjs = {
      val (parquetAvroSchema, galliaSchema) = readGalliaSchemasViaAvro(path)

      AObjs(
        galliaSchema,
        readGalliaDataViaAvro(parquetAvroSchema, galliaSchema)(path)
          .regenerate()
          .consumeAll
          .pipe(Objs.from)) }

    // ---------------------------------------------------------------------------
    private def readGalliaSchemasViaAvro(path: String): (AvroSchema, Cls) = {
      val parquetAvroSchema = origin.ParquetIn.readParquetSchemaViaAvro(path)
      parquetAvroSchema -> avro.schema.AvroToGalliaSchema.convertRecursively(parquetAvroSchema) }

    // ---------------------------------------------------------------------------
    private def readGalliaDataViaAvro(parquetAvroSchema: AvroSchema, galliaSchema: Cls)(path: String): DataRegenerationClosure[Obj] = {
      new DataRegenerationClosure[Obj] {
        def regenerate: () => aptus.CloseabledIterator[Obj] = () => {
          origin.ParquetIn
            .readParquetDataViaAvro(path)
            .map(avro.data.AvroToGalliaData.convertRecursively(galliaSchema, parquetAvroSchema)) }} }

  // ===========================================================================
  private def readGalliaPairDirectly(path: String): (Cls, CloseabledIterator[Obj]) =
    origin.ParquetIn
      .readParquetPairDirectly(path)
      .mapFirst(parquet.schema.ParquetToGalliaSchema.directly)  

}

// ===========================================================================
