package recommandation.coldstart

import com.google.inject.Inject
import myUtils.ColdStartConstants
import repository.daos.{ColdStartDAO, UserTagRelationDAO, UserDAO, RatingDAO}
import repository.services.{TagsRec, DummyRec, CBRRec}

class UserColdStartImpl @Inject()(
                                   val ratingDAO: RatingDAO,
                                   val tagRelationDAO: UserTagRelationDAO,
                                   val coldStartDAO: ColdStartDAO,
                                   val userDAO: UserDAO) extends UserColdStart {



  def findRecipesBasedOnTags(userId: Long, maxRecipes: Int): Seq[TagsRec] = {
    val coldStartResponses = coldStartDAO.getColdStartsForUser(userId)

    val skillsResponse = coldStartResponses.find(_.coldStartId == ColdStartConstants.SKILLS).map(_.answer)
    val spicyResponse = coldStartResponses.find(_.coldStartId == ColdStartConstants.SPICY).map(_.answer)

    val recs = tagRelationDAO.retrieveTopRecipesBasedOnTags(maxRecipes, userId)
    .filter(_.score > 0)

    val filteredOnSkill = skillsResponse match {
      case Some(answer) => if (!answer) recs.filter(x => x.difficulty == "Enkel" && x.time < 45) else recs
      case None => recs
    }

    val filteredOnSpicy = spicyResponse match {
      case Some(answer) => if (!answer) recs.filter(_.spicy == 1) else filteredOnSkill
      case None => filteredOnSkill
    }

    filteredOnSpicy.take(maxRecipes).map(x => TagsRec(userId, x.id, "tags", x.score))
  }

  def findTheRecipesYouWant(userId: Long, N: Int,
                            maxRecipesFromOneUser: Int, minThreshold: Double): Seq[CBRRec] = {

    userDAO.getUserColdStarts(userId) match {
      case None => Seq()
      case Some(thisUser) =>
        userDAO.getAllUserColdStarts(100).filter(_.id != userId)
        .map(x => (x.id, sim(thisUser, x))).filter(_._2 > minThreshold).sortBy(_._2)
          .map {
            case (simUserId, similarity) =>
              ratingDAO.getMaxRecipesRates(simUserId, maxRecipesFromOneUser)
              .map{
                 case (recipeId, rating) => CBRRec(userId, recipeId, "cbr", simUserId, similarity, rating)
              }
          }
          .flatten.sortBy(x => x.simToUser * x.simUserRating).take(N)
    }
  }

  // TODO: To be in the allusers list you have to at least have 2 recipes rates (already filtered list)
  def findTheUseryouWant(thisuser: UserColdStartModel, allusers: Seq[UserColdStartModel]): UserMatch = {

    val someuser = allusers.fold(allusers(0)) { (x, acc) => {
         if(thisuser != acc && sim(thisuser, acc) < sim(thisuser, x)) {
           x
         } else {
           acc
         }
      }
    }

    UserMatch(thisuser, someuser, sim(thisuser, someuser))
  }



  def sim(thisUser: UserColdStartModel, otherUser: UserColdStartModel): Double = {
    val diff = thisUser.response.zip(otherUser.response).count {
      case (x, y) => x != y
    }

    if(diff == 0)
      1.0
    else
      1.0 / diff
  }


}


case class UserColdStartModel(
                             id: Long,
                             response: Seq[Boolean]

                               )

case class  UserMatch(
                     thisUser: UserColdStartModel,
                     otheruser: UserColdStartModel,
                     matchNumber: Double
                       )