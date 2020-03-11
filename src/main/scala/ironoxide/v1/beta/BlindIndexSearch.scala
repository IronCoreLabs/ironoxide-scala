package ironoxide.v1.beta

import com.ironcorelabs.{sdk => jsdk}

case class BlindIndexSearch(javaBlindIndexSearch: jsdk.BlindIndexSearch) {
  def tokenizeQuery(query: String, partitionId: Option[String]): List[Int] =
    javaBlindIndexSearch.tokenizeQuery(query, partitionId.orNull).toList

  def tokenizeData(query: String, partitionId: Option[String]): List[Int] =
    javaBlindIndexSearch.tokenizeData(query, partitionId.orNull).toList
}
