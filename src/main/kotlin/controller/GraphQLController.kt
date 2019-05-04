package main.kotlin.controller

import graphql.schema.DataFetcher
import graphql.schema.StaticDataFetcher
import main.kotlin.graphql.GraphQLHandler
import main.kotlin.graphql.GraphQLRequest
import main.kotlin.service.GRWAFrontendService
import main.kotlin.service.WorldTradingDataService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import javax.annotation.PostConstruct

@RestController
class GraphQLController() {

    @Autowired
    val worldTradingDataService: WorldTradingDataService = WorldTradingDataService()

    @Autowired
    val gRWAFrontendService: GRWAFrontendService = GRWAFrontendService()

    private val logger = KotlinLogging.logger {}

    //Initiate schema from somewhere
    val schema ="""
            type Query{
                query_func1: Int
                query_runAnalysis(stocknum: String!): Int
                query_stockFull: ChartCollection
                query_stockFullLineChart: LineChartCollection
                query_stockFullCandleChart: CandleChartCollection
            }
            type ChartCollection{
                lineCharts: LineChartCollection,
                candleCharts: CandleChartCollection
            }
            type LineChartCollection{
                chart: [LineChart]
            }
            type LineChart{
                name: String
                valueList: [LineChartData]
            }
            type LineChartData{
                pindate: String
                value: String
            }
            type CandleChartCollection{
                chart: [CandleChart]
            }
            type CandleChart{
                name: String
                valueList: [CandleChartData]
            }
            type CandleChartData{
                pindate: String
                open: String
                close: String
                high: String
                low: String
            }"""

    lateinit var fetchers: Map<String, List<Pair<String, DataFetcher<out Any>>>>
    lateinit var handler:GraphQLHandler

    @PostConstruct
    fun init() {

        //initialize Fetchers
        fetchers = mapOf(
                "Query" to
                        listOf(
                                "query_func1" to StaticDataFetcher(999),
                                "query_runAnalysis" to DataFetcher{gRWAFrontendService.runAnalysis(it.getArgument("stocknum"))},
                                "query_stockFullLineChart" to DataFetcher{gRWAFrontendService.getLineChart()},
                                "query_stockFullCandleChart" to DataFetcher{gRWAFrontendService.getCandleChart()},
                                "lineCharts" to DataFetcher{gRWAFrontendService.getLineChart()},
                                "candleCharts" to DataFetcher{gRWAFrontendService.getCandleChart()}
                        )
        )

        handler = GraphQLHandler(schema, fetchers)
    }

    @RequestMapping("/")
    suspend fun pingcheck():String {
        println("ping")
        logger.debug { "Debugging" }
        return "success"
    }

    @CrossOrigin(origins = arrayOf("http://localhost:3000"))
    @PostMapping("/graphql")
    fun executeGraphQL(@RequestBody request:GraphQLRequest):Map<String, Any> {

        val result = handler.execute(request.query, request.params, request.operationName, ctx = null)

        return mapOf("data" to result.getData<Any>())
    }

}
