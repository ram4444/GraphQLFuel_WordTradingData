package main.kotlin.pojo.MongoSchema

import lombok.Getter
import lombok.Setter
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Getter
@Setter
@Document(collection="TimeSerialType")
data class TimeSerialType(
        @Id
        val id: ObjectId,
        val type_name: String, //Topic Name of Kafka
        val category: String,
        val paraList: List<TimeSerialTypePara>?
        /*Category include
        Source
        Derived eg. SMA
        Forecast eg regression, MA forcast
          */
    )