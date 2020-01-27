package ironoxide.v1.document

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.{FailedResult, SucceededResult}

/**
 *  Result of granting or revoking access to a document. Both grant and revoke support partial success.
 *
 * @param changed users and groups whose access was successfully changed
 * @param errors users and groups whose access failed to be modified
 */
case class DocumentAccessResult(
  changed: SucceededResult,
  errors: FailedResult
)

object DocumentAccessResult {
  def apply(dar: jsdk.DocumentAccessResult): DocumentAccessResult =
    DocumentAccessResult(
      SucceededResult(dar.getChanged),
      FailedResult(dar.getErrors)
    )
}
