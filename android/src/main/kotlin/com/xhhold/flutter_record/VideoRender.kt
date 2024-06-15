package com.xhhold.flutter_record

import android.content.Context
import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import android.opengl.EGL14
import android.view.Surface

class VideoRender(
    private val context: Context,
    private val outputSurface: Surface,
    private val width: Int,
    private val height: Int,
    private val fps: Int,
) : OnFrameAvailableListener {

    private var eglDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext = EGL14.EGL_NO_CONTEXT
    private var eglContextEncoder = EGL14.EGL_NO_CONTEXT
    private var eglSurface = EGL14.EGL_NO_SURFACE
    private var eglSurfaceEncoder = EGL14.EGL_NO_SURFACE

    private var surfaceTexture: SurfaceTexture? = null

    var inputSurface: Surface? = null
        private set

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {

    }

}