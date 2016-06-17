package org.ekstep.analytics.job

import org.ekstep.analytics.framework.JobDriver
import org.ekstep.analytics.framework.MeasuredEvent
import org.ekstep.analytics.updater.LearnerContentActivitySummary
import org.apache.spark.SparkContext
import org.ekstep.analytics.framework.util.JobLogger
import org.apache.log4j.Logger
import org.ekstep.analytics.framework.IJob

object LearnerContentActivityUpdater extends optional.Application with IJob{

    val className = "org.ekstep.analytics.job.LearnerContentActivityUpdater"

    def main(config: String)(implicit sc: Option[SparkContext] = None) {
        JobLogger.debug("Started executing Job", className)
        implicit val sparkContext: SparkContext = sc.getOrElse(null);
        //JobDriver.run[MeasuredEvent]("batch", config, LearnerContentActivitySummary);
        JobLogger.debug("Job completed.", className)
    }
}