package main.kotlin.pojo.httpRtn.Chart

import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id
import java.util.*

@Getter
@Setter
data class LineChartData(

        val pindate: Date,
        val value: Float?

)
