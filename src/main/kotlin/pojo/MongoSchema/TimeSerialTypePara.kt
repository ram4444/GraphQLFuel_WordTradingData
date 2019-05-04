package main.kotlin.pojo.MongoSchema

import lombok.Getter
import lombok.Setter
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Getter
@Setter
//@Document(collection="TimeSerial")
data class TimeSerialTypePara(
        //@Id
        //val id: ObjectId?,
        //val typeId: ObjectId?,
        val para: String,
        val value: BigDecimal
        /*Category include
        Source
        Derived eg. SMA
        Forecast eg regression, MA forcast
          */
    )