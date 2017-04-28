package beg.hr.kmnl.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by juraj on 26/04/2017.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

