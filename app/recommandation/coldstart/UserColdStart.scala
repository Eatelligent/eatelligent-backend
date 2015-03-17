package recommandation.coldstart

import repository.services.{TagsRec, CBRRec}

trait UserColdStart {

  def findTheUseryouWant(thisuser: UserColdStartModel, allusers: Seq[UserColdStartModel]): UserMatch

  def findTheRecipesYouWant(thisuser: Long, N: Int,
                            maxRecipesFromOneUser: Int, minThreshold: Double): Seq[CBRRec]

  def findRecipesBasedOnTags(userId: Long, maxRecipes: Int): Seq[TagsRec]

}
