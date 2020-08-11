<<<<<<< HEAD:MainProject/app/src/androidTest/java/com/hanul/project/ExampleInstrumentedTest.java
package com.hanul.project;
=======
package com.example.mainproject;
>>>>>>> 2d701573b2670476ea4be144bbf36277c01de72e:MainProject/app/src/androidTest/java/com/example/mainproject/ExampleInstrumentedTest.java

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
<<<<<<< HEAD:MainProject/app/src/androidTest/java/com/hanul/project/ExampleInstrumentedTest.java
        assertEquals("com.hanul.project", appContext.getPackageName());
=======
        assertEquals("com.example.mainproject", appContext.getPackageName());
>>>>>>> 2d701573b2670476ea4be144bbf36277c01de72e:MainProject/app/src/androidTest/java/com/example/mainproject/ExampleInstrumentedTest.java
    }
}