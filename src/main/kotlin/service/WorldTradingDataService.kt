package main.kotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.fuel.json.responseJson
import main.kotlin.logicinstance.GRWA
import main.kotlin.pojo.MongoSchema.Node
import main.kotlin.pojo.MongoSchema.TimeSerialType
import main.kotlin.pojo.httpRtn.WorldTradingData.StockRealtime
import org.apache.avro.Schema
import org.bson.types.ObjectId
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import java.io.File
import java.lang.Exception
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@Configuration
@Service
class WorldTradingDataService {

    @Value("\${WorldTradingData.api_token}")                         val api_token: String = ""
    @Value("\${WorldTradingData.url_stock_realtime}")                val url_stock_realtime: String = ""
    @Value("\${WorldTradingData.url_stock_intraday}")                val url_stock_intraday: String = ""
    @Value("\${WorldTradingData.url_stock_history}")                 val url_stock_history: String = ""
    @Value("\${WorldTradingData.url_stock_multisingledayhistory}")   val url_stock_multiSingleDayHistory: String = ""
    @Value("\${WorldTradingData.url_fx_realtime}")                   val url_fx_realtime: String = ""
    @Value("\${WorldTradingData.url_fx_history}")                    val url_fx_history: String = ""
    @Value("\${WorldTradingData.url_fx_singledayhistory}")           val url_fx_singleDayHistory: String = ""

    private val logger = KotlinLogging.logger {}

    //@Autowired
    //lateinit var kafkaTemplate: KafkaTemplate<String, GenericRecord>

