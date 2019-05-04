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
@Document(collection="ParentNode")
data class ParentNode(
        @Id
        val id: ObjectId?,
        val nodeId: ObjectId,
        val parentNodeId: List<ObjectId>
    )