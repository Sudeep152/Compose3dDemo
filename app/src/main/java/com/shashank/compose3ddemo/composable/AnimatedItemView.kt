package com.shashank.compose3ddemo.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener

@Composable
fun AnimatedItemView(
    modifier: Modifier = Modifier,
    assetModelLocation: String,
    environmentBg: Boolean = false,
    modelScale: Float = 0.25f,
    cameraPositionY: Float = -0.5f,
    cameraPositionZ: Float = 2.0f,
    backgroundColor: Color = Color.White
) {
    Card(modifier = modifier) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)

        // This node serves as a fixed center for the model to help keep it visually centered
        val centerNode = rememberNode(engine)

        // Create the camera node and attach it to centerNode
        val cameraNode = rememberCameraNode(engine) {
            position = Position(y = cameraPositionY, z = cameraPositionZ)
            lookAt(centerNode)
            centerNode.addChildNode(this)
        }

        // Configure the model node with a default scale
        val modelNode = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = assetModelLocation
                ),
                scaleToUnits = modelScale,
                autoAnimate = true
            )
        }

            Scene(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                isOpaque = environmentBg,
                engine = engine,
                modelLoader = modelLoader,
                cameraNode = cameraNode,
                cameraManipulator = rememberCameraManipulator(
                    orbitHomePosition = cameraNode.worldPosition,
                    targetPosition = centerNode.worldPosition
                ),
                childNodes = listOf(centerNode, modelNode),
                onFrame = {
                    cameraNode.lookAt(centerNode)
                },
                onGestureListener = rememberOnGestureListener(
                    onDoubleTap = { _, node ->
                        node?.apply {
                            // Apply a scaling factor to the model
                            scale *= 1.2f // Adjust scale factor as needed to increase model size
                        }
                    }
                )
            )
    }
}
