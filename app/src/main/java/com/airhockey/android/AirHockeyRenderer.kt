package com.airhockey.android

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.airhockey.android.util.Logger
import com.airhockey.android.util.ShaderHelper
import com.airhockey.android.util.TextResourceReader
import com.example.airhockey.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer(val context: Context) : GLSurfaceView.Renderer {
    private val POSITION_COMPONENT_COUNT = 2
    private val BYTES_PER_FLOAT = 4
    private val vertexData: FloatBuffer
    var tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // Line 1
        -0.5f, 0f,
        0.5f, 0f,

        // Mallets
        0f, -0.25f,
        0f, 0.25f,

        //Center
        0f, 0f,

        // Triangle 1
        -0.49f, -0.49f,
        0.49f, 0.49f,
        -0.49f, 0.49f,
        // Triangle 2
        -0.49f, -0.49f,
        0.49f, -0.49f,
        0.49f, 0.49f,


    )
    private val U_COLOR = "u_Color"
    private var uColorLocation = 0
    private val A_POSITION = "a_Position"
    private var aPositionLocation = 0

    init {
        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVerticesWithTriangles)
    }

    var program = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        val vertexShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_vertex_shader);
        val fragmentShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_fragment_shader)
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        if (Logger.ON)
            ShaderHelper.validateProgram(program);
        glUseProgram(program)
        uColorLocation = glGetUniformLocation(program, U_COLOR)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
            false, 0, vertexData)
        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glUniform4f(uColorLocation, 1.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6)

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 11, 6)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2)

        // Draw the first mallet blue.
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        // Draw the second mallet red.
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)

        // Draw the second mallet red.
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 10, 1)

    }

}
