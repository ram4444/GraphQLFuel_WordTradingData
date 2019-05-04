package main.kotlin.pojo.httpRtn.WorldTradingData

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.Setter
@Getter
@Setter
data class StockRealtimeData(
        val symbol: String,
        val name: String,
        val price: String,
        val currency: String,
        val price_open: String,
        val day_high: String,
        val day_low: String,
        @JsonProperty("52_week_high")
        val high_52week: String,
        @JsonProperty("52_week_low")
        val low_52week: String,
        val day_change: String,
        val change_pct: String,
        val close_yesterday: String,
        val market_cap: String,
        val volume: String,
        val volume_avg: String,
        val shares: String,
        val stock_exchange_long: String,
        val stock_exchange_short: String,
        val timezone: String,
        val timezone_name: String,
        val gmt_offset: String,
        val last_trade_time: String
)

/*https://www.worldtradingdata.com/api/v1/stock?symbol=AAPL,MSFT,HSBA.L&api_token=demo
{
    "symbols_requested": 1,
    "symbols_returned": 1,
    "data": [
        {
            "symbol": "AAPL",
            "name": "Apple Inc.",
            "price": "174.97",
            "currency": "USD",
            "price_open": "174.28",
            "day_high": "175.15",
            "day_low": "172.89",
            "52_week_high": "233.47",
            "52_week_low": "142.00",
            "day_change": "1.82",
            "change_pct": "1.05",
            "close_yesterday": "173.15",
            "market_cap": "825032547355",
            "volume": "25886167",
            "volume_avg": "28294177",
            "shares": "4715280000",
            "stock_exchange_long": "NASDAQ Stock Exchange",
            "stock_exchange_short": "NASDAQ",
            "timezone": "EST",
            "timezone_name": "America/New_York",
            "gmt_offset": "-18000",
            "last_trade_time": "2019-03-01 16:00:01"
        }
    ]
}
 */