package main.kotlin.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import mu.KotlinLogging
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.web.bind.annotation.*
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*

@RestController
class FuelController() {

    private val logger = KotlinLogging.logger {}

    //TODO: Rewrite curlasyncbyfuel
    @PostMapping("/curlasyncbyfuel")
    fun curlAsyncByfuel(@RequestHeader method: String, @RequestHeader url: String, @RequestBody requestBody: String): Map<String, Any> {

        logger.info { "Curl is called ASYNC by Fuel" }
        logger.debug { "method:" }; logger.debug { method }
        logger.debug { "url:" }; logger.debug { url }
        logger.debug { "body:" }; logger.debug { requestBody }

        var rtn: String = ""

        if (method.toLowerCase().equals("get")) {
            url.httpGet().responseString { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println("exception: " + ex)
                        println("response: " + response)
                        rtn = ex.toString()
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println("data: " + data)
                        println("response: " + response)
                        rtn = data.toString()
                    }
                }
            }
        } else if (method.toLowerCase().equals("post")) {
            url.httpPost().responseString { request, response, result ->
                //do something with response\

                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println("exception: " + ex)
                        println("response: " + response)
                        rtn = ex.toString()
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println("data: " + data)
                        println("response: " + response)
                        rtn = data.toString()
                    }
                }
            }
        } else {
            rtn = "Other method is not support"
        }
        return mapOf("data" to rtn)
    }

    @ResponseBody
    @PostMapping("/curlbyfuel", produces=["application/json"])
    fun curlByfuel(@RequestHeader method: String, @RequestHeader url: String, @RequestBody body: String): String {
        //JSON is in the @RequestBody
        logger.info { "Curl is called by Fuel" }
        logger.debug { "method:" }; logger.debug { method }
        logger.debug { "url:" }; logger.debug { url }
        logger.debug { "body:" }; logger.debug { body }

        //For Proxy
        /*
        if (proxy_enable.toLowerCase().equals("true")) {
            logger.info{ "Fuel is working under Proxy Setting" }
            FuelManager.instance.proxy= Proxy(Proxy.Type.HTTP, InetSocketAddress(proxy_host, proxy_port.toInt()))
        }
        */

        val bodymap: Map<String, Map<String, Any>> = jacksonObjectMapper().readValue(body.trim())
        val fuelcurlHeader_map: Map<String, Any>? = bodymap.get("header")
        val fuelcurlBody_map: Map<String, Any>? = bodymap.get("body")
        //TODO: A better way to initialize responResultofString
        var responResultofString: ResponseResultOf<String> = Fuel.get("http://127.0.0.1").responseString()

        if (method.toLowerCase().equals("get")) {
            //TODO: Implement Param


            if (null != fuelcurlHeader_map) {
                logger.info { "GET with Header" }
                responResultofString= Fuel.get(url).header(fuelcurlHeader_map).responseString()

            } else {
                logger.info { "GET without Header" }
                responResultofString = Fuel.get(url).responseString()
            }
        } else if (method.toLowerCase().equals("post")) {
            // Get the header and body (to be sent) from the body
            if (null != fuelcurlBody_map) {
                logger.info { "POST with Request Body" }
                responResultofString = Fuel.post(url)
                        .header(fuelcurlHeader_map.orEmpty())
                        .body(jacksonObjectMapper().writeValueAsString(fuelcurlBody_map))
                        .responseString()
            } else {
                logger.info { "POST without Request Body" }
                responResultofString = Fuel.post(url)
                        .header(fuelcurlHeader_map.orEmpty())
                        .responseString()
                logger.info { "POST without Request Body" }
            }
        } else {
            logger.error { "Other method is not support" }
            //return mapOf("rtn_code" to "fail", "msg" to "Other methmod is not allow")
            var jsonRtn = JSONObject()
            jsonRtn.put("rtn_code", "500")
            jsonRtn.put("response", "Other method is not support")
            jsonRtn.put("result", JSONObject())
            return jsonRtn.toString()
        }
        val request = responResultofString.first
        val response = responResultofString.second
        val result = responResultofString.third

        logger.debug { "request:" }; logger.debug { request.toString() }
        logger.debug { "response:" }; logger.debug { response.toString() }
        logger.debug { "result:" }; logger.debug { result.get() }

        var jsonRtn = JSONObject()
        jsonRtn.put("rtn_code", response.statusCode.toString())
        jsonRtn.put("response", response.responseMessage)

        if (result.get()[0].equals('{')) {
            try {
                jsonRtn.put("result", JSONObject(result.get()).toMap())
            } catch (e: Exception) {
                logger.error { "JSONObject conversion error" }
                jsonRtn.put("result", JSONObject())
            }
        } else if (result.get()[0].equals('[')) {
            try {
                jsonRtn.put("result", JSONArray(result.get()))
            } catch (e: Exception) {
                logger.error { "JSONArray conversion error" }
                jsonRtn.put("result", JSONObject())
            }
        } else {
            logger.debug { result.get()[0] }
            logger.error { "Not begin with brace [ or { " }
            jsonRtn.put("result", JSONObject())
        }

        return jsonRtn.toString()
    }

}
