package org.ekstep.analytics.framework.util

import org.ekstep.analytics.framework.BaseSpec
import org.joda.time.LocalDate
import java.io.File
import org.joda.time.DateTime
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.mutable.ListBuffer
import org.joda.time.format.DateTimeFormat
import org.ekstep.analytics.framework.JobConfig
import org.ekstep.analytics.framework.DtRange
import org.ekstep.analytics.framework.Event
import org.ekstep.analytics.framework.Period
import org.joda.time.DateTimeZone

class TestCommonUtil extends BaseSpec {

    it should "pass test case of all methods in CommonUtil" in {

        //datesBetween
        val from = new LocalDate("2016-01-01");
        val to = new LocalDate("2016-01-04");
        CommonUtil.datesBetween(from, to).toArray should be(Array(new LocalDate("2016-01-01"), new LocalDate("2016-01-02"), new LocalDate("2016-01-03"), new LocalDate("2016-01-04")))

        //deleteDirectory
        val path = "delete-this";
        val dir = new File(path)
        val dirCreated = dir.mkdir;
        dirCreated should be(true);
        val fp = "delete-this/delete-this.txt";
        val f = new File(fp);
        f.createNewFile();
        CommonUtil.deleteDirectory(path)
        dir.isDirectory() should be(false);
        f.isFile() should be(false);

        //deleteFile
        val filePath = "delete-this.txt";
        val file = new File(filePath);
        val created = file.createNewFile();
        created should be(true);
        CommonUtil.deleteFile(filePath)
        file.isFile() should be(false);

        //getAge
        val dateformat = new SimpleDateFormat("dd/MM/yyyy");
        val dob = dateformat.parse("04/07/1990");
        CommonUtil.getAge(dob) should be(25)

        //getDatesBetween
        CommonUtil.getDatesBetween("2016-01-01", Option("2016-01-04")) should be(Array("2016-01-01", "2016-01-02", "2016-01-03", "2016-01-04"))
        CommonUtil.getDatesBetween("2016-01-01", None) should not be null;

        //getEvent
        val line = "{\"eid\":\"OE_START\",\"ts\":\"2016-01-01T12:13:20+05:30\",\"@timestamp\":\"2016-01-02T00:59:22.924Z\",\"ver\":\"1.0\",\"gdata\":{\"id\":\"org.ekstep.aser.lite\",\"ver\":\"5.7\"},\"sid\":\"a6e4b3e2-5c40-4d5c-b2bd-44f1d5c7dd7f\",\"uid\":\"2ac2ebf4-89bb-4d5d-badd-ba402ee70182\",\"did\":\"828bd4d6c37c300473fb2c10c2d28868bb88fee6\",\"edata\":{\"eks\":{\"loc\":null,\"mc\":null,\"mmc\":null,\"pass\":null,\"qid\":null,\"qtype\":null,\"qlevel\":null,\"score\":0,\"maxscore\":0,\"res\":null,\"exres\":null,\"length\":null,\"exlength\":0.0,\"atmpts\":0,\"failedatmpts\":0,\"category\":null,\"current\":null,\"max\":null,\"type\":null,\"extype\":null,\"id\":null,\"gid\":null}}}";
        val event = JSONUtils.deserialize[Event](line);
        val line2 = "{\"eid\":\"OE_START\",\"ts\":\"01-01-2016\",\"@timestamp\":\"2016-01-02\",\"ver\":\"1.0\",\"sid\":\"a6e4b3e2-5c40-4d5c-b2bd-44f1d5c7dd7f\",\"uid\":\"2ac2ebf4-89bb-4d5d-badd-ba402ee70182\",\"did\":\"828bd4d6c37c300473fb2c10c2d28868bb88fee6\",\"edata\":{\"eks\":{\"loc\":null,\"mc\":null,\"mmc\":null,\"pass\":null,\"qid\":null,\"qtype\":null,\"qlevel\":null,\"score\":0,\"maxscore\":0,\"res\":null,\"exres\":null,\"length\":null,\"exlength\":0.0,\"atmpts\":0,\"failedatmpts\":0,\"category\":null,\"current\":null,\"max\":null,\"type\":null,\"extype\":null,\"id\":null,\"gid\":null}}}";
        val event2 = JSONUtils.deserialize[Event](line2);
        val line3 = "{\"eid\":\"OE_START\",\"ts\":\"01-01-2016\",\"@timestamp\":\"2016-01-02T00:59:22+05:30\",\"ver\":\"1.0\",\"sid\":\"a6e4b3e2-5c40-4d5c-b2bd-44f1d5c7dd7f\",\"uid\":\"2ac2ebf4-89bb-4d5d-badd-ba402ee70182\",\"did\":\"828bd4d6c37c300473fb2c10c2d28868bb88fee6\",\"edata\":{\"eks\":{\"loc\":null,\"mc\":null,\"mmc\":null,\"pass\":null,\"qid\":null,\"qtype\":null,\"qlevel\":null,\"score\":0,\"maxscore\":0,\"res\":null,\"exres\":null,\"length\":null,\"exlength\":0.0,\"atmpts\":0,\"failedatmpts\":0,\"category\":null,\"current\":null,\"max\":null,\"type\":null,\"extype\":null,\"id\":null,\"gid\":null}}}";
        val event3 = JSONUtils.deserialize[Event](line3);
        val line4 = "{\"eid\":\"OE_START\",\"ts\":\"01-01-2016\",\"@timestamp\":\"2016-01-02T00:59:22P:ST\",\"ver\":\"1.0\",\"sid\":\"a6e4b3e2-5c40-4d5c-b2bd-44f1d5c7dd7f\",\"uid\":\"2ac2ebf4-89bb-4d5d-badd-ba402ee70182\",\"did\":\"828bd4d6c37c300473fb2c10c2d28868bb88fee6\",\"edata\":{\"eks\":{\"loc\":null,\"mc\":null,\"mmc\":null,\"pass\":null,\"qid\":null,\"qtype\":null,\"qlevel\":null,\"score\":0,\"maxscore\":0,\"res\":null,\"exres\":null,\"length\":null,\"exlength\":0.0,\"atmpts\":0,\"failedatmpts\":0,\"category\":null,\"current\":null,\"max\":null,\"type\":null,\"extype\":null,\"id\":null,\"gid\":null}}}";
        val event4 = JSONUtils.deserialize[Event](line4);

        //getEventDate yyyy-MM-dd'T'HH:mm:ssZZ
        val evDate = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ").parseLocalDate("2016-01-01T12:13:20+05:30").toDate;
        CommonUtil.getEventDate(event) should be(evDate)

        //getEventTs
        CommonUtil.getEventTS(event) should be(1451630600000L)
        CommonUtil.getEventSyncTS(event) should be(1451696362924L)
        CommonUtil.getEventSyncTS(event2) should be(0L)
        CommonUtil.getEventSyncTS(event3) should be(1451676562000L)
        CommonUtil.getEventSyncTS(event4) should be(1451696362000L)
        
        CommonUtil.getEventTS(event2) should be(0)
        
        CommonUtil.getEventDate(event2) should be (null)

        //getGameId
        CommonUtil.getGameId(event) should be("org.ekstep.aser.lite")
        CommonUtil.getGameId(event2) should be(null)

        //getGameVersion
        CommonUtil.getGameVersion(event) should be("5.7")
        CommonUtil.getGameVersion(event2) should be(null)

        //getHourOfDay
        CommonUtil.getHourOfDay(1447154514000L, 1447158114000L) should be(ListBuffer(11, 12))
        CommonUtil.getHourOfDay(1447154514000L, 1447000L) should be(ListBuffer(11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0))

        //getParallelization
        val config = new JobConfig(null, None, None, null, None, None, Option(10), Option("testApp"), Option(false));
        CommonUtil.getParallelization(config) should be(10)
        
        val config2 = new JobConfig(null, None, None, null, None, None, None, Option("testApp"), Option(false));
        CommonUtil.getParallelization(config) should be(10)

        //getParallelization
        val con = Option(Map("search" -> null, "filters" -> null, "sort" -> null, "model" -> null, "modelParams" -> null, "output" -> null, "parallelization" -> "10", "appName" -> "testApp", "deviceMapping" -> null))
        CommonUtil.getParallelization(con) should be(10)

        //getStartDate
        CommonUtil.getStartDate(Option("2016-01-08"), 7) should be(Option("2016-01-01"))
        CommonUtil.getStartDate(None, 0) should be(Option(LocalDate.fromDateFields(new Date).toString()))

        //getTimeDiff
        CommonUtil.getTimeDiff(1451650400000L, 1451650410000L) should be(Option(10d))
        CommonUtil.getTimeDiff(1451650400000L, 1451650410000L) should be(Option(10d))
        
        CommonUtil.getTimeDiff(event, event) should be(Option(0d))
        CommonUtil.getTimeDiff(event, event2) should be(Option(0d))

        //getTimeSpent
        CommonUtil.getTimeSpent("10") should be(Option(10d))
        CommonUtil.getTimeSpent(10d.asInstanceOf[AnyRef]) should be(Option(10d))
        CommonUtil.getTimeSpent(10.asInstanceOf[AnyRef]) should be(Option(10d))
        CommonUtil.getTimeSpent(null) should be(Option(0d))
        CommonUtil.getTimeSpent(true.asInstanceOf[AnyRef]) should be(Option(0d))
        
        CommonUtil.getTimestamp("2016-01-02T00:59:22+P:ST") should be(1451696362000L);
        
        CommonUtil.roundDouble(12.7345, 2) should be (12.73);

        //gzip
        val testPath = "src/test/resources/sample_telemetry.log";
        CommonUtil.gzip(testPath)
        new File("src/test/resources/sample_telemetry.log.gz").isFile() should be(true)
        CommonUtil.deleteFile("src/test/resources/sample_telemetry.log.gz");
        
        a[Exception] should be thrownBy {
            CommonUtil.gzip("src/test/resources/sample_telemetry.txt")            
        }
        
        CommonUtil.getParallelization(None) should be (10);
        
        CommonUtil.getMessageId("ME_TEST", "123", "MONTH", DtRange(1451650400000L, 1451650400000L)) should be ("C19F6BCE41181AD104CA3C706C2F5FD7");
        CommonUtil.getMessageId("ME_TEST", "123", "MONTH", DtRange(1451650400000L, 1451650400000L), "org.ekstep.aser.lite") should be ("08EF6AB8668213851E407CEBCEFDF425");
        
        CommonUtil.getMessageId("ME_TEST", "123", "MONTH", 1451650400000L) should be ("95A1A252B816DAAAAE2A3E986FC91ABB");
    
        val periodM = CommonUtil.getPeriod(1464934330000l, Period.MONTH)
        val periodW = CommonUtil.getPeriod(1464934330000l, Period.WEEK)
        val periodD = CommonUtil.getPeriod(1464934330000l, Period.DAY)
        val periodC = CommonUtil.getPeriod(1464934330000l, Period.CUMULATIVE)
        val periodL7 = CommonUtil.getPeriod(1464934330000l, Period.LAST7)
        val periodL30 = CommonUtil.getPeriod(1464934330000l, Period.LAST30)
        val periodL90 = CommonUtil.getPeriod(1464934330000l, Period.LAST90)
        val p = CommonUtil.getPeriod(1464934330000l, null)
        
        periodM should be (201606)
        periodW should be (20167722)
        periodD should be (20160603)
        periodC should be (0)
        periodL7 should be (7)
        periodL30 should be (30)
        periodL90 should be (90)
        p should be (-1)
        
        CommonUtil.getWeeksBetween(1462268189000l,1464946589000l) should be (4)
        CommonUtil.daysBetween(new LocalDate(1462268189000l,DateTimeZone.UTC),new LocalDate(1464946589000l,DateTimeZone.UTC) ) should be (31)
    }
}