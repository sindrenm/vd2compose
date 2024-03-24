import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import generators.toImageVectorString
import parser.XmlParser

@Composable
@Preview
fun App() {
    var iconName by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }

    var xmlCode by remember { mutableStateOf("") }
    var composeCode by remember { mutableStateOf("") }

    var isComposeCodeCopied by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        iconName = "Activity"
        xmlCode = """<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="#FF000000">
    <path
        android:fillColor="#FF14233C"
        android:fillType="evenOdd"
        android:pathData="M8.07 13.22l2-4.3 3.6 10.15c0.1 0.28 0.42 0.44 0.8 0.43 0.38 0 0.72-0.18 0.82-0.48l1.87-5.15h3.53c0.21-0.3 0.3-0.51 0.31-0.72 0-0.21-0.08-0.45-0.31-0.78H16.7c-0.3 0-0.5 0.06-0.65 0.16-0.14 0.1-0.26 0.25-0.35 0.48l-1.24 2.94-3.6-9.51C10.75 6.14 10.44 5.99 10.1 6c-0.35 0-0.7 0.18-0.85 0.5l-2.77 5.87H3.33C3.08 12.68 2.99 12.91 3 13.11c0 0.21 0.1 0.44 0.33 0.76h3.71c0.3 0 0.5-0.06 0.64-0.16 0.15-0.1 0.28-0.26 0.39-0.49Z"/>
</vector>"""
    }

    MaterialTheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = iconName,
                    onValueChange = { iconName = it },
                    label = { Text("Icon Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = xmlCode,
                    onValueChange = { xmlCode = it },
                    label = { Text("Vector Drawable XML Code") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )

                Button(
                    onClick = {
                        val vectorDrawable = XmlParser.parse(xmlCode)

                        fileName = "$iconName.kt"
                        composeCode = vectorDrawable.toImageVectorString(iconName)
                        isComposeCodeCopied = false
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = iconName.isNotBlank() && xmlCode.isNotBlank(),
                ) {
                    Text("Convert")
                }
            }

            Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = {},
                    label = { Text("Output File Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = composeCode,
                    onValueChange = {},
                    label = { Text("Output Compose ImageVector Code") },
                    singleLine = false,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            isComposeCodeCopied = true
                        },
                        enabled = xmlCode.isNotBlank(),
                    ) {
                        Text("Copy")
                    }

                    AnimatedVisibility(isComposeCodeCopied) {
                        Text("Copied!", Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}
