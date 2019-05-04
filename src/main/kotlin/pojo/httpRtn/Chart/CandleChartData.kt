package main.kotlin.pojo.httpRtn.Chart

import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id
import java.util.*

@Getter
@Setter
data class CandleChartData(

        val pindate: Date,
        val open: Float?,
        val close: Float?,
        val high: Float?,
        val low: Float?

)
