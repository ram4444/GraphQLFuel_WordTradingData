package main.kotlin.pojo.MongoSchema

import lombok.Getter
import lombok.Setter
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Getter
@Setter
@Document(collection="Node")
data class Node(
        @Id
        val id: ObjectId?,
        val pinDate: Date, //Default to start of the date
        val pinTS: Date, //The target date
        val pinOriginTS: Date, //The origin of the estimation date, eg the last date of MA series estimate 3 days afterwards
        val timeSerialId: ObjectId,
        val createdDate: Date,
        val value: BigDecimal
    )