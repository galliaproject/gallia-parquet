package gallia
package parquet
package data

import org.apache.parquet.hadoop.ParquetReader
import aptus.CloseabledIterator

// ===========================================================================
object ParquetToGalliaData {
  
  def parquetRecords(path: HadoopPath): CloseabledIterator[ParquetRecord] = {
      val reader = this.parquetReader(path)

      val iter: Iterator[ParquetRecord ] =
        Iterator
          .continually(reader.read)
          .takeWhile(_ != null)

      CloseabledIterator.fromPair(iter, reader)
    }
    
    // ===========================================================================
    private def parquetReader(path: HadoopPath): ParquetReader[ParquetRecord] =
      ParquetReader
        .builder[ParquetRecord](
            schema.ParquetToGalliaSchema
              .directly(path)
              .pipe(???),//new parsing.GalliaReadSupport(_)), -- not ready (see t220222112205)
            path)
        .build()

}  

// ===========================================================================
