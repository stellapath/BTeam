package com.project.bteam.util

import android.app.Activity
import androidx.core.content.res.ResourcesCompat
import com.project.bteam.R

class MyMotionToast {

    companion object {
        @JvmStatic
        fun successToast(context: Activity, message: String) {
            www.sanju.motiontoast.MotionToast.createColorToast(context, message,
                    www.sanju.motiontoast.MotionToast.TOAST_SUCCESS,
                    www.sanju.motiontoast.MotionToast.GRAVITY_BOTTOM,
                    www.sanju.motiontoast.MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, R.font.worksans_medium))
        }
        @JvmStatic
        fun errorToast(context: Activity, message: String) {
            www.sanju.motiontoast.MotionToast.createColorToast(context, message,
                    www.sanju.motiontoast.MotionToast.TOAST_ERROR,
                    www.sanju.motiontoast.MotionToast.GRAVITY_BOTTOM,
                    www.sanju.motiontoast.MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, R.font.worksans_medium))
        }
        @JvmStatic
        fun warningToast(context: Activity, message: String) {
            www.sanju.motiontoast.MotionToast.createColorToast(context, message,
                    www.sanju.motiontoast.MotionToast.TOAST_WARNING,
                    www.sanju.motiontoast.MotionToast.GRAVITY_BOTTOM,
                    www.sanju.motiontoast.MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, R.font.worksans_medium))
        }
        @JvmStatic
        fun infoToast(context: Activity, message: String) {
            www.sanju.motiontoast.MotionToast.createColorToast(context, message,
                    www.sanju.motiontoast.MotionToast.TOAST_INFO,
                    www.sanju.motiontoast.MotionToast.GRAVITY_BOTTOM,
                    www.sanju.motiontoast.MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, R.font.worksans_medium))
        }
    }

}
