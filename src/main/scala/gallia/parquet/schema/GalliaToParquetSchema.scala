package gallia
package parquet
package schema

import org.apache.parquet.schema.Types

// ===========================================================================
object GalliaToParquetSchema {

  @deprecated("t220222112205 - not ready yet")
  def convertRecursively(c: Cls)                         : ParquetSchema = convertRecursively(c, None)

  @deprecated("t220222112205 - not ready yet")
  def convertRecursively(c: Cls, nameOpt: Option[String]): ParquetSchema =
      Types
        .buildMessage()
        .pipe(addFields(c))
        .named(nameOpt.getOrElse(DefaultName))

    // ===========================================================================      
    private[schema] def addFields[T](c: Cls)(fieldAssembler: Types.GroupBuilder[T]): Types.GroupBuilder[T] = // recursive
      c .fields
        .foldLeft(fieldAssembler) {
          ???//Baz.buildField(_)(_)
        }
}

// ===========================================================================
