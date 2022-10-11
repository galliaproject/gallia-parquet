package gallia
package parquet

// ===========================================================================
package object schema {  
  private[schema] val DefaultName = "Default"

  // ===========================================================================
  def writeSchemaIdlString(schema: ParquetSchema): String = schema.toString() /* seems stable */

  // ===========================================================================
  def readSchemaFromParquetFile(path: ParquetFilePath): ParquetSchema =
      readSchemaFromParquetFile(new HadoopPath(path))

    // ---------------------------------------------------------------------------
    def readSchemaFromParquetFile(path: HadoopPath): ParquetSchema =
      org.apache.parquet.hadoop.ParquetFileReader
        .readFooter(new HadoopConfiguration, path) // TODO: use: ParquetFileReader#open(InputFile, ParquetReadOptions)
        .getFileMetaData
        .getSchema
  
  // ---------------------------------------------------------------------------
  def readSchemaFromIdlString(content: String): ParquetSchema =
    org.apache.parquet.schema.MessageTypeParser
      .parseMessageType(content)

}

// ===========================================================================