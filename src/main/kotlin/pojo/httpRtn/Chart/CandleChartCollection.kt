package main.kotlin.pojo.httpRtn.Chart

import lombok.Getter
import lombok.Setter
@Getter
@Setter
data class CandleChartCollection(
        var chart: List<CandleChart>
)
