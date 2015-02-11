package repository.models

import org.joda.time.LocalDateTime

case class UserColdStart(
                            userId: Option[Long],
                            coldStartId: Long,
                            answer: Boolean,
                            answerTime: Option[LocalDateTime]
                            )