    val zoneId = ZoneId.systemDefault()
    val zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now())

    fun getRealTimeStock(){
        /*
        {
            "symbol": "0992.HK",
            "name": "Lenovo Group Limited",
            "currency": "HKD",
            "price": "7.01",
            "price_open": "6.91",
            "day_high": "7.07",
            "day_low": "6.86",
            "52_week_high": "7.40",
            "52_week_low": "3.53",
            "day_change": "0.06",
            "change_pct": "0.86",
            "close_yesterday": "6.95",
            "market_cap": "10570450633",
            "volume": "37536869",
            "volume_avg": "63251735",
            "shares": "12014791614",
            "stock_exchange_long": "Hong Kong Stock Exchange",
            "stock_exchange_short": "HKEX",
            "timezone": "LMT",
            "timezone_name": "Asia/Hong_Kong",
            "gmt_offset": "27402",
            "last_trade_time": "2019-04-09 16:08:22"
        }
         */
        val json_ReqBody:String
        val json_map_rtnStr:String

        logger.info("The time is now ${DateTimeFormatter.ISO_LOCAL_TIME.format(LocalDateTime.now())}")

        // TODO: Keep querying the API and put to Kafka Producer
        // TODO: Kafka consumer get the last prediction for open high low close etc
        // TODO: conclude a current trend
        // TODO: adjust the prediction

        // TODO: make desision for long and short

        // TODO: Use a better source of data for real time

        // TODO: Think a way to analysis multiple stock together

        //val fuelRtnMap = FuelController().curlByfuel("get", url_stock_realtime,  json_ReqBody)

        val (request, response, result) = Fuel.get(
                url_stock_realtime,
                listOf("symbol" to "AAPL,MSFT,HSBA.L",
                        "api_token" to api_token))
                .responseObject<StockRealtime>(jacksonObjectMapper())
        logger.debug{"Request: ${request}" }
        logger.debug{"Response: ${response}" }
        logger.debug{"Result: ${result}" }

        // pojo = result.get()
        //logger.debug { jacksonObjectMapper().writeValueAsString(result.get()) }

        /* Quick Start Method
        val param:List<Pair<String, Any?>> = listOf(Pair("api_token", api_token), Pair("symbol", "AAPL,MSFT,HSBA.L"))
        val header_map:Map<String,Any>? = mapOf(
                "api_token" to api_token,
                "symbol" to "AAPL,MSFT,HSBA.L"
        )
        val (request, response, result) = url_stock_realtime.httpGet(param).responseString()
        logger.debug{"Request: ${request}" }
        logger.debug{"Response: ${response}" }
        logger.debug{"Result: ${result}" }
        */

        //TODO: put to Kafka producer, serialize the object
        //kafkaTemplate.send(KakfaConfig.PRODUCER_SrcNodeOpen, nodeOpen)

    }

    fun getRealTimeFx(){
        /*
        https://www.worldtradingdata.com/api/v1/forex?base=USD&sort=newest&api_token=demo
        {
            "symbols_returned": 147
            "base": "USD",
            "data": {
                AED: "3.673000",
                AFN: "74.600000",
                ALL: "110.300000",
                AMD: "490.180000",
                ANG: "1.814900",
                AOA: "314.172000",
                ARS: "39.825000",
                AUD: "1.411800",
                AWG: "1.801250",
                AZN: "1.705000",
                ...
            }
        }
         */
        val (request, response, result) = Fuel.get(
                url_fx_realtime,
                listOf("base" to "USD",
                        "api_token" to api_token))
                .responseObject<StockRealtime>(jacksonObjectMapper())
        logger.debug{"Request: ${request}" }
        logger.debug{"Response: ${response}" }
        logger.debug{"Result: ${result}" }
    }

    fun getIntraDay(){
        /*
        https://www.worldtradingdata.com/api/v1/intraday?symbol=AAPL&range=1&interval=1&api_token=demo
        {
            "symbol": "AAPL",
            "stock_exchange_short": "NASDAQ",
            "timezone_name": "America/New_York",
            "intraday": {
                2018-10-19 15:59:00: {
                    "open": "219.49",
                    "close": "219.23",
                    "high": "219.61",
                    "low": "219.19",
                    "volume": "302415",
                },
                2018-10-19 15:58:00: {
                    "open": "219.62",
                    "close": "219.48",
                    "high": "219.70",
                    "low": "219.48",
                    "volume": "173762",
                },
                ...
            }
        }
         */
        val (request, response, result) = Fuel.get(
                url_stock_intraday,
                listOf("symbol" to "AAPL",
                        "range" to "1",
                        "interval" to "1",
                        "api_token" to api_token))
                .responseString()
        //logger.debug{"Request: ${request}" }
        //logger.debug{"Response: ${response}" }
        //logger.debug{"Result: ${result.get()}" }
        val json = JSONObject(result.get())
        val intraday = json.getJSONObject("intraday")
        //logger.debug{intraday.getJSONObject("2019-03-15 15:59:00")}
        //kafkaTemplate.send(KakfaConfig.PRODUCER_STREAM,intraday.getJSONObject("2019-03-15 15:59:00").toString())
        //TODO: Figure out a way for analyse real time to The history
    }

    fun getHistoryStock(stockcode:String?): JSONObject{
        var symbol = "2888.HK"
        if (!stockcode.isNullOrBlank()) symbol = stockcode

        /*
        https://www.worldtradingdata.com/api/v1/history?symbol=AAPL&sort=newest&api_token=demo
        {
            "name": "AAPL",
            "history": {
                2019-02-28: {
                    "open": "174.32",
                    "close": "173.15",
                    "high": "174.91",
                    "low": "172.92",
                    "volume": "28215416"
                },
                2019-02-26: {
                    "open": "173.71",
                    "close": "174.33",
                    "high": "175.30",
                    "low": "173.17",
                    "volume": "17070211"
                ...
            }
        }
         */
        val (request, response, result) = Fuel.get(
                url_stock_history,
                listOf("symbol" to symbol,
                        "sort" to "oldest",
                        "api_token" to api_token))
                .responseJson()
        //logger.debug{"Request: ${request}" }
        //logger.debug{"Response: ${response}" }
        //logger.debug{"Result: ${result.get().obj()}" }
        val obj = result.get().obj()


        return obj

    }

    fun getMultiSingleDayHistoryStock(){
        /*
        https://www.worldtradingdata.com/api/v1/history_multi_single_day?symbol=AAPL,MSFT&date=2018-01-02&api_token=demo
        {
            "date": "2018-01-02",
            "data": {
                AAPL: {
                    "open": "170.16",
                    "close": "172.26",
                    "high": "172.30",
                    "low": "169.26",
                    "volume": "25555934"
                },
                MSFT: {
                    "open": "86.13",
                    "close": "85.95",
                    "high": "86.31",
                    "low": "85.50",
                    "volume": "22483797"
                }
            }
        }
        */
        val (request, response, result) = Fuel.get(
                url_stock_multiSingleDayHistory,
                listOf("symbol" to "AAPL,MSFT",
                        "date" to "2018-01-02",
                        "api_token" to api_token))
                .responseString()
        logger.debug{"Request: ${request}" }
        logger.debug{"Response: ${response}" }
        logger.debug{"Result: ${result.get()}" }
        //TODO: Find a way to update the publisher to the latest
    }

    fun getHistoryFx(){
        /*
        https://www.worldtradingdata.com/api/v1/forex_history?base=USD&convert_to=GBP&sort=newest&api_token=demo
        {
            "symbol": "USDGBP",
            "history": {
                2019-03-01: "0.757000",
                2019-02-28: "0.753900",
                2019-02-27: "0.750885",
                2019-02-26: "0.754255",
                2019-02-25: "0.761820",
                2019-02-24: "0.765410",
                2019-02-23: "0.766080",
                2019-02-22: "0.766110",
                2019-02-21: "0.766835",
                2019-02-20: "0.766595",
                ...
            }
        }
         */
        val (request, response, result) = Fuel.get(
                url_fx_history,
                listOf("sort" to "newest",
                        "base" to "USD",
                        "convert_to" to "GBP",
                        "api_token" to api_token))
                .responseString()
        logger.debug{"Request: ${request}" }
        logger.debug{"Response: ${response}" }
        logger.debug{"Result: ${result.get()}" }
    }

    fun getSingleDayFx(){
        /*
        https://www.worldtradingdata.com/api/v1/forex_single_day?base=USD&date=2018-08-31&api_token=demo
        {
            "total_returned": 5,
            "total_results": 5,
            "total_pages": 1,
            "limit": 50,
            "page": 1,
            "data": [
                {
                    "symbol": "AAPL",
                    "name": "Apple Inc.",
                    "currency": "USD",
                    "price": "174.97",
                    "stock_exchange_long": "NASDAQ Stock Exchange",
                    "stock_exchange_short": "NASDAQ",
                },
                ...
            ]
        }
         */
        val (request, response, result) = Fuel.get(
                url_fx_singleDayHistory,
                listOf("date" to "2018-08-31",
                        "base" to "USD",
                        "api_token" to api_token))
                .responseString()
        logger.debug{"Request: ${request}" }
        logger.debug{"Response: ${response}" }
        logger.debug{"Result: ${result.get()}" }
    }
}