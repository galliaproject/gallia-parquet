package gallia

// ===========================================================================
package object parquet {
  type Closeabled[T] = aptus.Closeabled[T]
  val  Closeabled    = aptus.Closeabled  
  
  // ---------------------------------------------------------------------------
  type     Cls = meta.Cls
  lazy val Cls = meta.Cls

  type     Fld = meta.Fld
  lazy val Fld = meta.Fld

  type     SubInfo = meta.SubInfo
  lazy val SubInfo = meta.SubInfo

  type     ValueType = meta.ValueType
  lazy val ValueType = meta.ValueType
  
  type     Container = reflect.Container
  lazy val Container = reflect.Container
    
  type     BasicType = meta.basic.BasicType
  lazy val BasicType = meta.basic.BasicType
  
  // ===========================================================================
  implicit class ParquetPath(path: String) {
    // favor avro's way for now (direct isn't ready - see t220222112205)
    def readParquet(): HeadO = readParquetViaAvro()
    
      @deprecated("t220222112205 - not ready yet")
      def readParquetDirectly(): HeadO = ParquetGalliaIn.readToHeadDirectly(path)    
      def readParquetViaAvro (): HeadO = ParquetGalliaIn.readToHeadViaAvro (path)     
      
    // ---------------------------------------------------------------------------
    // favor avro's way for now (direct isn't ready - see t220222112205)
    def streamParquet(): HeadS = streamParquetViaAvro()
      
      @deprecated("t220222112205 - not ready yet")
      def streamParquetDirectly(): HeadS = ParquetGalliaIn.streamToHeadDirectly(path)    
      def streamParquetViaAvro (): HeadS = ParquetGalliaIn.streamToHeadViaAvro (path)
  }
  
  // ===========================================================================
  implicit class ParquetHeadO(head: HeadO) {    
      // favor avro's way for now (direct isn't ready - see t220222112205)
      def writeParquet(path: ParquetFilePath): Unit  = writeParquetViaAvro(path)
      
        @deprecated("t220222112205 - not ready yet")
        def writeParquetDirectly(path: ParquetFilePath): Unit  = ParquetGalliaOut.writeHeadDirectly(head.convertToMultiple)(path)    
        def writeParquetViaAvro (path: ParquetFilePath): Unit  = ParquetGalliaOut.writeHeadViaAvro (head.convertToMultiple)(path)      
    }
    
    // ---------------------------------------------------------------------------
    implicit class ParquetHeadS(head: HeadS) {    
      // favor avro's way for now (direct isn't ready - see t220222112205)
      def writeParquet(path: ParquetFilePath): Unit  = writeParquetViaAvro(path)
      
        @deprecated("t220222112205 - not ready yet")
        def writeParquetDirectly(path: ParquetFilePath): Unit  = ParquetGalliaOut.writeHeadDirectly(head)(path)    
        def writeParquetViaAvro (path: ParquetFilePath): Unit  = ParquetGalliaOut.writeHeadViaAvro (head)(path)      
    }

  // ===========================================================================
  type ParquetFilePath = aptus.FilePath // typically .parq extension, contains meta + data (binary)
    
  // ---------------------------------------------------------------------------
  type ParquetSchema = org.apache.parquet.schema.MessageType
  type ParquetRecord = gallia.Obj

  // ---------------------------------------------------------------------------
  type AvroSchema = gallia.avro.AvroSchema
  type AvroRecord = gallia.avro.AvroRecord

  // ---------------------------------------------------------------------------  
  type HadoopPath          = org.apache.hadoop.fs.Path
  type HadoopConfiguration = org.apache.hadoop.conf.Configuration

  // ===========================================================================
  private[parquet] val list    = "list"    // from parquet spec
  private[parquet] val element = "element" // from parquet spec
  
  // ===========================================================================
  // TODO: provide hook to the configured conversions  
  def parquetToAvro(value: ParquetSchema): AvroSchema    = new org.apache.parquet.avro.AvroSchemaConverter().convert(value)
  def avroToParquet(value: AvroSchema)   : ParquetSchema = new org.apache.parquet.avro.AvroSchemaConverter().convert(value)  
}

// ===========================================================================