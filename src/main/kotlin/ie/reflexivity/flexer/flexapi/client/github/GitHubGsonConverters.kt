package ie.reflexivity.flexer.flexapi.client.github

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ie.reflexivity.flexer.flexapi.db.domain.GitHubState
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val GIT_HUB_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

class GsonDateTimeConverter() : JsonDeserializer<LocalDateTime> {
    override fun deserialize(jsonElement: JsonElement, type: Type, context: JsonDeserializationContext): LocalDateTime {
        val value = jsonElement.asString
        val formatter = DateTimeFormatter.ofPattern(GIT_HUB_DATE_TIME_FORMAT)
        return LocalDateTime.parse(value, formatter)
    }
}

class GitHubStateConverter() : JsonDeserializer<GitHubState> {
    override fun deserialize(jsonElement: JsonElement, type: Type, context: JsonDeserializationContext): GitHubState {
        val value = jsonElement.asString.toUpperCase()
        return GitHubState.valueOf(value)
    }
}
