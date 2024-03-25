import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import generators.toImageVectorString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import parser.XmlParser
import java.net.URI
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.toPath
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(modifier: Modifier = Modifier) {
    var iconName by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }

    var xmlCode by remember { mutableStateOf("") }
    var composeCode by remember { mutableStateOf("") }

    MaterialTheme {
        val codeTextStyle = LocalTextStyle.current.copy(fontFamily = JetbrainsMono)

        Row(modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .onExternalDrag(
                        onDrop = {
                            when (val dragData = it.dragData) {
                                is DragData.FilesList -> {
                                    val file = parseFiles(dragData.readFiles())

                                    iconName = file.iconName
                                    xmlCode = file.contents
                                }

                                is DragData.Text -> {
                                    xmlCode = dragData.readText()
                                }

                                else -> {
                                    error("Unknown content type: $dragData")
                                }
                            }
                        },
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
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
                    textStyle = codeTextStyle,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )

                Button(
                    onClick = {
                        val vectorDrawable = XmlParser.parse(xmlCode)

                        fileName = "$iconName.kt"
                        composeCode = vectorDrawable.toImageVectorString(iconName)
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
                    textStyle = codeTextStyle,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var isComposeCodeCopied by remember { mutableStateOf(false) }

                    AnimatedVisibility(isComposeCodeCopied) {
                        Text("Copied!", Modifier.padding(end = 8.dp))
                    }

                    val coroutineScope = rememberCoroutineScope()
                    val clipboardManager = LocalClipboardManager.current

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                clipboardManager.setText(AnnotatedString(composeCode))

                                isComposeCodeCopied = true

                                delay(2.seconds)

                                isComposeCodeCopied = false
                            }
                        },
                        enabled = composeCode.isNotBlank(),
                    ) {
                        Text("Copy")
                    }

                    Button(
                        onClick = {
                            fileName = ""
                            composeCode = ""
                        },
                        enabled = fileName.isNotBlank() || composeCode.isNotBlank(),
                        modifier = Modifier.padding(start = 8.dp),
                    ) {
                        Text("Clear")
                    }
                }
            }
        }
    }
}

fun parseFiles(filePaths: List<String>): ParsedFile {
    if (filePaths.count() != 1) {
        error("Currently, only one file is supported at a time")
    }

    val filePath = URI(filePaths.first()).toPath()

    return ParsedFile(
        name = filePath.name,
        contents = filePath.readText(),
    )
}

data class ParsedFile(val name: String, val contents: String) {
    val iconName: String
        get() = name
            .replace("ic_", "")
            .replace(".xml", "")
            .split("_")
            .joinToString("") { it.capitalize(Locale.current) }
}
