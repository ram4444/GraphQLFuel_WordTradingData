package main.kotlin.pojo.httpRtn.Chart

import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id
@Getter
@Setter
data class LineChart(

        var name: String,
        var valueList: MutableList<LineChartData>

)
