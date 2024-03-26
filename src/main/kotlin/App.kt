import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import generators.toImageVectorString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import parser.XmlParser
import java.net.URI
import kotlin.io.path.*
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(modifier: Modifier = Modifier) {

    MaterialTheme {
        var selectedTabIndex by remember { mutableStateOf(1) }

        Column(modifier) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Single file") },
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Multiple files") },
                )
            }

            if (selectedTabIndex == 0) {
                SingleFilePage(Modifier.weight(1f))
            } else {
                MultipleFilesPage(Modifier.weight(1f))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun SingleFilePage(
    modifier: Modifier = Modifier,
) {
    var iconName by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }

    var xmlCode by remember { mutableStateOf("") }
    var composeCode by remember { mutableStateOf("") }

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
                                val file = parseFile(dragData.readFiles().first())

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

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun MultipleFilesPage(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        var vectorDrawableFiles: List<IconContentsFile> by remember { mutableStateOf(emptyList()) }
        var imageVectorFiles: List<IconContentsFile> by remember { mutableStateOf(emptyList()) }

        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(16.dp),
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    .onExternalDrag(
                        onDrop = {
                            when (val dragData = it.dragData) {
                                is DragData.FilesList -> {
                                    vectorDrawableFiles = parseFiles(dragData.readFiles())
                                }

                                else -> {
                                    error("Unknown content type: $dragData")
                                }
                            }

                        },
                    )
                    .verticalScroll(rememberScrollState()),
            ) {
                vectorDrawableFiles.forEach { file ->
                    ExpandableFileItem(
                        fileName = file.name,
                        fileContent = file.contents,
                    )
                }
            }

            Button(
                onClick = {
                    imageVectorFiles = vectorDrawableFiles
                        .map { it.iconName to XmlParser.parse(it.contents) }
                        .map { (name, vectorDrawable) -> "$name.kt" to vectorDrawable.toImageVectorString(name) }
                        .map { (name, contents) -> IconContentsFile(name, contents) }
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Convert")
            }
        }

        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    .verticalScroll(rememberScrollState()),
            ) {
                imageVectorFiles.forEach { file ->
                    ExpandableFileItem(
                        fileName = file.name,
                        fileContent = file.contents,
                    )
                }
            }

            Row(
                Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var outputDirectoryPath by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = outputDirectoryPath,
                    onValueChange = { outputDirectoryPath = it },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )

                Button(
                    onClick = { saveIconsTo(outputDirectoryPath, imageVectorFiles) },
                    enabled = outputDirectoryPath.isNotBlank() && imageVectorFiles.isNotEmpty(),
                ) {
                    Text("Save")
                }
            }

        }
    }
}

@Composable
private fun ExpandableFileItem(fileName: String, fileContent: String) {
    val codeTextStyle = LocalTextStyle.current.copy(fontFamily = JetbrainsMono)
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(fileName)

            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        if (isExpanded) {
            OutlinedTextField(
                value = fileContent,
                onValueChange = {},
                singleLine = false,
                textStyle = codeTextStyle,
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
            )
        }
    }
}

fun parseFiles(filePaths: List<String>): List<IconContentsFile> {
    return filePaths.map { parseFile(it) }
}

fun parseFile(filePath: String): IconContentsFile {
    val path = URI(filePath).toPath()

    return IconContentsFile(
        name = path.name,
        contents = path.readText(),
    )
}

data class IconContentsFile(val name: String, val contents: String) {
    val iconName: String
        get() = name
            .replace("ic_", "")
            .replace(".xml", "")
            .split("_")
            .joinToString("") { it.capitalize(Locale.current) }
}

private fun saveIconsTo(dirPath: String, files: List<IconContentsFile>) {
    val directory = URI(dirPath.withFileScheme().trim()).toPath()

    directory.createDirectories()

    files
        .map { directory.resolve(it.name) to it.contents }
        .forEach { (path, contents) -> path.writeText(contents) }
}

private fun String.withFileScheme(): String {
    val scheme = "file:"

    if (startsWith(scheme)) return this

    return "$scheme$this"
}
