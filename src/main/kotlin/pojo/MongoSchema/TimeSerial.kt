package main.kotlin.pojo.MongoSchema

import lombok.Getter
import lombok.Setter
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Getter
@Setter
@Document(collection="TimeSerial")
data class TimeSerial(
        @Id
        val id: ObjectId?,
        val symbol: String,
        val market: String,
        val typeId: ObjectId,
        val createdDate: Date
    )