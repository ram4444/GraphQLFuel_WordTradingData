package main.kotlin.service

import main.kotlin.logicinstance.GRWA
import main.kotlin.pojo.MongoSchema.Node
import main.kotlin.pojo.MongoSchema.TimeSerialType
import main.kotlin.pojo.httpRtn.Chart.CandleChartCollection
import main.kotlin.pojo.httpRtn.Chart.LineChartCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.json.JSONObject
import org.springframework.context.annotation.Configuration
import java.lang.Exception
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Configuration
@Service
class GRWAFrontendService {
    private val logger = KotlinLogging.logger {}

    @Autowired
    val adhocInstanceGRWA: GRWA = GRWA()

    @Autowired
    val worldTradingDataService: WorldTradingDataService = WorldTradingDataService()

    val zoneId = ZoneId.systemDefault()
    val zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now())

    fun runAnalysis(stockCode: String): Int {

        val obj = worldTradingDataService.getHistoryStock(stockCode)
        val history = obj.getJSONObject("history")
        var open    = BigDecimal(0)
        var close   = BigDecimal(0)
        var high    = BigDecimal(0)
        var low     = BigDecimal(0)
        var volume  = BigDecimal(0)

        val stockname = obj.getString("name")
        //logger.debug { "history.length: ${history.length()}" }

        var nodeloopOpen: Node?
        var nodeloopClose: Node?
        var nodeloopHigh: Node?
        var nodeloopLow: Node?
        var nodeloopVolume: Node?
        var nodeloopPinDate: Date?
        var nodeloopPinTSOpen: Date?
        var nodeloopPinTSClose: Date?
        var nodeloopPinOriginTS: Date?
        //var nodeloopPinTSHigh:Date?
        //var nodeloopPinTSLow:Date?

        var pindate = LocalDate.parse("1980-01-01")

        while (pindate.isBefore(LocalDate.now()) ) {
            try {
                var dayObj: JSONObject = JSONObject()
                try {
                    dayObj = history.getJSONObject(pindate.toString())
                } catch (e: Exception) {
                    logger.info { "-------------------${pindate} have no record-------------------" }
                    //throw e.fillInStackTrace()
                }
                open = dayObj.getString("open").toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP)
                close = dayObj.getString("close").toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP)
                high = dayObj.getString("high").toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP)
                low = dayObj.getString("low").toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP)
                volume = dayObj.getString("volume").toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP)
                /*
                logger.debug { "Record for ${pindate} is found for $stockname @ ${pindate}" }
                logger.debug { "Open: ${open}" }
                logger.debug { "Close: ${close}" }
                logger.debug { "High: ${high}" }
                logger.debug { "Low: ${low}" }
                logger.debug { "Volume: ${volume}" }\
                */

                //TODO: handle the stockname
                val timeSerialTypeOpen: TimeSerialType = TimeSerialType(ObjectId.get(), "src_open_${stockname}", "source", null)
                val timeSerialTypeClose: TimeSerialType = TimeSerialType(ObjectId.get(), "src_close_${stockname}", "source", null)
                val timeSerialTypeHigh: TimeSerialType = TimeSerialType(ObjectId.get(), "src_high_${stockname}", "source", null)
                val timeSerialTypeLow: TimeSerialType = TimeSerialType(ObjectId.get(), "src_low_${stockname}", "source", null)
                val timeSerialTypeVolume: TimeSerialType = TimeSerialType(ObjectId.get(), "src_volume_${stockname}", "source", null)
                //TODO: insert to db, get back the ID
                //TODO: Massage to Schema Format
                nodeloopPinDate = Date.from(pindate.atStartOfDay(zoneId).toInstant())
                nodeloopPinTSOpen = Date.from(pindate.atTime(9, 0).toInstant(zoneOffset))
                nodeloopPinTSClose = Date.from(pindate.atTime(16, 0).toInstant(zoneOffset))
                //nodeloopPinTSHigh = Date.from(pindate.atTime(16,0).toInstant(zoneOffset))
                //nodeloopPinTSLow = Date.from(pindate.atTime(16,0).toInstant(zoneOffset))
                nodeloopOpen = Node(ObjectId.get(), nodeloopPinDate, nodeloopPinTSOpen, nodeloopPinTSOpen, timeSerialTypeOpen.id, Date(), open)
                nodeloopClose = Node(ObjectId.get(), nodeloopPinDate, nodeloopPinTSClose, nodeloopPinTSClose, timeSerialTypeClose.id, Date(), close)
                nodeloopHigh = Node(ObjectId.get(), nodeloopPinDate, nodeloopPinTSClose, nodeloopPinTSClose, timeSerialTypeHigh.id, Date(), high)
                nodeloopLow = Node(ObjectId.get(), nodeloopPinDate, nodeloopPinTSClose, nodeloopPinTSClose, timeSerialTypeLow.id, Date(), low)
                nodeloopVolume = Node(ObjectId.get(), nodeloopPinDate, nodeloopPinTSClose, nodeloopPinTSClose, timeSerialTypeVolume.id, Date(), volume)

                adhocInstanceGRWA.loopAnalyseAndStore(nodeloopOpen, nodeloopClose, nodeloopHigh, nodeloopLow, nodeloopVolume)


            } catch (e: Exception) {
                logger.debug { "Error when executing ${pindate} of Stock $stockname: " }
                logger.error { "${e.message}" }
                //e.printStackTrace()
            } finally {
                pindate = pindate.plusDays(1)
            }
        }

        return 1
    }

    fun getLineChart(): LineChartCollection {
        return adhocInstanceGRWA.getLineChartsCollection()
    }

    fun getCandleChart(): CandleChartCollection {
        return adhocInstanceGRWA.getCandleChartsCollection()
    }



}