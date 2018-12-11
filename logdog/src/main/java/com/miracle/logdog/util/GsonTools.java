package com.miracle.logdog.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miracle.logdog.JsonMergeConflictException;

import java.util.Map;

/**
 * miracle
 * 2018/12/6 10:48
 */
public class GsonTools {

    public static enum ConflictStrategy {
        THROW_EXCEPTION, PREFER_FIRST_OBJ, PREFER_SECOND_OBJ, PREFER_NON_NULL;
    }

    public static void extendJsonObject(String json, ConflictStrategy strategy, Map<String,String> map)
            throws JsonMergeConflictException {
        JsonParser parser = new JsonParser();
        JsonObject leftObj = parser.parse(json).getAsJsonObject();
        JsonObject rightObj = new JsonObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            rightObj.addProperty(entry.getKey(),entry.getValue());
        }
        GsonTools.extendJsonObject(leftObj,strategy,rightObj);
    }

    public static void extendJsonObject(String leftJson, ConflictStrategy strategy, String rightJson)
            throws JsonMergeConflictException {
        JsonParser parser = new JsonParser();
        JsonObject leftObj = parser.parse(leftJson).getAsJsonObject();
        JsonObject rightObj =parser.parse(rightJson).getAsJsonObject();
        GsonTools.extendJsonObject(leftObj,strategy,rightObj);
    }

    public static void extendJsonObject(JsonObject destinationObject, ConflictStrategy strategy,
                                        JsonObject... objs)
            throws JsonMergeConflictException {
        for (JsonObject obj : objs) {
            extendJsonObject(destinationObject, obj, strategy);
        }
    }

    private static void extendJsonObject(JsonObject leftObj, JsonObject rightObj, ConflictStrategy conflictStrategy)
            throws JsonMergeConflictException {
        for (Map.Entry<String, JsonElement> rightEntry : rightObj.entrySet()) {
            String rightKey = rightEntry.getKey();
            JsonElement rightVal = rightEntry.getValue();
            if (leftObj.has(rightKey)) {
                //conflict
                JsonElement leftVal = leftObj.get(rightKey);
                if (leftVal.isJsonArray() && rightVal.isJsonArray()) {
                    JsonArray leftArr = leftVal.getAsJsonArray();
                    JsonArray rightArr = rightVal.getAsJsonArray();
                    //concat the arrays -- there cannot be a conflict in an array, it's just a collection of stuff
                    for (int i = 0; i < rightArr.size(); i++) {
                        leftArr.add(rightArr.get(i));
                    }
                } else if (leftVal.isJsonObject() && rightVal.isJsonObject()) {
                    //recursive merging
                    extendJsonObject(leftVal.getAsJsonObject(), rightVal.getAsJsonObject(), conflictStrategy);
                } else {//not both arrays or objects, normal merge with conflict resolution
                    handleMergeConflict(rightKey, leftObj, leftVal, rightVal, conflictStrategy);
                }
            } else {//no conflict, add to the object
                leftObj.add(rightKey, rightVal);
            }
        }
    }

    private static void handleMergeConflict(String key, JsonObject leftObj, JsonElement leftVal, JsonElement rightVal, ConflictStrategy conflictStrategy)
            throws JsonMergeConflictException {
        {
            switch (conflictStrategy) {
                case PREFER_FIRST_OBJ:
                    break;//do nothing, the right val gets thrown out
                case PREFER_SECOND_OBJ:
                    leftObj.add(key, rightVal);//right side auto-wins, replace left val with its val
                    break;
                case PREFER_NON_NULL:
                    //check if right side is not null, and left side is null, in which case we use the right val
                    if (leftVal.isJsonNull() && !rightVal.isJsonNull()) {
                        leftObj.add(key, rightVal);
                    }//else do nothing since either the left value is non-null or the right value is null
                    break;
                case THROW_EXCEPTION:
                    throw new JsonMergeConflictException("Key " + key + " exists in both objects and the conflict resolution strategy is " + conflictStrategy);
                default:
                    throw new UnsupportedOperationException("The conflict strategy " + conflictStrategy + " is unknown and cannot be processed");
            }
        }
    }

}
