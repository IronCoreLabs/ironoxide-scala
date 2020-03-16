package ironoxide.v1.beta

import com.ironcorelabs.{sdk => jsdk}

/**
 * This is a technique that allows you to hide the terms that have been indexed. This particular implementation uses tri-grams, which
 * are salted and hashed, to produce the list of tokens.
 * The BlindIndexSearch gives the ability to generate queries as well as create the search entries to store.
 */
case class BlindIndexSearch(private val javaBlindIndexSearch: jsdk.BlindIndexSearch) {

  /**
   * Generate the list of tokens to use to find entries that match the search query, given the specified partitionId.
   *
   * query - The string you want to tokenize and hash
   * partitionId - An extra string you want to include in every hash, this allows 2 queries with different partitionIds to produce a different set of tokens for the same query
   */
  def tokenizeQuery(query: String, partitionId: Option[String]): Set[Int] =
    javaBlindIndexSearch.tokenizeQuery(query, partitionId.orNull).toSet

  /**
   * Generate the list of tokens to create a search entry for `data`. This function will also return some random values in the Set, which will make
   * it harder for someone to know what the input was. Because of this, calling this function will not be the same as `tokenizeQuery`, but `tokenizeQuery` will always
   * return a subset of the values returned by `tokenizeData`.
   *
   * data - The string you want to tokenize and hash
   * partitionId - An extra string you want to include in every hash, this allows 2 queries with different partitionIds to produce a different set of tokens for the same data
   */
  def tokenizeData(query: String, partitionId: Option[String]): Set[Int] =
    javaBlindIndexSearch.tokenizeData(query, partitionId.orNull).toSet
}
