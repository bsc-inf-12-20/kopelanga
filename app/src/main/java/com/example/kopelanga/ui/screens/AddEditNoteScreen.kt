package com.example.kopelanga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatIndentDecrease
import androidx.compose.material.icons.filled.FormatIndentIncrease
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kopelanga.data.Note
import com.example.kopelanga.data.NoteBlock
import com.example.kopelanga.data.NoteSerializer
import com.example.kopelanga.ui.theme.SaveButtonColor
import com.example.kopelanga.ui.theme.gradientBrush
import com.example.kopelanga.utils.exportToCsv
import com.example.kopelanga.utils.exportToPdf
import com.example.kopelanga.utils.exportToTxt
import com.example.kopelanga.utils.getListPrefix
import com.example.kopelanga.viewmodel.NotesViewModel

@Composable
fun AddEditNoteScreen(
    viewModel: NotesViewModel,
    noteId: Int?,
    onSaveClick: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var blocks by remember { mutableStateOf(listOf(NoteBlock())) }
    var isNewNote by remember { mutableStateOf(true) }
    var showExportMenu by remember { mutableStateOf(false) }
    
    var activeBlockIndex by remember { mutableStateOf<Int?>(0) }

    // To request focus on newly created blocks
    val focusRequesters = remember(blocks.size) { List(blocks.size) { FocusRequester() } }

    if (noteId != null) {
        val note by viewModel.getNoteById(noteId).collectAsState(initial = null)
        LaunchedEffect(note) {
            if (note != null) {
                title = note!!.title
                blocks = NoteSerializer.deserialize(note!!.content)
                isNewNote = false
            }
        }
    }

    Scaffold(
        topBar = {
             // Custom Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush)
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                // Centered Title
                Text(
                    text = if (isNewNote) "New Note" else "Edit Note",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Left and Right buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onSaveClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Row {
                        // Export Menu
                        Box {
                            IconButton(onClick = { showExportMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Export",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showExportMenu,
                                onDismissRequest = { showExportMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Export as PDF") },
                                    onClick = {
                                        showExportMenu = false
                                        exportToPdf(context, title, blocks)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Export as CSV") },
                                    onClick = {
                                        showExportMenu = false
                                        exportToCsv(context, title, blocks)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Export as Text") },
                                    onClick = {
                                        showExportMenu = false
                                        exportToTxt(context, title, blocks)
                                    }
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val content = NoteSerializer.serialize(blocks)
                                val note = if (isNewNote) {
                                    Note(title = title, content = content, timestamp = System.currentTimeMillis())
                                } else {
                                    Note(id = noteId!!, title = title, content = content, timestamp = System.currentTimeMillis())
                                }
                                if (isNewNote) viewModel.insert(note) else viewModel.update(note)
                                onSaveClick()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SaveButtonColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(Color.White)
        ) {
            // Word-like Toolbar
            WordLikeToolbar(
                activeBlock = if (activeBlockIndex != null && activeBlockIndex!! < blocks.size) blocks[activeBlockIndex!!] else null,
                onUpdateBlock = { updatedBlock ->
                    if (activeBlockIndex != null && activeBlockIndex!! < blocks.size) {
                        val newBlocks = blocks.toMutableList()
                        newBlocks[activeBlockIndex!!] = updatedBlock
                        blocks = newBlocks
                    }
                }
            )

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                item {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = {
                            Text(
                                "Title",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                itemsIndexed(blocks) { index, block ->
                    // Safe focus requester retrieval
                    val focusRequester = if (index < focusRequesters.size) focusRequesters[index] else FocusRequester()

                    NoteBlockItem(
                        block = block,
                        index = index,
                        allBlocks = blocks,
                        isActive = index == activeBlockIndex,
                        focusRequester = focusRequester,
                        onFocus = { activeBlockIndex = index },
                        onUpdate = { newBlock ->
                            val newBlocks = blocks.toMutableList()
                            newBlocks[index] = newBlock
                            blocks = newBlocks
                        },
                        onEnterPressed = {
                            // Insert new block after current
                            val newBlock = NoteBlock(
                                type = block.type, // Continue list type
                                indentLevel = block.indentLevel, // Continue indentation
                                fontFamily = block.fontFamily,
                                fontSize = block.fontSize,
                                alignment = block.alignment
                            )
                            val newBlocks = blocks.toMutableList()
                            newBlocks.add(index + 1, newBlock)
                            blocks = newBlocks
                            
                            // Focus will be handled by LaunchedEffect or user tap, 
                            // ideally we'd set focus to index + 1 here
                            activeBlockIndex = index + 1
                        },
                        onBackspaceEmpty = {
                            if (index > 0) {
                                val newBlocks = blocks.toMutableList()
                                newBlocks.removeAt(index)
                                blocks = newBlocks
                                activeBlockIndex = index - 1
                            } else if (block.type != "Body") {
                                // Reset to body if it's the first item and empty
                                val newBlocks = blocks.toMutableList()
                                newBlocks[index] = block.copy(type = "Body", indentLevel = 0)
                                blocks = newBlocks
                            }
                        },
                        onRemove = {
                             if (blocks.size > 1) {
                                 val newBlocks = blocks.toMutableList()
                                 newBlocks.removeAt(index)
                                 blocks = newBlocks
                                 if (activeBlockIndex == index) {
                                     activeBlockIndex = if (index > 0) index - 1 else 0
                                 } else if (activeBlockIndex != null && activeBlockIndex!! > index) {
                                     activeBlockIndex = activeBlockIndex!! - 1
                                 }
                             }
                        }
                    )
                    
                    // Auto-focus new block if it became active
                    LaunchedEffect(activeBlockIndex) {
                        if (activeBlockIndex == index) {
                            try {
                                focusRequester.requestFocus()
                            } catch (e: Exception) {
                                // Ignore focus errors during composition
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            blocks = blocks + NoteBlock()
                            activeBlockIndex = blocks.size - 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0F0F0),
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Paragraph")
                    }
                }
                
                // Spacer for bottom padding
                item { Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding())) }
            }
        }
    }
}

@Composable
fun WordLikeToolbar(
    activeBlock: NoteBlock?,
    onUpdateBlock: (NoteBlock) -> Unit
) {
    val scrollState = rememberScrollState()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F3F3))
            .horizontalScroll(scrollState)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (activeBlock == null) {
            Text("Select a block to edit", color = Color.Gray, modifier = Modifier.padding(8.dp))
            return@Row
        }

        // Font Family
        var expandedFont by remember { mutableStateOf(false) }
        Box {
            Button(
                onClick = { expandedFont = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(4.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(activeBlock.fontFamily, fontSize = 12.sp)
            }
            DropdownMenu(expanded = expandedFont, onDismissRequest = { expandedFont = false }) {
                listOf("Default", "Serif", "SansSerif", "Monospace", "Cursive").forEach { font ->
                    DropdownMenuItem(
                        text = { Text(font) },
                        onClick = {
                            onUpdateBlock(activeBlock.copy(fontFamily = font))
                            expandedFont = false
                        }
                    )
                }
            }
        }
        
        // Font Size
        var expandedSize by remember { mutableStateOf(false) }
        Box {
            Button(
                onClick = { expandedSize = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(4.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(activeBlock.fontSize.toString(), fontSize = 12.sp)
            }
            DropdownMenu(expanded = expandedSize, onDismissRequest = { expandedSize = false }) {
                listOf(12, 14, 16, 18, 20, 24, 28, 32).forEach { size ->
                    DropdownMenuItem(
                        text = { Text(size.toString()) },
                        onClick = {
                            onUpdateBlock(activeBlock.copy(fontSize = size))
                            expandedSize = false
                        }
                    )
                }
            }
        }

        Divider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.LightGray)

        // Bold
        ToolbarToggleButton(
            icon = Icons.Default.FormatBold,
            checked = activeBlock.isBold,
            onCheckedChange = { onUpdateBlock(activeBlock.copy(isBold = it)) }
        )

        // Italic
        ToolbarToggleButton(
            icon = Icons.Default.FormatItalic,
            checked = activeBlock.isItalic,
            onCheckedChange = { onUpdateBlock(activeBlock.copy(isItalic = it)) }
        )
        
        // Underline
        ToolbarToggleButton(
            icon = Icons.Default.FormatUnderlined,
            checked = activeBlock.isUnderline,
            onCheckedChange = { onUpdateBlock(activeBlock.copy(isUnderline = it)) }
        )

        Divider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.LightGray)

        // Alignment
        // We cycle through Left -> Center -> Right for simplicity in limited space
        IconButton(
            onClick = {
                val nextAlign = when(activeBlock.alignment) {
                    "Center" -> "Center"
                    "Right" -> "Right"
                    else -> "Left"
                }
                onUpdateBlock(activeBlock.copy(alignment = nextAlign))
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = when(activeBlock.alignment) {
                    "Center" -> Icons.Default.FormatAlignCenter
                    "Right" -> Icons.Default.FormatAlignRight
                    else -> Icons.Default.FormatAlignLeft
                },
                contentDescription = "Align",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        Divider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.LightGray)
        
        // List Types
         var expandedList by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { expandedList = true }, modifier = Modifier.size(32.dp)) {
                 // Show icon based on current type if it's a list, else show Bullet generic
                 val icon = when(activeBlock.type) {
                     "Numbered" -> Icons.Default.FormatIndentIncrease // Placeholder for Numbered
                     "Roman" -> Icons.Default.FormatIndentIncrease // Placeholder for Roman
                     else -> Icons.Default.DragHandle // Placeholder for Bullet/List
                 }
                 Icon(icon, contentDescription = "Lists", tint = Color.Black, modifier = Modifier.size(20.dp))
            }
            DropdownMenu(expanded = expandedList, onDismissRequest = { expandedList = false }) {
                 listOf("Body", "Bullet", "Numbered", "Roman").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onUpdateBlock(activeBlock.copy(type = type))
                            expandedList = false
                        }
                    )
                }
            }
        }

        // Indentation
        IconButton(onClick = { 
            if (activeBlock.indentLevel > 0) onUpdateBlock(activeBlock.copy(indentLevel = activeBlock.indentLevel - 1))
        }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.FormatIndentDecrease, contentDescription = "Decrease Indent", tint = Color.Black, modifier = Modifier.size(20.dp))
        }
        IconButton(onClick = { 
            if (activeBlock.indentLevel < 4) onUpdateBlock(activeBlock.copy(indentLevel = activeBlock.indentLevel + 1))
        }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.FormatIndentIncrease, contentDescription = "Increase Indent", tint = Color.Black, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun ToolbarToggleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier
            .size(32.dp)
            .background(if (checked) Color(0xFFD0D0D0) else Color.Transparent, RoundedCornerShape(4.dp))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun NoteBlockItem(
    block: NoteBlock,
    index: Int,
    allBlocks: List<NoteBlock>,
    isActive: Boolean,
    focusRequester: FocusRequester,
    onFocus: () -> Unit,
    onUpdate: (NoteBlock) -> Unit,
    onEnterPressed: () -> Unit,
    onBackspaceEmpty: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .padding(start = (block.indentLevel * 16).dp)
            .border(width = 1.dp, color = if (isActive) Color(0xFF2196F3).copy(alpha = 0.3f) else Color.Transparent, shape = RoundedCornerShape(4.dp))
            .clickable { onFocus() }
            .padding(4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // List Prefix
        val prefix = getListPrefix(block.type, index, allBlocks)
        if (prefix.isNotEmpty()) {
             Text(
                 text = prefix,
                 style = getTextStyle(block),
                 modifier = Modifier.padding(end = 8.dp, top = 14.dp) // Adjust top padding to align with TextField
             )
        }

        // Text Input
        TextField(
            value = block.text,
            onValueChange = { 
                // Detect Enter press via text change (simple hack since KeyboardActions only handle 'Done' etc usually)
                // A more robust way involves using Key events, but for TextField, simple newline detection works often.
                if (it.endsWith("\n")) {
                    onUpdate(block.copy(text = it.dropLast(1))) // Remove the newline
                    onEnterPressed()
                } else {
                    onUpdate(block.copy(text = it))
                }
                onFocus()
            },
            placeholder = { if (block.type == "Body") Text("Type here...") },
            textStyle = getTextStyle(block).copy(textAlign = getTextAlign(block.alignment)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onEnterPressed() }
            ),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
        )
        
        IconButton(onClick = onRemove, modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)) {
            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.LightGray, modifier = Modifier.size(16.dp))
        }
    }
}

fun getTextAlign(alignment: String): TextAlign {
    return when (alignment) {
        "Center" -> TextAlign.Center
        "Right" -> TextAlign.Right
        else -> TextAlign.Left
    }
}

fun getTextStyle(block: NoteBlock): TextStyle {
    // Font size overrides type-based defaults if customized
    val fontSize = block.fontSize.sp

    val fontWeight = if (block.isBold) FontWeight.Bold else FontWeight.Normal
    val fontStyle = if (block.isItalic) FontStyle.Italic else FontStyle.Normal
    val textDecoration = if (block.isUnderline) TextDecoration.Underline else TextDecoration.None
    
    val fontFamily = when (block.fontFamily) {
        "Serif" -> FontFamily.Serif
        "SansSerif" -> FontFamily.SansSerif
        "Monospace" -> FontFamily.Monospace
        "Cursive" -> FontFamily.Cursive
        else -> FontFamily.Default
    }

    return TextStyle(
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        textDecoration = textDecoration,
        fontFamily = fontFamily,
        color = Color(block.textColor)
    )
}
