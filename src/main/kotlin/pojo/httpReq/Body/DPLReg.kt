/* This is a sample class for FUEL http req
 */
package main.kotlin.pojo.httpReq.Body

import lombok.Getter
import lombok.Setter
@Getter
@Setter
data class DPLReg(
        val param: Map<String, String?>,
        val signature: String
    )