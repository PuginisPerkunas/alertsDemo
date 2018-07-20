package buttons.games.sounds.alertstest.AlertData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.util.ArrayList;

public class AtributeDefaultTypeAdapter extends TypeAdapter <ArrayList<Object>> {
    @Override
    public void write(JsonWriter out, ArrayList<Object> value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayList<Object> read(JsonReader in) throws IOException {
        Gson gson = new Gson();
        // Pick next token as a JsonElement
        final JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
        // Note that Gson uses JsonNull singleton to denote a null
        if ( jsonElement.isJsonNull() ) {
            return null;
        }
        if ( jsonElement.isJsonArray() ) {
            JsonArray array = jsonElement
                    .getAsJsonArray();
            return gson.fromJson(array, TypeToken.getParameterized(ArrayList.class, Object.class).getType());

        }
        if ( jsonElement.isJsonPrimitive() ) {
            String jsonString = jsonElement
                    .getAsJsonPrimitive()
                    .getAsString();
            return gson.fromJson(jsonString, TypeToken.getParameterized(ArrayList.class, Object.class).getType());
        }
        // Not something we can handle
        throw new MalformedJsonException("Cannot parse: " + jsonElement);
    }
}
