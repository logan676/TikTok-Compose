package com.puskal.cameramedia.edit

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.opengl.GLES20
import android.view.Surface
import android.view.TextureView
import androidx.media3.exoplayer.ExoPlayer
import com.daasuu.mp4compose.filter.GlFilter
import com.daasuu.mp4compose.gl.GlPreviewFilter
import com.daasuu.mp4compose.gl.GlSurfaceTexture

/**
 * TextureView that renders an [ExoPlayer] output through a [GlFilter].
 * Based on [CameraGlPreviewView] but used for video playback.
 */
class GlFilterPlayerView(context: Context) : TextureView(context), TextureView.SurfaceTextureListener {

    private var surfaceTextureWrapper: GlSurfaceTexture? = null
    private var previewFilter: GlPreviewFilter? = null
    private var glFilter: GlFilter = GlFilter()

    private var texName: Int = 0
    private val mvpMatrix = FloatArray(16)
    private val stMatrix = FloatArray(16)

    private var eglDisplay: EGLDisplay? = null
    private var eglContext: EGLContext? = null
    private var eglSurface: EGLSurface? = null

    var player: ExoPlayer? = null
        set(value) {
            field = value
            value?.let { exo ->
                surfaceTextureWrapper?.surfaceTexture?.let { tex ->
                    exo.setVideoSurface(Surface(tex))
                }
            }
        }

    init {
        surfaceTextureListener = this
    }

    fun setGlFilter(filter: GlFilter) {
        glFilter.release()
        glFilter = filter
        glFilter.setup()
    }

    private fun initEgl(surface: SurfaceTexture) {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw RuntimeException("unable to get EGL14 display")
        }
        val version = IntArray(2)
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            eglDisplay = null
            throw RuntimeException("unable to initialize EGL14")
        }

        val attribList = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        val num = IntArray(1)
        EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, configs.size, num, 0)
        val attrib_list = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
        eglContext = EGL14.eglCreateContext(eglDisplay, configs[0], EGL14.EGL_NO_CONTEXT, attrib_list, 0)
        val surfaceAttribs = intArrayOf(EGL14.EGL_NONE)
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, configs[0], surface, surfaceAttribs, 0)
        if (eglSurface == null) {
            throw RuntimeException("surface was null")
        }
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun releaseEgl() {
        eglDisplay?.let { display ->
            eglSurface?.let { EGL14.eglDestroySurface(display, it) }
            eglContext?.let { EGL14.eglDestroyContext(display, it) }
            EGL14.eglReleaseThread()
            EGL14.eglTerminate(display)
        }
        eglDisplay = null
        eglContext = null
        eglSurface = null
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        initEgl(surface)
        val tex = IntArray(1)
        GLES20.glGenTextures(1, tex, 0)
        texName = tex[0]
        surfaceTextureWrapper = GlSurfaceTexture(texName)
        previewFilter = GlPreviewFilter(surfaceTextureWrapper!!.textureTarget)
        previewFilter?.setup()
        glFilter.setup()
        surfaceTextureWrapper!!.setOnFrameAvailableListener { drawFrame() }
        player?.setVideoSurface(Surface(surfaceTextureWrapper!!.surfaceTexture))
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        player?.clearVideoSurface()
        surfaceTextureWrapper?.release()
        previewFilter?.release()
        glFilter.release()
        releaseEgl()
        texName = 0
        surfaceTextureWrapper = null
        previewFilter = null
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        drawFrame()
    }

    private fun drawFrame() {
        val wrapper = surfaceTextureWrapper ?: return
        wrapper.updateTexImage()
        wrapper.getTransformMatrix(stMatrix)
        GLES20.glViewport(0, 0, width, height)
        android.opengl.Matrix.setIdentityM(mvpMatrix, 0)
        previewFilter?.draw(texName, mvpMatrix, stMatrix, 1f)
        glFilter.draw(texName, null)
        EGL14.eglSwapBuffers(eglDisplay, eglSurface)
    }
}
