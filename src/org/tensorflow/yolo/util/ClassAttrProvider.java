/* Copyright 2018 The Android YOLOv2 sample application Authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.tensorflow.yolo.util;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

import org.tensorflow.yolo.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import static org.tensorflow.yolo.Config.LOGGING_TAG;

/**
 * It is used to read names of the classes from the specified resource.
 * It also specifies a color for each classes.
 *
 * Created by Zoltan Szabo on 12/17/17.
 * URL: https://github.com/szaza/android-yolo-v2
 */

public final class ClassAttrProvider {
    private final Vector<String> labels = new Vector();
    private final Vector<Integer> colors = new Vector();
    private static ClassAttrProvider instance;

    private ClassAttrProvider(final AssetManager assetManager) {
        init(assetManager);
    }

    public static ClassAttrProvider newInstance(final AssetManager assetManager) {
        if (instance == null) {
            instance = new ClassAttrProvider(assetManager);
        }

        return instance;
    }

    private void init(final AssetManager assetManager) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(Config.LABEL_FILE)))) {
            br.lines().forEach((line) -> {
                labels.add(line);
                colors.add(convertClassNameToColor(line));
            });
        } catch (IOException ex) {
            throw new RuntimeException("Problem reading label file!", ex);
        }
    }

    private int convertClassNameToColor(String className) {
        String colorFromName = className.substring(className.indexOf('-') + 1, className.length());
        colorFromName = colorFromName.substring(0, colorFromName.indexOf('-'));

        switch (colorFromName) {
            case "red":
                return Color.RED;
            case "green":
                return Color.GREEN;
            case "purple":
                return Color.BLUE;
            default:
                return Color.GRAY;
        }
    }

    public Vector<String> getLabels() {
        return labels;
    }

    public Vector<Integer> getColors() {
        return colors;
    }
}
