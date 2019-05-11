package main.kotlin.pojo.httpRtn.Chart

import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id

@Getter
@Setter
data class CandleChartData(

        val date: String,
        val open: Float?,
        val close: Float?,
        val high: Float?,
        val low: Float?,
        val volume: Float?,
        val pdopen: Float?,
        val pdclose: Float?,
        val pdhigh: Float?,
        val pdlow: Float?,
        val pdopendisper: Float?,
        val pdcloseisper: Float?,
        val pdhighisper: Float?,
        val pdlowisper: Float?
)
