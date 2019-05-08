package main.kotlin.pojo.httpRtn.Chart

import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id

@Getter
@Setter
data class LineChartData(

        val date: String,
        val value: Float?

)
